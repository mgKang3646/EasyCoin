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
import util.SocketUtil;

public class JoinController implements Controller  {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	
	private Stage stage;
	private NewPage newPage;
	private Dao dao;
	private String userName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeObjects();
	}
	
	public void initializeObjects() {
		this.dao = new Dao();
	}
	
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
		newPage = new NewPage(stage);
	}
	
	@Override
	public void setObject(Object object) {}
	
	@Override
	public void mainThreadAction() {}
	
	@Override
	public void subButtonAction() throws IOException { 	// ���ɻ� : �α��� �������� ���ư���
		newPage.moveToLoginPage();
	}
	
	@Override
	public void mainButtonAction() throws IOException  {
			if(!isEmptyUserName()) {
				doJoinProcess(duplicateCheck());
			}
			else {
				setUserNameCheck("�г����� �����Դϴ�.");
			}
	}
	
	// ���ɻ� : userName ����üũ
	private boolean isEmptyUserName() {
		this.userName = userNameText.getText();
		if(userName == "") return true;
		else return false;
	}
	
	// ���ɻ� : ȸ������ �۾�����
	public void doJoinProcess(int duplicateResult) throws IOException {
			if(duplicateResult > 0) { // �ߺ��� �ȵ� ���
				makePemFile(); // Pem���ϻ���
				addPeerInDB(); // ȸ������ DB����
			}else if(duplicateResult == 0) { // �ߺ��� ��� 
				setUserNameCheck("�г����� �ߺ��˴ϴ�.");
			}else {} // SQL�� ���� ���� �߻� 
	}
	
	// ���ɻ� : userName �ߺ�üũ
	public int duplicateCheck() {
		int result = dao.checkDuplicateUserName(this.userName);
		return result;
	}
	
	// ���ɻ� : �α��ν� �ʿ��� Pem ���� �����
	private void makePemFile() {
		GeneratingKey generatingKey = new GeneratingKey();// ���ɻ� : ����Ű, ����Ű ����
		Pem pem = new Pem(this.userName); // ���ɻ� : Pem ���� �����
		pem.makePrivateAndPublicPemFile(generatingKey.getPrivateKey(), generatingKey.getPublicKey());
	}
	
	// ���ɻ� : ȸ������ DB�� �����ϱ�
	private void addPeerInDB() throws IOException {
		int result = dao.join(getLocalhost(), this.userName);
		if(result > 0) { // ȸ������ DB ���� ������ ���
			changePage();	
		}
		else {}	// ȸ������ DB ���� ������ ���
	}
	
	// ���ɻ� : ������ ��ȯ�ϱ�
	private void changePage() throws IOException {
		newPage.moveToLoginPage();
		newPage.createPopupPage("ȸ�������� �Ϸ�Ǿ����ϴ�.");
	}
	
	// ���ɻ� : Peer�� �ּһ���
	private String getLocalhost() {
		return "localhost:"+ (5500 + (int)(Math.random()*100));
	}
	// ���ɻ� : userName ��ȿ�� ���� ����
	private void setUserNameCheck(String msg) {
		userNameCheck.setText(msg);
		userNameCheck.setVisible(true);
	}
}	


