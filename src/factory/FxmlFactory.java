package factory;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class FxmlFactory {
	
	private FXMLLoader loader;
	private Scene scene;
	private Parent parent;
	private Controller controller;
	
	public void generateFxmlObjets(String url) {
		try {
			loader = new FXMLLoader(getClass().getResource(url));
			parent = loader.load();
			scene = new Scene(parent);
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FXMLLoader getLoader() {
		return loader;
	}

	public Scene getScene() {
		return scene;
	}

	public Parent getParent() {
		return parent;
	}
	
	public Controller getController() {
		return controller;
	}

	
}
