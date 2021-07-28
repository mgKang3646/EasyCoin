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
	@Override
	public void setObject(Object object) {}
	
	// ���ɻ� : �α��� ��ư Ŭ�� �� ���� ����
	@Override
	public void mainButtonAction() {
		// ���ɻ� : ���� Ž���� ��� ���ϴ� ����Ű PEM ���� ��� Ȯ���ϱ�
				String message = "�α��� �� ����Ű PEM ������ �����ϼ���.";
				FileChooserForPem createFileChooser = new FileChooserForPem();
				File file = createFileChooser.showFileChooser(message, stage); 
				
				if(file != null) {
					// ���ɻ� : PEM ���� ��θ� ���� ����Ű ��ü Ȯ���ϱ�
					try {
						KeyFromPem keyFromPem = new KeyFromPem();
						PrivateKey privateKey;
						privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
						
						String userName = keyFromPem.getUserName();
						
						//����Ű ��ü�� Ȯ���� ���
						if(privateKey != null) {
							// ���ɻ� : Peer ��ü ����
							Dao dao = new Dao();
							Peer peer = dao.getPeer(userName);
							peer.setPrivateKey(privateKey);
							// ���ɻ� : P2P �� ���� ȭ�� ����
							newPage = new NewPage("/view/accessing.fxml", stage);
							Controller controller = newPage.getController();
							controller.setObject(peer);
							controller.mainThreadAction();
							newPage.createPageOnNewStage();
						}
						//����Ű ��ü�� Ȯ������ ���� ���
						else {
							// ���ɻ� : �˾�â ����
							newPage = new NewPage("/view/popup.fxml", stage);
							Controller controller = newPage.getController();
							controller.setObject("�߸��� ����Ű �����Դϴ�.");
							newPage.createPageOnNewStage();
						}
					} catch (NoSuchAlgorithmException | IOException e) {
						e.printStackTrace();
					}
				}
			}
	
	// ���ɻ� : ȸ������ ��ư Ŭ���� ���� ����
	@Override
	public void subButtonAction() {
		try {
			this.newPage = new NewPage("/view/join.fxml", stage);
			newPage.createPageOnCurrentStage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void mainThreadAction() {}
	
	
	
}
