package util;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewPage {
	
	public void createPageOnCurrentStage(String url, Stage stageValue) {
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
	
	public void createPageOnNewStage(String url, Stage stageValue) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();	
			stage.setScene(scene);
			
			stage.setX(stageValue.getX()+100);
			stage.setY(stageValue.getY()+50);
			stage.show();
			
			Controller controller = loader.getController();
			controller.setStage(stage);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
