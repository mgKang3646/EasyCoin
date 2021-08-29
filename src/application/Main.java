package application;

import java.io.IOException;
import java.security.Security;

import javafx.application.Application;
import javafx.stage.Stage;
import newview.FxmlLoader;
import newview.FxmlScene;
import newview.FxmlStage;
import newview.NewView;
import newview.ViewURL;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FxmlLoader fxmlLoader = new FxmlLoader();
		FxmlScene fxmlScene = new FxmlScene();
		fxmlLoader.generateFXMLLoader();
		fxmlScene.generateScene();
		
		FxmlStage.setPrimaryStage(primaryStage);
		NewView newView = new NewView();
		newView.openView(ViewURL.loginURL);
	}
	
	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		launch(args);
	}
}
