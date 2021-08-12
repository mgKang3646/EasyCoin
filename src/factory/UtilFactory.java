package factory;

import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Peer;
import util.CircleRotate;
import util.JsonReceive;
import util.JsonSend;
import util.NewPage;
import util.SocketUtil;

public class UtilFactory {
	
	public JsonSend getJsonSend() {
		return new JsonSend();
	}
	
	public JsonReceive getJsonReceive(Peer peer) {
		return new JsonReceive(peer);
	}
	
	public NewPage getNewPage(Stage stage) {
		return new NewPage(stage);
	}
	
	public NewPage getNewPage(Stage stage, Peer peer) {
		return new NewPage(stage,peer);
	}
	
	public SocketUtil getSocketUtil() {
		return new SocketUtil();
	}
	
	public CircleRotate getCircleRotate(Circle circle, boolean reverse, int angle, int duration) {
		return new CircleRotate(circle,reverse,angle,duration);
	}

}
