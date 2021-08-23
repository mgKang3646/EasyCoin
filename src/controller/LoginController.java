package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Login;
import newview.NewView;
import newview.ViewURL;

public class LoginController implements Initializable  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Login login;
	private NewView newView;
	

	public void initialize(URL arg0, ResourceBundle arg1) {
		login = new Login();
		newView = new NewView();
		setButtonAction();
	}
	
	private void setButtonAction() {
		loginButton.setOnAction(ActionEvent -> {
			loginButtonAction();
		});
		joinButton.setOnAction(ActionEvent -> {
			joinButtonAction();
		});
	}
	
	private void loginButtonAction() {
		login.doLogin();
		if(login.isGetFile()) {
			if(login.isGetPrivateKey()) newView.getNewScene(ViewURL.mypageURL);
			else {
				String msg = "잘못된 개인키 형식입니다.";
				newView.setControllerObject(msg);
				newView.getNewWindow(ViewURL.popupURL);
			}
		}
	}
	
	private void joinButtonAction() {
		newView.getNewScene(ViewURL.joinURL);
	}
}
