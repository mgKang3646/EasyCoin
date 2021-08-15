package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.KeyFromPem;
import factory.NewPageFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Peer;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private FileChooser fc;
	private NewPageFactory newPageFactory;
	private KeyFromPem keyFromPem;
	private Dao dao;
	private Peer peer;
	private PrivateKey privateKey;
	private File file;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.newPageFactory = new NewPageFactory();
		this.fc = new FileChooser();
		this.keyFromPem = new KeyFromPem();
		this.dao = new Dao();
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
	
	private void setButtonAction() {
		loginButton.setOnAction(ActionEvent -> {
			loginButtonAction();
		});
		
		joinButton.setOnAction(ActionEvent -> {
			joinButtonAction();
		});
	}
	
	private void loginButtonAction() {
		if(isPemFile()) {
			getPrivateKeyFromPem(file);	
			createPeerInDB();
			applyPage();
		}
	}
	
	private void joinButtonAction() {
		newPageFactory.moveJoinPage();
	}
	
	// ���ɻ� : ���� Ž���� ��� ���ϴ� ����Ű PEM ���� ��� Ȯ���ϱ�
	private boolean isPemFile() {
		this.file = getPemFilePath();
		if(file!= null) {
			return true;
		}else {
			return false;
		}
	}
	// ���ɻ� : PEM ���� ��θ� ���� ����Ű ��ü Ȯ���ϱ�
	private void getPrivateKeyFromPem(File file) {
		if(file != null) {
			try {
				this.privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	// ���ɻ� : DB���� Peer���� �������
	private void createPeerInDB() {
		if(privateKey != null) {
			String userName = keyFromPem.getUserName();
			this.peer = dao.getPeer(userName);
			peer.setPrivateKey(privateKey);
		}
	}
	// ���ɻ� : ����Ű Ȯ�� ���ο� ���� ��������ȯ
	private void applyPage() {
		if(privateKey != null) {
			newPageFactory.createAccessingPage(peer);
		}else {
			String msg = "�߸��� ����Ű �����Դϴ�.";
			newPageFactory.createPopupPage(msg);
		}
	}
	// ���ɻ� : Pem���� ��� Ȯ���ϱ�
	private File getPemFilePath() {
		String message = "�α��� �� ����Ű PEM ������ �����ϼ���.";
		fc.setTitle(message);
		fc.setInitialDirectory(new File("./pem"));
		return fc.showOpenDialog(stage);
	}
	
	
	@Override
	public void setPeer(Peer peer) {}
	@Override
	public void setObject(Object object) {}
	
	
}
