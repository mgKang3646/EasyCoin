package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import util.NewPage;
//인덱스 페이지 miningHandler 구현

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		NewPage newPage = new NewPage(primaryStage);
		newPage.moveToLoginPage();
		newPage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
