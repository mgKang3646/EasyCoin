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
		this.localHostAddress = socket.getLocalAddress().getHostName(); // 해당 Peer의 로컬 주소
		this.localPort = socket.getLocalPort(); // 해당 Peer의 포트
		this.port = socket.getPort(); // 연결된 서버의 포트
		this.hostAddress = socket.getInetAddress().getHostName(); // 연결된 서버의 주소
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 소켓의 inputStream을 bufferedReader 버퍼에 연결시키기
		this.printWriter = new PrintWriter(socket.getOutputStream(),true); // 상대 서버스레드로 보내는 PrintWriter
		this.dao = new DAO();
	}
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				// 블록정보와 트랜잭션 정보를 이쪽에서 처리
				JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
				///////////////////////////////blocking///////////////////////////////////
				// 다른 Peer에서 블록을 채굴을 완료한 경우 검증
				if(jsonObject.containsKey("nonce")) {
					// 정확한 검증을 위한 초기화
					peerModel.totalRespondedCount=0;
					peerModel.verifiedPeerCount =0;
					
					// 검증 시작
					String previousHash = peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash();// 이전 해시
					String nonce = jsonObject.getString("nonce");
					String timestamp = jsonObject.getString("timestamp");
					
					int result = verifyBlock(previousHash,nonce,timestamp); // 검 증
						// 검증 성공 결과를 모든 Peer에게 보내기
						// ? 결과도 암호화하여 전송해보기 
					
					peerModel.totalRespondedCount += 1; // 본인 
					StringWriter sW = new StringWriter();
					
					if(result == 1) { // 검증 성공
						Json.createWriter(sW).writeObject(Json.createObjectBuilder()
																.add("verified", "true")
																.add("blockNum",Block.count) // 검증 과정에서 리더 Peer를 파악하기 위한 정보제공
																.build());		
						peerModel.getServerListerner().sendMessage(sW.toString());
						peerModel.verifiedPeerCount += 1; // 본인
					}else { // 검증 실패
						Json.createWriter(sW).writeObject(Json.createObjectBuilder()
																.add("verified", "false")
																.add("blockNum", Block.count)
																.build());
						peerModel.getServerListerner().sendMessage(sW.toString());
					}
					
					// 임시 블럭 미리 생성해놓기
					Block.count++; //블럭넘버 1 증가시키기
					peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
					
					if(!peerModel.proofOfWorkCompleteFlag) peerModel.proofOfWorkCompleteFlag = true;	// 현재 채굴 중이라면
					else { // 현재 채굴중이 아니라면
						System.out.println("채굴 중이 아닌 경우 검증 시작");
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
			
				
				//다른 Peer들의 검증 결과
				if(jsonObject.containsKey("verified")) {
						peerModel.totalRespondedCount++; // 다른 Peer 추가 
						if(jsonObject.getString("verified").equals("true")){ // 검증 성공이면 true
							System.out.println(hostAddress +":"+this.getPort()+ "의 검증여부 : true");
							peerModel.peerBlockNums.put(this, jsonObject.getInt("blockNum")); // HashMap에 해당Peer와 연결된 PeerThread와 블럭 넘버를 저장
							peerModel.verifiedPeerCount++;
						}else { 
							System.out.println(hostAddress +":"+this.getPort() + "의 검증여부 : false"); 
							peerModel.peerBlockNums.put(this, jsonObject.getInt("blockNum")); // HashMap에 해당Peer와 연결된 PeerThread와 블럭 넘버를 저장
							}		
				}
				//블록 요청 후 리더 Peer로부터 블럭 받기
				if(jsonObject.containsKey("requestVerified")) {
					boolean requestVerified = jsonObject.getBoolean("requestVerified");
					String previousHash = jsonObject.getString("RpreviousHash");
					String nonce = jsonObject.getString("Rnonce");
					String timestamp = jsonObject.getString("Rtimestamp");
					
					//검증이 완료되어 일부 블록만 받는 경우
					if(requestVerified) {
						Block.count++;
						peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
						peerModel.blockchainModel.getBlocks().add(new Block(previousHash,nonce,timestamp,Block.count));
						dao.storeBlock(peerModel.block,peerModel.walletModel.getUsername());
						
					}else { //검증이 실패하여 처음부터 받는 경우
						if(peerModel.isFirstResponse) {// json request의 반응(response)이 처음 들어오는 경우
							Block.count = 0; 
							dao.deleteAllBlock(peerModel.walletModel.getUsername());
							peerModel.blockchainModel.resetBlocks().add(new Block(previousHash,nonce,timestamp,Block.count)); // 새로운 블록체인에 정리
							peerModel.isFirstResponse = false;
						}else {
							Block.count++;
							peerModel.block = new Block(previousHash,nonce,timestamp,Block.count);
							peerModel.blockchainModel.getBlocks().add(new Block(previousHash,nonce,timestamp,Block.count));
							dao.storeBlock(peerModel.block, peerModel.walletModel.getUsername());
						}
					}
				}
				//본인이 리더라고 알리는 Peer와 연결된 PeerThread를 PeerModel 변수에 저장
				if(jsonObject.containsKey("leader")) {
					if(jsonObject.getInt("leaderBlockNum") > Block.count) {
						peerModel.threadForLeaderPeer = this; 
						peerModel.amILeader = false;
					}else { // 만약 자신이 더 블럭 개수가 많은 경우
						peerModel.amILeader = true;
						StringWriter sw = new StringWriter();
						//자신이 블럭이 더 많다고 알려주기 
						Json.createWriter(sw).writeObject(Json.createObjectBuilder()
															.add("biggerThanYou", Block.count)
															.build());
						printWriter.println(sw.toString());
					}
				}
				
				//거래 트랜잭션이 들어 온 경우
				if(jsonObject.containsKey("signature")) {
					
					//송금자 공개키 획득
					byte[] byteSender = Base64.decode(jsonObject.getString("sender"));
					X509EncodedKeySpec spec1 = new X509EncodedKeySpec(byteSender);
					KeyFactory factory1 = KeyFactory.getInstance("ECDSA","BC");
					PublicKey sender = factory1.generatePublic(spec1);
					//수금자 공개키 획득
					byte[] byteRecipient = Base64.decode(jsonObject.getString("recipient"));
					X509EncodedKeySpec spec2 = new X509EncodedKeySpec(byteRecipient);
					KeyFactory factory2 = KeyFactory.getInstance("ECDSA","BC");
					PublicKey recipient = factory2.generatePublic(spec2);
					
					//송금액 획득
					float value = Float.parseFloat(jsonObject.getString("value"));
					
					//전자서명 획득
					byte[] signature = Base64.decode(jsonObject.getString("signature"));
					
					//트랜잭션생성
					Transaction newTransaction = new Transaction(sender,recipient,value);
					newTransaction.setSignature(signature);
					//검증에 성공하면 트랜잭션 추가하기 
					if(newTransaction.verifySignature()) {
						peerModel.block.getTransactionList().add(newTransaction);
						System.out.println("========== 전자서명 검증 완료 ============");
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
	
	//P2P 네트워크 망에 연결되어 있는 Peer둘에게 연결 요청 보내기
	public void addressSend(String localhost) throws IOException {
		StringWriter sW = new StringWriter();
		Json.createWriter(sW).writeObject(Json.createObjectBuilder()
											.add("localhost", localhost)
											.build());
		printWriter.println(sW);	
	}

	// 다른 Peer에서 채굴된 블럭 검증하기
	private int verifyBlock(String previousBlockHash, String nonce, String timestamp) throws NoSuchAlgorithmException{
		String hashString = DigestUtils.sha256Hex(previousBlockHash+nonce+timestamp); // 해당 데이터들을 SHA256으로 해싱
		// 정말  Pow_MAX_Bound보다 작은지 검증
		if(hashString.substring(0,peerModel.zeroNum).equals(peerModel.hashDifficulty)) return 1;
		else return -1;	
	}
	
	// 리더Peer에게 블럭 요청하기 
	public void requestBlock() {
			StringWriter sw = new StringWriter();
			Json.createWriter(sw).writeObject(Json.createObjectBuilder()
										.add("blockNum", Block.count) 
										.add("blockHash", peerModel.blockchainModel.getBlocks().get(peerModel.blockchainModel.getBlocks().size()-1).getHash())
										.build());
			
			printWriter.println(sw.toString());	
	}
	
	//리더 Peer에게 본인이 리더임을 알림, 리더는 블록개수가 많은 Peer가 리더가 된다. 그러므로 여러명의 리더가 나올 수 있음(블록개수가 같은 경우)
	public void sendLeader() {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(Json.createObjectBuilder()
										.add("leader", true)
										.build());
		printWriter.println(sw.toString());
	}
	
	
	public int getPort() { return this.port;} // 연결된 서버 스레드 스레드의 포트 반환
	public String getHostAddress() { return this.hostAddress;} // 연결된 서버 스레드 스레드의 주소 반환
	public String toString() { return this.localHostAddress+":"+this.localPort;} // 본인의 주소:포트 반환
	
}
