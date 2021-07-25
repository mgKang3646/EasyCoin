package controller;

import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.NewPage;

public class JoinController implements Controller  {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	Stage stage;
	NewPage newPage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
	}
	
	public void backLoginPage() throws IOException {
		this.newPage = new NewPage("/view/login.fxml", stage);
		newPage.createPageOnCurrentStage();
	}
	
	// ���ɻ� : ���ο� Peer ȸ�� ���� ���� 
	public void join() throws IOException{
		
		String userName = userNameText.getText();
		
		// ���ɻ� : userName ��ȿ�� �˻� ( ���� )
		if(userName == "") {
			userNameCheck.setText("�г����� �����Դϴ�.");
			userNameCheck.setVisible(true);
		}
		// ���ɻ� : userName �ߺ� �˻� �� DB ����
		else {
			if(processDuplicateUserName(userName)) { // ���ɻ� : userName �ߺ� �˻� 
				
				GeneratingKey generatingKey = generateKey(userName); // ���ɻ� : ����Ű, ����Ű ����
				String localhost = getLocalhost();	// ���ɻ� : �ּ� ����
				makePemFile(generatingKey.getPrivateKey(),generatingKey.getPublicKey(),userName); // ���ɻ� : ����Ű, ����Ű Pem ���� ����
				
				// ���ɻ� : Peer ���� DB ����
				if(insertIntoDB(localhost,userName)) {
					// ���ɻ� : DB ���� ���� ��, ȸ������ ���� �˾� ���� �� �α��� �������� �ڵ��̵�.
					this.newPage = new NewPage("/view/login.fxml", stage);
					newPage.createPageOnCurrentStage();
					this.newPage = new NewPage("/view/popup.fxml", stage);
					PopupController popupController = (PopupController)this.newPage.getController();
					popupController.setMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
					newPage.createPageOnNewStage();
				}else {
					//DB ���԰��� �� ���� �߻�
				}	
			}
		}
	}
	
	// ���ɻ� : userName �ߺ� �˻� ��ȿ�� ��� ��ȯ
	public boolean processDuplicateUserName(String userName) throws IOException {
		
		Dao dao = new Dao();
		int duplicateResult = dao.checkDuplicateUserName(userName);
		
		if(duplicateResult == 1) { // �ߺ� X
			return true;
		}else if(duplicateResult == 0) { //�ߺ� O
			userNameCheck.setText("�г����� �ߺ��˴ϴ�.");
			userNameCheck.setVisible(true);
			return false;
		}else { // SQL�� ���� ���� �߻� 
			return false;
		}
	}
	
	// ���ɻ� : ����Ű, ����Ű ����
	public GeneratingKey generateKey(String userName) {
		// ���ɻ� : ����Ű, ����Ű ����
		GeneratingKey generatingKey = new GeneratingKey();
		generatingKey.generateKeyPair();
		
		return generatingKey;
	}
	
	// ���ɻ� : �ּ� ����
	public String getLocalhost() {
		return "localhost:"+ (5500 + (int)(Math.random()*100));
	}
	
	// ���ɻ� : ����Ű, ����Ű Pem ���� ����
	public void makePemFile(PrivateKey privateKey, PublicKey publicKey,String userName) throws IOException {
		
		Pem pem = new Pem(userName);
		pem.makePemFile(privateKey);
		pem.makePemFile(publicKey);
		
	}
	
	// ���ɻ� : Peer ���� DB ����
	public boolean insertIntoDB(String localhost, String userName) {
		// ���ɻ� : DB ���� ���� ����
		Dao dao = new Dao();
		int joinResult = dao.join(localhost, userName);
		if(joinResult > 0) {
			return true;
		}else {
			return false;
		}
	}



		
}	


