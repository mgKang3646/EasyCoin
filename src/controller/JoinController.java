package controller;

import java.io.IOException;
import java.net.URL;
import java.security.Security;
import java.util.ResourceBundle;

import database.DAO;
import database.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.BlockchainModel;
import model.PeerModel;
import model.WalletModel;
import model.produceKey;

public class JoinController implements Initializable {

	@FXML private Button joinButton;
	@FXML private TextField userNameText;
	@FXML private Button goToIndexButton;
	@FXML private Label privateKeyLabel;
	
	UserDTO userDTO;
	DAO dao;
	produceKey produceKey;
	PeerModel peerModel;
	WalletModel walletModel;
	BlockchainModel blockchainModel;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey�� publicKey ������ ���� provider �߰�
		userDTO = new UserDTO();
		dao = new DAO();
		produceKey = new produceKey(); //privateKey�� publicKey ������ ���� Model
		peerModel = new PeerModel(); // P2P ��� ��
		walletModel = new WalletModel(); // ���� ����
		blockchainModel = new BlockchainModel(); // ����ü�� ����

	}

	
	public void join() throws Exception {
		
		// DB�� ����  ��� ���� ����
		int registerResult = dao.registerCheck(userNameText.getText());
		
		//DB�� �̹� HOST �ּҰ� ��ϵǾ� �ִ� ���
		if(registerResult == 1) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registercheck.fxml"));
			Parent root = loader.load();
			RegisterCheckController rcc = loader.getController();
			rcc.setPrimaryStage((Stage)joinButton.getScene().getWindow());
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}
		// DB�� �ּҰ� ��ϵǾ� ���� ���� ���
		else {
		
			// ? ��Ȯ�� PrivateKey�� PublicKey ���������� ���Ŀ� �м�
			produceKey.generateKeyPair(); // privateKey, publicKey ����
			
			String username = userNameText.getText(); // DTO�� ����ȣ��Ʈ �ּ� ����
			String privateKey = produceKey.getPrivateKey().getEncoded().toString()+(int)(Math.random()*10);  // DTO�� ����Ű ���ڿ��� �ٲ� �� ����
			String publicKey = produceKey.getPublicKey().getEncoded().toString();    // DTO�� ����Ű ���ڿ��� �ٲ� �� ����
			String localhost = "localhost:"+ (5500 + (int)(Math.random()*100)); // �ּ� ���� ����
			//DB ����
			if(dao.join(localhost, privateKey, publicKey, username)>0) { // join()�� return���� 0 �̻��̸� DB���� ��������ó��
					
					try {
						
						//���� �ʱ�ȭ
						walletModel.setPrivateKey(privateKey);
						walletModel.setPublicKey(publicKey);
						walletModel.setUserLocalHost(localhost);
						walletModel.setUsername(username);
						
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
						npc.doProgress(peerModel, walletModel, blockchainModel,"join");
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}	
			} 
		}

	}

	

}