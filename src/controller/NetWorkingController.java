package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Dao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.OtherPeer;
import model.P2PNet;
import model.Peer;
import model.PeerThread;
import model.ServerListener;
import newview.NewView;
import newview.ViewURL;

public class NetWorkingController implements Controller {
	
	private @FXML TextArea progressTextArea;
	private @FXML ProgressBar progressBar;
	private @FXML Label percentLabel;
	private @FXML Label titleLabel;
	
	private Dao dao;
	private ArrayList<OtherPeer> otherPeers;
	private P2PNet p2pNet;
	private NewView newView;
	private double progress;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		p2pNet = new P2PNet();
		newView = new NewView();
		dao = new Dao();
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
					doNetWorking();
					sleepMoment();
					moveToMypage();
					closeStage();
			}
		};
		progressThread.start();
	}
	
	private void doNetWorking() {
		if(p2pNet.runServerListener()) {
			processUI("���� ���� �Ϸ� : " + P2PNet.getServerListener().toString(), getProgress(0.1)); // ���ɻ� : UI ó��
			connectOtherPeers(); // 2. PeerThread �����Ͽ� DB ����� Peer��� ���Ͽ���
			processUI("P2P ��Ʈ��ũ�� ����Ϸ�",1); // ���ɻ� : UI ó��	
		}else {
			closeStage();
			openErrorPopup();
		}
	}
	
	private void connectOtherPeers() {
		getOtherPeers();
		doConnect();
	}
	
	private void getOtherPeers() {
		otherPeers = dao.getPeers(Peer.myPeer.getUserName());
	}
	
	private void doConnect()  {
			for(OtherPeer otherPeer : otherPeers) {
				PeerThread peerThread = p2pNet.connectOtherPeer(otherPeer.getLocalhost());
				if(peerThread != null) {
					peerThread.start();
					p2pNet.requestConnect(peerThread);
					Peer.peerList.add(otherPeer);
					processUI(getConnectionMsg(otherPeer,true), getProgress(getConnectionPercent(otherPeers.size())));
				}
				else processUI(getConnectionMsg(otherPeer,false), getProgress(getConnectionPercent(otherPeers.size())));
			}	
	}
	
	private void sleepMoment() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private double getProgress(double value) {
		this.progress += value;
		return this.progress;
	}
	
	private String getConnectionMsg(OtherPeer otherPeer, boolean isConnect) {
		return otherPeer.getUserName()+" ���� ��� : " + isConnect;
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
			String msg = "�̹� ���� ���� ����Ű�Դϴ�.";
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
