package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import util.NewPage;
//해야될 일 : NewPage 리팩토링 MiningPage 리팩토링 + mainButtonAction 없애고 setButtonAction 관심사 구분하기
// 블록체인 화면 NewContent 구현할 방법 찾기

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
