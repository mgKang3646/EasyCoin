package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Dao;
import factory.SocketThreadFactory;
import factory.UtilFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Peer;
import model.ServerListener;
import model.SocketThread;
import util.JsonSend;
import util.NewPage;
import util.SocketUtil;

public class AccessingController implements Controller {
	
	private @FXML TextArea progressTextArea;
	private @FXML ProgressBar progressBar;
	private @FXML Label progressLabel;
	
	private Dao dao;
	private Peer peer;
	private Stage stage;
	private ServerListener serverListener;
	private NewPage newPage;
	private UtilFactory utilFactory;
	private SocketThreadFactory socketThreadFactory;
	private SocketUtil socketUtil;
	private double progress;
	private JsonSend jsonSend;
	private ArrayList<Peer> peers;
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createObjects();
		initializeComponents();
	}
	private void createObjects() {
		utilFactory = new UtilFactory();
		socketThreadFactory = new SocketThreadFactory();
		dao = new Dao();
		socketUtil = utilFactory.getSocketUtil();
		jsonSend = utilFactory.getJsonSend();
	}
	
	public void initializeComponents() {
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}
	@Override
	public void setStage(Stage stage) {	
		this.stage = stage;
	}
	@Override
	public void setPeer(Peer peer) {	
		this.peer = peer;
	}
	@Override
	public void executeDefaultProcess() throws IOException {
		newPage = utilFactory.getNewPage(stage, peer);
	}
	
	@Override// 이거 하나 때문에 모든 컨트롤러가 mainThreadAction을 가져야함 제거하는 방향으로 설계 필요
	public void mainThreadAction() {
		Thread progressThread = new Thread() {
			public void run() {
				try {
					
					doAccessing();
					moveToMypage();
					closeStage();
					
				} catch (IOException e) {
					// 이미 해당주소로 창이 열려있는 경우 팝업창 띄우기
				}
			}
		};
		progressThread.start();
	}
	
	private void doAccessing() throws IOException {
		createServerListener();
		processUI("서버 생성 완료 : " + serverListener.toString(), getProgress(0.1)); // 관심사 : UI 처리
		connectToAnotherServerListener(); // 2. PeerThread 생성하여 DB 저장된 Peer들과 소켓연결
		processUI("P2P 네트워크망 연결완료",1); // 관심사 : UI 처리	
	}
	
	// 관심사 : 서버리스너 만들기
	private void createServerListener() throws IOException {
		runServerListener(getPortNum());
	}
	// 관심사 : 다른 Peer의 서버리스너와 연결하기
	private void connectToAnotherServerListener() throws IOException {
		setPeersFromDB();
		connectToServerListenerOfAnotherPeers();
	}
	// 관심사 : 서버리스너 실행
	private void runServerListener(String portNum) throws IOException {
		makeServerListener(portNum);
		serverListener.start();
	}
	
	// 관심사 : DB에 저장된 Peer들 정보 갖고오기
	private void setPeersFromDB() {
		this.peers = dao.getPeers(peer.getUserName());
	}
	
	// 관심사 : 다른 Peer의 서버리스너와 연결하기 + 관심사 : UI 처리하기 ( 한 가지 메소드 안에 두 가지 관심사 )
	private void connectToServerListenerOfAnotherPeers() throws IOException {
			for(Peer peerValue : this.peers) {
				doConnect(peerValue);
			}	
	}
	
	// 관심사 : 포트번호 추출
	private String getPortNum() {
		String[] address = peer.getLocalhost().split(":");
		String portNum = address[1];
		return portNum;
	}
	
	// 관심사 : 서버리스너 생성
	private void makeServerListener(String portNum) throws IOException {
			this.serverListener = new ServerListener(portNum);
			this.serverListener.setPeer(peer);
	}
	
	// 관심사 : Socket 연결 시작하기
	private void doConnect(Peer peerValue) throws IOException {
			if(connectServerListener(getSocketAddress(peerValue))) {
				processUI(getConnectionMsg(peerValue,true), getProgress(getConnectionPercent(this.peers.size()))); // 관심사 : UI 처리
			}else {
				processUI(getConnectionMsg(peerValue,false), getProgress(getConnectionPercent(this.peers.size()))); // 관심사 : UI 처리
			}
	}
	
	// 관심사 : 소켓 어드레스 생성
	private SocketAddress getSocketAddress(Peer peer) throws UnknownHostException {
		return socketUtil.makeSocketAddress(peer.getLocalhost());
	}
	
	// 관심사 : 상대 ServerListener와 연결
	private boolean connectServerListener(SocketAddress socketAddress) throws IOException {
		Socket socket = socketUtil.getSocket();
		if(socketUtil.connectToSocketAddress(socketAddress,socket)) {
			createPeerThread(socket);
			return true;
		}
		return false;
	}
	
	// 관심사 : PeerThread 생성
	private void createPeerThread(Socket socket) throws IOException {
			SocketThread socketThread = socketThreadFactory.getPeerThread(socket,peer);
			socketThread.start();
			socketThread.send(jsonSend.requestPeerThread(serverListener.toString()));// 관심사가 다름 분리해야 됨
	}
	
	// 관심사 : 추가된 progress 리턴
	private double getProgress(double value) {
		this.progress += value;
		return this.progress;
	}
	
	// 관심사 : 연결 결과 메세지 반환
	private String getConnectionMsg(Peer peer, boolean isConnect) {
		return peer.getUserName()+" 연결 결과 : " + isConnect;
	}
	
	// 관심사 : 연결 퍼센트 값 반환
	private double getConnectionPercent(int size) {
		return 0.9/size;
	}
	
	// 관심사 : UI 처리하기
	private void processUI(String msg, double progress) {
		Platform.runLater(()->{
			progressBar.setProgress(progress);
			progressLabel.setText("P2P망 접속중("+(int)(progress*100)+"%)");
			progressTextArea.appendText(msg+"\n");
		});
	}
	
	private void moveToMypage() {
		Platform.runLater(()->{
			newPage.moveToMyPage();
		});
	}
	
	private void closeStage() {
		Platform.runLater(()->{
			Stage stage = (Stage)progressBar.getScene().getWindow();
			stage.close();
		});
	}
	
	
	@Override
	public void setObject(Object object) {}
	@Override
	public void mainButtonAction() {}
	@Override
	public void subButtonAction() {}
	
}
