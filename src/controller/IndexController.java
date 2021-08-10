package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import factory.UtilFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Peer;
import util.NewPage;

public class IndexController implements Controller  {
	
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
	
	private Peer peer;
	private Stage stage;
	private String childPage;
	private UtilFactory utilFactory;
	private NewPage newPage;
	private Image upgradeButtonImage;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createObjects();
	}
	
	private void createObjects() {
		utilFactory = new UtilFactory();
	}
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	@Override
	public void setObject(Object object) {
		childPage = (String)object;
	}
	@Override
	public void executeDefaultProcess() throws IOException {
		newPage = utilFactory.getNewPage(stage,peer);
		setUserName();
		mainButtonAction();
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(miningButton,"mining");
		setButtonAction(stateConnectionButton,"stateConnection");
		//setButtonAction(wireButton,"wire");
		//업그레이드 버튼 추가되어야 함
	}
	
	@Override
	public void mainThreadAction() {}

	@Override
	public void mainButtonAction() throws IOException {
		switch(childPage) {
			case "blockchain" : blockchainHandler(); break;
			case "mining" : miningHandler(); break;
			case "stateConnection" : stateConnectionHandler(); break;
			case "wire" : wireHandler(); break;
			default : break;
		}
	}

	//MyPage로 가기
	@Override
	public void subButtonAction() throws IOException {
	}
	
	public void setChildPage(String childPage) {
		this.childPage = childPage;
	}

	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			try {
				childPage = name;
				mainButtonAction();
			} catch (IOException e) {}
		});
	}
	
	private void setUserName() {
		idText.setText(peer.getUserName());
	}
	
	private void miningHandler() throws IOException {
		newPage.addMiningPage(content);
	}
	
	private void blockchainHandler() throws IOException {
		System.out.println("블록체인 핸들러 실행");
	}
	
	private void stateConnectionHandler() throws IOException {
		System.out.println("연결관계 행들러 실행");
	}
	
	private void wireHandler() {
		System.out.println("송금 핸들러 실행");
	}

	private void doUpgrade() throws IOException {
	}

	

	
}
