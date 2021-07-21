package application;

import java.security.Security;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.CreateNewPage;
//block ī���͸� blockchain ������� �ٲپ��
//�۱��� �ϴ� �ڽŵ� Transaction�� ������ ��
//TransactionInput�� value�� ������ �Ѵ�. input�� ��� ��ģ �ݾ׿��� output�� value�� �� �ܾ��� ������ UTXO�� ������־�� �Ǳ� ������... (peerModel�� processTX()����)
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		CreateNewPage createNewPage = new CreateNewPage();
		createNewPage.createNewPage("/view/login.fxml",primaryStage);
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
