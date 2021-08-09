package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.KeyFromPem;
import factory.UtilFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Peer;
import util.NewPage;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private NewPage newPage;
	private FileChooser fc;
	private UtilFactory utilFactory;
	private KeyFromPem keyFromPem;
	private Dao dao;
	private Peer peer;
	private PrivateKey privateKey;
	private File file;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createObjects();
	}
	private void createObjects() {
		this.utilFactory = new UtilFactory();
		this.fc = new FileChooser();
		this.keyFromPem = new KeyFromPem();
		this.dao = new Dao();
	}
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@Override
	public void executeDefaultProcess() throws IOException {
		newPage = utilFactory.getNewPage(stage);
	}
	
	// ���ɻ� : ȸ������ �������� �Ѿ��
	@Override
	public void subButtonAction() {
		newPage.moveToJoinPage();
	}
	
	// ���ɻ� : �α����ϱ�
	@Override
	public void mainButtonAction() {
		if(isPemFile()) {
			getPrivateKeyFromPem(file);	
			createPeerInDB();
			applyPage();
		}
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
		newPage.setPeer(peer);
		if(privateKey != null) {
			newPage.createAccessingPage();
		}else {
			newPage.createPopupPage("�߸��� ����Ű �����Դϴ�.");
		}
	}
	// ���ɻ� : Pem���� ��� Ȯ���ϱ�
	private File getPemFilePath() {
		String message = "�α��� �� ����Ű PEM ������ �����ϼ���.";
		fc.setTitle(message);
		fc.setInitialDirectory(new File("./pem"));
		return fc.showOpenDialog(newPage.getStage());
	}
	
	
	@Override
	public void setPeer(Peer peer) {}
	@Override
	public void setObject(Object object) {}
	@Override
	public void mainThreadAction() {}
	
}
