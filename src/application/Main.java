package application;

import java.security.Security;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.NewPage;

// 해야 될 일 : 회원가입 PEM 파일 생성 
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		NewPage createNewPage = new NewPage();
		createNewPage.createNewPage("/view/login.fxml",primaryStage);
		primaryStage.setTitle("EasyCoin");
		primaryStage.setResizable(false);
		
		//종료 버튼 클릭시, 프로그램 종료
		primaryStage.setOnCloseRequest(e->{
				System.exit(0);
		});
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
