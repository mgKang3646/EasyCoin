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
	@FXML Button goToIndex;
	Stage primaryStage;
	PeerModel peerModel;
	
	public void goIndex() throws IOException {
		
		Stage stage = (Stage)goToIndex.getScene().getWindow();
		stage.close();
		
		FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/index.fxml"));
		Parent root = loader.load();
		IndexController indexController = loader.getController(); // fxml이 로드되는 동시에 연결된 컨트롤러 객체가 자동생성.
		indexController.setPeerModel(peerModel);
		
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
