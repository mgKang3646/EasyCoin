package controller;

import java.net.URL;
import java.util.ResourceBundle;

import factory.NewPageFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Peer;

public class MiningResultController implements Controller {
	
	@FXML private Label verifiedLabel;
	@FXML private HBox content;
	@FXML private Rectangle rec;
	
	private Stage stage;
	private Peer peer;
	private NewPageFactory newPageFactory;
	private String miningResult;
	
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
		miningResult = (String)object;
	}
	
	@Override
	public void execute() {
		judgeResult();
		newPageFactory.setStage(stage);
	}
	
	private void judgeResult() {
		switch(miningResult) {
			case "successMining" : doSuccessMining(); break;
			case "successVerify" : doSuccessVerify(); break;
			case "failedVerify" : doFailVerify(); break;
			default : break;
		}
	}
	
	private void doSuccessMining() {
			verifiedLabel.setText("블록 채굴 성공");
			newPageFactory.addMiningResultPage(content, peer);
	}	
	
	private void doSuccessVerify(){}
	private void doFailVerify(){}
}
