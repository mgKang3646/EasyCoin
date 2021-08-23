package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.Peer;
import newview.NewView;
import newview.ViewURL;

public class IndexController implements Initializable  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	//@FXML private Button wireButton;
	@FXML private Button upgradeButton;
	@FXML private Button goMyPageButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView upgradeButtonImageView;
	@FXML private AnchorPane EasyCoin;	
	
	private NewView newView;
	private String contentPage;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
		contentPage = (String)NewView.getControllerObject();
		setIdText();
		setButton();
		switchContent();
	}	
	
	private void setIdText() {
		idText.setText(Peer.myPeer.getUserName());
	}

	private void switchContent() {
		switch(contentPage) {
			case "blockchain" : blockchainHandler(); break;
			case "mining" : miningHandler(); break;
			case "stateConnection" : stateConnectionHandler(); break;
			case "wire" : wireHandler(); break;
			default : break;
		}
	}
	
	private void setButton() {
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(miningButton,"mining");
		setButtonAction(stateConnectionButton,"stateConnection");
		//setButtonAction(wireButton,"wire");
	}

	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			contentPage = name;
			switchContent();
		});
	}
	
	private void miningHandler() {
		newView.addNewContent(ViewURL.miningURL,content);
	}
	
	private void blockchainHandler()  {
		newView.addNewContent("BlockChain", content);
	}
	
	private void stateConnectionHandler()  {
		System.out.println("연결관계 행들러 실행");
	}
	
	private void wireHandler() {
		System.out.println("송금 핸들러 실행");
	}

	private void doUpgrade(){
	}
	
}
