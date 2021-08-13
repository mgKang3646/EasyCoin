package util;

import controller.Controller;
import factory.FxmlFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewStage implements NewPage{
	private Stage stage;
	private Stage parentStage;
	private Scene scene;
	private Controller controller;
	private Peer peer;
	private Object object;
	private FxmlFactory fxmlFactory;
	
	public NewStage(Stage parentStage) {
		stage = new Stage();
		this.parentStage = parentStage;
		fxmlFactory = new FxmlFactory();
	}
	
	public NewStage(Stage parentStage, Peer peer) {
		stage = new Stage();
		this.parentStage = parentStage;
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
		fxmlFactory.generateFxmlObjets(url);
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

	private void setNewStage() {
		stage.setScene(scene);			
		stage.setX(getWidth());
		stage.setY(getHeight());
		stage.setTitle("EasyCoin");
		stage.setResizable(false);
	}
	
	private double getWidth() {
		return parentStage.getX()+parentStage.getWidth()/2-fxmlFactory.getParent().prefWidth(0)/2;
	}
	
	private double getHeight() {
		return parentStage.getY()+35;
	}
	
	
}
