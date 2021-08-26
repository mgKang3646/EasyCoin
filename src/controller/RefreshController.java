package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import model.CircleRotate;
import model.Refresh;

public class RefreshController implements Controller {


	@FXML private Label refreshLabel;
	@FXML private Circle refreshCircle;
	private CircleRotate circleRotate;
	private Refresh refresh;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		circleRotate = new CircleRotate(refreshCircle,false,360, 5);
		circleRotate.setCircleImage("/image/rotateCoin.png");
	}

	@Override
	public void throwObject(Object object) {
	}

	@Override
	public void execute() {
		refresh = new Refresh();
		refreshLabel.setText("�ֽ�ȭ ������...");
		circleRotate.start();
		startRefresh();
	}
	
	private void startRefresh() {
			Thread thread = new Thread() {
				public void run() {
					refresh.requestBlockNum();
					sleepThread(3000);
					refresh.generateLeader();
					if(refresh.getLeaderPeer() != null) {
						refresh.requestLeaderBlocks();
						sleepThread(3000);
						otherPeerLeaderUI();
					}else myPeerLeaderUI();
				}
			};
			thread.start();
	}
	
	public void myPeerLeaderUI() {
		Platform.runLater(()->{
			refreshLabel.setText("���� ������ �����Դϴ�.");
			circleRotate.stop();
		});
	}
	
	public void otherPeerLeaderUI() {
		Platform.runLater(()->{
			refreshLabel.setText("��ü���� �ֽ�ȭ�Ͽ����ϴ�.");
			circleRotate.stop();
		});
	}
	
	public void sleepThread(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
