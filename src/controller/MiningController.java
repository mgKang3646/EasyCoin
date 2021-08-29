package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import model.MiningCenter;
import model.Peer;
import util.CircleRotate;


public class MiningController implements Controller{
	
	//리팩토링 필요
	@FXML private Button miningButton;
	@FXML private Pane blockContent;
	@FXML private Label validLabel;
	@FXML private Circle c1;
	@FXML private Circle c2;
	
	private MiningCenter miningCenter;
	private CircleRotate cr1;
	private CircleRotate cr2;
	private boolean isMining;
	private boolean isVerifying;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		miningCenter = new MiningCenter();
		cr1 = new CircleRotate(c1,true,270,10);
		cr2 = new CircleRotate(c2,true,180, 5);
		cr2.setCircleImage("/image/rotateCoin.png");
	}
	
	@Override
	public void throwObject(Object object) {}

	@Override
	public void execute() {
		setPossibleMining();
		setButtonAction();
	}
	
	private void setPossibleMining() {
		if(Peer.myPeer.getPublicKey() == null) {
			miningButton.setDisable(true);
			validLabel.setVisible(true);
		}else {
			if(!isVerifying) {
				miningButton.setDisable(false);
				validLabel.setVisible(false);
			}else {
				validLabel.setVisible(false);
			}
		}
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
		setMining(true);
		miningCenter.start();
	}
	
	private void stopMining() {
		setMining(false);
		miningCenter.stop();
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
			setVerifying(false);
			miningButton.setText("채굴 시작");
			if(Peer.myPeer.getPublicKey() == null) miningButton.setDisable(true);
			else miningButton.setDisable(false);
			cr1.stop();
			cr2.stop();
		});
	}
	
	public void processTxUI() {
		Platform.runLater(()->{
			setVerifying(true);
			miningButton.setText("트랜잭션 처리 중..");
			miningButton.setDisable(true);
		});
	}
	
	public void verifyUI() {
		Platform.runLater(()->{
			setVerifying(true);
			miningButton.setText("검증 중...");
			miningButton.setDisable(true);
		});
	}
	
	public void basicUI() {
		Platform.runLater(()->{
			setVerifying(false);
			miningButton.setText("채굴 시작");
			if(Peer.myPeer.getPublicKey() == null) miningButton.setDisable(true);
			else miningButton.setDisable(false);
		});
	}
	
	private void setMining(boolean result) {
		isMining = result;
	}
	
	private void setVerifying(boolean result) {
		isVerifying = result;
	}
}
