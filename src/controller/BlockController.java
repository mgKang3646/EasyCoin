package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Block;

public class BlockController {
	
	@FXML private TextField blockNumText;
	@FXML private TextField nonceText;
	@FXML private TextField previousHashText;
	@FXML private TextField hashText;
	@FXML private TextArea dataText;
	@FXML private TextField timestampText;
	
	public void createBlock(Block block) {
		
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

}
