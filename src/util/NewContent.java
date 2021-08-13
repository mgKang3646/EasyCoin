package util;

import java.util.ArrayList;

import controller.Controller;
import factory.FxmlFactory;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import model.Block;
import model.Peer;

public class NewContent implements NewPage {
	private FxmlFactory fxmlFactory;
	private HBox content;
	private Peer peer;
	private Object object;
	private Parent parent;
	private Controller controller;
	
	public NewContent(HBox content) {
		this.content = content;
		fxmlFactory = new FxmlFactory();
	}
	
	public NewContent(HBox content,Peer peer) {
		this.peer = peer;
		this.content = content;
		fxmlFactory = new FxmlFactory();
	}
	
	public void makePage(String url) {
		if(isBlockChainUrl(url)) {
			makeBlockChainPage("/view/block.fxml");
		}else {
			makeGeneralPage(url);
		}
	}
	public void makePage(String url, Object object) {
		if(isBlockChainUrl(url)) {
			makeBlockChainPage("/view/block.fxml");
		}else {
			setObject(object);
			makeGeneralPage(url);
		}
	}
	
	public void show() {}
	
	private void makeGeneralPage(String url) {
		clearContent();
		setFxmlFactory(url);
		setParent();
		addIntoContent();
		setController();
	}
	
	private void makeBlockChainPage(String url) {
		clearContent();
		addBlockChainIntoContent(url);
		
	}
	
	private boolean isBlockChainUrl(String url) {
		if(url.equals("BlockChain")) {
			return true;
		}else {
			return false;
		}
	}
	
	private void setFxmlFactory(String url) {
		fxmlFactory.generateFxmlObjets(url);
	}
	
	private void setObject(Object object) {
		this.object = object;
	}
	
	private void setParent() {
		parent = fxmlFactory.getParent();
	}
	
	private void clearContent() {
		content.getChildren().clear();
	}
	private void addIntoContent() {
		content.getChildren().add(parent);
	}
	
	private void addBlockChainIntoContent(String url) {
		ArrayList<Block> blocks = peer.getBlockchain().getBlocks();
		for(Block block : blocks) {
			setObject(block);
			setFxmlFactory(url);
			setParent();
			setController();
			content.getChildren().add(parent);		
		}
	}
	
	private void setController() {
		controller = fxmlFactory.getController();
		controller.setPeer(peer);
		controller.setObject(object);
		controller.execute();
	}
}
