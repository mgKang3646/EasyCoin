package factory;

import javafx.scene.shape.Circle;
import model.Peer;
import util.CircleRotate;
import util.SocketUtil;

public class UtilFactory {
	
	
	public SocketUtil getSocketUtil() {
		return new SocketUtil();
	}
	
	public CircleRotate getCircleRotate(Circle circle, boolean reverse, int angle, int duration) {
		return new CircleRotate(circle,reverse,angle,duration);
	}

}
