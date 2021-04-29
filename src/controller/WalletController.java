package controller;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javax.json.Json;

import org.bouncycastle.util.encoders.Base64;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PeerModel;
import model.TransactionOutput;

public class WalletController implements Initializable{

	@FXML TextField balanceTextField;
	@FXML Button wireButton;
	private PeerModel peerModel = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		balanceTextField.setEditable(false);
	}
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
		//잔액 계산하기
		balanceTextField.setText(getBalance()+"");
	}
	
	//송금하기
	public void wire() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wire.fxml"));
		Parent root = loader.load();
		WireController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setX(balanceTextField.getScene().getWindow().getX()+220);
		stage.setY(balanceTextField.getScene().getWindow().getY());
		stage.show();
		
	}
	
	// 잔액 산출하기
	public float getBalance() {
			float total=0;
			for(TransactionOutput UTXO : peerModel.walletModel.getUTXOWallet()) {
				total += UTXO.value;
			}
			return total;	
	}
		
}
