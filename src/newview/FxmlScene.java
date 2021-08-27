package newview;

import javafx.scene.Scene;

public class FxmlScene extends ViewURL {
	
	public static Scene networkingScene;
	public static Scene blockScene;
	public static Scene indexScene;
	public static Scene joinScene;
	public static Scene loginScene;
	public static Scene miningScene;
	public static Scene miningResultScene;
	public static Scene mypageScene;
	public static Scene popupScene;
	public static Scene connectionScene;
	public static Scene refreshScene;
	public static Scene wireScene;
	
	public static Scene getFXMLScene(String url) {
		switch(url){
			case networkingURL : return networkingScene;
			case indexURL : return indexScene;
			case joinURL : return joinScene;
			case loginURL : return loginScene;
			case miningURL: return miningScene;
			case miningResultURL : return miningResultScene;
			case mypageURL : return mypageScene;
			case popupURL : return popupScene;
			case blockURL : return blockScene;
			case connectionTableURL : return connectionScene;
			case refreshURL : return refreshScene;
			case wireURL : return wireScene;
			default : return null;
		}
	}
	
	public void generateScene() {
		networkingScene = getScene(networkingURL);
		blockScene = getScene(blockURL);
		indexScene = getScene(indexURL);
		joinScene = getScene(joinURL);
		loginScene = getScene(loginURL);
		miningScene = getScene(miningURL);
		miningResultScene = getScene(miningResultURL);
		mypageScene = getScene(mypageURL);
		popupScene = getScene(popupURL);
		connectionScene = getScene(connectionTableURL);
		refreshScene = getScene(refreshURL);
		wireScene = getScene(wireURL);
	}
	
	private Scene getScene(String url) {
		return new Scene(FxmlLoader.getFXMLLoader(url).getRoot());
	}
}
