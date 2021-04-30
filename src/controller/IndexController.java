package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Block;
import model.PeerModel;
import model.PeerModel.Peer;

public class IndexController implements Initializable  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private Button upgradeButton;
	@FXML private TextField idText;
	@FXML private Button walletButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView blockchainImageView; 
	Image blockchainButtonImage;
	
	private PeerModel peerModel;
	private Parent miningPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		
		blockchainButton.setOnMouseEntered(e->{
			blockchainButtonImage = new Image("/image/blockchainEntered.png");
			blockchainImageView.setImage(blockchainButtonImage);
		});
			
		blockchainButton.setOnMouseExited(e->{
			blockchainButtonImage = new Image("/image/blockchain.png");
			blockchainImageView.setImage(blockchainButtonImage);
		});
		
			
		
		
	}
	
	public void setPeerModel(PeerModel peerModel) throws IOException{
		this.peerModel = peerModel;
		idText.setText(this.peerModel.walletModel.getUsername()); // ID 세팅하기
		idText.setEditable(false); // 수정 못하도록
		
		//채굴 버튼 미리 PeerModel에 저장하기
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mining.fxml"));
		miningPane = loader.load();
		MiningController mc= loader.getController();
		mc.setPeerModel(peerModel);
		peerModel.setMiningStartButton(mc.getMiningStartButton());
		
	}
	public void miningHandler() throws IOException {
		content.getChildren().clear();
		content.getChildren().add(miningPane);
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
	
	public void stateConnectionHandler() throws IOException {
		content.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/stateConnection.fxml"));
		content.getChildren().add(loader.load());// 로드가 된 후 Controller 객체를 쓸 수 있다.
		StateConnectionController scc = loader.getController();
		scc.setPeerModel(peerModel);
	}
	
	public void walletHandler() throws IOException {
		
		content.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wallet.fxml"));
		content.getChildren().add(loader.load());// 로드가 된 후 Controller 객체를 쓸 수 있다.
		WalletController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
	}
	
	public void doUpgrade() throws IOException {
		Peer peer = peerModel.getLeader();
		peerModel.isFirst = true; // 블록 총 삭제시 필요
		
		if(peer != null) {
			peer.getPeerThread().requestBlock(); //리더 Peer에게 블럭 요청하기
		}else { //리더가 없는 경우
			if(!peerModel.amILeader) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/popup.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setX(idText.getScene().getWindow().getX()+250);
				stage.setY(idText.getScene().getWindow().getY()+150);
				stage.show();
			}
		}
	}
	
}
