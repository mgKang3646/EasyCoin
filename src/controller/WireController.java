package controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import org.bouncycastle.util.encoders.Base64;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PeerModel;
import model.ReadPemFile;
import model.Transaction;
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
	
	public void wire() throws IOException {
		checkvalue.setVisible(false);

		try {
			//�۱ݾ�
			float value = Float.parseFloat(valueTextField.getText());
			
			//Ʈ����� ����
			if(value >= 0.05) {
				
				// �۱ݾ׸�ŭ�� �ܾ��� �ִ��� Ȯ��
				float total =0;
				JsonArrayBuilder minerArr = Json.createArrayBuilder();
				JsonArrayBuilder utxoHashArr = Json.createArrayBuilder();
				JsonArrayBuilder inputValueArr = Json.createArrayBuilder();

				for(TransactionOutput input : peerModel.walletModel.getUTXOWallet()) {
					total += input.getValue();
					inputValueArr.add(input.getValue());
					minerArr.add(input.getMiner());
					utxoHashArr.add(input.getTxoHash());
					System.out.println("JsonArray �� ������ : "+ input.getMiner());
					if(total >= value) break;
				}
				// �ܾ��� �۱ݾ׺��� ū ���
				if(total >= value) {
				
					//Ʈ����� ����
					Transaction newTransaction = new Transaction(sender,recipient,value); // Ʈ����� ����
					newTransaction.generateSignature(peerModel.walletModel.getPrivateKey()); //���ڼ��� ����
					newTransaction.generateHash();
					
					//�ӽ� Ʈ����� �����ϱ�
					StringWriter sw = new StringWriter();
					
					Json.createWriter(sw).writeObject(Json.createObjectBuilder()
															.add("sender", Base64.toBase64String(sender.getEncoded()))															
															.add("recipient",Base64.toBase64String(recipient.getEncoded()))
															.add("value", value+"")
															.add("signature", Base64.toBase64String(newTransaction.signature))
															.add("miners", minerArr)
															.add("utxoHashs", utxoHashArr)
															.add("inputValueArr", inputValueArr)
															.add("transactionHash", newTransaction.getHash())
															.build());
					
					peerModel.getServerListerner().sendMessage(sw.toString());
					
					// �ܾ��� �۱ݾ׺��� ���� ���
					String msg = "�۱��� �Ϸ�Ǿ����ϴ�.";
					openPopup(msg);
				
					}else {
						// �ܾ��� �۱ݾ׺��� ���� ���
						String msg = "�ܾ��� �����մϴ�.";
						openPopup(msg);
					}
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
			PublicKey tmpRecipient = readPemFile.readPublicKeyFromPemFile(file.getPath());
			if(tmpRecipient!=null) {
				recipient = tmpRecipient;
				recipientName = readPemFile.getUsername();
				recipientTextField.setText(recipientName);
			}else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/popup.fxml"));
				Parent root = loader.load();
				PopupController pc = loader.getController();
				pc.setMessage("�߸��� Ű �����Դϴ�. \r ����Ű Pem������ �������ֽʽÿ�.");
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setX(wireButton.getScene().getWindow().getX());
				stage.setY(wireButton.getScene().getWindow().getY()+40);
				stage.show();
			}
		}
	}
	
	public void openPopup(String msg) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/popup.fxml"));
		Parent root = loader.load();
		PopupController pc = loader.getController();
		pc.setMessage(msg);
		Scene scene = new Scene(root);
		Stage stage = (Stage)wireButton.getScene().getWindow();
		stage.setScene(scene);
	}

	
}
