package factory;

import javafx.scene.shape.Circle;
import util.CircleRotate;

public class UtilFactory {
	
	public CircleRotate getCircleRotate(Circle circle, boolean reverse, int angle, int duration) {
		return new CircleRotate(circle,reverse,angle,duration);
	}

}
