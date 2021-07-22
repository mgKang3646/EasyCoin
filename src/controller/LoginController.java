package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.CreateFileChooser;
import util.CreateNewPage;
import util.KeyFromPem;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private CreateNewPage createNewPage = new CreateNewPage();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setStage(Stage stageValue) {
		stage = stageValue;
	}
	
	public void login() throws NoSuchAlgorithmException, IOException{
		
		CreateFileChooser createFileChooser = new CreateFileChooser();
		String message = "로그인 할 개인키 PEM 파일을 선택하세요.";
		
		File file = createFileChooser.showFileChooser(message, stage);
		
		KeyFromPem keyFromPem = new KeyFromPem();
		PrivateKey privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
		
	}
	
	public void goJoinPage() {
		createNewPage.createNewPage("/view/join.fxml", stage);
		
	
	}

	
}
