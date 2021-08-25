package newview;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class FxmlLoader extends ViewURL{
	
	private static FXMLLoader NetWorkingLoader;
	private static FXMLLoader IndexLoader;
	private static FXMLLoader JoinLoader;
	private static FXMLLoader LoginLoader;
	private static FXMLLoader MiningLoader;
	private static FXMLLoader MiningResultLoader;
	private static FXMLLoader MypageLoader;
	private static FXMLLoader PopupLoader;
	private static FXMLLoader BlockLoader;
	private static FXMLLoader ConnectionTableLoader;
	
	public void generateFXMLLoader() {
		IndexLoader = getLoader(indexURL);
		JoinLoader = getLoader(joinURL);
		LoginLoader = getLoader(loginURL);
		MiningLoader = getLoader(miningURL);
		MiningResultLoader = getLoader(miningResultURL);
		MypageLoader = getLoader(mypageURL);
		PopupLoader = getLoader(popupURL);
		BlockLoader = getLoader(blockURL);
		NetWorkingLoader =  getLoader(networkingURL);
		ConnectionTableLoader = getLoader(connectionTableURL);
		
	}
	
	public static FXMLLoader getFXMLLoader(String url) {
		switch(url){
			case networkingURL : return NetWorkingLoader;
			case indexURL : return IndexLoader;
			case joinURL : return JoinLoader;
			case loginURL : return LoginLoader;
			case miningURL: return MiningLoader;
			case miningResultURL : return MiningResultLoader;
			case mypageURL : return MypageLoader;
			case popupURL : return PopupLoader;
			case blockURL : return BlockLoader;
			case connectionTableURL : return ConnectionTableLoader;
			default : return null;
		}
	}
	
	private FXMLLoader getLoader(String url) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
			loader.load();
			return loader;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public FXMLLoader getBlockLoader() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(blockURL));
			loader.load();
			return loader;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
