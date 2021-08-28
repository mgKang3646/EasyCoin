package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.BlockChain;
import model.Login;
import model.Peer;
import newview.NewView;
import newview.ViewURL;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Login login;
	private NewView newView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		login = new Login();
		newView = new NewView();
	}
	
	@Override
	public void throwObject(Object object) {}

	@Override
	public void execute() {
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
		switch(login.doLogin()) {
			case NONEFILE : break;
			case NONEKEY : showErrorPopup(); break;
			case KEYEXISTED : login(); break;
			default : break;
		}
	}

	private void login() {
		Peer.myPeer.setMyPeerFromDB(login.getUserName());
		Peer.myPeer.setPrivateKey(login.getPrivateKey());
		BlockChain.blockList.getBlocksFromDB();
		newView.getNewWindow(ViewURL.networkingURL);
	}
	
	public void showErrorPopup() {
		String msg = "잘못된 개인키 형식입니다.";
		newView.getNewWindow(ViewURL.popupURL,msg);
	}
	
	private void joinButtonAction() {
		newView.getNewScene(ViewURL.joinURL);
	}

	
}
