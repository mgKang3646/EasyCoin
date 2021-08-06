package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Peer;

public class MyPageController implements Controller  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	@FXML private Button wireButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView mainImageView;
	@FXML private TextField balanceTextField;
	
	private Stage stage;
	private Peer peer;
	private String buttonName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(miningButton,"mining");
		setButtonAction(wireButton,"wire");
		setButtonAction(stateConnectionButton,"stateConnection");
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@Override
	public void setObject(Object object) {
		this.peer = (Peer)object;
		setUserName();
	}
	@Override
	public void mainButtonAction() throws IOException {
		switch(buttonName) {
			case "blockchain" : blockchainHandler(); break;
			case "mining" : miningHandler(); break;
			case "wire" : wireHandler(); break;
			case "stateConnection" : stateConnectionHandler(); break;
			default : break;
		}
	}
	
	@Override
	public void subButtonAction() throws IOException {
	}
	@Override
	public void mainThreadAction() {}
	
	private void setUserName() {
		idText.setText(peer.getUserName());
	}
	
	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			try {
				buttonName = name;
				mainButtonAction();
			} catch (IOException e) {}
		});
	}
	
	private void wireHandler() throws IOException {
		System.out.println("wireHandler 작동");
	}
		
	private void miningHandler() throws IOException {
		System.out.println("mingHandler 작동");
	}
	
	private void blockchainHandler() throws IOException {
		System.out.println("blockchainHandler 작동");
	}
	
	private void stateConnectionHandler() throws IOException {
		System.out.println("stateConnectionHandler 작동");
	}
}
