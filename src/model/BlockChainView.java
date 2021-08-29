package model;

import controller.MiningController;
import javafx.application.Platform;
import newview.FxmlLoader;
import newview.NewView;
import newview.ViewURL;

public class BlockChainView {
	
	private NewView newView;
	
	public BlockChainView() {
		newView = new NewView();
	}
	
	public void showPopUp(String msg) {
		Platform.runLater(()->{
			newView.getNewWindow(ViewURL.popupURL,msg);
		});
	}
	
	public void startVerify() {
		MiningController mc = FxmlLoader.getFXMLLoader(ViewURL.miningURL).getController();
		mc.verifyUI();
	}
	
	public void processTx() {
		MiningController mc = FxmlLoader.getFXMLLoader(ViewURL.miningURL).getController();
		mc.processTxUI();
	}
	
	public void endVerify() {
		MiningController mc = FxmlLoader.getFXMLLoader(ViewURL.miningURL).getController();
		mc.basicUI();
	}
	
	public void showResult(MiningState verifyResult) {
		Platform.runLater(()->{
			switch(verifyResult) {
				case MININGGRANTED : newView.getNewWindow(ViewURL.miningResultURL,verifyResult); break;
				case OTHERMININGGRANTED : newView.getNewWindow(ViewURL.miningResultURL,verifyResult); break;
				case FAILEDGRANTED : {
					String msg = "블럭검증에 실패했습니다";
					newView.getNewWindow(ViewURL.popupURL,msg);
					break;
				}
				default : break;
			}
		});
	}

}
