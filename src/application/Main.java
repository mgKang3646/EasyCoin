package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import model.NewPage;

// �ؾ� �� �� : 
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		NewPage createNewPage = new NewPage("/view/login.fxml",primaryStage);
		createNewPage.createPageOnCurrentStage();
		primaryStage.setTitle("EasyCoin");
		primaryStage.setResizable(false);
		
		//���� ��ư Ŭ����, ���α׷� ����
		primaryStage.setOnCloseRequest(e->{
				System.exit(0);
		});
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		launch(args);
	}
}
