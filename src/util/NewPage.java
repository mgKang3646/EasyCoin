package util;

import java.io.IOException;
import java.util.ArrayList;

import controller.BlockController;
import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Block;
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
	// ���ɻ� : �ε��� �������� ä�������� �����ϱ�
	public void addMiningPage(HBox content) {
			url = "/view/mining.fxml";
			content.getChildren().clear();
			doAddProcess(content);
	}
	
	// ���ɻ� : �ε��� �������� ���ü�� ������ �����ϱ�
	public void addBlockChainPage(HBox content) {
			url = "/view/block.fxml";
			content.getChildren().clear();
			addBlockProcess(content);
	}
	
	// ���ɻ� : �������� â ����
	public void show() {
			stage.show();
	}
	
	// ���ɻ� : ������ ����� ����
	private void doDefaultProcess() {
				initializeLoader(url);
				initalizeParent();
				initializeScene();
				setController();
				setStageSettings();
	}
	
	//���ɻ� :  ������ ���� ����
	private void doAddProcess(HBox content) {
				initializeLoader(url);
				initalizeParent();
				setController();
				content.getChildren().add(parent);
	}
	
	//���ɻ� : ��� ���� ���� ����
	private void addBlockProcess(HBox content) {
				ArrayList<Block> blocks = peer.getBlockchain().getBlocks();
				for(int i=0; i<blocks.size();i++) {
					this.object = blocks.get(i);
					doAddProcess(content);
				}
	}
	
	
	// ���ɻ� : FXMLLoader ��ü �����ϱ�
	private void initializeLoader(String url){
			loader = new FXMLLoader(getClass().getResource(url));
	}
	
	// ���ɻ� : Parent ��ü �����ϱ�
	private void initalizeParent() {
			try {
				parent = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	// ���ɻ� : Scene ��ü �����ϱ�
	private void initializeScene(){
			scene = new Scene(parent);
	}
	// ���ɻ� : Controller �����ϱ�
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
