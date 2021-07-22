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
import util.FileChooserForPem;
import util.NewPage;
import util.KeyFromPem;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private NewPage createNewPage = new NewPage();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setStage(Stage stageValue) {
		stage = stageValue;
	}
	
	public void login() throws NoSuchAlgorithmException, IOException{
		
		// 관심사 : 파일 탐색기 열어서 원하는 개인키 PEM 파일 경로 확보하기
		String message = "로그인 할 개인키 PEM 파일을 선택하세요.";
		FileChooserForPem createFileChooser = new FileChooserForPem();
		File file = createFileChooser.showFileChooser(message, stage); 
		
		// 관심사 : PEM 파일 경로를 통해 개인키 객체 확보하기
		KeyFromPem keyFromPem = new KeyFromPem();
		PrivateKey privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
		
		if(privateKey != null) {
			// 네트워크 연결 페이지
		}else {
			// 팝업창 띄우기
		}
		
	}
	
	public void goJoinPage() {
		createNewPage.createNewPage("/view/join.fxml", stage);
		
	
	}

	
}
