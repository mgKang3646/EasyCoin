package application;

import java.io.IOException;
import java.security.Security;

import factory.NewPageFactory;
import javafx.application.Application;
import javafx.stage.Stage;

// ������ �ٸ� Peer Button �ұ�� + Upgrade ��� DB ������� + ä�� �� �ٽ� ä���� �� ��ư�� �� �� Ŭ���ؾ� ��
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		NewPageFactory newPageFactory = new NewPageFactory();
		newPageFactory.setStage(primaryStage);
		newPageFactory.createStartPage();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		launch(args);
	}
}
