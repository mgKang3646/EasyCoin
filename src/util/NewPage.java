package util;

import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Peer;

public class NewPage {
	
	private Stage stage;
	private FXMLLoader loader;
	private Parent parent;
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
	
	// 관심사 : 스테이지 반환
	public Stage getStage() {
			return this.stage;
	}
	// 관심사 : peer 세팅
	public void setPeer(Peer peer) {
			this.peer = peer;
	}
	
	// 관심사 : 로그인 페이지로 전환하기
	public void moveToLoginPage() {
			url ="/view/login.fxml";
			doDefaultProcess();
			createPageOnCurrentStage();
	}
	
	// 관심사 : 회원가입 페이지로 이동
	public void moveToJoinPage() {
			url = "/view/join.fxml";
			doDefaultProcess();
			createPageOnCurrentStage();
	}
	
	// 관심사 : 마이페이지로 이동
	public void moveToMyPage(){
			url ="/view/mypage.fxml";
			doDefaultProcess();
			createPageOnCurrentStage();
	}
	
	// 관심사 : 인덱스페이지로 이동
	public void moveToIndexPage(String childPage){
			url ="/view/index.fxml";
			object = childPage;
			doDefaultProcess();
			createPageOnCurrentStage();
	}
		
	// 관심사 : AccessingPage로 이동하기
	public void createAccessingPage() {
			url = "/view/accessing.fxml";
			doDefaultProcess();
			createPageOnNewStage();
	}
	
	// 관심사 : 팝업창 띄우기
	public void createPopupPage(String msg){
			url = "/view/popup.fxml";
			object = msg;
			doDefaultProcess();
			createPageOnNewStage();
	}
	
	public void addMiningPage(HBox content) {
			url = "/view/mining.fxml";
			doAddProcess(content);
	}
	
	// 관심사 : 스테이지 창 열기
	public void show() {
			stage.show();
	}
	
	// 관심사 : 페이지 만들기 절차
	private void doDefaultProcess() {
				initializeLoader(url);
				initalizeParent();
				initializeScene();
				setController();
				setStageSettings();
	}
	
	//관심사 :  페이지 삽입 절차
	private void doAddProcess(HBox content) {
				content.getChildren().clear();
				initializeLoader(url);
				initalizeParent();
				setController();
				content.getChildren().add(parent);
	}
	
	// 관심사 : FXMLLoader 객체 생성하기
	private void initializeLoader(String url){
			loader = new FXMLLoader(getClass().getResource(url));
	}
	
	// 관심사 : Parent 객체 생성하기
	private void initalizeParent() {
			try {
				parent = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	// 관심사 : Scene 객체 생성하기
	private void initializeScene(){
			scene = new Scene(parent);
	}
	// 관심사 : Controller 설정하기
	private void setController() {
			try {
				controller = loader.getController();
				controller.setStage(stage);
				controller.setPeer(peer);
				controller.setObject(object);
				controller.mainThreadAction();
				controller.executeDefaultProcess();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	// 관심사 : 기존 창에 새로운 페이지 열기
	private void createPageOnCurrentStage() {
			stage.setScene(scene);
	}
	// 관심사 : 새로운 창에 페이지 열기
	private void createPageOnNewStage() {
			Stage newStage = new Stage();	
			newStage.setScene(scene);
			newStage.setX(stage.getX()+100);
			newStage.setY(stage.getY()+50);
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
