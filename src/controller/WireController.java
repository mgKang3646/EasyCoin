package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Peer;
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	@Override
	public void throwObject(Object object) {}
	@Override
	public void execute() {
		wire = new Wire();
		initializeUI();
		setMyPublicButtonAction();
		setOtherPublicButtonAction();
		setWireButtonAction();
	}
	
	private void setMyPublicButtonAction() {
		myPublicButton.setOnAction(ActionEvent->{
			wire.settingPublicKey(getStage());
			if(isValidPublicKey() == 1) {
				if(wire.isUserNameEqual(Peer.myPeer.getUserName())) {
					setMyPublicKey();
					validLabel.setVisible(false);
				}
				else {
					myPublicKeyText.setText("");
					showValidLabel("본인 계좌가 아닙니다.");
				}
			}
			else if(isValidPublicKey() == 0) {
				myPublicKeyText.setText("");
				showValidLabel("잘못된 파일 형식입니다.");
			}
		});
	}
	
	private void setOtherPublicButtonAction() {
		otherPublicButton.setOnAction(ActionEvent->{
			wire.settingPublicKey(getStage());
			if(isValidPublicKey() == 1) {
				if(wire.isUserNameEqual(Peer.myPeer.getUserName())) {
					myPublicKeyText.setText("");
					showValidLabel("본인 계좌입니다.");
				}
				else {
					setOtherPublicKey();
					validLabel.setVisible(false);
				}
			}
			else if(isValidPublicKey() == 0) {
				otherPublicKeyText.setText("");
				showValidLabel("잘못된 파일 형식입니다.");
			}
		});
		
	}
	
	private void setWireButtonAction() {
		wireButton.setOnAction(ActionEvent->{
			if(wireValidCheck()) {
				//송금
			}
		});
	}
	
	private void setMyPublicKey() {
		Peer.myPeer.setPublicKey(wire.getPublicKey());
		myPublicKeyText.setText(wire.getUserName());
	}
	
	private void setOtherPublicKey() {
		otherPublicKeyText.setText(wire.getUserName());
	}
	
	private int isValidPublicKey() {
		if(wire.isFile()) {
			if(wire.isPublicKey()) return 1;
			else return 0;
		}
		return -1;
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
		if(Double.parseDouble(valueTextField.getText()) < 0.05) {
			showValidLabel("0.05ETC 이상의 금액만 송금 가능합니다."); 
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
}



