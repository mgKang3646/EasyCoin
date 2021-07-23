package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.NewPage;

public class JoinController implements Controller {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
	}
	
	public void backLoginPage() {
		NewPage newPage = new NewPage();
		newPage.createNewPage("/view/login.fxml", stage);
	}
	
	public void join() throws IOException {
		
		// ���ɻ� : ID �ߺ�üũ
		Dao dao = new Dao();
		int duplicateResult = dao.checkDuplicateUserName(userNameText.getText());
		
		if(duplicateResult == 1) { // �ߺ� X
			// ���ɻ� : ����Ű, ����Ű ����
			GeneratingKey generatingKey = new GeneratingKey();
			generatingKey.generateKeyPair();
			
			// ���ɻ� : ����Ű, ����Ű Pem ���� ����
			Pem pem = new Pem(userNameText.getText());
			pem.makePemFile(generatingKey.getPrivateKey());
			pem.makePemFile(generatingKey.getPublicKey());

			
		}else if(duplicateResult == 0) { //�ߺ� O
		}else { 
			// SQL�� ���� ���� �߻� 
		}
		
	}
	
	
	
	
	
	

}
