package application;

import java.security.Security;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//Peer 중첩클래스 끝낸 후 리더 선출 작업 손보기
//block 넘버도 손 보기
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
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
