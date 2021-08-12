package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import factory.UtilFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Block;
import model.Peer;
import util.NewPage;

public class MiningResultController implements Controller {
	
	@FXML private Label verifiedLabel;
	@FXML private HBox content;
	@FXML private Rectangle rec;
	
	private Peer peer;
	private Block block;
	private UtilFactory utilFactory;
	private NewPage newPage;
	private String miningResult;
	
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
	public void setObject(Object object) {
		miningResult = (String)object;
	}
	
	@Override
	public void execute() {
		setNewPage();
		judgeResult();
	}
	
	private void setNewPage() {
		newPage = utilFactory.getNewPage(null,peer);
	}
	
	private void judgeResult() {
		switch(miningResult) {
		case "successMining" : doSuccessMining(); break;
		case "successVerify" : doSuccessVerify(); break;
		case "failedVerify" : doFailVerify(); break;
		}
	}
	
	private void doSuccessMining() {
			verifiedLabel.setText("블록 채굴 성공");
			newPage.addBlockResultPage(content);
	}	
	
	private void doSuccessVerify(){}
	private void doFailVerify(){}

	
	
	@Override
	public void mainButtonAction() throws IOException {}
	@Override
	public void subButtonAction() throws IOException {}
	
	
}
