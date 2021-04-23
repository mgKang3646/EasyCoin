package controller;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.PeerModel;

public class MiningController implements Initializable {
	
	
	@FXML private Button miningStartButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	private PeerModel peerModel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Image image = new Image("/image/rotateCoin.png");
		c2.setFill(new ImagePattern(image));
	}
	
	public void miningStart() throws IOException, InterruptedException {
		miningStartButton.setText("ä�� ��...");	
	
		setRotate(c1,true,270,10);
		setRotate(c2,true,180, 5);
		
			
		peerModel.proofOfWorkCompleteFlag = false; // ���� �ٸ� ��밡 ä���� �Ϸ����� ����.
		peerModel.miningFlag = true; // ���� Peer�� ä��ON! ä���� �Ϸ�Ǹ� false�� �ٲ�
		
		Thread miningThread = new Thread() {
			public void run() {
				int index = 0;
				while(peerModel.miningFlag) {
						if(peerModel.proofOfWorkCompleteFlag) {
							System.out.println("������ ���� ä�� �ߴ�");
							break;
						}else {
							String hash = peerModel.mineBlock(index++);
						}
				}
				//���� ����
				verifyMining();
			
			}
		};
		
		miningThread.start();
		
	}
	
	public void verifyMining() {
		try {
			int verifyResult = peerModel.verifyBlock();//���� ����
			//���� ������ ���
			if(verifyResult==1) {
					//UI ����
					if(peerModel.miningFlag==false) { //ä�� ������ ���
						Platform.runLater(new Runnable() {
							public void run() {
								try {
									blockContent.getChildren().clear();
									peerModel.proofOfWorkCompleteFlag = true;
									FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
									blockContent.getChildren().add(loader.load());
									MiningVerifyController bc = loader.getController();
									bc.miningSuccess(peerModel.block);
								
								} catch (IOException e) {e.printStackTrace();}
							}
						});
					}else { // ä���� ������ ���
						Platform.runLater(new Runnable() {
							public void run() {
							try {
								blockContent.getChildren().clear();
								peerModel.proofOfWorkCompleteFlag = true;
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
								blockContent.getChildren().add(loader.load());
								MiningVerifyController bc = loader.getController();
								bc.resultOfVerify(peerModel.block);	
							
							} catch (IOException e) {e.printStackTrace();}	
						}
					});	
				}
			
			}else {// ������ ������ ���, ���߿� �����ϱ� 
				Platform.runLater(new Runnable() {
					public void run() {
					try {
						blockContent.getChildren().clear();
						peerModel.proofOfWorkCompleteFlag = true;
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/miningVerify.fxml"));
						blockContent.getChildren().add(loader.load());
						MiningVerifyController bc = loader.getController();
						bc.failedVerify(peerModel.block);	
					
					} catch (IOException e) {e.printStackTrace();}	
				}
			});	
				
				
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void setRotate(Circle c, boolean reverse, int angle,int duration) {
		RotateTransition rt = new RotateTransition(Duration.seconds(duration),c);
		
		rt.setAutoReverse(reverse);
		rt.setByAngle(angle);
		rt.setRate(3);
		rt.setCycleCount(18);
		rt.play();
	}
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
	}

	
}
