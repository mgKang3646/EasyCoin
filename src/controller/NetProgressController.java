package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.BlockchainModel;
import model.PeerModel;
import model.WalletModel;

public class NetProgressController implements Initializable {
	
	@FXML TextArea progressTextArea;
	@FXML ProgressBar progressBar;
	@FXML Label progressLabel;
	double progress=0; // ������
	int totalPeer =0;
	double amountOfProgress;
	String address;
	Stage currentStage;
	Stage primaryStage;
	String privatePath;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void setPrivateKeyPath(String privatePath) {
		this.privatePath = privatePath;
	}
	
	
	public void doProgress(PeerModel peerModel, WalletModel walletModel, BlockchainModel blockchainModel,String whosCall) {
		
		Thread progressThread = new Thread() {
			public void run() {
				//P2P ��Ʈ��ũ ���� ����
				try {
					//1. ���� ���� �ϱ�
					int result = peerModel.doConnection(peerModel, walletModel,blockchainModel,primaryStage);
					
					if(result==1) {
						progress += 0.1; // ���� ���� 10% ���� 
						Platform.runLater(()->{
							progressBar.setProgress(progress);
							progressLabel.setText("P2P�� ������("+(int)(progress*100)+"%)");
							progressTextArea.appendText("���� ���� �Ϸ� : " + peerModel.getServerListerner().toString()+"\n");
						});
					}else {// �ּҰ� �ߺ��� ��� 
						Platform.runLater(()->{
							progressTextArea.appendText("���� ������ ������ �߻��߽��ϴ�. ");
							Stage stage = (Stage)progressBar.getScene().getWindow();
							stage.close();
						});
					}
						
					//2.DB�ȿ� ����� Peer ���� �ľ��ϱ� 
					totalPeer = peerModel.getPeersInDB(peerModel.getServerListerner());
					
					//db�� ������ Peer�� ����Ǿ� �ִ� ���
					if(totalPeer!=0) {
						amountOfProgress = 0.9/totalPeer; // ����� ���ΰ� ���� ������ amountOfProgress��ŭ ProgressBar �߰�
						
						//DB�� �ִ� ���������� �ּҿ� ����� Peer������'��' ����
						for(int i =0; i<totalPeer;i++) {
							int connectResult = peerModel.connectToPeerInDB(i, peerModel.peers.get(i));
							// ���� ������ ��� 
							if(connectResult == 1) {
								progress += amountOfProgress;
								address = peerModel.peers.get(i);
								Platform.runLater(()->{
									progressBar.setProgress(progress);
									progressLabel.setText("P2P�� ������("+Math.round(progress*100)+"%)");
									progressTextArea.appendText(address+"�� ���Ῡ�� : true"+"\n");
								});
							//���� ������ ���
							}else if(connectResult == -1) {
								progress += amountOfProgress;
								address = peerModel.peers.get(i);
								Platform.runLater(()->{
									progressBar.setProgress(progress);
									progressLabel.setText("P2P�� ������("+Math.round(progress*100)+"%)");
									progressTextArea.appendText(address+"�� ���Ῡ�� : false"+"\n");
								});
							}
						}	
					}
					
					// ������ 100% �����
					Platform.runLater(()->{
						progressBar.setProgress(1);
						progressLabel.setText("P2P�� ������(100%)");
						progressTextArea.appendText("P2P ��Ʈ��ũ�� ����Ϸ�"+"\n");
					});
					
					Thread.sleep(1000); //100% �����ֱ����� ��� �����ֱ�
					
					//3. ����Ű �ޱ� �������� �Ѿ��
					// index ȭ������ ��ȯ
					
					if(whosCall.equals("join")) {
						Platform.runLater(()->{
							try {
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/warning.fxml"));
								Parent root= loader.load();
								WarningController wc = loader.getController();
								wc.setPeerModel(peerModel); //peer ��ü �����ּ� ��������
								wc.setPrimaryStage(primaryStage); // ȸ������ Stage ��ü�ּ� WarningController�� ������
								wc.setPath(privatePath);
								
								Scene scene = new Scene(root);
								Stage stage = new Stage();
								stage.setScene(scene);
								stage.setX(progressBar.getScene().getWindow().getX());
								stage.setY(progressBar.getScene().getWindow().getY());
								//���� �������� �ݾ��ֱ� 
								Stage currentStage = (Stage)progressBar.getScene().getWindow();
								currentStage.close();
		
								stage.show();
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
				    }
					
					else if(whosCall.equals("login")) {
						Platform.runLater(()->{
							try {
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/index.fxml"));
								Parent root = loader.load();
								IndexController indexController = loader.getController(); // fxml�� �ε�Ǵ� ���ÿ� ����� ��Ʈ�ѷ� ��ü�� �ڵ�����.
								indexController.setPeerModel(peerModel);
								Scene scene = new Scene(root);
								//���� �������� �ݾ��ֱ� 
								Stage currentStage = (Stage)progressBar.getScene().getWindow();
								currentStage.close();
								primaryStage.setScene(scene);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		progressThread.start();
		
	}

	
	
}
