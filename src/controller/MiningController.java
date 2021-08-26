package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import model.CircleRotate;
import model.MiningCenter;


public class MiningController implements Controller{
	
	
	@FXML private Button miningButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	
	private MiningCenter miningCenter;
	private CircleRotate cr1;
	private CircleRotate cr2;
	private boolean isMining;
	
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
		setButtonAction();
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
			miningButton.setText("√§±º ¡ﬂ...");
			cr1.start();
			cr2.start();
		});
	}
	
	public void stopUI() {
		Platform.runLater(()->{
			miningButton.setText("√§±º Ω√¿€");
			miningButton.setDisable(false);
			cr1.stop();
			cr2.stop();
		});
	}
	
	public void verifyUI() {
		Platform.runLater(()->{
			miningButton.setText("∞À¡ı ¡ﬂ...");
			miningButton.setDisable(true);
		});
	}
	
	public void basicUI() {
		Platform.runLater(()->{
			miningButton.setText("√§±º Ω√¿€");
			miningButton.setDisable(false);
		});
	}
}
