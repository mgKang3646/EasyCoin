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


public class MiningController implements Controller {
	
	
	@FXML private Button miningButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	
	private UtilFactory utilFactory;
	private CircleRotate cr1;
	private CircleRotate cr2;
	private Stage stage;
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
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@Override
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	@Override
	public void executeDefaultProcess() throws IOException {
		mining = new Mining(peer.getBlockchain());
		cr1 = utilFactory.getCircleRotate(c1,true,270,10);
		cr2 = utilFactory.getCircleRotate(c2,true,180, 5);
		cr2.setCircleImage("/image/rotateCoin.png");
	}
	
	//√§±ºπˆ∆∞
	@Override
	public void mainButtonAction() throws IOException {
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
	private void finishMining() {
		isMining = false;
		Platform.runLater(()->{
			stopUI();
		});
	}
	
	private void startUI() {
		miningButton.setText("√§±º ¡ﬂ...");
		cr1.start();
		cr2.start();
	}
	
	private void stopUI() {
		miningButton.setText("√§±º Ω√¿€");
		cr1.stop();
		cr2.stop();
	}
	
	
	@Override
	public void setObject(Object object) {}
	@Override
	public void subButtonAction() throws IOException {}
	@Override
	public void mainThreadAction() {}
}
