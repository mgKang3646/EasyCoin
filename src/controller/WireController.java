package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.PeerModel;

public class WireController implements Initializable{

	@FXML TextField senderTextField;
	@FXML TextField receiverTextField;
	@FXML TextField balanceTextField;
	@FXML Button wireButton;
	private PeerModel peerModel = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		senderTextField.setEditable(false);
	}
	
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
	}
	
	public void wire() {
		
	}

	
}
