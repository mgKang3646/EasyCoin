package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import model.NewPage;

// �ؾ� �� �� : P2P ����� ù �α����� �����ϳ� �� ��° �α����� �� ��
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		NewPage newPage = new NewPage(primaryStage);
		newPage.moveToLoginPage();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		launch(args);
	}
}
