package factory;

import controller.MiningController;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.MiningState;
import model.Peer;
import newpage.FxmlLoader;
import newpage.NewContent;
import newpage.NewPage;
import newpage.NewScene;
import newpage.NewStage;


public class NewPageFactory {
	
	private NewPage newPage;
	private static Stage stage;
	private static MiningController miningController;
	
	public static Stage getStage() {
		return stage;
	}
	
	public static FxmlLoader getMiningFxmlObjects() {
		return miningFxmlObjects;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void createStartPage() {
		newPage = getNewScene();
		newPage.makePage("/view/login.fxml");
		newPage.show();
	}
	
	public void createAccessingPage(Peer peer) {
		newPage = getNewStage(peer);
		newPage.makePage("/view/accessing.fxml",peer);
		newPage.show();
	}
	
	public void createPopupPage(String msg) {
		newPage = getNewStage();
		newPage.makePage("/view/popup.fxml",msg);
		newPage.show();
	}
	
	public void createMiningResult(Peer peer, MiningState miningResult) {
		newPage = getNewStage(peer);
		newPage.makePage("/view/miningResult.fxml",miningResult);
		newPage.show();
	}
	
	public void moveJoinPage() {
		newPage = getNewScene();
		newPage.makePage("/view/join.fxml");
	}
	
	public void moveLoginPage() {
		newPage = getNewScene();
		newPage.makePage("/view/login.fxml");
	}
	
	public void moveMyPage(Peer peer, FxmlLoader miningFxmlObjects) {
		this.miningFxmlObjects = miningFxmlObjects;
		newPage = getNewScene(peer);
		newPage.makePage("/view/mypage.fxml",miningFxmlObjects);
	}
	
	public void moveIndexPage(Peer peer,String childPage) {
		newPage = getNewScene(peer);
		newPage.makePage("/view/index.fxml",childPage);
	}
	
	public void moveIndexPageForMining(Peer peer,FxmlLoader miningFxmlObjects) {
		newPage = getNewScene(peer);
		newPage.makePage("/view/index.fxml",miningFxmlObjects);
	}
	
	public void addMiningPage(HBox content, Peer peer,FxmlLoader miningFxmlObjects) {
		newPage = getNewContent(content,peer);
		newPage.makePage("/view/mining.fxml", miningFxmlObjects);
	}
	
	public void addBlockChainPage(HBox content, Peer peer) {
		newPage = getNewContent(content,peer);
		newPage.makePage("BlockChain");
	}
	
	public void addMiningResultPage(HBox content, Peer peer) {
		newPage = getNewContent(content);
		newPage.makePage("/view/block.fxml",peer.getBlockchain().getLastBlock());
	}
	
	
	private NewStage getNewStage() {
		return new NewStage(stage);
	}
	
	private NewStage getNewStage( Peer peer ) {
		return new NewStage(stage, peer);
	}

	private NewScene getNewScene() {
		return new NewScene(stage);
	}
	
	private NewScene getNewScene( Peer peer ) {
		return new NewScene(stage, peer);
	}
	
	private NewContent getNewContent(HBox content) {
		return new NewContent(stage, content);
	}
	
	private NewContent getNewContent(HBox content, Peer peer) {
		return new NewContent(stage, content, peer);
	}

}
