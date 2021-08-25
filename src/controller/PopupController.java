package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import newview.NewView;

public class PopupController implements Controller {
	
	private @FXML Label popupLabel;
	private @FXML Button popupButton;
	private String msg;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void throwObject(Object object) {
		msg = (String)object;
	}
	@Override
	public void execute() {
		setLabelText();
		setButtonAction();		
	}
	
	private void setLabelText() {
		popupLabel.setText(msg);
	}
	
	private void setButtonAction() {
		popupButton.setOnAction(ActionEvent -> {
			popupButtonAction();
		});
	}
	
	public void popupButtonAction() {
		getStage().close();
	}
	
	private Stage getStage() {
		return (Stage)(popupButton.getScene().getWindow());
	}

	
}
