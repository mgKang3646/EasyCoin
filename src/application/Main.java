package application;

import java.security.Security;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//Peer ��øŬ���� ���� �� ���� ���� �۾� �պ���
//block �ѹ��� �� ����
//C:\Users\USER\ProjectForGraduation\BlockChain_v2.9

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("COIN");
			primaryStage.setResizable(false);
			//���� ��ư Ŭ����, ���α׷� ����
			primaryStage.setOnCloseRequest(e->{
				System.exit(0);
			});
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		launch(args);
	}
}
