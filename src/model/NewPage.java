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
	// ���ɻ� : FXMLLoader ��ü �����ϱ�
	public void createLoader() {
		this.loader = new FXMLLoader(getClass().getResource(url));	
	}
	// ���ɻ� : Scene ��ü �����ϱ�
	public void createScene() {
		try {
			Parent root = this.loader.load();
			this.scene = new Scene(root);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	// ���ɻ� : ��Ʈ�ѷ� �����ϱ�
	public void setController() {
		this.controller = this.loader.getController();
	}
	
	// ���ɻ� : ���� â�� ���ο� ������ ����
	public void createPageOnCurrentStage() {
			stage.setScene(this.scene);
			this.controller.setStage(stage);

	}
	// ���ɻ� : ���ο� â�� ������ ����
	public void createPageOnNewStage() {
			Stage stage = new Stage();	
			stage.setScene(this.scene);
			stage.setX(this.stage.getX()+100);
			stage.setY(this.stage.getY()+50);
			stage.show();	
			
			this.controller.setStage(stage);
	}
	
	
	// ���ɻ� : Controller ��ü ��� ( �߰����� ������ �ʿ��� ��� )
	public Controller getController() {
		return this.controller;
	}
}
