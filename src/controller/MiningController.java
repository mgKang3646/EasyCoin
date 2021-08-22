package controller;

import java.net.URL;
import java.util.ResourceBundle;

import factory.NewPageFactory;
import factory.UtilFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.MiningCenter;
import model.Peer;
import newpage.FxmlLoader;
import util.CircleRotate;


public class MiningController implements Controller {
	
	
	@FXML private Button miningButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	
	private MiningCenter miningCenter;
	private NewPageFactory newPageFactory;
	private UtilFactory utilFactory;
	private Stage stage;
	private CircleRotate cr1;
	private CircleRotate cr2;
	private Peer peer;
	private boolean isMining;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newPageFactory = new NewPageFactory();
		utilFactory = new UtilFactory();
		miningCenter = new MiningCenter();
		cr1 = utilFactory.getCircleRotate(c1,true,270,10);
		cr2 = utilFactory.getCircleRotate(c2,true,180, 5);
		cr2.setCircleImage("/image/rotateCoin.png");
		setButtonAction();
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
	public void execute(){ //추상화 수준이 안맞음
		newPageFactory.setStage(stage);
	}
	
	private void setButtonAction() {
		miningButton.setOnAction(ActionEvent->{
			miningButtonAction();
		});
	}
	
	private void miningButtonAction() {
		if(!isMining) startMining();
		else stopMining();
	}
	
	private void startMining() {
		setIsMining(true);
		miningCenter.initializeObjects(peer);
		miningCenter.setMiningController(this);
		miningCenter.start();
	}
	
	private void stopMining() {
		setIsMining(false);
		miningCenter.stop();
	}
	
	private void setIsMining(boolean result) {
		isMining = result;
	}
	
	public void startUI() {
		Platform.runLater(()->{
			miningButton.setText("채굴 중...");
			cr1.start();
			cr2.start();
		});
	}
	
	public void stopUI() {
		Platform.runLater(()->{
			miningButton.setText("채굴 시작");
			miningButton.setDisable(false);
			cr1.stop();
			cr2.stop();
		});
	}
	
	public void verifyUI() {
		Platform.runLater(()->{
			miningButton.setText("검증 중...");
			miningButton.setDisable(true);
		});
	}
	
	public void basicUI() {
		Platform.runLater(()->{
			miningButton.setText("채굴 시작");
			miningButton.setDisable(false);
		});
	}
	
	
	@Override
	public void setObject(Object object) {}
	
	
}
