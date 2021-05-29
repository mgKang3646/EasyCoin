package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database.DAO;
import database.DTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.BlockchainModel;
import model.PeerModel;
import model.Pem;
import model.WalletModel;
import model.produceKey;

public class JoinController implements Initializable {

	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	Image goLoginPageButtonImage;
	
	DTO userDTO;
	DAO dao;
	produceKey produceKey;
	PeerModel peerModel;
	WalletModel walletModel;
	BlockchainModel blockchainModel;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		goLoginPageButtonImageView.setOnMouseEntered(e->{
			goLoginPageButtonImage = new Image("/image/goBackIconEntered.png");
			goLoginPageButtonImageView.setImage(goLoginPageButtonImage);
		});
		
		userDTO = new DTO();
		dao = new DAO();
		produceKey = new produceKey(); //privateKey와 publicKey 생성을 위한 Model
		peerModel = new PeerModel(); // P2P 통신 모델
		walletModel = new WalletModel(); // 지갑 생성
		blockchainModel = new BlockchainModel(); // 블록체인 생성
	
	}

	
	public void goLoginPage() {
		
		try {
			Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage)goLoginPageButton.getScene().getWindow();
			primaryStage.setScene(scene);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void join() throws Exception {
		
		// ID 중복체크
		
		String username = userNameText.getText(); 
		int registerResult = dao.registerCheck(username);
		
		//DB에 이미 HOST 주소가 등록되어 있는 경우
		if(registerResult == 1) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registercheck.fxml"));
			Parent root = loader.load();
			RegisterCheckController rcc = loader.getController();
			rcc.setPrimaryStage((Stage)join_linkButton.getScene().getWindow());
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}
		
		// DB에 주소가 등록되어 있지 않은 경우
		else {
		
			produceKey.generateKeyPair(); // privateKey, publicKey 생성
			
			//PrivateKey 용, PublicKey용 Pem 파일 만들기
			Pem pemFileForPrivate = new Pem(produceKey.getPrivateKey(),username); 
			pemFileForPrivate.write(username+"privatekey.pem");
			System.out.println(String.format("EC 암호키 %s파일을 내보냈습니다.",username+"privatekey.pem"));

			Pem pemFileForPublic = new Pem(produceKey.getPublicKey(),username); 
			pemFileForPublic.write(username+"publickey.pem");
			System.out.println(String.format("EC 암호키 %s파일을 내보냈습니다.",username+"publickey.pem"));

			//주소 랜덤 생성하기
			String localhost = "localhost:"+ (5500 + (int)(Math.random()*100)); // 주소 임의 설정
			
			//DB 접근
			if(dao.join(localhost, username)>0) { // join()의 return값이 0 이상이면 DB삽입 명령정상처리
					
					try {
						//지갑 초기화
						walletModel.setPrivateKey(produceKey.getPrivateKey());
						walletModel.setPublicKey(produceKey.getPublicKey());
						walletModel.setUserLocalHost(localhost);
						walletModel.setUsername(username);
						
						// P2P 연결 진행시키기
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/netprogress.fxml"));
						Parent root = loader.load();
						
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.setX(join_linkButton.getScene().getWindow().getX()+65);
						stage.setY(join_linkButton.getScene().getWindow().getY()+50);
						stage.setResizable(false);
						stage.show();
						
						NetProgressController npc = loader.getController(); 
						npc.setPrimaryStage((Stage)join_linkButton.getScene().getWindow());
						npc.doProgress(peerModel, walletModel, blockchainModel,"join");
						npc.setPrivateKeyPath(pemFileForPrivate.getPath());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}	
			} 
		}
	}
}
