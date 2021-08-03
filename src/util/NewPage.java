package util;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewPage {
	
	private Stage stage;
	private Scene scene;
	private Controller controller;
	
	public NewPage(Stage stage) { 
		this.stage = stage;
	}
	
	// ���ɻ� : �α��� �������� ��ȯ�ϱ�
	public void moveToLoginPage() throws IOException {
		String url ="/view/login.fxml";
		doDefaultSetting(url);
		setStageSettings();
		createPageOnCurrentStage();
	}
	
	// ���ɻ� : ȸ������ �������� �̵�
	public void moveToJoinPage() {
			String url = "/view/join.fxml";
			doDefaultSetting(url);
			createPageOnCurrentStage();
	}
		
	// ���ɻ� : AccessingPage�� �̵��ϱ�
	public void createAccessingPage(Peer peer) {
			String url = "/view/accessing.fxml";
			doDefaultSetting(url);
			this.controller.setObject(peer);
			controller.mainThreadAction();
			createPageOnNewStage();
	}
	
	// ���ɻ� : �˾�â ����
	public void createPopupPage(String msg) {
			String url = "/view/popup.fxml";
			doDefaultSetting(url);
			controller.setObject(msg);
			createPageOnNewStage();
	}
	
	// ���ɻ� : �������� â ����
	public void show() {
		stage.show();
	}
	
	// ���ɻ� : FXMLLoader ��ü �����ϱ�
	private void doDefaultSetting(String url){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
			Parent root = loader.load();
			this.scene = new Scene(root);
			this.controller = loader.getController();
			this.controller.setStage(this.stage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ���ɻ� : ���� â�� ���ο� ������ ����
	private void createPageOnCurrentStage() {
			stage.setScene(this.scene);
	}
	// ���ɻ� : ���ο� â�� ������ ����
	private void createPageOnNewStage() {
			Stage newStage = new Stage();	
			newStage.setScene(this.scene);
			newStage.setX(this.stage.getX()+100);
			newStage.setY(this.stage.getY()+50);
			newStage.show();	
	}
	
	// ���ɻ� : Stage �⺻ ���� ����
	private void setStageSettings() {
		stage.setTitle("EasyCoin");
		stage.setResizable(false);
		stage.setOnCloseRequest(e->{
				System.exit(0);
		});
	}
}
