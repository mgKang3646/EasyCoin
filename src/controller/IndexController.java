package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Block;
import model.PeerModel;

public class IndexController  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private Button upgradeButton;
	@FXML private TextField idText;
	@FXML private Button walletButton;
	
	private PeerModel peerModel;
	
	public void setPeerModel(PeerModel peerModel){
		this.peerModel = peerModel;
		idText.setText(this.peerModel.walletModel.getUsername()); // ID �����ϱ�
		idText.setEditable(false); // ���� ���ϵ���
	}
	public void miningHandler() throws IOException {
		
		content.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mining.fxml"));
		content.getChildren().add(loader.load());// �ε尡 �� �� Controller ��ü�� �� �� �ִ�.
		MiningController mc= loader.getController();
		mc.setPeerModel(peerModel);
	}
	
	public void blockchainHandler() throws IOException {
		
		content.getChildren().clear();

		ArrayList<Block> blocks = peerModel.blockchainModel.getBlocks();
		
		for(int i=0; i<blocks.size();i++) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/block.fxml"));
			content.getChildren().add(loader.load());
			BlockController bc = loader.getController();
			bc.createBlock(blocks.get(i));
			
		}

	}
	
	public void walletHandler() throws IOException {
		
		content.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wallet.fxml"));
		content.getChildren().add(loader.load());// �ε尡 �� �� Controller ��ü�� �� �� �ִ�.
		WalletController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
	}
	
	public void doUpgrade() {
		
		if(peerModel.threadForLeaderPeer != null) {
			peerModel.isFirstResponse = true; 
			peerModel.threadForLeaderPeer.requestBlock(); //���� Peer���� �� ��û�ϱ�
		}
		
		
	}
	
	

}
