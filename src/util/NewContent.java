package util;

import java.util.ArrayList;

import controller.Controller;
import factory.FxmlFactory;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import model.Block;
import model.Peer;

public class NewContent implements PageMaker {
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
	
	public void makePage(String url) {
		content.getChildren().clear();
		setFxmlFactory(url);

	}
	public void makePage(String url, Object object) {
		setFxmlFactory(url);
		setParent();
		setController();
		addIntoContent();
	}
	
	private void setFxmlFactory(String url) {
		fxmlFactory.setUrl(url);
		fxmlFactory.generateFxmlObjets();
	}
	
	private void setParent() {
		this.parent = fxmlFactory.getParent();
	}
	
	private void addIntoContent() {
		content.getChildren().clear();
		content.getChildren().add(parent);
	}
	
	private void setController() {
		controller = fxmlFactory.getController();
		controller.setPeer(peer);
		controller.setObject(object);
		controller.execute();
	}
		
	private void addBlockProcess(HBox content) {
				ArrayList<Block> blocks = peer.getBlockchain().getBlocks();
				for(int i=0; i<blocks.size();i++) {
					this.object = blocks.get(i);
					doAddProcess(content);
				}
	}
}
