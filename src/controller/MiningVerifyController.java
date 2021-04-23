package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Block;

public class MiningVerifyController {
	
	@FXML private Label verifiedLabel;
	@FXML private HBox center;
	@FXML private Rectangle rec;
	
	
	public void miningSuccess(Block block) throws IOException {
		
			verifiedLabel.setText("ä�� ����");
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/block.fxml"));
			center.getChildren().add(loader.load());
			BlockController bc = loader.getController();
			bc.createBlock(block);
		
	}
	
	public void resultOfVerify(Block block) throws IOException {
		
		verifiedLabel.setText("���� �Ϸ�");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/block.fxml"));
		center.getChildren().add(loader.load());
		BlockController bc = loader.getController();
		bc.createBlock(block);
		
	}
	
	public void failedVerify(Block block) throws IOException{
		//verifiedLabel.setStyle("-fx-background-color: #FA5882;");
		rec.setFill(Color.web("#FA5882"));
		verifiedLabel.setText("���� ����");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/block.fxml"));
		center.getChildren().add(loader.load());
		BlockController bc = loader.getController();
		bc.createBlock(block);
	}
	
}
