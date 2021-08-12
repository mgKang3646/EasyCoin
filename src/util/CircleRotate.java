package util;

import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CircleRotate {
	private Circle circle;
	private RotateTransition rt;
	
	public CircleRotate(Circle circle, boolean reverse, int angle, int duration) {
		this.circle = circle;
		setRotate(reverse,angle,duration);
	}
	
	public void setCircleImage(String url) {
		circle.setFill(new ImagePattern(new Image(url)));
	}
	public void start() {
		rt.play();
	}
	
	public void stop() {
		rt.stop();
	}
	
	private void setRotate(boolean reverse, int angle, int duration) {
		rt = new RotateTransition(Duration.seconds(duration),circle);
		rt.setAutoReverse(reverse);
		rt.setByAngle(angle);
		rt.setRate(3);
		rt.setCycleCount(18);
	}
	
	
}
