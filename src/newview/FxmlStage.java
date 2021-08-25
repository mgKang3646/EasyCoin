package newview;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxmlStage {
	
	private static Stage primaryStage;
	private Stage newStage;
	private FxmlScene fxmlScene;
	private String url;
	
	public FxmlStage() {
		fxmlScene = new FxmlScene();
	}
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void setPrimaryStage(Stage primaryStage) {
		settingPrimaryStage(primaryStage);
	}
	
	private static void settingPrimaryStage(Stage stage) {
		primaryStage = stage;
		primaryStage.setTitle("EasyCoin");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e->{ 
			System.exit(0);
		});
	}
	
	public void showNewStage(String url) {
		this.url = url;
		settingNewStage();
		newStage.show();
	}
	
	private void settingNewStage() {
		newStage = new Stage();
		newStage.setX(getWidth());
		newStage.setY(getHeight());
		newStage.setTitle("EasyCoin");
		newStage.setResizable(false);
		newStage.setScene(FxmlScene.getFXMLScene(url));
	}
	
	private double getWidth() {
		Parent parent = FxmlLoader.getFXMLLoader(url).getRoot();
		return primaryStage.getX()+primaryStage.getWidth()/2-parent.prefWidth(0)/2;
	}
	
	private double getHeight() {
		return primaryStage.getY()+35;
	}
	
	
}
