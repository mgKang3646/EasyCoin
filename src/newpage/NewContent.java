package newpage;

import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Block;
import model.Peer;

public class NewContent implements NewPage {
	private Stage stage;
	private FxmlObjects fxmlObjects;
	private HBox content;
	private Peer peer;
	private Object object;
	private Parent parent;
	private ArrayList<Block> blocks;

	
	public NewContent(Stage stage, HBox content) {
		this.stage = stage;
		this.content = content;
		fxmlObjects = new FxmlObjects();
	}
	
	public NewContent(Stage stage, HBox content,Peer peer) {
		this.stage = stage;
		this.peer = peer;
		this.content = content;
		this.blocks = peer.getBlockchain().getBlocks();
		this.fxmlObjects = new FxmlObjects();
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
		}
		else if(url.equals("/view/mining.fxml")) {
			makeMiningPage(url);
		}
		else {
			makeOtherPage(url);
		}
	}
	
	private void makeOtherPage(String url) {
		clearContent();
		setFxmlObjects(url);
		addIntoContent();
	}
	
	private void makeBlockChainPage(String url) {
		clearContent();
		addBlockChainIntoContent(url);
	}
	
	private void makeMiningPage(String url) {
		clearContent();
		setMiningFxmlObjects();
		addIntoContent();
	}
	
	private void setFxmlObjects(String url) {
		setFxmlFactory(url);
		setParent();
		setController();
	}
	
	private void setMiningFxmlObjects() {
		fxmlObjects = (FxmlObjects)object;
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
	
	private void clearContent() {
		content.getChildren().clear();
	}
	private void addIntoContent() {
		content.getChildren().add(parent);
	}
	
	private void setFxmlFactory(String url) {
		fxmlObjects.generateFxmlObjets(url);
	}
	
	private void setController() {
		fxmlObjects.setController(stage, peer, object);
	}
	
	private void setParent() {
		parent = fxmlObjects.getParent();
	}
	
	private void setObject(Object object) {
		this.object = object;
	}
	
	public void show() {}

}
