package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import factory.UtilFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Peer;
import util.NewPage;

public class MyPageController implements Controller  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	@FXML private Button wireButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView mainImageView;
	@FXML private TextField balanceTextField;
	
	private Peer peer;
	private Stage stage;
	private String childPage;
	private NewPage newPage;
	private UtilFactory utilFactory;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createObjects();
	}
	
	private void createObjects() {
		utilFactory = new UtilFactory();
	}
	
	@Override
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	@Override
	public void execute() {
		setUserName();
		newPage = utilFactory.getNewPage(stage,peer);
		setButtonAction(blockchainButton,"blockchain");
		setButtonAction(miningButton,"mining");
		setButtonAction(wireButton,"wire");
		setButtonAction(stateConnectionButton,"stateConnection");
	}
	@Override
	public void mainButtonAction() throws IOException {
		newPage.moveToIndexPage(childPage);
	}
	
	private void setUserName() {
		idText.setText(peer.getUserName());
	}
	private void setButtonAction(Button button, String name) {
		button.setOnAction(ActionEvent->{
			try {
				childPage = name;
				mainButtonAction();
			} catch (IOException e) {}
		});
	}

	
	@Override
	public void setObject(Object object) {}
	@Override
	public void subButtonAction() throws IOException {}
	
}
