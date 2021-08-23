package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import newview.FxmlStage;
import newview.NewView;
import newview.ViewURL;
//�������� Controller ���� �̸� ������� ����
// ������ �ٸ� Peer Button �ұ�� ( �̱��� ??? )
//+ Upgrade ��� DB ������� + ä�� �� �ٽ� ä���� �� ��ư�� �� �� Ŭ���ؾ� ��
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FxmlStage.setPrimaryStage(primaryStage);
		NewView newView = new NewView();
		newView.openView(ViewURL.loginURL);
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		launch(args);
	}
}
