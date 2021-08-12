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
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	@Override
	public void execute(){
		newPage = utilFactory.getNewPage(stage,peer);
		mining = new Mining(peer.getBlockchain());
		cr1 = utilFactory.getCircleRotate(c1,true,270,10);
		cr2 = utilFactory.getCircleRotate(c2,true,180, 5);
		cr2.setCircleImage("/image/rotateCoin.png");
	}
	
	//채굴버튼
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
	 // 채굴이 종료되어 끝나는 것과 채굴버튼을 눌러 정지시킬 때를 구분해야 한다. 
	private void finishMining() {
		isMining = false;
		Platform.runLater(()->{
			stopUI();
			viewMiningResult();
		});
	}
	// 좀 더 다각화 하거나 관심사를 분리하거나 하자.
	private void viewMiningResult() {
		newPage.createMiningResultPage("successMining");
	}
	
	private void startUI() {
		miningButton.setText("채굴 중...");
		cr1.start();
		cr2.start();
	}
	
	private void stopUI() {
		miningButton.setText("채굴 시작");
		cr1.stop();
		cr2.stop();
	}
	
	
	@Override
	public void setObject(Object object) {}
	@Override
	public void subButtonAction() throws IOException {}
	
}
