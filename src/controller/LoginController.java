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
		WalletModel walletModel = dao.getPeer(privateKey); // ����Ű ���� ������ DB���� ������ ����
		PeerModel peerModel = new PeerModel();
		blockchainModel = new BlockchainModel();
		
		// DB�� ����Ű�� ��ϵǾ� �ִ� ���
		if(!(walletModel.getPrivateKey()==null)) {
			
			//DB���� ���� ������ ����
			LinkedList<Block> blocks = dao.getBlocks(privateKey);
			System.out.println("�� �������� ����");
			
			for(int i=0; i<blocks.size(); i++) {
				 blockchainModel.getBlocks().add(blocks.get(i)); 
				 if(i==blocks.size()-1) { // ����Ʈ�� ������ ���� ���
					 peerModel.block = blocks.get(i); 
					 Block.count=blocks.get(i).getNum();
				 }
			}
			
			//�ٸ� Peer��� ��Ʈ��ũ �����ϱ�
			// P2P ���� �����Ű��
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
		//DB�� ����Ű�� ��ϵǾ� ���� ���� ���
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
