package newview;

import javafx.scene.layout.HBox;
import model.Block;
import model.BlockChain;

public class NewView{

	private static Object controllerObject;
	private FxmlLoader fxmlLoader;
	private FxmlStage fxmlStage;
	private FxmlScene fxmlScene;
	private FxmlContent fxmlContent;
	
	public NewView(){
		fxmlStage = new FxmlStage();
		fxmlScene = new FxmlScene();
		fxmlContent = new FxmlContent();
	}
	
	public static Object getControllerObject() {
		return controllerObject;
	}
	
	public void setControllerObject(Object object) {
		this.controllerObject = object;
	}
	
	public void openView(String url) {
		FxmlStage.getPrimaryStage().setScene(fxmlScene.getFXMLScene(url));
		FxmlStage.getPrimaryStage().show();
	}
	
	public void getNewScene(String url) {
		FxmlStage.getPrimaryStage().setScene(fxmlScene.getFXMLScene(url));
	}
	
	public void getNewWindow(String url) {
		fxmlStage.showNewStage(url);
	}
	
	public void addNewContent(String url, HBox content) {
		if(url.equals("BlockChain")) addBlockChain(url,content);
		else {
			fxmlContent.setContent(content);
			fxmlContent.clearContent();
			fxmlContent.addContent(url);
		}
	}
	
	private void addBlockChain(String url, HBox content) {
		for(Block block : BlockChain.blockList.getBlocks()) {
			setControllerObject(block);
			content.getChildren().add(fxmlLoader.getBlockLoader().getRoot());
		}
	}
}
