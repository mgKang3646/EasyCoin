package model;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;

import org.apache.commons.codec.digest.DigestUtils;

import database.DAO;
import javafx.stage.Stage;


public class PeerModel {
	public String hashDifficulty = "00000";
	public int zeroNum = 5;
	
	public Block block = null; // 가장 최신 블럭
	public boolean proofOfWorkCompleteFlag = true;
	public BlockchainModel blockchainModel =null;
	DAO dao = null;
	PeerModel peerModel;
	public WalletModel walletModel = null;
	private ServerListener serverListener =null;
	
	ArrayList<PeerThread> peerThreads = new ArrayList<PeerThread>();
	ArrayList<ServerListener> serverListeners = new ArrayList<ServerListener>();
	ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();
	public ArrayList<String> peers;
	public ArrayList<PeerThread> peerThs; //연결된 상대 Peer 서버리스너에게 해당 Peer 서버리스너 전달하여 상대 PeerThread와 소켓연결 유도
	

	public int verifiedPeerCount = 0; // 채굴된 블럭 검증 성공한 Peer의 개수
	public double totalRespondedCount = 0; // 블럭 검증 결과를 전송한 Peer의 개수
	
	double verifiedCutLine = 0.51;
	double connectedPeersSize; // 본인포함
	
	public HashMap<PeerThread,Integer> peerBlockNums = new HashMap<PeerThread,Integer>(); 
	public PeerThread threadForLeaderPeer = null;
	public boolean isFirstResponse = true;
	
	public boolean amILeader = false;
	public boolean miningFlag = false;
	
	public Stage primaryStage;
	
	
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
				dao.storeBlock(block, walletModel.getPrivateKey()); // 제네시스 블록 DB 저장
			}
			
			return 1; // 서버 생성 성공
	}
	
	//DB안에 저장된 Peer들 갖고오기 
	public int getPeersInDB(ServerListener serverListener) {
		
		peers =  new ArrayList<String>();
		peerThs = new ArrayList<PeerThread>();
		
		peers = dao.getPeersLocalhost(); // DAO를 통해서 DB안의 서버리스너 주소 갖고오기
		
		//DB에 있는 본인 서버리스너 제거
		for(int i =0; i<peers.size(); i++) {
				if(peers.get(i).equals("localhost:"+serverListener.getPort())) {
					System.out.println("해당 주소 삭제 : " + peers.remove(i));
				}
			}
		
		System.out.println("peers 리스트 개수 : " + peers.size());
		
		return peers.size();
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
							
							PeerThread peerThread= new PeerThread(socket,peerModel);//Peer 스레드 생성
							peerThs.add(peerThread); // 연결 완료된 PeerThread들 리스트에 저장
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
							StringWriter sw = new StringWriter();
							Json.createWriter(sw).writeObject(Json.createObjectBuilder()
																.add("previousHash", previousHash)
																.add("nonce", nonce)
																.add("timestamp", currentTime)
																.build());
														
							serverListener.sendMessage(sw.toString());
							
							// 검증 결과 전송하기 
							verifiedPeerCount++; // 본인 추가
							totalRespondedCount++; // 본인 추가
							
							StringWriter sW = new StringWriter();
							Json.createWriter(sW).writeObject(Json.createObjectBuilder()
														.add("verified", "true")
														.add("blockNum",Block.count) // 검증 과정에서 리더 Peer를 파악하기 위한 정보제공
																	.build());		
							serverListener.sendMessage(sW.toString());
					
							// 검증결과 기다리기 
							System.out.println("채굴 성공 후 검증 대기 중");
							
							// 임시 블럭 미리 생성해놓기
							Block.count++; //블럭넘버 1 증가시키기
							peerModel.block = new Block(previousHash,nonce,currentTime,Block.count);
							
							
							miningFlag = false;
						}
			
			return hashString;
	}
	
	public int verifyBlock() throws InterruptedException {
		
		long start = System.currentTimeMillis();
		amILeader = false;  // 검증하는 과정에서 모든 Peer는 리더의 지위를 내려놓는다. 
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
		if(whoIsLeader() !=null ) {
			System.out.println("리더 선출 완료 : "+ threadForLeaderPeer.getHostAddress()+":"+threadForLeaderPeer.getPort());
			threadForLeaderPeer.sendLeader();
		
		}else {
			System.out.println("내가 리더!");
		}
		
		if(((double)verifiedPeerCount/totalRespondedCount) >= verifiedCutLine) {
				System.out.println("검증 성공한 Peer 개수 : " + verifiedPeerCount);
				System.out.println("응답한 Peer 개수 : " + totalRespondedCount);
				System.out.println("합 의 율 : " + (double)verifiedPeerCount/totalRespondedCount );
				applyBlock(); // 임시블럭을 블럭체인에 적용하기
				verifiedPeerCount=0; //초기화
				totalRespondedCount = 0; //초기화
				return 1;
		}
		else {
				System.out.println("51% 합의 실패! ");
				Block.count--; //블럭 생성에 실패한 경우, 블럭 넘버 되돌리기
				verifiedPeerCount=0; //초기화
				totalRespondedCount = 0; //초기화
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
		dao.storeBlock(block, walletModel.getPrivateKey());
	}
	
	//리더 블록 선정하기
	private PeerThread whoIsLeader() {
		
		Set<PeerThread> keySet = peerBlockNums.keySet();
		Iterator<PeerThread> iterator = keySet.iterator();
		
		int biggestNum =0;
		PeerThread leaderPeer = null;
		int count =0;
		
		while(iterator.hasNext()) {
			PeerThread pt = iterator.next();
			if(count ==0) {
				leaderPeer = pt;
				biggestNum = peerBlockNums.get(pt);
			}else {
				if(peerBlockNums.get(pt) > biggestNum) {
					biggestNum = peerBlockNums.get(pt);
					leaderPeer = pt;
				}
			}
		}
		// 자신의 블럭개수가 더 많거나 혼자 채굴한 경우
		if(Block.count >= biggestNum || leaderPeer == null) {
			amILeader = true; // 자기 자신이 리더가 되기
		}
			
		threadForLeaderPeer = leaderPeer; // 
		
		return leaderPeer;
	}
	
	//현재 사용 중인 스레드들 분류
	private void listConnections() {
		// 현재 활동 중인 스레드들 모두 불러들이기
		Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
		Thread.currentThread().getThreadGroup().enumerate(threads);
		
		//반복문과 instanceof 키워드를 통해 분류
		for(int i =0; i<threads.length;i++) {
			if(threads[i] instanceof PeerThread) {
				if(!isOverlap(threads[i])) { // 겹치지 않는 경우
					peerThreads.add((PeerThread)threads[i]);
				}
			}
			else if(threads[i] instanceof ServerThread) {
				if(!isOverlap(threads[i])) { // 겹치지 않는 경우
					serverThreads.add((ServerThread)threads[i]);
				}
			}
			else if(threads[i] instanceof ServerListener)  {
				if(!isOverlap(threads[i])) { // 겹치지 않는 경우
					serverListeners.add((ServerListener)threads[i]);
				}
			}
		}
	}
	// 스레드의 문자열 값이 겹치는게 있는지 확인
	private boolean isOverlap(Object obj) {
		
		if(obj instanceof PeerThread) {
			obj = (PeerThread)obj;
			for(int i=0; i < peerThreads.size(); i++) {
				if(obj.toString().equals(peerThreads.get(i).toString())){
					return true;
				}
			}
			return false;
		}
		else if(obj instanceof ServerThread) {
			obj = (ServerThread)obj;
			for(int i=0; i < serverThreads.size(); i++) {
				if(obj.toString().equals(serverThreads.get(i).toString())) return true;
			}
			return false;
		}
		else if(obj instanceof ServerListener) {
			obj = (ServerListener)obj;
			for(int i=0; i < serverListeners.size(); i++) {
				if(obj.toString().equals(serverListeners.get(i).toString())) return true;
			}
			return false;
		}
		
		return false;
	}
}
