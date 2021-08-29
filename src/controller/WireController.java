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
				newView.getNewWindow(ViewURL.popupURL,"송금이 완료되었습니다.");
			}
		});
	}
	
	private void doNoneKeyMyPublic() {
		myPublicKeyText.setText("");
		showValidLabel("잘못된 파일 형식입니다.");
	}
	
	private void doNotEqualUserNameMyPublic() {
		myPublicKeyText.setText("");
		showValidLabel("본인 계좌가 아닙니다.");
	}
	
	private void doEqualUserNameMyPublic() {
		Peer.myPeer.setPublicKey(wire.getPublicKey());
		System.out.println("내 계좌 이름 : " + wire.getUserName());
		myPublicKeyText.setText(wire.getUserName());
		validLabel.setVisible(false);
	}
	
	private void doNoneKeyOtherPublic() {
		otherPublicKeyText.setText("");
		showValidLabel("잘못된 파일 형식입니다.");
	}
	
	private void doNotEqualUserNameOtherPublic() {
		validLabel.setVisible(false);
		System.out.println("상대 계좌 이름 : " + wire.getUserName());
		otherPublicKeyText.setText(wire.getUserName());
		recipient = wire.getPublicKey();
		
	}
	
	private void doEqualUserNameOtherPublic() {
		otherPublicKeyText.setText("");
		showValidLabel("본인 계좌입니다.");
	}
	
	private boolean wireValidCheck() {
		if(myPublicKeyText.getText().equals("")) {
			showValidLabel("내 계좌를 선택해주십시오."); 
			return false;
		}
		if(otherPublicKeyText.getText().equals("")) {
			showValidLabel("상대 계좌를 선택해주십시오.");
			return false;
		}
		if(valueTextField.getText().equals("")) {
			showValidLabel("입금액을 입력해주십시오."); 
			return false;
		}
		if(getValue() < 0.05) {
			showValidLabel("0.05ETC 이상의 금액만 송금 가능합니다."); 
			return false;
		}
		if(!wire.isAfford(getValue())){
			showValidLabel("잔액이 부족합니다."); 
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



