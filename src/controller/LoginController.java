package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.CreateNewPage;

public class LoginController implements Initializable  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	CreateNewPage createNewPage = new CreateNewPage();
	Image mainImage;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
//		mainImageView.setOnMousePressed(e->{
//			mainImage = new Image("/image/main.gif");
//			mainImageView.setImage(mainImage);
//		});
//		mainImageView.setOnMouseReleased(e->{
//			mainImage = new Image("/image/main.png");
//			mainImageView.setImage(mainImage);
//		});

	}
	
	public void goJoinPage() {
		
		createNewPage.createNewPage("/view/join.fxml", (Stage)joinButton.getScene().getWindow());
	
	}
}
