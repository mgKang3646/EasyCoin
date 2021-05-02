package controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javax.json.Json;

import org.bouncycastle.util.encoders.Base64;

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
import model.TransactionInput;
import model.TransactionOutput;

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
			//�۱ݾ�
			float value = Float.parseFloat(valueTextField.getText());
			
			//Ʈ����� ����
			if(value >= 0.05) {
				
				// �۱ݾ׸�ŭ�� �ܾ��� �ִ��� Ȯ��
				float total =0;
				for(TransactionOutput inputs : peerModel.walletModel.getUTXOWallet()) {
					total += inputs.getValue();
				}
				
				//Ʈ����� ����
				Transaction newTransaction = new Transaction(sender,recipient,value); // Ʈ����� ����
				newTransaction.generateSignature(peerModel.walletModel.getPrivateKey()); //���ڼ��� ����
				
				//�ӽ� Ʈ����� �����ϱ�
				StringWriter sw = new StringWriter();
				
				Json.createWriter(sw).writeObject(Json.createObjectBuilder()
														.add("sender", Base64.toBase64String(sender.getEncoded()))															
														.add("recipient",Base64.toBase64String(recipient.getEncoded()))
														.add("value", value+"")
														.add("signature", Base64.toBase64String(newTransaction.signature))
														.build());
				
				peerModel.getServerListerner().sendMessage(sw.toString());
			
			}
			
			//�Աݾ��� 0.05 �̸��� ���
			else {
				checkvalue.setText("0.05ETC �̸��� �۱� �Ͻ� �� �����ϴ�.");
				checkvalue.setVisible(true);
			}
			
		}catch(NumberFormatException e) {
			checkvalue.setText("�߸��� ������ �۱ݾ��Դϴ�.");
			checkvalue.setVisible(true);
		}
		
		
		
	}
	
	public void findRecipient() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// �޴� ��� ����Ű ���� �����ϱ�
		FileChooser fc = new FileChooser();
		fc.setTitle("�޴� ����� ����Ű ������ �������ּ���.");
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
