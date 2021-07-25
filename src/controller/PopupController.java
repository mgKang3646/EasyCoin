package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopupController implements Controller {
	
	@FXML Label popupLabel;
	@FXML Button popupButton;
	String msg;
	Stage parentStage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}

	@Override
	public void setStage(Stage stageValue) {
		this.parentStage = stageValue;
	}
	
	public void setMsg(String msg) {
		popupLabel.setText(msg);
	}
	
	public void closePopup() {
		Stage stage = (Stage)(popupButton.getScene().getWindow());
		stage.close();
	}

	
}
