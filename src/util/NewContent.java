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
	private ArrayList<Block> blocks;

	
	public NewContent(HBox content) {
		this.content = content;
		fxmlFactory = new FxmlFactory();
	}
	
	public NewContent(HBox content,Peer peer) {
		this.peer = peer;
		this.content = content;
		this.blocks = peer.getBlockchain().getBlocks();
		this.fxmlFactory = new FxmlFactory();
	}
	
	public void makePage(String url) {
		makePageProcess(url);
	}
	public void makePage(String url, Object object) {
		setObject(object);
		makePageProcess(url);
	}
	
	private void makePageProcess(String url) {
		if(isBlockChainUrl(url)) {
			makeBlockChainPage("/view/block.fxml");
		}else {
			makeGeneralPage(url);
		}
	}
	
	private void makeGeneralPage(String url) {
		clearContent();
		setFxmlObjects(url);
		addIntoContent();
	}
	
	private void makeBlockChainPage(String url) {
		clearContent();
		addBlockChainIntoContent(url);
	}
	
	private void setFxmlObjects(String url) {
		setFxmlFactory(url);
		setParent();
		setController();
	}
	
	private void addBlockChainIntoContent(String url) {
		for(Block block : blocks) {
			setObject(block);
			setFxmlObjects(url);
			addIntoContent();		
		}
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
	
	private void setController() {
		controller = fxmlFactory.getController();
		controller.setPeer(peer);
		controller.setObject(object);
		controller.execute();
	}
	
	public void show() {}

}
