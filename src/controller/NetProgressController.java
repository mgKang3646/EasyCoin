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
	double progress=0; // 진행율
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
				//P2P 네트워크 연결 과정
				try {
					//1. 서버 생성 하기
					int result = peerModel.doConnection(peerModel, walletModel,blockchainModel,primaryStage);
					
					if(result==1) {
						progress += 0.1; // 서버 생성 10% 진행 
						Platform.runLater(()->{
							progressBar.setProgress(progress);
							progressLabel.setText("P2P망 접속중("+(int)(progress*100)+"%)");
							progressTextArea.appendText("서버 생성 완료 : " + peerModel.getServerListerner().toString()+"\n");
						});
					}else {// 주소가 중복된 경우 
						Platform.runLater(()->{
							progressTextArea.appendText("서버 생성에 오류가 발생했습니다. ");
							Stage stage = (Stage)progressBar.getScene().getWindow();
							stage.close();
						});
					}
						
					//2.DB안에 저장된 Peer 개수 파악하기 
					totalPeer = peerModel.getPeersInDB(peerModel.getServerListerner());
					
					//db에 연결할 Peer가 저장되어 있는 경우
					if(totalPeer!=0) {
						amountOfProgress = 0.9/totalPeer; // 연결될 여부가 나올 때마다 amountOfProgress만큼 ProgressBar 추가
						
						//DB에 있는 서버리스너 주소와 연결될 Peer스레드'들' 생성
						for(int i =0; i<totalPeer;i++) {
							int connectResult = peerModel.connectToPeerInDB(i, peerModel.peers.get(i));
							// 연결 성공한 경우 
							if(connectResult == 1) {
								progress += amountOfProgress;
								address = peerModel.peers.get(i);
								Platform.runLater(()->{
									progressBar.setProgress(progress);
									progressLabel.setText("P2P망 접속중("+Math.round(progress*100)+"%)");
									progressTextArea.appendText(address+"의 연결여부 : true"+"\n");
								});
							//연결 실패한 경우
							}else if(connectResult == -1) {
								progress += amountOfProgress;
								address = peerModel.peers.get(i);
								Platform.runLater(()->{
									progressBar.setProgress(progress);
									progressLabel.setText("P2P망 접속중("+Math.round(progress*100)+"%)");
									progressTextArea.appendText(address+"의 연결여부 : false"+"\n");
								});
							}
						}	
					}
					
					// 마지막 100% 만들기
					Platform.runLater(()->{
						progressBar.setProgress(1);
						progressLabel.setText("P2P망 접속중(100%)");
						progressTextArea.appendText("P2P 네트워크망 연결완료"+"\n");
					});
					
					Thread.sleep(1000); //100% 보여주기위해 잠시 쉬어주기
					
					//3. 개인키 받기 페이지로 넘어가기
					// index 화면으로 전환
					
					if(whosCall.equals("join")) {
						Platform.runLater(()->{
							try {
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/warning.fxml"));
								Parent root= loader.load();
								WarningController wc = loader.getController();
								wc.setPeerModel(peerModel); //peer 객체 참조주소 가져가기
								wc.setPrimaryStage(primaryStage); // 회원가입 Stage 객체주소 WarningController에 보내기
								wc.setPath(privatePath);
								
								Scene scene = new Scene(root);
								Stage stage = new Stage();
								stage.setScene(scene);
								stage.setX(progressBar.getScene().getWindow().getX());
								stage.setY(progressBar.getScene().getWindow().getY());
								//현재 스테이지 닫아주기 
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
								IndexController indexController = loader.getController(); // fxml이 로드되는 동시에 연결된 컨트롤러 객체가 자동생성.
								indexController.setPeerModel(peerModel);
								Scene scene = new Scene(root);
								//현재 스테이지 닫아주기 
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
