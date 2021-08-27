package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Peer;
import newview.NewView;
import newview.ViewURL;

public class MyPageController implements Controller  {
	
	@FXML private Button miningButton;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	@FXML private Button wireButton;
	@FXML private Button connectionTableButton;
	@FXML private ImageView mainImageView;
	@FXML private TextField balanceTextField;
	
	private String contentPage;
	private NewView newView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
	}
	@Override
	public void throwObject(Object object) {}
	@Override
	public void execute() {
		setUserName();
		setButton();
	}
	
	private void setButton() {
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(connectionTableButton,"connectionTable");
		setButtonAction(miningButton,"mining");
		setWireButtonAction();
	}
	
	public void goIndexPage(){
		newView.getNewScene(ViewURL.indexURL, contentPage); 
	}
	
	private void setUserName() {
		idText.setText(Peer.myPeer.getUserName());
	}
	
	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			contentPage = name;
			goIndexPage();
		});
	}
	
	private void setWireButtonAction() {
		wireButton.setOnAction(ActionEvent ->{
			newView.getNewWindow(ViewURL.wireURL);
		});
	}
}
