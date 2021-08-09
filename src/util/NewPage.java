package util;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewPage {
	
	private FXMLLoader loader;
	private Stage stage;
	private Scene scene;
	private Controller controller;
	private Peer peer;
	private Object object;
	private String url;
	
	public NewPage(Stage stage) { 
			this.stage = stage;
	}
	
	public NewPage(Stage stage, Peer peer) {
			this.stage = stage;
			this.peer = peer;
	}
	
	// ���ɻ� : �������� ��ȯ
	public Stage getStage() {
			return this.stage;
	}
	// ���ɻ� : peer ����
	public void setPeer(Peer peer) {
			this.peer = peer;
	}
	
	// ���ɻ� : �α��� �������� ��ȯ�ϱ�
	public void moveToLoginPage() {
			url ="/view/login.fxml";
			doDefaultProcess();
			createPageOnCurrentStage();
	}
	
	// ���ɻ� : ȸ������ �������� �̵�
	public void moveToJoinPage() {
			url = "/view/join.fxml";
			doDefaultProcess();
			createPageOnCurrentStage();
	}
	
	// ���ɻ� : ������������ �̵�
	public void moveToMyPage(){
			url ="/view/mypage.fxml";
			doDefaultProcess();
			createPageOnCurrentStage();
	}
	
	// ���ɻ� : �ε����������� �̵�
	public void moveToIndexPage(String childPage){
			url ="/view/index.fxml";
			object = childPage;
			doDefaultProcess();
			createPageOnCurrentStage();
	}
		
	// ���ɻ� : AccessingPage�� �̵��ϱ�
	public void createAccessingPage() {
			url = "/view/accessing.fxml";
			doDefaultProcess();
			createPageOnNewStage();
	}
	
	// ���ɻ� : �˾�â ����
	public void createPopupPage(String msg){
			url = "/view/popup.fxml";
			object = msg;
			doDefaultProcess();
			createPageOnNewStage();
	}
	
	// ���ɻ� : �������� â ����
	public void show() {
			stage.show();
	}
	
	// ���ɻ� : ������ ����� ����
	private void doDefaultProcess(){
			try {
				initializeLoader(url);
				initializeScene();
				setController();
				setStageSettings();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	// ���ɻ� : FXMLLoader ��ü �����ϱ�
	private void initializeLoader(String url){
			loader = new FXMLLoader(getClass().getResource(url));
	}
	// ���ɻ� : Scene ��ü �����ϱ�
	private void initializeScene() throws IOException {
			Parent root = loader.load();
			scene = new Scene(root);
	}
	// ���ɻ� : Controller �����ϱ�
	private void setController() throws IOException {
			controller = loader.getController();
			controller.setStage(stage);
			controller.setPeer(peer);
			controller.setObject(object);
			controller.mainThreadAction();
			controller.executeDefaultProcess();
	}
	
	// ���ɻ� : ���� â�� ���ο� ������ ����
	private void createPageOnCurrentStage() {
			stage.setScene(scene);
	}
	// ���ɻ� : ���ο� â�� ������ ����
	private void createPageOnNewStage() {
			Stage newStage = new Stage();	
			newStage.setScene(scene);
			newStage.setX(stage.getX()+100);
			newStage.setY(stage.getY()+50);
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
