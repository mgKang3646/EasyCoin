package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;
import factory.UtilFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Peer;
import util.NewPage;

public class JoinController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	
	private NewPage newPage;
	private UtilFactory utilFactory;
	private Dao dao;
	private String userName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.utilFactory = new UtilFactory();
		this.dao = new Dao();
	}
	
	@Override
	public void execute() {
	}
	
	private void setNewPage(NewPage newPage) {
		this.newPage = newPage;
	}
	
	private Stage getStage() {
		return (Stage)joinButton.getScene().getWindow();
	}

	@Override		// ���ɻ� : �α��� �������� ���ư���
	public void subButtonAction() throws IOException {
		setNewPage(utilFactory.getNewScene(getStage()));
		newPage.makePage("/view/login.fxml");
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
		Stage stage = getStage();
		setNewPage(utilFactory.getNewScene(stage));
		newPage.makePage("/view/login.fxml");
		setNewPage(utilFactory.getNewStage(stage));
		newPage.makePage("/view/popup.fxml","ȸ�������� �Ϸ�Ǿ����ϴ�.");
		newPage.show();
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

	@Override
	public void setPeer(Peer peer) {}
	@Override
	public void setObject(Object object) {}
	
}	


