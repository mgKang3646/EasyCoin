package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController implements Initializable  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	Image mainImage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		mainImageView.setOnMousePressed(e->{
			mainImage = new Image("/image/main.gif");
			mainImageView.setImage(mainImage);
		});
		mainImageView.setOnMouseReleased(e->{
			mainImage = new Image("/image/main.png");
			mainImageView.setImage(mainImage);
		});

	}
	
	
	public void goJoinPage() {
		try {
			Parent login = FXMLLoader.load(getClass().getResource("/view/join.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage)joinButton.getScene().getWindow();
			primaryStage.setScene(scene);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
