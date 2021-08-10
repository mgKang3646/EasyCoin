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
	
	private Stage stage;
	private RotateTransition rt1;
	private RotateTransition rt2;
	private boolean rotateFlag;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	@Override
	public void executeDefaultProcess() throws IOException {
		settingRotateCircle();
	}
	
	@Override
	public void mainButtonAction() throws IOException {
		rotateCircle();
	}
	
	
	private void rotateCircle() {
		if(!rotateFlag) {
			miningStartButton.setText("√§±º ¡ﬂ...");	
			rt1.play();
			rt2.play();
			rotateFlag = true;
		}else {
			miningStartButton.setText("√§±º Ω√¿€");
			rt1.stop();
			rt2.stop();
			rotateFlag = false;
		}
	}
	
	private void settingRotateCircle() {
		c2.setFill(new ImagePattern(new Image("/image/rotateCoin.png")));
		rt1 = new RotateTransition();
		rt2 = new RotateTransition();
		rt1 = setRotate(c1,true,270,10);
		rt2 = setRotate(c2,true,180, 5);
	}
	
	private RotateTransition setRotate(Circle c, boolean reverse, int angle, int duration) {
		RotateTransition rt = new RotateTransition(Duration.seconds(duration),c);
		rt.setAutoReverse(reverse);
		rt.setByAngle(angle);
		rt.setRate(3);
		rt.setCycleCount(18);
		return rt;
	}
	
	
	@Override
	public void setPeer(Peer peer) {}
	@Override
	public void setObject(Object object) {}
	@Override
	public void subButtonAction() throws IOException {}
	@Override
	public void mainThreadAction() {}
}
