package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import model.NewPage;

// 해야 될 일 : P2P 연결시 첫 로그인은 가능하나 두 번째 로그인이 안 됨
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		NewPage newPage = new NewPage(primaryStage);
		newPage.moveToLoginPage();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
