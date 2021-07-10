package controller;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.json.Json;

import org.bouncycastle.util.encoders.Base64;

import javafx.application.Platform;
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
import model.TransactionOutput;
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
	

	
	//�۱��ϱ�
	public void wire() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wire.fxml"));
		Parent root = loader.load();
		WireController wc = loader.getController();
		wc.setPeerModel(peerModel);
		
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setX(balanceTextField.getScene().getWindow().getX()+220);
		stage.setY(balanceTextField.getScene().getWindow().getY());
		stage.show();
		
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
		
		setUTXO();
		getBalance();
		
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
	
	public void setUTXO() {
				//UTXO value �޾ƿ���
				//���� �ȿ� UTXO����� �����
				peerModel.walletModel.makeUTXOWallet();
				
				//UTXO ��û�ϱ�
				StringWriter sw = new StringWriter();
				Json.createWriter(sw).writeObject(Json.createObjectBuilder()
														.add("requestUTXO", "")
														.add("owner", Base64.toBase64String(peerModel.walletModel.getPublicKey().getEncoded()))
														.build());
				
				peerModel.getServerListerner().sendMessage(sw.toString());
				
				
				//���� UTXOs�� �ڽ��� publickey�� ���� UTXO�� �ִ��� Ȯ���ϱ� 
				for(int i=0; i<peerModel.UTXOs.size();i++) {
					if(peerModel.UTXOs.get(i).isMine(peerModel.walletModel.getPublicKey())) {
						peerModel.walletModel.getUTXOWallet().add(peerModel.UTXOs.get(i));
					}
				}
		}
	

	public void getBalance() {
		Thread balanceThread = new Thread(new Runnable() {
			float total=0;
			int count = 0;
			@Override
			public void run() {
				while(count<=5) {
					try {
						total = 0; // �ʱ�ȭ �� ���� �����
						for(TransactionOutput UTXO : peerModel.walletModel.getUTXOWallet()) {
							total += UTXO.getValue();
						}
						Platform.runLater(()->{
							balanceTextField.setText(total+"");
						});
						count++;
						Thread.sleep(800);
					} catch (InterruptedException e) {e.printStackTrace();}						
				}	
			}
		});
		
		balanceThread.start();		
	}
	
}
