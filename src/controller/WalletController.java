package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.PeerModel;

public class WalletController {

	@FXML TextField skTextField;
	@FXML TextField pkTextField;
	@FXML TextField balanceTextField;
	@FXML Button wireButton;
	private PeerModel peerModel = null;
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
		skTextField.setText(peerModel.walletModel.getPrivateKey());
		pkTextField.setText(peerModel.walletModel.getPublicKey());
	}
	
	public void wire() {
		
	}
	
}
