package newpage;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewStage implements NewPage{
	private Stage stage;
	private Stage parentStage;
	private Scene scene;
	private Peer peer;
	private Object object;
	private FxmlLoader fxmlObjects;
	
	public NewStage(Stage parentStage) {
		stage = new Stage();
		this.parentStage = parentStage;
		fxmlObjects = new FxmlLoader();
	}
	
	public NewStage(Stage parentStage, Peer peer) {
		stage = new Stage();
		this.parentStage = parentStage;
		this.peer = peer;
		fxmlObjects = new FxmlLoader();
	}
	
	public void makePage(String url) {
		doProcess(url);
	}
	
	public void makePage(String url, Object obj) {
		setObject(obj);
		doProcess(url);
	}
	
	public void show() {
		stage.show();
	}
	
	private void doProcess(String url) {
		setFxmlFactory(url);
		setScene();
		setController();
		setNewStage();
	}
	
	private void setFxmlFactory(String url) {
		fxmlObjects.generateFxmlObjets(url);
	}
	
	private void setObject(Object obj) {
		this.object = obj;
	}
	
	private void setScene() {
		this.scene = fxmlObjects.getScene();
	}

	private void setController() {
		fxmlObjects.setController(parentStage, peer, object);
	}
	private void setNewStage() {
		stage.setScene(scene);			
		stage.setX(getWidth());
		stage.setY(getHeight());
		stage.setTitle("EasyCoin");
		stage.setResizable(false);
	}
	
	private double getWidth() {
		return parentStage.getX()+parentStage.getWidth()/2-fxmlObjects.getParent().prefWidth(0)/2;
	}
	
	private double getHeight() {
		return parentStage.getY()+35;
	}
	
	
}
