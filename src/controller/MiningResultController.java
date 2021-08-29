package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.BlockChain;
import model.MiningState;
import newview.NewView;
import newview.ViewURL;

public class MiningResultController implements Controller {
	
	@FXML private Label verifiedLabel;
	@FXML private HBox content;
	@FXML private Rectangle rec;
	
	private NewView newView;
	private MiningState miningResult;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newView = new NewView();
	}
	@Override
	public void throwObject(Object object) {
		miningResult = (MiningState)object;		
	}
	@Override
	public void execute() {
		judgeResult();		
	}

	private void judgeResult() {
		switch(miningResult) {
			case MININGGRANTED : doSuccessMining(); break;
			case OTHERMININGGRANTED : doSuccessVerify(); break;
			default : break;
		}
	}
	
	private void doSuccessMining() {
		verifiedLabel.setText("√§±º");
		verifiedLabel.setTextFill(Color.GREEN);
		newView.addNewContent(content, ViewURL.blockURL, BlockChain.blockList.getLastBlock());
	}	
	
	private void doSuccessVerify(){
		verifiedLabel.setText("∞À¡ı");
		newView.addNewContent(content, ViewURL.blockURL, BlockChain.blockList.getLastBlock());
	}
	
}
