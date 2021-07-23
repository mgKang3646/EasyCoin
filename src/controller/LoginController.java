package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ResourceBundle;

import encrypt.FileChooserForPem;
import encrypt.KeyFromPem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.NewPage;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private NewPage newPage = new NewPage();
	
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
			
			if(privateKey != null) {
				// ��Ʈ��ũ ���� ������
			}else {
				// �˾�â ����
			}
		}
	}
	
	public void goJoinPage() {
		newPage.createNewPage("/view/join.fxml", stage);
		
	
	}

	
}
