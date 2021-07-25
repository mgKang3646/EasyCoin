package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.FileChooserForPem;
import encrypt.KeyFromPem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.NewPage;
import model.Peer;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private NewPage newPage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setStage(Stage stageValue) {
		stage = stageValue;
	}
	
	public void login() throws NoSuchAlgorithmException, IOException{
		
		// ���ɻ� : ���� Ž���� ��� ���ϴ� ����Ű PEM ���� ��� Ȯ���ϱ�
		String message = "�α��� �� ����Ű PEM ������ �����ϼ���.";
		FileChooserForPem createFileChooser = new FileChooserForPem();
		File file = createFileChooser.showFileChooser(message, stage); 
		
		if(file != null) {
			// ���ɻ� : PEM ���� ��θ� ���� ����Ű ��ü Ȯ���ϱ�
			KeyFromPem keyFromPem = new KeyFromPem();
			PrivateKey privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
			String userName = keyFromPem.getUserName();
			
			//����Ű ��ü�� Ȯ���� ���
			if(privateKey != null) {
				// ���ɻ� : Peer ��ü ����
				Dao dao = new Dao();
				Peer peer = dao.getPeer(userName);
				peer.setPrivateKey(privateKey);
				// ���ɻ� : P2P �� ���� ȭ�� ����
				this.newPage = new NewPage("/view/accessing.fxml", stage);
				AccessingController ac = (AccessingController)newPage.getController();
				ac.setPeer(peer);
				ac.doProgress();
				newPage.createPageOnNewStage();
			}
			//����Ű ��ü�� Ȯ������ ���� ���
			else {
				// ���ɻ� : �˾�â ����
				this.newPage = new NewPage("/view/popup.fxml", stage);
				PopupController pc = (PopupController)this.newPage.getController();
				pc.setMsg("�߸��� ������ ����Ű�Դϴ�.");
				newPage.createPageOnNewStage();
			}
		}
	}
	
	public void goJoinPage() throws IOException {
		this.newPage = new NewPage("/view/join.fxml", stage);
		newPage.createPageOnCurrentStage();
	}
	

	
}
