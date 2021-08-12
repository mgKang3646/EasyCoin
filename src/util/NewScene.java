package util;


import controller.Controller;
import factory.FxmlFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewScene implements PageMaker {
	private Stage stage;
	private Scene scene;
	private Controller controller;
	private Peer peer;
	private Object object;
	private FxmlFactory fxmlFactory;
	
	public NewScene(Stage stage) {
		this.stage = stage;
		fxmlFactory = new FxmlFactory();
	}
	
	public NewScene(Stage stage, Peer peer) {
		this.stage = stage;
		this.peer = peer;
		fxmlFactory = new FxmlFactory();
	}
	
	public void makePage(String url) {
		doProcess(url);
	}
	
	public void makePage(String url, Object obj) {
		setObject(obj);
		doProcess(url);
	}
	
	private void doProcess(String url) {
		setFxmlFactory(url);
		setScene();
		setController();
		changeScene();
	}

	private void setFxmlFactory(String url) {
		fxmlFactory.setUrl(url);
		fxmlFactory.generateFxmlObjets();
	}
	
	private void setObject(Object obj) {
		this.object = obj;
	}
	
	private void setScene() {
		this.scene = fxmlFactory.getScene();
	}

	private void setController() {
		controller = fxmlFactory.getController();
		controller.setPeer(peer);
		controller.setObject(object);
		controller.execute();
	}
	
	private void changeScene() {
		stage.setScene(scene);
	}
}


