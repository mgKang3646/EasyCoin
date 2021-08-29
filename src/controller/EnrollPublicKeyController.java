package controller;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.EnrollPublicKey;
import model.Peer;
import model.Wallet;
import newview.NewView;
import newview.ViewURL;

public class EnrollPublicKeyController implements Controller {
	@FXML private Button enrollButton;
	@FXML private Label validLabel;
	@FXML private Label enrollLabel;
	
	private EnrollPublicKey enrollPublicKey;
	private Wallet wallet;
	private NewView newView;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		enrollPublicKey = new EnrollPublicKey();
		wallet = new Wallet();
		newView = new NewView();
	}

	@Override
	public void throwObject(Object object) {
		
	}

	@Override
	public void execute() {
		enrollLabel.setText("계좌를 등록해야 채굴 및 송금이 가능합니다.");
		validLabel.setVisible(false);
		setEnrollButtonAction();
	}
	
	public void setEnrollButtonAction() {
		enrollButton.setOnAction(ActionEvent->{
			switch(enrollPublicKey.enrollPublicKey(getStage())) {
				case NONEFILE : break;
				case NONEKEY : doNoneKey(); break;
				case NOTEQUALUSERNAME :doNotEqualUserName(); break;
				case EQUALUSERNAME : doEqualUserName(); break;
				default : break;
			}
		});
	}
	
	private void doNoneKey() {
		validLabel.setVisible(true);
		validLabel.setText("잘못된 파일 형식입니다.");
		enrollLabel.setText("계좌를 등록해야 채굴 및 송금이 가능합니다.");
	}
	
	private void doNotEqualUserName() {
		validLabel.setVisible(true);
		validLabel.setText("본인 계좌가 아닙니다.");
		enrollLabel.setText("계좌를 등록해야 채굴 및 송금이 가능합니다.");
	}
	
	private void doEqualUserName() {
		Peer.myPeer.setPublicKey(enrollPublicKey.getPublicKey());
		//wallet.requestITXO(); 구체화 
		getStage().close();
		newView.getNewWindow(ViewURL.popupURL, "계좌 등록 완료"	);
	}
	
	private Stage getStage() {
		return (Stage)enrollButton.getScene().getWindow();
	}
	
	

}
