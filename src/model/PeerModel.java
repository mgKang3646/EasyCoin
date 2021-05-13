package model;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

import database.DAO;
import database.DTO;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class PeerModel {
	public static final float REWARD = 50f;
	public String hashDifficulty = "00000";
	public int zeroNum = 5;
	public Block block = null; // 가장 최신 블럭
	public boolean proofOfWorkCompleteFlag = true;
	public BlockchainModel blockchainModel =null;
	DAO dao = null;
	PeerModel peerModel;
	public WalletModel walletModel = null;
	private ServerListener serverListener =null;
	public int verifiedPeerCount = 0; // 채굴된 블럭 검증 성공한 Peer의 개수
	public double totalRespondedCount = 0; // 블럭 검증 결과를 전송한 Peer의 개수
	double verifiedCutLine = 0.51;
	double connectedPeersSize; // 본인포함
	public boolean amILeader = false;
	public boolean miningFlag = false;
	public Stage primaryStage;
	public boolean isFirst = true;
	public Button miningStartButton;
	
	public Vector<TransactionOutput> UTXOs = new Vector<TransactionOutput>(); // 스레드 간의 동시접속이 가능한 리스트여서 Vector로 생성
	public ArrayList<DTO> dtos = new ArrayList<DTO>();
	public ArrayList<Peer> peerList = new ArrayList<Peer>();
	public ArrayList<Transaction> transactionList = new ArrayList<Transaction>();


	public ServerListener getServerListerner() {
		return peerModel.serverListener;
	}
	
	//P2P 네트워크 참여를 위한 서버생성하기
	public int doConnection(PeerModel peerModel, WalletModel walletModel, BlockchainModel blockchainModel,Stage primaryStage) throws IOException, Exception {
			//제네시스 블럭 : 최초 블럭 생성
			this.peerModel = peerModel;
			this.walletModel = walletModel;
			this.blockchainModel = blockchainModel;
			this.primaryStage = primaryStage;
			
			//서버리스너 생성
			String[] address = walletModel.getUserLocalHost().split(":");
			String portNumb = address[1];
			
			try {
				serverListener = new ServerListener(portNumb,peerModel); // 서버리스너 생산
				serverListener.start();

			} catch (IOException e) {
				System.out.println("서버리스너 생성 에러( 주소 바인드 중 중복 발생 ) ");
				return -1; // 서버 생성 실패
			} // 포트번호로 서버리스너 생성
			
			dao = new DAO();
			
			//회원가입을 통한 접근시에만 제네시스 블록 생성
			if(blockchainModel.getBlocks().size()==0) {
				block = new Block(DigestUtils.sha256Hex("Genesis Block"),"00000000000000000","1608617882515",Block.count);// 제네시스 블록 생성
				peerModel.blockchainModel.getBlocks().add(block); // 제네시스 블록 블록체인 Model에 저장
				dao.storeBlock(block, walletModel.getUsername()); // 제네시스 블록 DB 저장
			}
			
			return 1; // 서버 생성 성공
	}
	
	//DB안에 저장된 Peer들 갖고오기 
	public int getPeersInDB(ServerListener serverListener) {
		dtos = dao.getPeers(); // DAO를 통해서 DB안의 서버리스너 주소 갖고오기
		
		//DB에 있는 본인 서버리스너 제거
		for(int i =0; i<dtos.size();i++) {
			//DTO에 본인 정보 제거하기
			if(dtos.get(i).getLocalhost().equals("localhost:"+serverListener.getPort())) {
					System.out.println("해당 주소 삭제 : " + dtos.remove(i)); 
			}
		}
		return dtos.size();
	}

	// DB에 저장된 서버리스너 주소들에 대응하는 Peer 스레드 생성
	public int connectToPeerInDB(int i, String peerAddress) throws Exception {
	 
		//String peerAddress = peers.get(i);
		String[] address = peerAddress.split(":");
		Socket socket=null;		
		try {
				// 만약 이미 해당 주소가 연결중(return값 : true)이라면 연결을 시도하지 않는다.
				if(!validatePeers(peerAddress)) { 
							socket = new Socket();
							InetAddress ip = InetAddress.getByName(address[0]);
							int port = Integer.valueOf(address[1]);
							
							SocketAddress socketAddress = new InetSocketAddress(ip,port);
							socket.connect(socketAddress);
							/////////////////////////////blocking////////////////////////////////////////////////
							
							//Peer 스레드 생성
							PeerThread peerThread= new PeerThread(socket,peerModel);
							peerThread.setPeer(new Peer(dtos.get(i).getUsername(),dtos.get(i).getLocalhost(),peerThread));
							peerList.add(peerThread.getPeer());
							peerThread.start();//Peer스레드 실행
								
							peerThread.addressSend("localhost:"+serverListener.getPort());// peerThread가 생성되면 자신의 서버리스너 주소보내기			
							
							return 1; // 연결 성공한 경우
				}
				
				return 0; // 중복된 스레드가 있는 경우
		}catch(Exception e) {
			return -1; // 연결 실패한 경우
		}
	}
	
	//상대방 주소로 이미 만들어진 스레드가 있는지 검증하는 메소드
	private boolean validatePeers(String inputValue) {
		boolean flag = false;
		
		Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()]; // 현재 스레드가 속한 스레드 그룹에서 활동중인 스레드의 개수만큼 배열 생성
		Thread.currentThread().getThreadGroup().enumerate(threads);// 현재 스렏가 속한 스레드 그룹의 스레드들을 모두 배열에 저장
		
		ArrayList<String> peers = new ArrayList<String>(); // 여러 가지 스레드중 Peer 스레드와 Server스레드만 걸러서 peers배열에 따로 저장
		
		// 반복문과 instanceof를 통해 PeerThread와 ServerThread만 색출
		for(int i=0; i< threads.length;i++) {
			if(threads[i] instanceof PeerThread && !peers.contains(((PeerThread)threads[i]).toString()))
				peers.add(((PeerThread)threads[i]).getHostAddress()+":"+((PeerThread)threads[i]).getPort());
			else if(threads[i] instanceof ServerListener && !peers.contains(((ServerListener)threads[i]).toString()))
					peers.add(((ServerListener)threads[i]).toString());
		}
		
		//Peer스레드와 Server스레드만 걸러서 peers라는 배열에 모두 넣은 뒤 inputValue(통신하려는 대상)와 비교하여 동일한 것이 있는지 검사
		if(peers.contains(inputValue)) flag = true; // 동일한 것이 있다면 true를 반환
		return flag; // 없다면 false를 반환
	}
	
	//블록채굴
	public String mineBlock(int index) {
			String hashString = null;	
			String currentTime = Long.toString(System.currentTimeMillis()); // 현재시간 확보
			String previousHash = peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash(); //이전블럭 해시 확보
			String nonce = index+"";	
			
			// 해쉬 스트링 생성  
			hashString = DigestUtils.sha256Hex(previousHash+nonce+currentTime);
		
		   // 블록 채굴 조건에 부합하면 if문 실행
			if(hashString.substring(0,zeroNum).equals(hashDifficulty)) { 
							
							//상대 Peer에게 블록채굴완료 정보 전송하기
							System.out.println("채굴 성공!");
							HashMap<String,String> additems = new HashMap<String,String>();
							additems.put("previousHash", previousHash);
							additems.put("nonce", nonce);
							additems.put("timestamp", currentTime);																	
							serverListener.sendMessage(makeJsonObject(additems));
							
							// 검증 결과 전송하기 
							verifiedPeerCount++; // 본인 추가
							totalRespondedCount++; // 본인 추가
							
							additems = new HashMap<String,String>();
							additems.put("verified", "true");
							additems.put("blockNum",Block.count+"");													
							serverListener.sendMessage(makeJsonObject(additems));
					
							// 검증결과 기다리기 
							System.out.println("채굴 성공 후 검증 대기 중");
							
							// 임시 블럭 미리 생성해놓기
							Block.count++; //블럭넘버 1 증가시키기
							peerModel.block = new Block(previousHash,nonce,currentTime,Block.count);
							System.out.println("채굴 성공 임시 블럭 넘버 : " + Block.count);
							
							
							miningFlag = false;
						}
			
			return hashString;
	}
	
	//MiningStartButton 최신화
	public void setMiningStartButton(Button miningStartButton) {
			this.miningStartButton = miningStartButton;
	}
	
	public int verifyBlock() throws InterruptedException {
		//버튼 변화
		Platform.runLater(()->{
			miningStartButton.setDisable(true);
			miningStartButton.setText("합의 중....");
		});
		
		
		long start = System.currentTimeMillis();
		
		// 모든 Peer는 리더의 지위를 내려놓는다. 
		amILeader = false;  
		initializeLeader();
		
		// 검증시간 5초 기다리기 
		while(true) {
			long end = System.currentTimeMillis();
			System.out.println("검증 경과 시간 : " + (end - start)/1000);
			if(end - start > 4000) { 
				break;
			}
			Thread.sleep(1000); 	
		}
		
		//리더 Peer 선출하기 
		Peer leaderPeer = whoIsLeader();
		if( leaderPeer != null ) {
			System.out.println("리더 선출 완료 : "+ leaderPeer.getUserName());
			leaderPeer.getPeerThread().sendLeader();
		}else {
			System.out.println("내가 리더!");
		}
		
		//합의율 계산하기
		if(((double)verifiedPeerCount/totalRespondedCount) >= verifiedCutLine) {
				System.out.println("검증 성공한 Peer 개수 : " + verifiedPeerCount);
				System.out.println("응답한 Peer 개수 : " + totalRespondedCount);
				System.out.println("합 의 율 : " + (double)verifiedPeerCount/totalRespondedCount );
				System.out.println("합읜 성공 Block count : "+ Block.count);
				applyBlock(); // 임시블럭을 블럭체인에 적용하기
				verifiedPeerCount=0; //초기화
				totalRespondedCount = 0; //초기화
				Platform.runLater(()->{
					miningStartButton.setText("트랜잭션 처리 중...");
				});
				return 1;
		}
		else {
				System.out.println("51% 합의 실패! ");
				Block.count--; //블럭 생성에 실패한 경우, 블럭 넘버 되돌리기
				verifiedPeerCount=0; //초기화
				totalRespondedCount = 0; //초기화
				System.out.println("합읜 실패 Block count : "+ Block.count);
				Platform.runLater(()->{
					miningStartButton.setDisable(false);
					miningStartButton.setText("채굴시작");
				});
				return 2; 
		}
	} 

	
	// 블록을 블록체인과 DB에 저장하기
	public void applyBlock() {
		
		System.out.println("51% 합의 달성!");
		
		// 블록체인에 저장
		if(peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash().equals(block.getPreviousBlockHash())) {
			peerModel.blockchainModel.getBlocks().add(new Block(block.getPreviousBlockHash(), block.getNonce(),block.getTimestamp(),Block.count));
			System.out.println("> 블록 검증 완료\n"+block);
		} else { System.out.println("[ 블록체인에 블록을 추가할 수 없습니다. 유효하지 않는 이전블록 해쉬입니다. ]\n");}
		//db에 블록 저장
		dao.storeBlock(block, walletModel.getUsername());
	}
	
	//리더 Peer 선정하기
	private Peer whoIsLeader() {
		
		Peer leaderPeer = null;
		
		for(int i =0; i<peerList.size();i++) {
			if(i==0) leaderPeer = peerList.get(i);
			else {
				if(peerList.get(i).getBlockNum() > leaderPeer.getBlockNum()) {
					leaderPeer = peerList.get(i);
				}
			}	
		}
		// 자신의 블럭개수가 더 많거나 혼자 채굴한 경우
		if(leaderPeer == null || Block.count >= leaderPeer.getBlockNum()) {
			amILeader = true; // 자기 자신이 리더가 되기
			return null;
		}else {
			leaderPeer.setLeader(true);
			return leaderPeer;
		}				
	}
	//리더 설정 초기화하기
	public void initializeLeader() {
		for(Peer peer : peerList) {
			peer.setLeader(false);
		}
	}
	//리더 추출하기
	public Peer getLeader() {
		for(Peer peer : peerList) {
			if(peer.isLeader()) {
				return peer;
			}
		}
		return null;
	}
	
	//JsonObject 만들기
	public String makeJsonObject(HashMap<String,String> additems) {
			StringWriter sw = new StringWriter();
			JsonObjectBuilder job = Json.createObjectBuilder();
			
			Set<String> keyset = additems.keySet();
			Iterator<String> iterator = keyset.iterator();
			
			while(iterator.hasNext()) {
				String key = iterator.next();
				String value = additems.get(key);
				
				job.add(key, value);
			}
			
			Json.createWriter(sw).writeObject(job.build());
			
			return sw.toString();
	}
	// 트랜잭션 처리하기
	public boolean processTransaction(Transaction tx) {
		
		float total = 0;
		float balance = 0;
		float value = tx.value;
		
		for(int i = 0; i < tx.inputs.size(); i++) {
			total += tx.inputs.get(i).getInputValue();
		}
		// 총 금액이 송금액보다 작으면 false이다. 
		if(value > total) {
			return false;
			
		}else { // 총 금액이 송금액보다 크면 true이다. 
			balance = total - value;
			
			//트랜잭션의 UTXO 생성
			UTXOs.add(new TransactionOutput(tx.recipient, value, walletModel.getUsername()));
			UTXOs.get(UTXOs.size()-1).generateHash();
			tx.outputs.add(UTXOs.get(UTXOs.size()-1));
			
			//잔액 UTXO 생성
			UTXOs.add(new TransactionOutput(tx.sender, balance, walletModel.getUsername()));
			UTXOs.get(UTXOs.size()-1).generateHash();
			tx.outputs.add(UTXOs.get(UTXOs.size()-1));
						
			return true;
		}	
	}
	
	
	public static class Peer{
		PublicKey publickey = null;
		PeerThread peerThread = null;
		String userName = null;
		String localhost = null;
		boolean leader = false;	
		int blockNum = 0;
		
		public Peer(String userName, String localhost,PeerThread peerThread) {
			this.userName = userName;
			this.localhost = localhost;
			this.peerThread = peerThread;
		}
		
		public int getBlockNum() {
			return blockNum;
		}

		public void setBlockNum(int blockNum) {
			this.blockNum = blockNum;
		}

		public boolean isLeader() {
			return leader;
		}
		public void setLeader(boolean leader) {
			this.leader = leader;
		}
		
		public String getLocalhost() {
			return localhost;
		}
		public void setLocalhost(String localhost) {
			this.localhost = localhost;
		}
		public PublicKey getPublickey() {
			return publickey;
		}
		public PeerThread getPeerThread() {
			return peerThread;
		}
		public String getUserName() {
			return userName;
		}
		public void setPublickey(PublicKey publickey) {
			this.publickey = publickey;
		}
		public void setPeerThread(PeerThread peerThread) {
			this.peerThread = peerThread;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		@Override
		public String toString() {
			String result = userName +"의 Peer \n";
			result += "localhost : " + localhost;
			
			return result;
		}
	}
	
}
