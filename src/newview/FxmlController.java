package newview;

import controller.Controller;

public class FxmlController {
	
	public void setController(String url) {
		Controller controller = FxmlLoader.getFXMLLoader(url).getController();
		controller.execute();
	}
	
	public void setController(String url, Object throwObject) {
		Controller controller = FxmlLoader.getFXMLLoader(url).getController();
		controller.throwObject(throwObject);
		controller.execute();
	}

}
