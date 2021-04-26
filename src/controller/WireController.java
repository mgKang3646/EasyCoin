package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PeerModel;
import model.ReadPemFile;

public class WireController implements Initializable{

	@FXML TextField recipientTextField;
	@FXML Button recipientButton;
	@FXML TextField balanceTextField;
	@FXML Button wireButton;
	private PeerModel peerModel = null;
	private PublicKey recipient = null;
	private String recipientName = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		recipientTextField.setEditable(false);
	}
	
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
	}
	
	public void wire() {
		
	}
	
	public void findRecipient() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// 받는 사람 공개키 파일 선택하기
		FileChooser fc = new FileChooser();
		fc.setTitle("받는 사람의 공개키 파일을 선택해주세요.");
		fc.setInitialDirectory(new File("./pem"));
		File file = null;
		file = fc.showOpenDialog((Stage)wireButton.getScene().getWindow());
		
		if(file != null) {
			ReadPemFile readPemFile = new ReadPemFile();
			recipient = readPemFile.readPublicKeyFromPemFile(file.getPath());
			recipientName = readPemFile.getUsername();
			
			recipientTextField.setText(recipientName);
		}
	}

	
}
