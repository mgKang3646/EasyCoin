package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.CreateFileChooser;
import util.CreateNewPage;

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
	
	public void login() {
		
		CreateFileChooser createFileChooser = new CreateFileChooser();
		String message = "�α��� �� ����Ű PEM ������ �����ϼ���.";
		
		File file = createFileChooser.showFileChooser(message, stage);
		
		
	}
	
	public void goJoinPage() {
		createNewPage.createNewPage("/view/join.fxml", stage);
		
	
	}

	
}
