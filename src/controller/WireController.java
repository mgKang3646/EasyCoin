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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PeerModel;
import model.ReadPemFile;
import model.Transaction;

public class WireController implements Initializable{

	@FXML TextField recipientTextField;
	@FXML Button recipientButton;
	@FXML TextField valueTextField;
	@FXML Button wireButton;
	@FXML Label checkvalue;
	
	private PeerModel peerModel = null;
	private PublicKey sender = null;
	private PublicKey recipient = null;
	private String recipientName = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		recipientTextField.setEditable(false);
		checkvalue.setVisible(false);
	}
	
	
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
		this.sender = peerModel.walletModel.getPublicKey();
	}
	
	public void wire() {
		checkvalue.setVisible(false);

		try {
			float value = Float.parseFloat(valueTextField.getText());
			
			//트랜잭션 생성
			if(value >= 0.05) {
				Transaction newTransaction = new Transaction(sender,recipient,value); // 트랜잭션 생성
				newTransaction.generateSignature(peerModel.walletModel.getPrivateKey()); //전자서명 생성
			}
			
			//입금액이 0.05 미만인 경우
			else {
				checkvalue.setText("0.05ETC 미만은 송금 하실 수 없습니다.");
				checkvalue.setVisible(true);
			}
			
		}catch(NumberFormatException e) {
			checkvalue.setText("잘못된 형식의 송금액입니다.");
			checkvalue.setVisible(true);
		}
		
		
		
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
