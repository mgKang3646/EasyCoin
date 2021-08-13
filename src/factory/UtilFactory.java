package factory;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Peer;
import util.CircleRotate;
import util.JsonReceive;
import util.JsonSend;
import util.NewContent;
import util.NewScene;
import util.NewStage;
import util.SocketUtil;

public class UtilFactory {
	
	public JsonSend getJsonSend() {
		return new JsonSend();
	}
	
	public JsonReceive getJsonReceive(Peer peer) {
		return new JsonReceive(peer);
	}
	
	public NewStage getNewStage(Stage stage) {
		return new NewStage(stage);
	}
	
	public NewStage getNewStage(Stage stage, Peer peer) {
		return new NewStage(stage, peer);
	}

	public NewScene getNewScene(Stage stage) {
		return new NewScene(stage);
	}
	
	public NewScene getNewScene(Stage stage, Peer peer) {
		return new NewScene(stage, peer);
	}
	
	public NewContent getNewContent(HBox content) {
		return new NewContent(content);
	}
	
	public NewContent getNewContent(HBox content, Peer peer) {
		return new NewContent(content, peer);
	}

	public SocketUtil getSocketUtil() {
		return new SocketUtil();
	}
	
	public CircleRotate getCircleRotate(Circle circle, boolean reverse, int angle, int duration) {
		return new CircleRotate(circle,reverse,angle,duration);
	}

}
