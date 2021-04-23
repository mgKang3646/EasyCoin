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
	
	public Block block = null; // ���� �ֽ� ��
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
	public ArrayList<PeerThread> peerThs; //����� ��� Peer ���������ʿ��� �ش� Peer ���������� �����Ͽ� ��� PeerThread�� ���Ͽ��� ����
	

	public int verifiedPeerCount = 0; // ä���� �� ���� ������ Peer�� ����
	public double totalRespondedCount = 0; // �� ���� ����� ������ Peer�� ����
	
	double verifiedCutLine = 0.51;
	double connectedPeersSize; // ��������
	
	public HashMap<PeerThread,Integer> peerBlockNums = new HashMap<PeerThread,Integer>(); 
	public PeerThread threadForLeaderPeer = null;
	public boolean isFirstResponse = true;
	
	public boolean amILeader = false;
	public boolean miningFlag = false;
	
	public Stage primaryStage;
	
	
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
				dao.storeBlock(block, walletModel.getPrivateKey()); // ���׽ý� ��� DB ����
			}
			
			return 1; // ���� ���� ����
	}
	
	//DB�ȿ� ����� Peer�� ������� 
	public int getPeersInDB(ServerListener serverListener) {
		
		peers =  new ArrayList<String>();
		peerThs = new ArrayList<PeerThread>();
		
		peers = dao.getPeersLocalhost(); // DAO�� ���ؼ� DB���� ���������� �ּ� �������
		
		//DB�� �ִ� ���� ���������� ����
		for(int i =0; i<peers.size(); i++) {
				if(peers.get(i).equals("localhost:"+serverListener.getPort())) {
					System.out.println("�ش� �ּ� ���� : " + peers.remove(i));
				}
			}
		
		System.out.println("peers ����Ʈ ���� : " + peers.size());
		
		return peers.size();
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
							
							PeerThread peerThread= new PeerThread(socket,peerModel);//Peer ������ ����
							peerThs.add(peerThread); // ���� �Ϸ�� PeerThread�� ����Ʈ�� ����
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
							StringWriter sw = new StringWriter();
							Json.createWriter(sw).writeObject(Json.createObjectBuilder()
																.add("previousHash", previousHash)
																.add("nonce", nonce)
																.add("timestamp", currentTime)
																.build());
														
							serverListener.sendMessage(sw.toString());
							
							// ���� ��� �����ϱ� 
							verifiedPeerCount++; // ���� �߰�
							totalRespondedCount++; // ���� �߰�
							
							StringWriter sW = new StringWriter();
							Json.createWriter(sW).writeObject(Json.createObjectBuilder()
														.add("verified", "true")
														.add("blockNum",Block.count) // ���� �������� ���� Peer�� �ľ��ϱ� ���� ��������
																	.build());		
							serverListener.sendMessage(sW.toString());
					
							// ������� ��ٸ��� 
							System.out.println("ä�� ���� �� ���� ��� ��");
							
							// �ӽ� �� �̸� �����س���
							Block.count++; //���ѹ� 1 ������Ű��
							peerModel.block = new Block(previousHash,nonce,currentTime,Block.count);
							
							
							miningFlag = false;
						}
			
			return hashString;
	}
	
	public int verifyBlock() throws InterruptedException {
		
		long start = System.currentTimeMillis();
		amILeader = false;  // �����ϴ� �������� ��� Peer�� ������ ������ �������´�. 
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
		if(whoIsLeader() !=null ) {
			System.out.println("���� ���� �Ϸ� : "+ threadForLeaderPeer.getHostAddress()+":"+threadForLeaderPeer.getPort());
			threadForLeaderPeer.sendLeader();
		
		}else {
			System.out.println("���� ����!");
		}
		
		if(((double)verifiedPeerCount/totalRespondedCount) >= verifiedCutLine) {
				System.out.println("���� ������ Peer ���� : " + verifiedPeerCount);
				System.out.println("������ Peer ���� : " + totalRespondedCount);
				System.out.println("�� �� �� : " + (double)verifiedPeerCount/totalRespondedCount );
				applyBlock(); // �ӽú��� ��ü�ο� �����ϱ�
				verifiedPeerCount=0; //�ʱ�ȭ
				totalRespondedCount = 0; //�ʱ�ȭ
				return 1;
		}
		else {
				System.out.println("51% ���� ����! ");
				Block.count--; //�� ������ ������ ���, �� �ѹ� �ǵ�����
				verifiedPeerCount=0; //�ʱ�ȭ
				totalRespondedCount = 0; //�ʱ�ȭ
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
		dao.storeBlock(block, walletModel.getPrivateKey());
	}
	
	//���� ��� �����ϱ�
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
		// �ڽ��� �������� �� ���ų� ȥ�� ä���� ���
		if(Block.count >= biggestNum || leaderPeer == null) {
			amILeader = true; // �ڱ� �ڽ��� ������ �Ǳ�
		}
			
		threadForLeaderPeer = leaderPeer; // 
		
		return leaderPeer;
	}
	
	//���� ��� ���� ������� �з�
	private void listConnections() {
		// ���� Ȱ�� ���� ������� ��� �ҷ����̱�
		Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
		Thread.currentThread().getThreadGroup().enumerate(threads);
		
		//�ݺ����� instanceof Ű���带 ���� �з�
		for(int i =0; i<threads.length;i++) {
			if(threads[i] instanceof PeerThread) {
				if(!isOverlap(threads[i])) { // ��ġ�� �ʴ� ���
					peerThreads.add((PeerThread)threads[i]);
				}
			}
			else if(threads[i] instanceof ServerThread) {
				if(!isOverlap(threads[i])) { // ��ġ�� �ʴ� ���
					serverThreads.add((ServerThread)threads[i]);
				}
			}
			else if(threads[i] instanceof ServerListener)  {
				if(!isOverlap(threads[i])) { // ��ġ�� �ʴ� ���
					serverListeners.add((ServerListener)threads[i]);
				}
			}
		}
	}
	// �������� ���ڿ� ���� ��ġ�°� �ִ��� Ȯ��
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
