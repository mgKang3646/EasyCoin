package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

import controller.MiningVerifyController;
import database.DAO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PeerThread extends Thread {

	private BufferedReader bufferedReader;
	private int port;
	private int localPort;
	private String localHostAddress = null;
	private String hostAddress = null;
	private Socket socket = null;
	private PeerModel peerModel;
	PrintWriter printWriter;
	private DAO dao = null;
	private int requestCount = 0;
	

	public PeerThread(Socket socket, PeerModel peerModel) throws IOException{
		this.peerModel = peerModel;
		this.socket = socket;
		this.localHostAddress = socket.getLocalAddress().getHostName(); // �ش� Peer�� ���� �ּ�
		this.localPort = socket.getLocalPort(); // �ش� Peer�� ��Ʈ
		this.port = socket.getPort(); // ����� ������ ��Ʈ
		this.hostAddress = socket.getInetAddress().getHostName(); // ����� ������ �ּ�
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // ������ inputStream�� bufferedReader ���ۿ� �����Ű��
		this.printWriter = new PrintWriter(socket.getOutputStream(),true); // ��� ����������� ������ PrintWriter
		this.dao = new DAO();
	}
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				// ��������� Ʈ����� ������ ���ʿ��� ó��
				JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
				///////////////////////////////blocking///////////////////////////////////
				// �ٸ� Peer���� ����� ä���� �Ϸ��� ��� ����
				if(jsonObject.containsKey("nonce")) {
					// ��Ȯ�� ������ ���� �ʱ�ȭ
					peerModel.totalRespondedCount=0;
					peerModel.verifiedPeerCount =0;
					
					// ���� ����
					String previousHash = peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash();// ���� �ؽ�
					String nonce = jsonObject.getString("nonce");
					String timestamp = jsonObject.getString("timestamp");
					
					int result = verifyBlock(previousHash,nonce,timestamp); // �� ��
						// ���� ���� ����� ��� Peer���� ������
						// ? ����� ��ȣȭ�Ͽ� �����غ��� 
					
					peerModel.totalRespondedCount += 1; // ���� 
					StringWriter sW = new StringWriter();
					
					if(result == 1) { // ���� ����
						Json.createWriter(sW).writeObject(Json.createObjectBuilder()
																.add("verified", "true")
																.add("blockNum",Block.count) // ���� �������� ���� Peer�� �ľ��ϱ� ���� ��������
																.build());		
						peerModel.getServerListerner().sendMessage(sW.toString());
						peerModel.verifiedPeerCount += 1; // ����
					}else { // ���� ����
						Json.createWriter(sW).writeObject(Json.createObjectBuilder()
																.add("verified", "false")
																.add("blockNum", Block.count)
																.build());
						peerModel.getServerListerner().sendMessage(sW.toString());
					}
					
					// �ӽ� �� �̸� �����س���
					Block.count++; //���ѹ� 1 ������Ű��
					peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
					
					if(!peerModel.proofOfWorkCompleteFlag) peerModel.proofOfWorkCompleteFlag = true;	// ���� ä�� ���̶��
					else { // ���� ä������ �ƴ϶��
						System.out.println("ä�� ���� �ƴ� ��� ���� ����");
						Thread verifyThread = new Thread() {
							public void run() {
									try {
										int result = peerModel.verifyBlock();
										if(result == 1) {
											Platform.runLater(()->{
												try {
													peerModel.proofOfWorkCompleteFlag = true;
													FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
													
													Parent root = loader.load();
													root.minWidth(350);
													root.maxWidth(350);
													root.prefWidth(350);
													
													Scene scene = new Scene(root);
													Stage stage = new Stage();
													stage.setScene(scene);
											
													stage.setX(peerModel.primaryStage.getX()+320);
													stage.setY(peerModel.primaryStage.getY());
													stage.show();
													
													MiningVerifyController bc = loader.getController();
													bc.resultOfVerify(peerModel.block);	
													
												} catch (IOException e) {e.printStackTrace();}
											});
										}else {
											Platform.runLater(()->{
												try {
													peerModel.proofOfWorkCompleteFlag = true;
													FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
													Parent root = loader.load();
													root.minWidth(350);
													root.maxWidth(350);
													root.prefWidth(350);
													Scene scene = new Scene(root);
													Stage stage = new Stage();
													stage.setScene(scene);
													stage.setX(peerModel.primaryStage.getX()+320);
													stage.setY(peerModel.primaryStage.getY());
													stage.show();
													
													MiningVerifyController bc = loader.getController();
													bc.failedVerify(peerModel.block);	
													
												} catch (IOException e) {e.printStackTrace();}
											});
										}		
									} catch (InterruptedException e) {e.printStackTrace();} 
							}
							
						};
						
						verifyThread.start();
						
					} 
				}
			
				
				//�ٸ� Peer���� ���� ���
				if(jsonObject.containsKey("verified")) {
						peerModel.totalRespondedCount++; // �ٸ� Peer �߰� 
						if(jsonObject.getString("verified").equals("true")){ // ���� �����̸� true
							System.out.println(hostAddress +":"+this.getPort()+ "�� �������� : true");
							peerModel.peerBlockNums.put(this, jsonObject.getInt("blockNum")); // HashMap�� �ش�Peer�� ����� PeerThread�� �� �ѹ��� ����
							peerModel.verifiedPeerCount++;
						}else { 
							System.out.println(hostAddress +":"+this.getPort() + "�� �������� : false"); 
							peerModel.peerBlockNums.put(this, jsonObject.getInt("blockNum")); // HashMap�� �ش�Peer�� ����� PeerThread�� �� �ѹ��� ����
							}		
				}
				//��� ��û �� ���� Peer�κ��� �� �ޱ�
				if(jsonObject.containsKey("requestVerified")) {
					boolean requestVerified = jsonObject.getBoolean("requestVerified");
					String previousHash = jsonObject.getString("RpreviousHash");
					String nonce = jsonObject.getString("Rnonce");
					String timestamp = jsonObject.getString("Rtimestamp");
					
					//������ �Ϸ�Ǿ� �Ϻ� ��ϸ� �޴� ���
					if(requestVerified) {
						Block.count++;
						peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
						peerModel.blockchainModel.getBlocks().add(new Block(previousHash,nonce,timestamp,Block.count));
						dao.storeBlock(peerModel.block,peerModel.walletModel.getUsername());
						
					}else { //������ �����Ͽ� ó������ �޴� ���
						if(peerModel.isFirstResponse) {// json request�� ����(response)�� ó�� ������ ���
							Block.count = 0; 
							dao.deleteAllBlock(peerModel.walletModel.getUsername());
							peerModel.blockchainModel.resetBlocks().add(new Block(previousHash,nonce,timestamp,Block.count)); // ���ο� ���ü�ο� ����
							peerModel.isFirstResponse = false;
						}else {
							Block.count++;
							peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
							peerModel.blockchainModel.getBlocks().add(new Block(previousHash,nonce,timestamp,Block.count));
							dao.storeBlock(peerModel.block, peerModel.walletModel.getUsername());
						}
					}
				}
				//������ ������� �˸��� Peer�� ����� PeerThread�� PeerModel ������ ����
				if(jsonObject.containsKey("leader")) {
					if(jsonObject.getInt("leaderBlockNum") > Block.count) {
						peerModel.threadForLeaderPeer = this; 
						peerModel.amILeader = false;
					}else { // ���� �ڽ��� �� �� ������ ���� ���
						peerModel.amILeader = true;
						StringWriter sw = new StringWriter();
						//�ڽ��� ���� �� ���ٰ� �˷��ֱ� 
						Json.createWriter(sw).writeObject(Json.createObjectBuilder()
															.add("biggerThanYou", Block.count)
															.build());
						printWriter.println(sw.toString());
					}
				}
				
				//�ŷ� Ʈ������� ��� �� ���
				if(jsonObject.containsKey("signature")) {
					
					//�۱��� ����Ű ȹ��
					byte[] byteSender = Base64.decode(jsonObject.getString("sender"));
					X509EncodedKeySpec spec1 = new X509EncodedKeySpec(byteSender);
					KeyFactory factory1 = KeyFactory.getInstance("ECDSA","BC");
					PublicKey sender = factory1.generatePublic(spec1);
					//������ ����Ű ȹ��
					byte[] byteRecipient = Base64.decode(jsonObject.getString("recipient"));
					X509EncodedKeySpec spec2 = new X509EncodedKeySpec(byteRecipient);
					KeyFactory factory2 = KeyFactory.getInstance("ECDSA","BC");
					PublicKey recipient = factory2.generatePublic(spec2);
					
					//�۱ݾ� ȹ��
					float value = Float.parseFloat(jsonObject.getString("value"));
					
					//���ڼ��� ȹ��
					byte[] signature = Base64.decode(jsonObject.getString("signature"));
					
					//Ʈ����ǻ���
					Transaction newTransaction = new Transaction(sender,recipient,value);
					newTransaction.setSignature(signature);
					//������ �����ϸ� Ʈ����� �߰��ϱ� 
					if(newTransaction.verifySignature()) {
						peerModel.block.getTransactionList().add(newTransaction);
						System.out.println("========== ���ڼ��� ���� �Ϸ� ============");
						System.out.println("sender : "+ newTransaction.sender);
						System.out.println("recipient : " + newTransaction.recipient);
						System.out.println("value : " + newTransaction.value +"ETC");
					}
					
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
				interrupt();
			}
		}
	}
	
	//P2P ��Ʈ��ũ ���� ����Ǿ� �ִ� Peer�ѿ��� ���� ��û ������
	public void addressSend(String localhost) throws IOException {
		StringWriter sW = new StringWriter();
		Json.createWriter(sW).writeObject(Json.createObjectBuilder()
											.add("localhost", localhost)
											.build());
		printWriter.println(sW);	
	}

	// �ٸ� Peer���� ä���� �� �����ϱ�
	private int verifyBlock(String previousBlockHash, String nonce, String timestamp) throws NoSuchAlgorithmException{
		String hashString = DigestUtils.sha256Hex(previousBlockHash+nonce+timestamp); // �ش� �����͵��� SHA256���� �ؽ�
		// ����  Pow_MAX_Bound���� ������ ����
		if(hashString.substring(0,peerModel.zeroNum).equals(peerModel.hashDifficulty)) return 1;
		else return -1;	
	}
	
	// ����Peer���� �� ��û�ϱ� 
	public void requestBlock() {
			StringWriter sw = new StringWriter();
			Json.createWriter(sw).writeObject(Json.createObjectBuilder()
										.add("blockNum", Block.count) 
										.add("blockHash", peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash())
										.build());
			
			printWriter.println(sw.toString());	
	}
	
	//���� Peer���� ������ �������� �˸�, ������ ��ϰ����� ���� Peer�� ������ �ȴ�. �׷��Ƿ� �������� ������ ���� �� ����(��ϰ����� ���� ���)
	public void sendLeader() {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(Json.createObjectBuilder()
										.add("leader", true)
										.build());
		printWriter.println(sw.toString());
	}
	
	
	public int getPort() { return this.port;} // ����� ���� ������ �������� ��Ʈ ��ȯ
	public String getHostAddress() { return this.hostAddress;} // ����� ���� ������ �������� �ּ� ��ȯ
	public String toString() { return this.localHostAddress+":"+this.localPort;} // ������ �ּ�:��Ʈ ��ȯ
	
}
