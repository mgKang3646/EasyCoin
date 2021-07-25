package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import model.NewPage;

// 해야 될 일 : 
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		NewPage createNewPage = new NewPage("/view/login.fxml",primaryStage);
		createNewPage.createPageOnCurrentStage();
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
