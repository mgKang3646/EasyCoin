package application;

import java.io.IOException;
import java.security.Security;

import factory.NewPageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
//해야될 일 : Mining 각 Peer간 검증 단계 구현중
// 구현한 것 : blockChain 객체러 tmpBlock을 이용하여 동기화 구현 
// 구현해야할 것 : 검증결과 송신 후 검증결과 취합후 블록 채굴과정 구현

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
