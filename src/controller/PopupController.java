package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Peer;

public class PopupController implements Controller {
	
	@FXML Label popupLabel;
	@FXML Button popupButton;
	String msg;
	Stage parentStage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	
	@Override
	public void setObject(Object object) {
		msg = (String)object;
	}
	@Override
	public void execute()  {
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
	public void setPeer(Peer peer) {}

	
}
