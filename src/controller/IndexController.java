package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Block;
import model.PeerModel;
import model.PeerModel.Peer;

public class IndexController implements Initializable  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	@FXML private Button wireButton;
	@FXML private Button upgradeButton;
	@FXML private Button goMyPageButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView upgradeButtonImageView;
	@FXML private AnchorPane EasyCoin;	
	Image upgradeButtonImage;
	
	
	private PeerModel peerModel;
	private Parent miningPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	
		upgradeButtonImageView.setOnMouseEntered(e->{
			upgradeButtonImage = new Image("/image/main.gif");
			upgradeButtonImageView.setImage(upgradeButtonImage);
		});

	}
	
	public void goMyPage() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mypage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage)goMyPageButton.getScene().getWindow();
			primaryStage.setScene(scene);
			
			MyPageController mpc = loader.getController();
			mpc.setPeerModel(peerModel);
			
			
		} catch (IOException e) {
					}
		
	}
	
	public void setPeerModel(PeerModel peerModel) throws IOException{
		this.peerModel = peerModel;
		idText.setText(this.peerModel.walletModel.getUsername()); // ID �����ϱ�
		idText.setEditable(false); // ���� ���ϵ���
		
		//ä�� ��ư �̸� PeerModel�� �����ϱ�
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
		content.getChildren().add(loader.load());// �ε尡 �� �� Controller ��ü�� �� �� �ִ�.
		StateConnectionController scc = loader.getController();
		scc.setPeerModel(peerModel);
	}
	
	public void walletHandler() throws IOException {
		
		content.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wallet.fxml"));
		content.getChildren().add(loader.load());// �ε尡 �� �� Controller ��ü�� �� �� �ִ�.
		WalletController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
	}
	
	public void doUpgrade() throws IOException {
		Peer peer = peerModel.getLeader();
		peerModel.isFirst = true; // ��� �� ������ �ʿ�
		
		if(peer != null) {
			peer.getPeerThread().requestBlock(); //���� Peer���� �� ��û�ϱ�
		}else { //������ ���� ���
			if(!peerModel.amILeader) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/popup.fxml"));
				Parent root = loader.load();
				PopupController pc = loader.getController();
				pc.setMessage("�ֽ�ȭ�� �Ϸ��� ������ �־�� �մϴ�.\r���� ������ ���� ä���� �������ֽʽÿ�.");
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
