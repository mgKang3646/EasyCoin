package application;

import java.io.IOException;
import java.security.Security;

import factory.UtilFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import util.NewPage;
//해야될 일 : MiningPage 리팩토링 + mainButtonAction 없애고 setButtonAction 관심사 구분하기

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		UtilFactory utilFactory = new UtilFactory();
		NewPage newPage = utilFactory.getNewScene(primaryStage);
		newPage.makePage("/view/login.fxml");
		newPage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
