package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.NewPage;

public class JoinController implements Controller {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
	}
	
	public void backLoginPage() {
		NewPage newPage = new NewPage();
		newPage.createNewPage("/view/login.fxml", stage);
	}
	
	public void join() {
		
	}
	
	
	
	
	
	

}
