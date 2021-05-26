package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.ResourceBundle;

import database.DAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Block;
import model.BlockchainModel;
import model.PeerModel;
import model.ReadPemFile;
import model.WalletModel;

public class LoginController implements Initializable  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView loginButtonImageView;
	@FXML private ImageView joinButtonImageView;
	@FXML private ImageView mainImageView;
	@FXML private ImageView titleImageView;
	BlockchainModel blockchainModel = null;
	Image joinButtonImage;
	Image loginButtonImage;
	Image mainImage;
	Image titleImage;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		mainImageView.setOnMousePressed(e->{
			mainImage = new Image("/image/main.gif");
			mainImageView.setImage(mainImage);
		});
		mainImageView.setOnMouseReleased(e->{
			mainImage = new Image("/image/main.png");
			mainImageView.setImage(mainImage);
		});
		
		titleImageView.setOnMouseClicked(e->{
			titleImage = new Image("/image/EasyCoinClicked.png");
			titleImageView.setImage(titleImage);
		});
	
		loginButton.setOnMouseEntered(e->{
			loginButtonImage = new Image("/image/loginEntered.png");
			loginButtonImageView.setImage(loginButtonImage);
		});
			
		loginButton.setOnMouseExited(e->{
			loginButtonImage = new Image("/image/login.png");
			loginButtonImageView.setImage(loginButtonImage);
		});
		
		
		joinButton.setOnMouseEntered(e->{
			joinButtonImage = new Image("/image/joinEntered.png");
			joinButtonImageView.setImage(joinButtonImage);
		});
			
		joinButton.setOnMouseExited(e->{
			joinButtonImage = new Image("/image/join.png");
			joinButtonImageView.setImage(joinButtonImage);
		});
	}
	
	
	
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
	
	
	//? �α��� �� ����Ű ����Ű �����ϴ� �� �����ؾ��� ������ ����Ű�ε� ���Ӱ���
	public void goMyPage() throws Exception {
		// DB ���� ��ü ����
		DAO dao = new DAO();
		
		// ���� Ž���� ����
		FileChooser fc = new FileChooser();
		fc.setTitle("���ϴ� ������ �����ϼ���");
		fc.setInitialDirectory(new File("./pem"));
		File file = null;
		file = fc.showOpenDialog((Stage)loginButton.getScene().getWindow());
		
		// ����Ű ������ ���� �ִ� ���
		if(file != null) {
			
			PeerModel peerModel = new PeerModel();
			blockchainModel = new BlockchainModel();
			
			//���� �о����
			ReadPemFile readPemFile = new ReadPemFile();
			
			//���� ���� �� �ʱ�ȭ�ϱ�
			PrivateKey privateKey = readPemFile.readPrivateKeyFromPemFile(file.getPath());
			PublicKey publicKey = readPemFile.readPublicKeyFromPemFile("./pem/"+readPemFile.getUsername()+"publickey.pem");
			WalletModel walletModel = dao.getPeer(readPemFile.getUsername());
			walletModel.setPrivateKey(privateKey);
			walletModel.setPublicKey(publicKey);
			
			//DB���� ���� ������ ����
			LinkedList<Block> blocks = dao.getBlocks(readPemFile.getUsername());
			
			//���ü�� ä���
			for(int i=0; i<blocks.size(); i++) {
				 blockchainModel.getBlocks().add(blocks.get(i)); 
				 if(i==blocks.size()-1) { // ����Ʈ�� ������ ���� ���
					 peerModel.block = blocks.get(i); 
					 Block.count=blocks.get(i).getNum();
				 }
			}
			
			// P2P�� ���� ����
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
	}



}
