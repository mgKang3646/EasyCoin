package model;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewPage {
	
	FXMLLoader loader;
	Scene scene;
	Stage stage;
	Controller controller;

	public NewPage(String url, Stage stageValue) throws IOException {
		this.loader = new FXMLLoader(getClass().getResource(url));
		Parent parent = loader.load();
		this.scene = new Scene(parent);
		this.stage = stageValue;
		this.controller = setController(this.loader);
	}
	
	// 관심사 : 기존 창에 새로운 페이지 열기
	public void createPageOnCurrentStage() {	
			stage.setScene(this.scene);
	}
	// 관심사 : 새로운 창에 페이지 열기
	public void createPageOnNewStage() {
			Stage stage = new Stage();	
			stage.setScene(this.scene);
			stage.setX(this.stage.getX()+100);
			stage.setY(this.stage.getY()+50);
			stage.show();	
	}
	
	public Controller setController(FXMLLoader loader) {
		Controller controller = loader.getController();
		controller.setStage(this.stage);
		return controller;
	}
	
	public Controller getController() {
		return this.controller;
	}
}
