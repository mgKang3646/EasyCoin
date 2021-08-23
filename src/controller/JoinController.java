package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.Dao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Join;
import newview.NewView;
import newview.ViewURL;

public class JoinController implements Initializable  {
	
	@FXML private Button joinButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	
	private NewView newView;
	private Join join;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
		join = new Join();
		setButtonAction();
	}

	private void setButtonAction() {
		joinButton.setOnAction(ActionEvent -> {
			joinButtonAction();
		});
		goLoginPageButton.setOnAction(ActionEvent -> {
			goLoginPageAction();
		});
	}
	
	private void joinButtonAction()  {
		if(isEmptyUserName()) setUserNameValidCheck("닉네임이 공백입니다.");
		else doJoin();
	}
	
	private void goLoginPageAction() {
		newView.getNewScene(ViewURL.loginURL);
	}

	private boolean isEmptyUserName() {
		if(userNameText.getText().equals("")) return true;
		else return false;
	}
	
	private void doJoin() {
		join.setUserName(userNameText.getText());
		if(!join.duplicateCheck()) { 
			if(join.doJoin()) changePage();
		}else { 
			setUserNameValidCheck("닉네임이 중복됩니다.");
		}
	}
	
	private void setUserNameValidCheck(String msg) {
		userNameCheck.setText(msg);
		userNameCheck.setVisible(true);
	}
	
	private void changePage()  {
		String msg = "회원가입이 완료되었습니다.";
		newView.getNewScene(ViewURL.loginURL);
		newView.setControllerObject(msg);
		newView.getNewWindow(ViewURL.popupURL);
	}
}	


