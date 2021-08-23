package controller;

import java.net.URL;
import java.util.ResourceBundle;
import factory.NewPageFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Peer;
import newview.FxmlLoader;

public class MyPageController implements Controller  {
	
	@FXML private Button miningButton;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	@FXML private Button wireButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView mainImageView;
	@FXML private TextField balanceTextField;
	
	private Stage stage;
	private Peer peer;
	private String childPage;
	private NewPageFactory newPageFactory;
	private FxmlLoader miningFxmlObjects;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newPageFactory = new NewPageFactory();
	}
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@Override
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	@Override
	public void setObject(Object object) {
		this.miningFxmlObjects = (FxmlLoader)object;
	}
	@Override
	public void execute() {
		newPageFactory.setStage(stage);
		setUserName();
		setMiningButtonAction();
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(wireButton,"wire");
		setButtonAction(stateConnectionButton,"stateConnection");
	}
	
	public void goIndexPage(){
		newPageFactory.moveIndexPage(peer,childPage);
	}
	
	private void setUserName() {
		idText.setText(peer.getUserName());
	}
	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			childPage = name;
			goIndexPage();
		});
	}
	
	private void setMiningButtonAction() {
		miningButton.setOnAction(ActionEvent->{
			newPageFactory.moveIndexPageForMining(peer, miningFxmlObjects);
		});
	}
}
