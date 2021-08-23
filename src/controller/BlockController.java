package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Block;
import newview.NewView;

public class BlockController implements Initializable {
	@FXML private TextField nonceText;
	@FXML private TextField previousHashText;
	@FXML private TextField hashText;
	@FXML private TextArea dataText;
	@FXML private TextField timestampText;
	@FXML private Label blockNumLabel;
	
	private Block block;

	public void initialize(URL arg0, ResourceBundle arg1) {
		this.block = (Block)NewView.getControllerObject();
		execute();
	}
	
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
}
