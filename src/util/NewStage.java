package util;

import controller.Controller;
import factory.FxmlFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewStage implements PageMaker{
	private Stage stage;
	private Scene scene;
	private Controller controller;
	private Peer peer;
	private Object object;
	private FxmlFactory fxmlFactory;
	
	public NewStage() {
		stage = new Stage();
		fxmlFactory = new FxmlFactory();
	}
	
	public NewStage(Peer peer) {
		stage = new Stage();
		this.peer = peer;
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
		createNewStage();
	}

	private void createNewStage() {
		setNewStage();
		showStage();
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

	private void setNewStage() {
		stage.setScene(scene);			
		stage.setX(getWidth());
		stage.setY(getHeight());
		stage.setTitle("EasyCoin");
		stage.setResizable(false);
	}
	
	private void showStage() {
		stage.show();
	}
	
	private double getWidth() {
		return stage.getX()+stage.getWidth()/2-fxmlFactory.getParent().prefWidth(0)/2;
	}
	
	private double getHeight() {
		return stage.getY()+35;
	}
	
	public void setCloseProgram() {
		stage.setOnCloseRequest(e->{ 
			System.exit(0);
		});
	}
}
