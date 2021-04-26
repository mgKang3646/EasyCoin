package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PeerModel;

public class WalletController implements Initializable{

	@FXML TextField skTextField;
	@FXML TextField pkTextField;
	@FXML TextField balanceTextField;
	@FXML Button wireButton;
	private PeerModel peerModel = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		skTextField.setEditable(false);
		pkTextField.setEditable(false);
		balanceTextField.setEditable(false);
	}
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
		skTextField.setText(peerModel.walletModel.getPrivateKey());
		pkTextField.setText(peerModel.walletModel.getPublicKey());
	}
	
	public void wire() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wire.fxml"));
		Parent root = loader.load();
		WireController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setX(skTextField.getScene().getWindow().getX()+220);
		stage.setY(skTextField.getScene().getWindow().getY());
		stage.show();
		
		
	}


	
}
