package controller;

import java.net.URL;
import java.security.PublicKey;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Peer;
import model.Transaction;
import model.Wallet;
import model.Wire;
import newview.NewView;
import newview.ViewURL;

public class WireController implements Controller {
	@FXML private TextField myPublicKeyText;
	@FXML private TextField otherPublicKeyText;
	@FXML private TextField valueTextField;
	@FXML private Button myPublicButton;
	@FXML private Button otherPublicButton;
	@FXML private Button wireButton;
	@FXML private Label validLabel;
	
	private Wire wire;	
	private PublicKey recipient;
	private NewView newView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
	}
	@Override
	public void throwObject(Object object) {}
	@Override
	public void execute() {
		wire = new Wire();
		initializeUI();
		setMyPublicText();
		setMyPublicButtonAction();
		setOtherPublicButtonAction();
		setWireButtonAction();
	}
	
	private void setMyPublicText() {
		if(Peer.myPeer.getPublicKey() != null) {
			myPublicKeyText.setText(Peer.myPeer.getUserName());
		}
	}
	
	private void setMyPublicButtonAction() {
		myPublicButton.setOnAction(ActionEvent->{
			switch(wire.searchPublicKey(getStage())) {
				case NONEFILE : break;
				case NONEKEY : doNoneKeyMyPublic(); break;
				case NOTEQUALUSERNAME : doNotEqualUserNameMyPublic(); break;
				case EQUALUSERNAME : doEqualUserNameMyPublic(); break;
				default : break;
			}
		});
	}

	private void setOtherPublicButtonAction() {
		otherPublicButton.setOnAction(ActionEvent->{
				switch(wire.searchPublicKey(getStage())) {
				case NONEFILE : break;
				case NONEKEY : doNoneKeyOtherPublic(); break;
				case NOTEQUALUSERNAME : doNotEqualUserNameOtherPublic(); break;
				case EQUALUSERNAME : doEqualUserNameOtherPublic(); break;
				default : break;
			}
		});
	}
	
	private void setWireButtonAction() {
		wireButton.setOnAction(ActionEvent->{
			if(wireValidCheck()) {
				Transaction tx = wire.makeTransaction(recipient, getValue());
				Wallet.txList.addTxList(tx);
				wire.broadcastingTX(tx);
				getStage().close();
				newView.getNewWindow(ViewURL.popupURL,"�۱��� �Ϸ�Ǿ����ϴ�.");
			}
		});
	}
	
	private void doNoneKeyMyPublic() {
		myPublicKeyText.setText("");
		showValidLabel("�߸��� ���� �����Դϴ�.");
	}
	
	private void doNotEqualUserNameMyPublic() {
		myPublicKeyText.setText("");
		showValidLabel("���� ���°� �ƴմϴ�.");
	}
	
	private void doEqualUserNameMyPublic() {
		Peer.myPeer.setPublicKey(wire.getPublicKey());
		System.out.println("�� ���� �̸� : " + wire.getUserName());
		myPublicKeyText.setText(wire.getUserName());
		validLabel.setVisible(false);
	}
	
	private void doNoneKeyOtherPublic() {
		otherPublicKeyText.setText("");
		showValidLabel("�߸��� ���� �����Դϴ�.");
	}
	
	private void doNotEqualUserNameOtherPublic() {
		validLabel.setVisible(false);
		System.out.println("��� ���� �̸� : " + wire.getUserName());
		otherPublicKeyText.setText(wire.getUserName());
		recipient = wire.getPublicKey();
		
	}
	
	private void doEqualUserNameOtherPublic() {
		otherPublicKeyText.setText("");
		showValidLabel("���� �����Դϴ�.");
	}
	
	private boolean wireValidCheck() {
		if(myPublicKeyText.getText().equals("")) {
			showValidLabel("�� ���¸� �������ֽʽÿ�."); 
			return false;
		}
		if(otherPublicKeyText.getText().equals("")) {
			showValidLabel("��� ���¸� �������ֽʽÿ�.");
			return false;
		}
		if(valueTextField.getText().equals("")) {
			showValidLabel("�Աݾ��� �Է����ֽʽÿ�."); 
			return false;
		}
		if(getValue() < 0.05) {
			showValidLabel("0.05ETC �̻��� �ݾ׸� �۱� �����մϴ�."); 
			return false;
		}
		if(!wire.isAfford(getValue())){
			showValidLabel("�ܾ��� �����մϴ�."); 
			return false;
		}
		else {
			validLabel.setVisible(false);
			return true;
		}
	}
	
	private Stage getStage() {
		return (Stage)myPublicKeyText.getScene().getWindow();
	}
	
	private void showValidLabel(String msg) {
		validLabel.setText(msg);
		validLabel.setVisible(true);
	}
	
	private void initializeUI() {
		myPublicKeyText.setText("");
		otherPublicKeyText.setText("");
		valueTextField.setText("");
		validLabel.setVisible(false);
	}
	
	private float getValue() {
		return Float.parseFloat(valueTextField.getText());
	}
}



