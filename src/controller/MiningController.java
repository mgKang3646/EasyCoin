package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import factory.UtilFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Mining;
import model.Peer;
import util.CircleRotate;
import util.NewPage;


public class MiningController implements Controller {
	
	
	@FXML private Button miningButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	
	private UtilFactory utilFactory;
	private NewPage newPage;
	private CircleRotate cr1;
	private CircleRotate cr2;
	private Peer peer;
	private Mining mining;
	
	private boolean isMining;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createObjects();
	}
	
	private void createObjects() {
		utilFactory = new UtilFactory();
	}
	
	@Override
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	@Override
	public void execute(){ //�߻�ȭ ������ �ȸ���
		mining = new Mining(peer.getBlockchain());
		cr1 = utilFactory.getCircleRotate(c1,true,270,10);
		cr2 = utilFactory.getCircleRotate(c2,true,180, 5);
		cr2.setCircleImage("/image/rotateCoin.png");
		setButtonAction();
	}
	
	private void setButtonAction() {
		miningButton.setOnAction(ActionEvent->{
			miningButtonAction();
		});
	}
	
	private void miningButtonAction() {
		changeMiningUI();
		doMineBlock();
	}
	
	private void changeMiningUI() {
		if(isMining) {
			stopUI();
		}else {
			startUI();
		}
	}

	private void doMineBlock() {
		if(isMining) {
			mining.setMiningFlag(false);
		}else {
			runMiningThread();
		}
	}
	
	private void runMiningThread() {
		Thread miningThread = new Thread() {
			public void run() {
				startMining();
				finishMining();
			}
		};
		miningThread.start();
	}
	
	private void startMining() {
		isMining = true;
		mining.setMiningFlag(true);
		mining.mineBlock();
	}
	 // ä���� ����Ǿ� ������ �Ͱ� ä����ư�� ���� ������ų ���� �����ؾ� �Ѵ�. 
	private void finishMining() {
		isMining = false;
		Platform.runLater(()->{
			stopUI();
			viewMiningResult();
		});
	}
	
	// �� �� �ٰ�ȭ �ϰų� ���ɻ縦 �и��ϰų� ����.
	private void viewMiningResult() {
		setNewPage(utilFactory.getNewStage(getStage(),peer));
		newPage.makePage("/view/miningResult.fxml","successMining");
		newPage.show();
	}
	
	private void startUI() {
		miningButton.setText("ä�� ��...");
		cr1.start();
		cr2.start();
	}
	
	private void stopUI() {
		miningButton.setText("ä�� ����");
		cr1.stop();
		cr2.stop();
	}
	
	private void setNewPage(NewPage newPage) {
		this.newPage = newPage;
	}
	
	private Stage getStage() {
		return (Stage)miningButton.getScene().getWindow();
	}
	
	
	@Override
	public void setObject(Object object) {}
	
}
