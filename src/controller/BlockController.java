package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Block;
import model.Peer;

public class BlockController implements Controller {
	@FXML private TextField nonceText;
	@FXML private TextField previousHashText;
	@FXML private TextField hashText;
	@FXML private TextArea dataText;
	@FXML private TextField timestampText;
	@FXML private Label blockNumLabel;
	
	private Peer peer;
	private Block block;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}

	@Override
	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	@Override
	public void setObject(Object object) {
		this.block = (Block)object;
	}
	@Override
	public void execute()  {
		blockNumLabel.setText("Block #"+block.getNum());
		nonceText.setText(block.getNonce()+"");
		previousHashText.setText(block.getPreviousBlockHash());
		hashText.setText(block.getHash());
		timestampText.setText(block.getTimestamp());
		
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
	

}
