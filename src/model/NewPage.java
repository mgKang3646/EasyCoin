package model;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewPage {
	
	String url;
	Stage stage;
	FXMLLoader loader;
	Scene scene;
	Controller controller;

	public NewPage(String url, Stage stageValue) throws IOException {
		this.url = url;
		this.stage = stageValue;
		createLoader();
		createScene();
		setController();
	}
	// 관심사 : FXMLLoader 객체 생성하기
	public void createLoader() {
		this.loader = new FXMLLoader(getClass().getResource(url));	
	}
	// 관심사 : Scene 객체 생성하기
	public void createScene() {
		try {
			Parent root = this.loader.load();
			this.scene = new Scene(root);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	// 관심사 : 컨트롤러 설정하기
	public void setController() {
		this.controller = this.loader.getController();
	}
	
	// 관심사 : 기존 창에 새로운 페이지 열기
	public void createPageOnCurrentStage() {
			stage.setScene(this.scene);
			this.controller.setStage(stage);

	}
	// 관심사 : 새로운 창에 페이지 열기
	public void createPageOnNewStage() {
			Stage stage = new Stage();	
			stage.setScene(this.scene);
			stage.setX(this.stage.getX()+100);
			stage.setY(this.stage.getY()+50);
			stage.show();	
			
			this.controller.setStage(stage);
	}
	
	
	// 관심사 : Controller 객체 얻기 ( 추가적인 세팅이 필요한 경우 )
	public Controller getController() {
		return this.controller;
	}
}
