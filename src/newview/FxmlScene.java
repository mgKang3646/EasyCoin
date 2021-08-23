package newview;

import javafx.scene.Scene;

public class FxmlScene {
	public Scene getFXMLScene(String url) {
		return new Scene(FxmlLoader.getFXMLLoader(url).getRoot());
	}
}
