package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Refresh;
import model.Peer;
import newview.NewView;
import newview.ViewURL;

public class IndexController implements Controller  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	//@FXML private Button wireButton;
	@FXML private Button upgradeButton;
	@FXML private Button goMyPageButton;
	@FXML private Button refreshButton;
	@FXML private Button connectionTableButton;
	@FXML private ImageView upgradeButtonImageView;
	@FXML private Label easyLabel;
	@FXML private Label coinLabel;
	
	private NewView newView;
	private String contentPage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();		
	}	
	
	@Override
	public void throwObject(Object object) {
		contentPage = (String)object;
	}
	
	@Override
	public void execute() {
		setIdText();
		setEventAction();
		switchContent();
	}
	
	private void setIdText() {
		idText.setText(Peer.myPeer.getUserName());
	}

	private void switchContent() {
		switch(contentPage) {
			case "blockchain" : blockchainHandler(); break;
			case "mining" : miningHandler(); break;
			case "connectionTable" : connectionTableHandler(); break;
			case "wire" : wireHandler(); break;
			case "refresh" : refresh(); break;
			default : break;
		}
	}
	
	private void miningHandler() {
		newView.addNewContent(content, ViewURL.miningURL);
	}
	
	private void blockchainHandler()  {
		newView.addBlockChainContent(content);
	}
	
	private void connectionTableHandler()  {
		newView.addNewContent(content, ViewURL.connectionTableURL);
	}
	
	private void refresh(){
		newView.getNewWindow(ViewURL.refreshURL);
	}
	
	private void wireHandler() {
		System.out.println("송금 핸들러 실행");
	}

	private void setEventAction() {
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(miningButton,"mining");
		setButtonAction(connectionTableButton,"connectionTable");
		setButtonAction(refreshButton,"refresh");
		setLabelMouseEventAction();
		//setButtonAction(wireButton,"wire");
	}

	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			contentPage = name;
			switchContent();
		});
	}
	
	private void setLabelMouseEventAction() {
		easyLabel.setOnMouseEntered(e->{
			easyLabel.setTextFill(Color.web("#fff600"));
			coinLabel.setTextFill(Color.WHITE);
		});
		easyLabel.setOnMouseExited(e->{
			easyLabel.setTextFill(Color.WHITE);
			coinLabel.setTextFill(Color.web("#fff600"));
		});
		coinLabel.setOnMouseEntered(e->{
			easyLabel.setTextFill(Color.web("#fff600"));
			coinLabel.setTextFill(Color.WHITE);
		});
		coinLabel.setOnMouseExited(E->{
			easyLabel.setTextFill(Color.WHITE);
			coinLabel.setTextFill(Color.web("#fff600"));
		});
		
		easyLabel.setOnMousePressed(e->{
			newView.getNewScene(ViewURL.mypageURL);
		});
		coinLabel.setOnMousePressed(e->{
			newView.getNewScene(ViewURL.mypageURL);
		});
	}
}
