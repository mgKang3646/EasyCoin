package newview;

import controller.BlockController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import model.Block;
import model.BlockChain;

public class NewView{

	private FxmlLoader fxmlLoader;
	private FxmlStage fxmlStage;
	private FxmlContent fxmlContent;
	private FxmlController fxmlController;
	
	public NewView(){
		fxmlLoader = new FxmlLoader();
		fxmlStage = new FxmlStage();
		fxmlContent = new FxmlContent();
		fxmlController = new FxmlController();
	}
	
	public void openView(String url) {
		fxmlController.setController(url);
		FxmlStage.getPrimaryStage().setScene(FxmlScene.getFXMLScene(url));
		FxmlStage.getPrimaryStage().show();
	}
	
	public void openView(String url, Object object) {
		fxmlController.setController(url, object);
		FxmlStage.getPrimaryStage().setScene(FxmlScene.getFXMLScene(url));
		FxmlStage.getPrimaryStage().show();
	}
	
	public void getNewScene(String url) {
		fxmlController.setController(url);
		FxmlStage.getPrimaryStage().setScene(FxmlScene.getFXMLScene(url));
	}
	
	public void getNewScene(String url, Object object) {
		fxmlController.setController(url,object);
		FxmlStage.getPrimaryStage().setScene(FxmlScene.getFXMLScene(url));
	}
	
	public void getNewWindow(String url) {
		fxmlController.setController(url);
		fxmlStage.showNewStage(url);
	}
	
	public void getNewWindow(String url,Object object) {
		fxmlController.setController(url,object);
		fxmlStage.showNewStage(url);
	}
	
	public void addBlockChainContent(HBox content) {
		addBlockChain(content);
	}
	
	public void addNewContent(HBox content, String url) {
		fxmlController.setController(url);
		fxmlContent.addContent(content, url);
	}
	
	public void addNewContent(HBox content, String url, Object object) {
		fxmlController.setController(url, object);
		fxmlContent.addContent(content, url);
	}
	
	private void addBlockChain(HBox content) {
		content.getChildren().clear();
		for(Block block : BlockChain.blockList.getBlocks()) {
			FXMLLoader loader = fxmlLoader.getBlockLoader();
			BlockController bc = loader.getController();
			bc.throwObject(block);
			bc.execute();
			content.getChildren().add(loader.getRoot());
		}
	}
}
