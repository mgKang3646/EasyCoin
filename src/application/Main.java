package application;
// �� ���� �޼���!!!!
// ���� �޼���!!!
// ������� �߽�~!

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//Ʈ�����
//ȭ����
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
		launch(args);
	}
}
