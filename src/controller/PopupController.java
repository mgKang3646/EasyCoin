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
	
	private @FXML Label popupLabel;
	private @FXML Button popupButton;
	private String msg;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setObject(Object object) {
		msg = (String)object;
	}
	@Override
	public void execute()  {
		setLabelText();
	}
	@Override
	public void mainButtonAction() {
		getStage().close();
	}
	
	private Stage getStage() {
		return (Stage)(popupButton.getScene().getWindow());
	}
	
	private void setLabelText() {
		popupLabel.setText(msg);
	}

	@Override
	public void subButtonAction() {}
	@Override
	public void setPeer(Peer peer) {}

	
}
