package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;
import factory.NewPageFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Peer;

public class JoinController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	
	private Stage stage;
	private NewPageFactory newPageFactory;
	private Dao dao;
	private String userName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newPageFactory = new NewPageFactory();
		dao = new Dao();
	}
	@Override 
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@Override
	public void execute() {
		setButtonAction();
		newPageFactory.setStage(stage);
	}
	
	public void setButtonAction() {
		joinButton.setOnAction(ActionEvent -> {
			joinButtonAction();
		});
		
		goLoginPageButton.setOnAction(ActionEvent -> {
			goLoginPageButton();
		});
	}
	
	public void joinButtonAction()  {
		if(!isEmptyUserName()) {
			doJoinProcess(duplicateCheck());
		}
		else {
			setUserNameCheck("�г����� �����Դϴ�.");
		}
	}
	
	public void goLoginPageButton() {
		newPageFactory.moveLoginPage();
	}

	// ���ɻ� : userName ����üũ
	private boolean isEmptyUserName() {
		this.userName = userNameText.getText();
		if(userName == "") return true;
		else return false;
	}
	
	// ���ɻ� : ȸ������ �۾�����
	public void doJoinProcess(int duplicateResult) {
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
	private void addPeerInDB()  {
		int result = dao.join(getLocalhost(), this.userName);
		if(result > 0) { // ȸ������ DB ���� ������ ���
			changePage();	
		}
		else {}	// ȸ������ DB ���� ������ ���
	}
	
	// ���ɻ� : ������ ��ȯ�ϱ�
	private void changePage()  {
		String msg = "ȸ�������� �Ϸ�Ǿ����ϴ�.";
		newPageFactory.moveLoginPage();
		newPageFactory.createPopupPage(msg);
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


