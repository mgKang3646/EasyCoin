package newview;

import javafx.scene.layout.HBox;

public class FxmlContent {
		
	public void addContent(HBox content, String url) {
		content.getChildren().clear();
		content.getChildren().add(FxmlLoader.getFXMLLoader(url).getRoot());
	}	
}
