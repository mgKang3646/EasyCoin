package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import util.NewPage;
// 해야될일 서버스레드와 peer 스레드의 같게 만들것이냐 확실히 분리할것이냐
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
