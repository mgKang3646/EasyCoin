package newview;

import javafx.scene.layout.HBox;

public class FxmlContent {
	
	private HBox content;
	
	public void setContent(HBox content) {
		this.content = content;
	}
	
	public void clearContent() {
		content.getChildren().clear();
	}
	
	public void addContent(String url) {
		content.getChildren().add(FxmlLoader.getFXMLLoader(url).getRoot());
	}	
}
