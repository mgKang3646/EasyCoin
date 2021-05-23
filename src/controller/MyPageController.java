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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Block;
import model.PeerModel;
import model.PeerModel.Peer;

public class MyPageController implements Initializable  {
	
	@FXML private Button miningButton;
	@FXML private HBox content;
	@FXML private Button blockchainButton;
	@FXML private TextField idText;
	@FXML private Button wireButton;
	@FXML private Button stateConnectionButton;
	@FXML private ImageView mainImageView;
	@FXML private TextField balanceTextField;
	
	
	private PeerModel peerModel;
	private Parent miningPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		

				
		
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/index.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage)blockchainButton.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		IndexController ic = loader.getController();
		ic.setPeerModel(peerModel);
		ic.miningHandler();
	}
	
	public void blockchainHandler() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/index.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage)blockchainButton.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		IndexController ic = loader.getController();
		ic.setPeerModel(peerModel);
		ic.blockchainHandler();
		
		

	}
	
	public void stateConnectionHandler() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/index.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage)blockchainButton.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		IndexController ic = loader.getController();
		ic.setPeerModel(peerModel);
		ic.stateConnectionHandler();
	}
	
	public void walletHandler() throws IOException {
		
		content.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wallet.fxml"));
		content.getChildren().add(loader.load());// 로드가 된 후 Controller 객체를 쓸 수 있다.
		WalletController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
	}
	
}
