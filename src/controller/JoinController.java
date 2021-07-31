package controller;

import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ResourceBundle;

import database.Dao;
import encrypt.GeneratingKey;
import encrypt.Pem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.NewPage;
import util.SocketUtil;

public class JoinController implements Controller  {
	
	@FXML private Button join_linkButton;
	@FXML private TextField userNameText;
	@FXML private Button goLoginPageButton;
	@FXML private Label privateKeyLabel;
	@FXML private ImageView goLoginPageButtonImageView;
	@FXML private Text userNameCheck;
	
	private Stage stage;
	private NewPage newPage;
	private Dao dao;
	private String userName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeObjects();
	}
	
	public void initializeObjects() {
		this.dao = new Dao();
	}
	
	@Override
	public void setStage(Stage stageValue) {
		this.stage = stageValue;
		newPage = new NewPage(stage);
	}
	
	@Override
	public void setObject(Object object) {}
	
	@Override
	public void mainThreadAction() {}
	
	@Override
	public void subButtonAction() throws IOException { 	// 관심사 : 로그인 페이지로 돌아가기
		newPage.moveToLoginPage();
	}
	
	@Override
	public void mainButtonAction() throws IOException  {
			if(!isEmptyUserName()) {
				doJoinProcess(duplicateCheck());
			}
			else {
				setUserNameCheck("닉네임이 공백입니다.");
			}
	}
	
	// 관심사 : userName 공백체크
	private boolean isEmptyUserName() {
		this.userName = userNameText.getText();
		if(userName == "") return true;
		else return false;
	}
	
	// 관심사 : 회원가입 작업진행
	public void doJoinProcess(int duplicateResult) throws IOException {
			if(duplicateResult > 0) { // 중복이 안된 경우
				makePemFile(); // Pem파일생성
				addPeerInDB(); // 회원정보 DB저장
			}else if(duplicateResult == 0) { // 중복된 경우 
				setUserNameCheck("닉네임이 중복됩니다.");
			}else {} // SQL문 실행 문제 발생 
	}
	
	// 관심사 : userName 중복체크
	public int duplicateCheck() {
		int result = dao.checkDuplicateUserName(this.userName);
		return result;
	}
	
	// 관심사 : 로그인시 필요한 Pem 파일 만들기
	private void makePemFile() {
		GeneratingKey generatingKey = new GeneratingKey();// 관심사 : 개인키, 공개키 생성
		Pem pem = new Pem(this.userName); // 관심사 : Pem 파일 만들기
		pem.makePrivateAndPublicPemFile(generatingKey.getPrivateKey(), generatingKey.getPublicKey());
	}
	
	// 관심사 : 회원정보 DB에 저장하기
	private void addPeerInDB() throws IOException {
		int result = dao.join(getLocalhost(), this.userName);
		if(result > 0) { // 회원정보 DB 저장 성공한 경우
			changePage();	
		}
		else {}	// 회원정보 DB 저장 실패한 경우
	}
	
	// 관심사 : 페이지 전환하기
	private void changePage() throws IOException {
		newPage.moveToLoginPage();
		newPage.createPopupPage("회원가입이 완료되었습니다.");
	}
	
	// 관심사 : Peer의 주소생성
	private String getLocalhost() {
		return "localhost:"+ (5500 + (int)(Math.random()*100));
	}
	// 관심사 : userName 유효성 여부 띄우기
	private void setUserNameCheck(String msg) {
		userNameCheck.setText(msg);
		userNameCheck.setVisible(true);
	}
}	


