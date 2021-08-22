package newpage;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Peer;

public class NewScene implements NewPage {
	private Stage stage;
	private Scene scene;
	private Peer peer;
	private Object object;
	private FxmlLoader fxmlObjects;
	
	public NewScene(Stage stage) {
		this.stage = stage;
		this.fxmlObjects = new FxmlLoader();
	}
	
	public NewScene(Stage stage, Peer peer) {
		this.stage = stage;
		this.peer = peer;
		this.fxmlObjects = new FxmlLoader();
	}
	
	public void makePage(String url) {
		doProcess(url);
	}
	
	public void makePage(String url, Object obj) {
		setObject(obj);
		doProcess(url);
	}
	
	public void show() {
		stage.show();
	}
	
	private void doProcess(String url) {
		setStage();
		setFxmlFactory(url);
		setScene();
		setController();
		changeScene();
	}

	private void setFxmlFactory(String url) {
		fxmlObjects.generateFxmlObjets(url);
	}
	
	private void setObject(Object obj) {
		this.object = obj;
	}
	
	private void setScene() {
		this.scene = fxmlObjects.getScene();
	}

	private void setController() {
		fxmlObjects.setController(stage, peer, object);
	}
	
	private void changeScene() {
		stage.setScene(scene);
	}
	
	private void setStage() {
		stage.setTitle("EasyCoin");
		stage.setResizable(false);
		stage.setOnCloseRequest(e->{ 
			System.exit(0);
		});
	}
}


