package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.NetWorking;
import model.OtherPeer;
import newview.NewView;
import newview.ViewURL;
import util.P2PNet;
import util.ThreadUtil;

public class NetWorkingController implements Controller {
	
	private @FXML TextArea progressTextArea;
	private @FXML ProgressBar progressBar;
	private @FXML Label percentLabel;
	private @FXML Label titleLabel;
	
	private NetWorking netWorking;
	private ArrayList<OtherPeer> otherPeers;
	private NewView newView;
	private double progress;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
		netWorking = new NetWorking();
	}
	@Override
	public void throwObject(Object object) {}
	@Override
	public void execute() {
		startNetWorking();
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}

	public void startNetWorking() {
		Thread progressThread = new Thread() {
			public void run() {
				if(serverListenerUI()) {
					connectOtherPeersUI();
					ThreadUtil.sleepThread(700);
					moveToMypage();
					closeStage();
				}else {
					closeStage();
					openErrorPopup();
				}
			}
		};
		progressThread.start();
	}
	
	private boolean serverListenerUI() {
		if(netWorking.runServerListener()) {
			processUI("서버 생성 완료 : " + P2PNet.getServerListener().toString(), getProgress(0.1)); // 관심사 : UI 처리
			return true;
		}
		else return false;
	}
	
	private void connectOtherPeersUI() {
		doConnect();
		processUI("P2P 네트워크망 연결완료",1); // 관심사 : UI 처리	
	}
	
	private void doConnect()  {
		otherPeers = netWorking.getOtherPeers();
		for(OtherPeer otherPeer : otherPeers ) {
			if(netWorking.doConnect(otherPeer)) processUI(getConnectionMsg(otherPeer,true), getProgress(getConnectionPercent(otherPeers.size())));
			else processUI(getConnectionMsg(otherPeer,false), getProgress(getConnectionPercent(otherPeers.size())));
		}	
	}

	private double getProgress(double value) {
		this.progress += value;
		return this.progress;
	}
	
	private String getConnectionMsg(OtherPeer otherPeer, boolean isConnect) {
		return otherPeer.getUserName()+" 연결 결과 : " + isConnect;
	}
	
	private double getConnectionPercent(int size) {
		return 0.9/size;
	}
	
	private void processUI(String msg, double progress) {
		Platform.runLater(()->{
			progressBar.setProgress(progress);
			percentLabel.setText("("+(int)(progress*100)+"%)");
			progressTextArea.appendText(msg+"\n");
		});
	}
	
	private void moveToMypage() {
		Platform.runLater(()->{
			newView.getNewScene(ViewURL.mypageURL);
		});
	}
	
	private void openErrorPopup() {
		Platform.runLater(()->{
			String msg = "이미 접속 중인 개인키입니다.";
			newView.getNewWindow(ViewURL.popupURL,msg);
		});
	}
	
	private void closeStage() {
		Platform.runLater(()->{
			getStage().close();
		});
	}
	
	private Stage getStage() {
		return (Stage)progressBar.getScene().getWindow();
	}

	
}
