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
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // privateKey와 publicKey 생성을 위한 provider 추가
		userDTO = new UserDTO();
		dao = new DAO();
		produceKey = new produceKey(); //privateKey와 publicKey 생성을 위한 Model
		peerModel = new PeerModel(); // P2P 통신 모델
		walletModel = new WalletModel(); // 지갑 생성
		blockchainModel = new BlockchainModel(); // 블록체인 생성

	}

	
	public void join() throws Exception {
		
		// DB에 같은  등록 여부 변수
		int registerResult = dao.registerCheck(userNameText.getText());
		
		//DB에 이미 HOST 주소가 등록되어 있는 경우
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
		// DB에 주소가 등록되어 있지 않은 경우
		else {
		
			// ? 정확한 PrivateKey와 PublicKey 생성원리는 추후에 분석
			produceKey.generateKeyPair(); // privateKey, publicKey 생성
			
			String username = userNameText.getText(); // DTO에 로컬호스트 주소 저장
			String privateKey = produceKey.getPrivateKey().getEncoded().toString()+(int)(Math.random()*10);  // DTO에 개인키 문자열로 바꾼 후 저장
			String publicKey = produceKey.getPublicKey().getEncoded().toString();    // DTO에 공개키 문자열로 바꾼 후 저장
			String localhost = "localhost:"+ (5500 + (int)(Math.random()*100)); // 주소 임의 설정
			//DB 접근
			if(dao.join(localhost, privateKey, publicKey, username)>0) { // join()의 return값이 0 이상이면 DB삽입 명령정상처리
					
					try {
						
						//지갑 초기화
						walletModel.setPrivateKey(privateKey);
						walletModel.setPublicKey(publicKey);
						walletModel.setUserLocalHost(localhost);
						walletModel.setUsername(username);
						
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
						npc.doProgress(peerModel, walletModel, blockchainModel,"join");
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}	
			} 
		}

	}

	

}
