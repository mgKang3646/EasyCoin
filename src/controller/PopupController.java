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
	
	@Override
	public void setObject(Object object) {
		String msg = (String)object;
		popupLabel.setText(msg);
	}

	@Override
	public void mainButtonAction() {
		Stage stage = (Stage)(popupButton.getScene().getWindow());
		stage.close();
	}

	@Override
	public void subButtonAction() {}
	@Override
	public void mainThreadAction() {}

	
}
