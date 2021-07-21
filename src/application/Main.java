package application;

import java.security.Security;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.CreateNewPage;
//block 카운터를 blockchain 사이즈로 바꾸어보기
//송금을 하는 자신도 Transaction을 가져야 함
//TransactionInput도 value를 가져야 한다. input을 모두 합친 금액에서 output의 value를 뺀 잔액을 새로인 UTXO로 만들어주어야 되기 떄문에... (peerModel의 processTX()참고)
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		CreateNewPage createNewPage = new CreateNewPage();
		createNewPage.createNewPage("/view/login.fxml",primaryStage);
		primaryStage.setTitle("EasyCoin");
		primaryStage.setResizable(false);
		
		//종료 버튼 클릭시, 프로그램 종료
		primaryStage.setOnCloseRequest(e->{
				System.exit(0);
		});
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
