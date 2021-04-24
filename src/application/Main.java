package application;
// 놉 조장 메세지!!!!
// 조원 메세지!!!
// 변경사항 발쌩~!

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//트랜잭션
//화이팅
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("COIN");
			primaryStage.setResizable(false);
			//종료 버튼 클릭시, 프로그램 종료
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
