package controller;



import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.PeerModel;
import model.PeerModel.Peer;
import model.Transaction;
import model.TransactionInput;

public class MiningController implements Initializable {
	
	
	@FXML private Button miningStartButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	private PeerModel peerModel;
	RotateTransition rt1 = new RotateTransition();
	RotateTransition rt2 = new RotateTransition();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Image image = new Image("/image/rotateCoin.png");
		c2.setFill(new ImagePattern(image));
	}
	
	public Button getMiningStartButton() {
		return miningStartButton;
	}
	
	public void miningStart() throws IOException, InterruptedException {
		miningStartButton.setText("채굴 중...");	

		rt1 = setRotate(c1,true,270,10);
		rt2 = setRotate(c2,true,180, 5);
		
		rt1.play();
		rt2.play();
	
		peerModel.proofOfWorkCompleteFlag = false; // 아직 다른 상대가 채굴을 완료하지 않음.
		peerModel.miningFlag = true; // 본인 Peer은 채굴ON! 채굴이 완료되면 false로 바뀜
		
		Thread miningThread = new Thread() {
			public void run() {
				int index = 0;
				while(peerModel.miningFlag) {
						if(peerModel.proofOfWorkCompleteFlag) {
							System.out.println("검증을 위한 채굴 중단");
							break;
						}else {
							String hash = peerModel.mineBlock(index++);
						}
				}
				//검증 시작
				verifyMining();
			
			}
		};
		
		miningThread.start();
		
	}
	
	public void verifyMining() {
		try {
			int verifyResult = peerModel.verifyBlock();//검증 시작
			//검증 성공한 경우
			if(verifyResult==1) {
					//UI 변경
					if(peerModel.miningFlag==false) { //채굴 성공 및 합의 성공						
						//UTXO 처리 시작
						for(Transaction tx : peerModel.transactionList) {
							for(TransactionInput input : tx.inputs) {
								for(Peer peer : peerModel.peerList) {
									if(peer.getUserName().equals(input.getMiners())) {
										System.out.println("input.getMiners() : "+input.getMiners());
										HashMap<String,String> additems = new HashMap<String,String>();
										additems.put("getUTXO", "");
										additems.put("txHash", tx.getHash());
										additems.put("utxoHash", input.getUtxoHash());
										System.out.println("채굴 성공 측 : UTXO 보내주라!");
										peer.getPeerThread().send(peerModel.makeJsonObject(additems));
									}
								}
							}
						}
						Platform.runLater(new Runnable() {
							public void run() {
								try {
									peerModel.proofOfWorkCompleteFlag = true;
									rt1.stop(); //회전 종료
									rt2.stop(); //회전 종료
									FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
									Parent root = loader.load();
									Scene scene = new Scene(root);
									Stage verifyStage = new Stage();
									verifyStage.setScene(scene);
									verifyStage.setX(peerModel.primaryStage.getX()+320);
									verifyStage.setY(peerModel.primaryStage.getY());
									MiningVerifyController mvc = loader.getController();
									mvc.miningSuccess(peerModel.block);
									verifyStage.show();
								
								} catch (IOException e) {e.printStackTrace();}
							}
						});
						
						
					}else { // 채굴 실패 및 합의 성공
						Platform.runLater(new Runnable() {
							public void run() {
							try {
								peerModel.proofOfWorkCompleteFlag = true;
								rt1.stop(); //회전 종료
								rt2.stop(); //회전 종료
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
								Parent root = loader.load();
								Scene scene = new Scene(root);
								Stage verifyStage = new Stage();
								verifyStage.setScene(scene);
								verifyStage.setX(peerModel.primaryStage.getX()+320);
								verifyStage.setY(peerModel.primaryStage.getY());
								MiningVerifyController mvc = loader.getController();
								mvc.resultOfVerify(peerModel.block);	
								verifyStage.show();

							} catch (IOException e) {e.printStackTrace();}	
						}
					});	
				}
			
			}else {//합의에 실패한 경우
				Platform.runLater(new Runnable() {
					public void run() {
					try {
						peerModel.proofOfWorkCompleteFlag = true;
						rt1.stop(); //회전 종료
						rt2.stop(); //회전 종료
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
						Parent root = loader.load();
						Scene scene = new Scene(root);
						Stage verifyStage = new Stage();
						verifyStage.setScene(scene);
						verifyStage.setX(peerModel.primaryStage.getX()+320);
						verifyStage.setY(peerModel.primaryStage.getY());
						MiningVerifyController mvc = loader.getController();
						mvc.failedVerify(peerModel.block);	
						verifyStage.show();

					} catch (IOException e) {e.printStackTrace();}	
				}
			});	
				
				
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public RotateTransition setRotate(Circle c, boolean reverse, int angle,int duration) {
		RotateTransition rt = new RotateTransition(Duration.seconds(duration),c);
		
		rt.setAutoReverse(reverse);
		rt.setByAngle(angle);
		rt.setRate(3);
		rt.setCycleCount(18);
		
		return rt;
	}
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
	}

	
}
