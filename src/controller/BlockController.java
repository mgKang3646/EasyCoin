package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Block;
import model.Peer;

public class BlockController implements Controller {
	@FXML private TextField blockNumText;
	@FXML private TextField nonceText;
	@FXML private TextField previousHashText;
	@FXML private TextField hashText;
	@FXML private TextArea dataText;
	@FXML private TextField timestampText;
	
	private Stage stage;
	private Peer peer;
	private Block block;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}

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
		this.block = (Block)object;
	}
	@Override
	public void executeDefaultProcess() throws IOException {
		blockNumText.setText("#"+block.getNum());
		nonceText.setText(block.getNonce());
		previousHashText.setText(block.getPreviousBlockHash());
		hashText.setText(block.getHash());
		timestampText.setText(block.getTimestamp());
		
		blockNumText.setEditable(false);
		nonceText.setEditable(false);
		dataText.setEditable(false);
		previousHashText.setEditable(false);
		hashText.setEditable(false);
		timestampText.setEditable(false);
	}
	
	
	
	@Override
	public void mainButtonAction() throws IOException {	}
	@Override
	public void subButtonAction() throws IOException {	}
	@Override
	public void mainThreadAction() {}

}
