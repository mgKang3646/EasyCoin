package factory;

import javafx.stage.Stage;
import util.JsonUtil;
import util.NewPage;
import util.SocketUtil;

public class UtilFactory {
	
	public JsonUtil getJsonUtil() {
		return new JsonUtil();
	}
	
	public NewPage getNewPage(Stage stage) {
		return new NewPage(stage);
	}
	
	public SocketUtil getSocketUtil() {
		return new SocketUtil();
	}

}
