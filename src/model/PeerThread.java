package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

import controller.MiningVerifyController;
import database.DAO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PeerModel.Peer;

public class PeerThread extends Thread {

	private BufferedReader bufferedReader;
	private int port;
	private int localPort;
	private String localHostAddress = null;
	private String hostAddress = null;
	private Socket socket = null;
	private PeerModel peerModel;
	private Peer peer;
	PrintWriter printWriter;
	private DAO dao = null;
	
	

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
				HashMap<String,String> additems = new HashMap<String,String>();
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
					
					if(result == 1) { // ���� ����
						additems.put("verified", "true");
						additems.put("blockNum", Block.count+"");
						peerModel.getServerListerner().sendMessage(peerModel.makeJsonObject(additems));
						peerModel.verifiedPeerCount += 1; // ����
					}else { // ���� ����
						additems.put("verified", "false");
						additems.put("blockNum", Block.count+"");
						peerModel.getServerListerner().sendMessage(peerModel.makeJsonObject(additems));
					}
					
					// �ӽ� �� �̸� �����س���
					Block.count++; //���ѹ� 1 ������Ű��
					peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
					System.out.println("���� �Ϸ� Block ī��Ʈ : "+Block.count);
					
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
													Scene scene = new Scene(root);
													Stage stage = new Stage();
													stage.setScene(scene);
													stage.setX(peerModel.primaryStage.getX()+320);
													stage.setY(peerModel.primaryStage.getY());
													stage.show();
													
													MiningVerifyController mvc = loader.getController();
													mvc.resultOfVerify(peerModel.block);	
													
												} catch (IOException e) {e.printStackTrace();}
											});
										}else {
											Platform.runLater(()->{
												try {
													peerModel.proofOfWorkCompleteFlag = true;
													FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
													Parent root = loader.load();
													Scene scene = new Scene(root);
													Stage stage = new Stage();
													stage.setScene(scene);
													stage.setX(peerModel.primaryStage.getX()+320);
													stage.setY(peerModel.primaryStage.getY());
													stage.show();
													
													MiningVerifyController mvc = loader.getController();
													mvc.failedVerify(peerModel.block);	
													
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
							peer.setBlockNum(Integer.parseInt(jsonObject.getString("blockNum"))); 
							peerModel.verifiedPeerCount++;
						}else { 
							System.out.println(hostAddress +":"+this.getPort() + "�� �������� : false"); 
							peer.setBlockNum(Integer.parseInt(jsonObject.getString("blockNum"))); 
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
						if(peerModel.isFirst) {
							Block.count = 0; 
							dao.deleteAllBlock(peerModel.walletModel.getUsername());
							peerModel.blockchainModel.resetBlocks().add(new Block(previousHash,nonce,timestamp,Block.count)); // ���ο� ���ü�ο� ����
							peerModel.isFirst = false; // �������ʹ� 0�� �ƴ� ���޾� �ޱ�
						}else {
							Block.count++;
							peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
							peerModel.blockchainModel.getBlocks().add(new Block(previousHash,nonce,timestamp,Block.count));
							dao.storeBlock(peerModel.block,peerModel.walletModel.getUsername());
						}
					}
				}
				//������ ������� �˸��� Peer�� ����� PeerThread�� PeerModel ������ ����
				if(jsonObject.containsKey("leader")) {
					if(jsonObject.getInt("leaderBlockNum") > Block.count) {
						peer.setLeader(true); 
						peerModel.amILeader = false;
					}else { // ���� �ڽ��� �� �� ������ ���� ���
						peerModel.amILeader = true;
						additems.put("biggerThanYou", Block.count+"");
						additems.put("username", peer.getUserName());
						send(peerModel.makeJsonObject(additems));
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
					String transactionHash = jsonObject.getString("transactionHash");
					//Ʈ����ǻ���
					Transaction newTransaction = new Transaction(sender,recipient,value);
					newTransaction.setSignature(signature);
					newTransaction.setHash(transactionHash);
					
					for(int i=0; i< jsonObject.getJsonArray("miners").size();i++) {
						JsonValue miner = jsonObject.getJsonArray("miners").get(i);
						JsonValue utxoHash = jsonObject.getJsonArray("utxoHashs").get(i);
						System.out.println("Ʈ����� input miner : " + miner.toString());
						System.out.println("Ʈ����� input hash : " + utxoHash.toString());
						TransactionInput input = new TransactionInput(miner.toString(),utxoHash.toString());
						input.setTransactionHash(newTransaction.getHash());
						newTransaction.inputs.add(input);
					}
				
					//���ڼ��� ������ �����ϸ� TransactionList�� �ӽ�����
					if(newTransaction.verifySignature()) {
						peerModel.transactionList.add(newTransaction); // TransactioList�� ���ǿ� �����ϸ� refresh�Ǿ�� ��.
						System.out.println("Ʈ����� ����Ʈ�� �߰�");
						//next : minerController.java
					}
				}
				
				//UTXO ��û
				if(jsonObject.containsKey("requestUTXO")) {
					System.out.println("UTXO��û �� ����");
					byte[] byteOwner = Base64.decode(jsonObject.getString("owner"));
					X509EncodedKeySpec spec = new X509EncodedKeySpec(byteOwner);
					KeyFactory factory = KeyFactory.getInstance("ECDSA","BC");
					PublicKey owner = factory.generatePublic(spec);
					
					for(int i=0; i<peerModel.UTXOs.size();i++) {
						TransactionOutput UTXO = peerModel.UTXOs.get(i);
						if(UTXO.isMine(owner)) {
							additems.put("responseUTXO","");
							additems.put("nonce",UTXO.getNonce()+"");
							additems.put("miner", UTXO.getMiner());
							additems.put("utxoHash", UTXO.getTxoHash());
							additems.put("value", UTXO.value+"");
							send(peerModel.makeJsonObject(additems));
						}
					}
				}
				
				if(jsonObject.containsKey("sendUTXO")) {					
					//UTXO ����
					String utxoHash = jsonObject.getString("sendUTXO");
					double nonce = Double.parseDouble(jsonObject.getString("nonce"));
					String miner = jsonObject.getString("miner");
					float value = Float.parseFloat(jsonObject.getString("value"));
					String txHash = jsonObject.getString("txHash");
					
					byte[] byteRecipient = Base64.decode(jsonObject.getString("recipient"));
					X509EncodedKeySpec spec = new X509EncodedKeySpec(byteRecipient);
					KeyFactory factory = KeyFactory.getInstance("ECDSA","BC");
					PublicKey recipient = factory.generatePublic(spec);
					
					//���� ����
					if(utxoHash.equals(DigestUtils.sha256(miner+nonce+value+recipient))) {
						System.out.println("ä�� ���� �� : UTXO ���� ���� �� ���� �Ϸ�");
						TransactionOutput UTXO = new TransactionOutput(recipient,value,miner);
						UTXO.setHash(utxoHash);
						UTXO.setNonce(nonce);
						UTXO.setTransactionHash(txHash);
						
						for(Transaction tx : peerModel.transactionList) {
							if(tx.getHash().equals(UTXO.getTransactionHash())) {
								tx.outputs.add(UTXO);
							}
						}
						
						//UTXO ���� ��û ������
						additems = new HashMap<String,String>();
						additems.put("deleteUTXO",utxoHash);
						send(peerModel.makeJsonObject(additems));
					}
				}
				//������ ������ ������ ��� peerList���� ����
			} catch (Exception e) {
				try {
					e.printStackTrace();
					for(Peer peer : peerModel.peerList) {
						if(peer.getPeerThread() == PeerThread.this) {
							peerModel.peerList.remove(peer);
							break;
						}
					}
					socket.close();
					flag=false;

				} catch (IOException e1) { e1.printStackTrace();}
			}
		}
	}
	
	//P2P ��Ʈ��ũ ���� ����Ǿ� �ִ� Peer�ѿ��� ���� ��û ������
	public void addressSend(String localhost) throws IOException {
		HashMap<String,String> additems = new HashMap<String,String>();
		additems.put("localhost", localhost);
		additems.put("username",peerModel.walletModel.getUsername());
		send(peerModel.makeJsonObject(additems));
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
			HashMap<String,String> additems = new HashMap<String,String>();
			additems.put("blockNum", Block.count+"");
			additems.put("blockHash",peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash());
			send(peerModel.makeJsonObject(additems));
	}
	
	//���� Peer���� ������ �������� �˸�, ������ ��ϰ����� ���� Peer�� ������ �ȴ�. �׷��Ƿ� �������� ������ ���� �� ����(��ϰ����� ���� ���)
	public void sendLeader() {
		HashMap<String,String> additems = new HashMap<String,String>();
		additems.put("miningLeader", "true");
		send(peerModel.makeJsonObject(additems));
	}
	
	public void send(String sw) {
		printWriter.println(sw);
	}
	
	public Peer getPeer() {
		return peer;
	}
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	public int getPort() { return this.port;} // ����� ���� ������ �������� ��Ʈ ��ȯ
	public String getHostAddress() { return this.hostAddress;} // ����� ���� ������ �������� �ּ� ��ȯ
	public String toString() { return this.localHostAddress+":"+this.localPort;} // ������ �ּ�:��Ʈ ��ȯ
	
}
