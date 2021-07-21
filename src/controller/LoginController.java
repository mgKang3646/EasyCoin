package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.CreateNewPage;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	private Stage stage;
	CreateNewPage createNewPage = new CreateNewPage();
	Image mainImage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	@Override
	public void setStage(Stage stageValue) {
		stage = stageValue;
	}
	
	public void login() {
		
		FileChooser fc = new FileChooser();
		fc.setTitle("로그인 할 개인키 파일을 선택해주세요.");
		fc.setInitialDirectory(new File("./pem"));
		File file = fc.showOpenDialog(stage);
		
	}
	
	public void goJoinPage() {
		createNewPage.createNewPage("/view/join.fxml", (Stage)joinButton.getScene().getWindow());
		
	
	}

	
}
