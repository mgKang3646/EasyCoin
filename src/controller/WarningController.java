package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PeerModel;

public class WarningController {

	@FXML TextField path;
	@FXML Button goMyPage;
	Stage primaryStage;
	PeerModel peerModel;
	
	public void goMyPage() throws IOException {
		
		Stage stage = (Stage)goMyPage.getScene().getWindow();
		stage.close();
		
		FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/mypage.fxml"));
		Parent root = loader.load();
		MyPageController mypageController = loader.getController(); // fxml�� �ε�Ǵ� ���ÿ� ����� ��Ʈ�ѷ� ��ü�� �ڵ�����.
		mypageController.setPeerModel(peerModel);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
	
	}
	public void setPeerModel(PeerModel peerModel) {
		this.peerModel = peerModel;
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void setPath(String path) {
		this.path.setText(path);
	}

}
