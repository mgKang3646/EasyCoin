package newpage;

import java.io.IOException;
import javafx.scene.Parent;

public class FxmlParent extends URL {
	
	public Parent getFXMLParent(String url) {
		switch(url){
			case accessingURL : return getParent(url);
			case blockURL : return getParent(url);
			case indexURL : return getParent(url);
			case joinURL : return getParent(url);
			case loginURL : return getParent(url);
			case miningURL: return getParent(url);
			case miningResultURL : return getParent(url);
			case mypageURL : return getParent(url);
			case popupURL : return getParent(url);
			default : return null;
		}
	}
	
	private Parent getParent(String url) {
		try {
			return FxmlLoader.getFXMLLoader(url).load();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
