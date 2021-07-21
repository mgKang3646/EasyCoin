package util;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateNewPage {
	
	public void createNewPage(String url, Stage stageValue) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = stageValue;	
			stage.setScene(scene);
			
			Controller controller = loader.getController();
			controller.setStage(stage);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
