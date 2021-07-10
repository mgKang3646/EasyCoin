package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController {
	
	@FXML Label message;
	
	public void setMessage(String msg) {
		message.setText(msg);
	}
	
	

}
