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
	public Block block = null; // ���� �ֽ� ��
	public boolean proofOfWorkCompleteFlag = true;
	public BlockchainModel blockchainModel =null;
	DAO dao = null;
	PeerModel peerModel;
	public WalletModel walletModel = null;
	private ServerListener serverListener =null;
	public int verifiedPeerCount = 0; // ä���� �� ���� ������ Peer�� ����
	public double totalRespondedCount = 0; // �� ���� ����� ������ Peer�� ����
	double verifiedCutLine = 0.51;
	double connectedPeersSize; // ��������
	public boolean amILeader = false;
	public boolean miningFlag = false;
	public Stage primaryStage;
	public boolean isFirst = true;
	public Button miningStartButton;
	
	public Vector<TransactionOutput> UTXOs = new Vector<TransactionOutput>(); // ������ ���� ���������� ������ ����Ʈ���� Vector�� ����
	public ArrayList<DTO> dtos = new ArrayList<DTO>();
	public ArrayList<Peer> peerList = new ArrayList<Peer>();
	public ArrayList<Transaction> transactionList = new ArrayList<Transaction>();


	public ServerListener getServerListerner() {
		return peerModel.serverListener;
	}
	
	//P2P ��Ʈ��ũ ������ ���� ���������ϱ�
	public int doConnection(PeerModel peerModel, WalletModel walletModel, BlockchainModel blockchainModel,Stage primaryStage) throws IOException, Exception {
			//���׽ý� �� : ���� �� ����
			this.peerModel = peerModel;
			this.walletModel = walletModel;
			this.blockchainModel = blockchainModel;
			this.primaryStage = primaryStage;
			
			//���������� ����
			String[] address = walletModel.getUserLocalHost().split(":");
			String portNumb = address[1];
			
			try {
				serverListener = new ServerListener(portNumb,peerModel); // ���������� ����
				serverListener.start();

			} catch (IOException e) {
				System.out.println("���������� ���� ����( �ּ� ���ε� �� �ߺ� �߻� ) ");
				return -1; // ���� ���� ����
			} // ��Ʈ��ȣ�� ���������� ����
			
			dao = new DAO();
			
			//ȸ�������� ���� ���ٽÿ��� ���׽ý� ��� ����
			if(blockchainModel.getBlocks().size()==0) {
				block = new Block(DigestUtils.sha256Hex("Genesis Block"),"00000000000000000","1608617882515",Block.count);// ���׽ý� ��� ����
				peerModel.blockchainModel.getBlocks().add(block); // ���׽ý� ��� ���ü�� Model�� ����
				dao.storeBlock(block, walletModel.getUsername()); // ���׽ý� ��� DB ����
			}
			
			return 1; // ���� ���� ����
	}
	
	//DB�ȿ� ����� Peer�� ������� 
	public int getPeersInDB(ServerListener serverListener) {
		dtos = dao.getPeers(); // DAO�� ���ؼ� DB���� ���������� �ּ� �������
		
		//DB�� �ִ� ���� ���������� ����
		for(int i =0; i<dtos.size();i++) {
			//DTO�� ���� ���� �����ϱ�
			if(dtos.get(i).getLocalhost().equals("localhost:"+serverListener.getPort())) {
					System.out.println("�ش� �ּ� ���� : " + dtos.remove(i)); 
			}
		}
		return dtos.size();
	}

	// DB�� ����� ���������� �ּҵ鿡 �����ϴ� Peer ������ ����
	public int connectToPeerInDB(int i, String peerAddress) throws Exception {
	 
		//String peerAddress = peers.get(i);
		String[] address = peerAddress.split(":");
		Socket socket=null;		
		try {
				// ���� �̹� �ش� �ּҰ� ������(return�� : true)�̶�� ������ �õ����� �ʴ´�.
				if(!validatePeers(peerAddress)) { 
							socket = new Socket();
							InetAddress ip = InetAddress.getByName(address[0]);
							int port = Integer.valueOf(address[1]);
							
							SocketAddress socketAddress = new InetSocketAddress(ip,port);
							socket.connect(socketAddress);
							/////////////////////////////blocking////////////////////////////////////////////////
							
							//Peer ������ ����
							PeerThread peerThread= new PeerThread(socket,peerModel);
							peerThread.setPeer(new Peer(dtos.get(i).getUsername(),dtos.get(i).getLocalhost(),peerThread));
							peerList.add(peerThread.getPeer());
							peerThread.start();//Peer������ ����
								
							peerThread.addressSend("localhost:"+serverListener.getPort());// peerThread�� �����Ǹ� �ڽ��� ���������� �ּҺ�����			
							
							return 1; // ���� ������ ���
				}
				
				return 0; // �ߺ��� �����尡 �ִ� ���
		}catch(Exception e) {
			return -1; // ���� ������ ���
		}
	}
	
	//���� �ּҷ� �̹� ������� �����尡 �ִ��� �����ϴ� �޼ҵ�
	private boolean validatePeers(String inputValue) {
		boolean flag = false;
		
		Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()]; // ���� �����尡 ���� ������ �׷쿡�� Ȱ������ �������� ������ŭ �迭 ����
		Thread.currentThread().getThreadGroup().enumerate(threads);// ���� ������ ���� ������ �׷��� ��������� ��� �迭�� ����
		
		ArrayList<String> peers = new ArrayList<String>(); // ���� ���� �������� Peer ������� Server�����常 �ɷ��� peers�迭�� ���� ����
		
		// �ݺ����� instanceof�� ���� PeerThread�� ServerThread�� ����
		for(int i=0; i< threads.length;i++) {
			if(threads[i] instanceof PeerThread && !peers.contains(((PeerThread)threads[i]).toString()))
				peers.add(((PeerThread)threads[i]).getHostAddress()+":"+((PeerThread)threads[i]).getPort());
			else if(threads[i] instanceof ServerListener && !peers.contains(((ServerListener)threads[i]).toString()))
					peers.add(((ServerListener)threads[i]).toString());
		}
		
		//Peer������� Server�����常 �ɷ��� peers��� �迭�� ��� ���� �� inputValue(����Ϸ��� ���)�� ���Ͽ� ������ ���� �ִ��� �˻�
		if(peers.contains(inputValue)) flag = true; // ������ ���� �ִٸ� true�� ��ȯ
		return flag; // ���ٸ� false�� ��ȯ
	}
	
	//���ä��
	public String mineBlock(int index) {
			String hashString = null;	
			String currentTime = Long.toString(System.currentTimeMillis()); // ����ð� Ȯ��
			String previousHash = peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash(); //������ �ؽ� Ȯ��
			String nonce = index+"";	
			
			// �ؽ� ��Ʈ�� ����  
			hashString = DigestUtils.sha256Hex(previousHash+nonce+currentTime);
		
		   // ��� ä�� ���ǿ� �����ϸ� if�� ����
			if(hashString.substring(0,zeroNum).equals(hashDifficulty)) { 
							
							//��� Peer���� ���ä���Ϸ� ���� �����ϱ�
							System.out.println("ä�� ����!");
							HashMap<String,String> additems = new HashMap<String,String>();
							additems.put("previousHash", previousHash);
							additems.put("nonce", nonce);
							additems.put("timestamp", currentTime);																	
							serverListener.sendMessage(makeJsonObject(additems));
							
							// ���� ��� �����ϱ� 
							verifiedPeerCount++; // ���� �߰�
							totalRespondedCount++; // ���� �߰�
							
							additems = new HashMap<String,String>();
							additems.put("verified", "true");
							additems.put("blockNum",Block.count+"");													
							serverListener.sendMessage(makeJsonObject(additems));
					
							// ������� ��ٸ��� 
							System.out.println("ä�� ���� �� ���� ��� ��");
							
							// �ӽ� �� �̸� �����س���
							Block.count++; //���ѹ� 1 ������Ű��
							peerModel.block = new Block(previousHash,nonce,currentTime,Block.count);
							System.out.println("ä�� ���� �ӽ� �� �ѹ� : " + Block.count);
							
							
							miningFlag = false;
						}
			
			return hashString;
	}
	
	//MiningStartButton �ֽ�ȭ
	public void setMiningStartButton(Button miningStartButton) {
			this.miningStartButton = miningStartButton;
	}
	
	public int verifyBlock() throws InterruptedException {
		//��ư ��ȭ
		Platform.runLater(()->{
			miningStartButton.setDisable(true);
			miningStartButton.setText("���� ��....");
		});
		
		
		long start = System.currentTimeMillis();
		
		// ��� Peer�� ������ ������ �������´�. 
		amILeader = false;  
		initializeLeader();
		
		// �����ð� 5�� ��ٸ��� 
		while(true) {
			long end = System.currentTimeMillis();
			System.out.println("���� ��� �ð� : " + (end - start)/1000);
			if(end - start > 4000) { 
				break;
			}
			Thread.sleep(1000); 	
		}
		
		//���� Peer �����ϱ� 
		Peer leaderPeer = whoIsLeader();
		if( leaderPeer != null ) {
			System.out.println("���� ���� �Ϸ� : "+ leaderPeer.getUserName());
			leaderPeer.getPeerThread().sendLeader();
		}else {
			System.out.println("���� ����!");
		}
		
		//������ ����ϱ�
		if(((double)verifiedPeerCount/totalRespondedCount) >= verifiedCutLine) {
				System.out.println("���� ������ Peer ���� : " + verifiedPeerCount);
				System.out.println("������ Peer ���� : " + totalRespondedCount);
				System.out.println("�� �� �� : " + (double)verifiedPeerCount/totalRespondedCount );
				System.out.println("���� ���� Block count : "+ Block.count);
				applyBlock(); // �ӽú��� ��ü�ο� �����ϱ�
				verifiedPeerCount=0; //�ʱ�ȭ
				totalRespondedCount = 0; //�ʱ�ȭ
				Platform.runLater(()->{
					miningStartButton.setText("Ʈ����� ó�� ��...");
				});
				return 1;
		}
		else {
				System.out.println("51% ���� ����! ");
				Block.count--; //�� ������ ������ ���, �� �ѹ� �ǵ�����
				verifiedPeerCount=0; //�ʱ�ȭ
				totalRespondedCount = 0; //�ʱ�ȭ
				System.out.println("���� ���� Block count : "+ Block.count);
				Platform.runLater(()->{
					miningStartButton.setDisable(false);
					miningStartButton.setText("ä������");
				});
				return 2; 
		}
	} 

	
	// ����� ���ü�ΰ� DB�� �����ϱ�
	public void applyBlock() {
		
		System.out.println("51% ���� �޼�!");
		
		// ���ü�ο� ����
		if(peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash().equals(block.getPreviousBlockHash())) {
			peerModel.blockchainModel.getBlocks().add(new Block(block.getPreviousBlockHash(), block.getNonce(),block.getTimestamp(),Block.count));
			System.out.println("> ��� ���� �Ϸ�\n"+block);
		} else { System.out.println("[ ���ü�ο� ����� �߰��� �� �����ϴ�. ��ȿ���� �ʴ� ������� �ؽ��Դϴ�. ]\n");}
		//db�� ��� ����
		dao.storeBlock(block, walletModel.getUsername());
	}
	
	//���� Peer �����ϱ�
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
		// �ڽ��� �������� �� ���ų� ȥ�� ä���� ���
		if(leaderPeer == null || Block.count >= leaderPeer.getBlockNum()) {
			amILeader = true; // �ڱ� �ڽ��� ������ �Ǳ�
			return null;
		}else {
			leaderPeer.setLeader(true);
			return leaderPeer;
		}				
	}
	//���� ���� �ʱ�ȭ�ϱ�
	public void initializeLeader() {
		for(Peer peer : peerList) {
			peer.setLeader(false);
		}
	}
	//���� �����ϱ�
	public Peer getLeader() {
		for(Peer peer : peerList) {
			if(peer.isLeader()) {
				return peer;
			}
		}
		return null;
	}
	
	//JsonObject �����
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
	// Ʈ����� ó���ϱ�
	public boolean processTransaction(Transaction tx) {
		
		float total = 0;
		float balance = 0;
		float value = tx.value;
		
		for(int i = 0; i < tx.inputs.size(); i++) {
			total += tx.inputs.get(i).getInputValue();
		}
		// �� �ݾ��� �۱ݾ׺��� ������ false�̴�. 
		if(value > total) {
			return false;
			
		}else { // �� �ݾ��� �۱ݾ׺��� ũ�� true�̴�. 
			balance = total - value;
			
			//Ʈ������� UTXO ����
			UTXOs.add(new TransactionOutput(tx.recipient, value, walletModel.getUsername()));
			UTXOs.get(UTXOs.size()-1).generateHash();
			tx.outputs.add(UTXOs.get(UTXOs.size()-1));
			
			//�ܾ� UTXO ����
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
			String result = userName +"�� Peer \n";
			result += "localhost : " + localhost;
			
			return result;
		}
	}
	
}
