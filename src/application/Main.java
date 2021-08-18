package application;

import java.io.IOException;
import java.security.Security;

import factory.NewPageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
//해야될 일 : Mining 클래스 리팩토링, blockVerify initialize 로직 구현

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		NewPageFactory newPageFactory = new NewPageFactory();
		newPageFactory.setStage(primaryStage);
		newPageFactory.createStartPage();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
