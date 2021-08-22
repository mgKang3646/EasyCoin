package newpage;

import controller.Controller;

public class FxmlController extends URL {
	
	public Controller getFXMLController(String url) {
		switch(url){
			case accessingURL : return getController(url);
			case blockURL : return getController(url);
			case indexURL : return getController(url);
			case joinURL : return getController(url);
			case loginURL : return getController(url);
			case miningURL: return getController(url);
			case miningResultURL : return getController(url);
			case mypageURL : return getController(url);
			case popupURL : return getController(url);
			default : return null;
		}
	}
	
	private Controller getController(String url) {
		return FxmlLoader.getFXMLLoader(url).getController();
	}

}
