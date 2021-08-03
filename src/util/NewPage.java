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
	
	// 관심사 : 로그인 페이지로 전환하기
	public void moveToLoginPage() throws IOException {
		String url ="/view/login.fxml";
		doDefaultSetting(url);
		setStageSettings();
		createPageOnCurrentStage();
	}
	
	// 관심사 : 회원가입 페이지로 이동
	public void moveToJoinPage() {
			String url = "/view/join.fxml";
			doDefaultSetting(url);
			createPageOnCurrentStage();
	}
		
	// 관심사 : AccessingPage로 이동하기
	public void createAccessingPage(Peer peer) {
			String url = "/view/accessing.fxml";
			doDefaultSetting(url);
			this.controller.setObject(peer);
			controller.mainThreadAction();
			createPageOnNewStage();
	}
	
	// 관심사 : 팝업창 띄우기
	public void createPopupPage(String msg) {
			String url = "/view/popup.fxml";
			doDefaultSetting(url);
			controller.setObject(msg);
			createPageOnNewStage();
	}
	
	// 관심사 : 스테이지 창 열기
	public void show() {
		stage.show();
	}
	
	// 관심사 : FXMLLoader 객체 생성하기
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
	
	// 관심사 : 기존 창에 새로운 페이지 열기
	private void createPageOnCurrentStage() {
			stage.setScene(this.scene);
	}
	// 관심사 : 새로운 창에 페이지 열기
	private void createPageOnNewStage() {
			Stage newStage = new Stage();	
			newStage.setScene(this.scene);
			newStage.setX(this.stage.getX()+100);
			newStage.setY(this.stage.getY()+50);
			newStage.show();	
	}
	
	// 관심사 : Stage 기본 세팅 설정
	private void setStageSettings() {
		stage.setTitle("EasyCoin");
		stage.setResizable(false);
		stage.setOnCloseRequest(e->{
				System.exit(0);
		});
	}
}
