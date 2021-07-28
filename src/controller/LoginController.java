package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.FileChooserForPem;
import encrypt.KeyFromPem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.NewPage;
import model.Peer;

public class LoginController implements Controller  {
	
	@FXML private Button joinButton;
	@FXML private Button loginButton;
	@FXML private TextField privateKeyText;
	@FXML private ImageView mainImageView;
	
	private Stage stage;
	private NewPage newPage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	
	@Override
	public void setStage(Stage stageValue) {
		stage = stageValue;
	}
	@Override
	public void setObject(Object object) {}
	
	// 관심사 : 로그인 버튼 클릭 시 동작 로직
	@Override
	public void mainButtonAction() {
		// 관심사 : 파일 탐색기 열어서 원하는 개인키 PEM 파일 경로 확보하기
				String message = "로그인 할 개인키 PEM 파일을 선택하세요.";
				FileChooserForPem createFileChooser = new FileChooserForPem();
				File file = createFileChooser.showFileChooser(message, stage); 
				
				if(file != null) {
					// 관심사 : PEM 파일 경로를 통해 개인키 객체 확보하기
					try {
						KeyFromPem keyFromPem = new KeyFromPem();
						PrivateKey privateKey;
						privateKey = keyFromPem.readPrivateKeyFromPemFile(file.getPath());
						
						String userName = keyFromPem.getUserName();
						
						//개인키 객체를 확보한 경우
						if(privateKey != null) {
							// 관심사 : Peer 객체 생성
							Dao dao = new Dao();
							Peer peer = dao.getPeer(userName);
							peer.setPrivateKey(privateKey);
							// 관심사 : P2P 망 접속 화면 띄우기
							newPage = new NewPage("/view/accessing.fxml", stage);
							Controller controller = newPage.getController();
							controller.setObject(peer);
							controller.mainThreadAction();
							newPage.createPageOnNewStage();
						}
						//개인키 객체를 확보하지 못한 경우
						else {
							// 관심사 : 팝업창 띄우기
							newPage = new NewPage("/view/popup.fxml", stage);
							Controller controller = newPage.getController();
							controller.setObject("잘못된 개인키 형식입니다.");
							newPage.createPageOnNewStage();
						}
					} catch (NoSuchAlgorithmException | IOException e) {
						e.printStackTrace();
					}
				}
			}
	
	// 관심사 : 회원가입 버튼 클릭시 동작 로직
	@Override
	public void subButtonAction() {
		try {
			this.newPage = new NewPage("/view/join.fxml", stage);
			newPage.createPageOnCurrentStage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void mainThreadAction() {}
	
	
	
}
