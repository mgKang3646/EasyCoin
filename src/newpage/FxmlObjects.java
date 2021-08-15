package newpage;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class FxmlObjects {
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
	
	public void setController(Stage stage , Peer peer, Object object) {
		controller.setPeer(peer);
		controller.setObject(object);
		controller.setStage(stage);
		controller.execute();	
	}
}
