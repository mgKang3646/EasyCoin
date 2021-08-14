package application;

import java.io.IOException;
import java.security.Security;

import factory.UtilFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import util.NewPage;
//�ؾߵ� �� : MiningPage �����丵 + mainButtonAction ���ְ� setButtonAction ���ɻ� �����ϱ�

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		UtilFactory utilFactory = new UtilFactory();
		NewPage newPage = utilFactory.getNewScene(primaryStage);
		newPage.makePage("/view/login.fxml");
		newPage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		launch(args);
	}
}
