package controller;

import java.io.IOException;
import java.util.LinkedList;

import database.DAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Block;
import model.BlockchainModel;
import model.PeerModel;
import model.PeerThread;
import model.WalletModel;

public class LoginController {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	BlockchainModel blockchainModel = null;

	public void goJoinPage() {
		
		try {
			Parent login = FXMLLoader.load(getClass().getResource("/view/join.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage)joinButton.getScene().getWindow();
			primaryStage.setScene(scene);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void goIndex() throws Exception {
		
		String privateKey = privateKeyText.getText();
		DAO dao = new DAO();
		WalletModel walletModel = dao.getPeer(privateKey); // 개인키 관련 데이터 DB에서 가지고 오기
		PeerModel peerModel = new PeerModel();
		blockchainModel = new BlockchainModel();
		
		// DB에 개인키가 등록되어 있는 경우
		if(!(walletModel.getPrivateKey()==null)) {
			
			//DB에서 블럭들 가지고 오기
			LinkedList<Block> blocks = dao.getBlocks(privateKey);
			System.out.println("블럭 가져오기 성공");
			
			for(int i=0; i<blocks.size(); i++) {
				 blockchainModel.getBlocks().add(blocks.get(i)); 
				 if(i==blocks.size()-1) { // 리스트의 마지막 블럭인 경우
					 peerModel.block = blocks.get(i); 
					 Block.count=blocks.get(i).getNum();
				 }
			}
			
			//다른 Peer들과 네트워크 연결하기
			// P2P 연결 진행시키기
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/netprogress.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setX(joinButton.getScene().getWindow().getX()+65);
			stage.setY(joinButton.getScene().getWindow().getY()+50);
			stage.setResizable(false);
			stage.show();
			
			NetProgressController npc = loader.getController(); 
			npc.setPrimaryStage((Stage)joinButton.getScene().getWindow());
			npc.doProgress(peerModel, walletModel, blockchainModel,"login");
			
		}
		//DB에 개인키가 등록되어 있지 않은 경우
		else {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/logincheck.fxml"));
			Parent root = loader.load();
			LoginCheckController lcc = loader.getController();
			lcc.setPrimaryStage((Stage)privateKeyText.getScene().getWindow());
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}
			
		
		
	}

}
