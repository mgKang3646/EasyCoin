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

public class JoinController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	
	private NewView newView;
	private Join join;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
		join = new Join();
	}
	@Override
	public void throwObject(Object object) {}

	@Override
	public void execute() {
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
		if(isEmptyUserName()) setUserNameValidCheck("�г����� �����Դϴ�.");
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
			setUserNameValidCheck("�г����� �ߺ��˴ϴ�.");
		}
	}
	
	private void setUserNameValidCheck(String msg) {
		userNameCheck.setText(msg);
		userNameCheck.setVisible(true);
	}
	
	private void changePage()  {
		String msg = "ȸ�������� �Ϸ�Ǿ����ϴ�.";
		newView.getNewScene(ViewURL.loginURL);
		newView.getNewWindow(ViewURL.popupURL,msg);
	}
}	


