package controller;

import java.io.IOException;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Peer;


public class MiningController implements Controller {
	
	
	@FXML private Button miningStartButton;
	@FXML private Pane blockContent;
	@FXML private Circle c1;
	@FXML private Circle c2;
	@FXML private Circle c3;
	RotateTransition rt1 = new RotateTransition();
	RotateTransition rt2 = new RotateTransition();
	RotateTransition rt3 = new RotateTransition();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Image image = new Image("/image/rotateCoin.png");
		c2.setFill(new ImagePattern(image));
	}
	
	@Override
	public void setStage(Stage stage) {}
	@Override
	public void setPeer(Peer peer) {}
	@Override
	public void setObject(Object object) {}
	@Override
	public void mainButtonAction() throws IOException {}
	@Override
	public void subButtonAction() throws IOException {}
	@Override
	public void mainThreadAction() {}
	@Override
	public void executeDefaultProcess() throws IOException {}
	
	private Button getMiningStartButton() {
		return miningStartButton;
	}
	
	private void miningStart() throws IOException, InterruptedException {
		miningStartButton.setText("Ã¤±¼ Áß...");	

		rt1 = setRotate(c1,true,270,10);
		rt2 = setRotate(c2,true,180, 5);
	
		rt1.play();
		rt2.play();
	}
	
	private void verifyMining() {}
	
	private RotateTransition setRotate(Circle c, boolean reverse, int angle,int duration) {
		RotateTransition rt = new RotateTransition(Duration.seconds(duration),c);
		
		rt.setAutoReverse(reverse);
		rt.setByAngle(angle);
		rt.setRate(3);
		rt.setCycleCount(18);
		
		return rt;
	}
}
