package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Dao;
import factory.JsonFactory;
import factory.NewPageFactory;
import factory.SocketThreadFactory;
import factory.UtilFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import json.JsonSend;
import model.Peer;
import model.PeerList;
import model.ServerListener;
import model.SocketThread;
import util.SocketUtil;

public class AccessingController implements Controller {
	
	private @FXML TextArea progressTextArea;
	private @FXML ProgressBar progressBar;
	private @FXML Label percentLabel;
	private @FXML Label titleLabel;
	
	private Dao dao;
	private Peer peer;
	ArrayList<Peer> peers;
	private PeerList peerList ;
	private Stage parentStage;
	private ServerListener serverListener;
	private NewPageFactory newPageFactory;
	private UtilFactory utilFactory;
	private SocketThreadFactory socketThreadFactory;
	private SocketUtil socketUtil;
	private double progress;
	private JsonFactory jsonFactory;
	private JsonSend jsonSend;
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createObjects();
		initializeComponents();
	}
	private void createObjects() {
		jsonFactory = new JsonFactory();
		utilFactory = new UtilFactory();
		newPageFactory = new NewPageFactory();
		socketThreadFactory = new SocketThreadFactory();
	}
	
	public void initializeComponents() {
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}
	@Override
	public void setStage(Stage stage) {
		this.parentStage = stage;
	}
	@Override
	public void setPeer(Peer peer) {	
		this.peer = peer;
	}
	@Override
	public void setObject(Object object) {}
	
	@Override
	public void execute() {
		runAccessingThread();
		dao = new Dao();
		newPageFactory.setStage(parentStage);
		socketUtil = utilFactory.getSocketUtil();
		jsonSend = jsonFactory.getJsonSend();
		peerList = peer.getPeerList();
	}
	
	private Stage getStage() {
		return (Stage)progressBar.getScene().getWindow();
	}
	
	public void runAccessingThread() {
		Thread progressThread = new Thread() {
			public void run() {
				try {
					doAccessing();
					sleepMoment();
					moveToMypage();
					closeStage();
				} catch (IOException e) {
					closeStage();
					openErrorPopup();
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
	
	private void sleepMoment() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		peers = dao.getPeers(peer.getUserName());
	}
	
	// 관심사 : 다른 Peer의 서버리스너와 연결하기 + 관심사 : UI 처리하기 ( 한 가지 메소드 안에 두 가지 관심사 )
	private void connectToServerListenerOfAnotherPeers() throws IOException {
			for(Peer peerValue : peers) {
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
			serverListener = new ServerListener(portNum);
			// 관심사 : 관계설정 ( 분리 필요 )
			serverListener.setPeer(peer);
			peer.setServerListener(serverListener);
	}
	
	// 관심사 : Socket 연결 시작하기
	private void doConnect(Peer peerValue) throws IOException {
			if(connectServerListener(getSocketAddress(peerValue))) {
				processUI(getConnectionMsg(peerValue,true), getProgress(getConnectionPercent(peers.size()))); // 관심사 : UI 처리
				peerList.addPeer(peerValue);
			}else {
				processUI(getConnectionMsg(peerValue,false), getProgress(getConnectionPercent(peers.size()))); // 관심사 : UI 처리
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
			socketThread.send(jsonSend.jsonConnectMessage(serverListener.toString(),peer.getUserName()));// 관심사가 다름 분리해야 됨
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
			percentLabel.setText("("+(int)(progress*100)+"%)");
			progressTextArea.appendText(msg+"\n");
		});
	}
	
	private void moveToMypage() {
		Platform.runLater(()->{
			newPageFactory.moveMyPage(peer);
		});
	}
	
	private void openErrorPopup() {
		Platform.runLater(()->{
			String msg = "이미 접속 중인 개인키입니다.";
			newPageFactory.createPopupPage(msg);
		});
	}
	
	private void closeStage() {
		Platform.runLater(()->{
			getStage().close();
		});
	}
	
	
}
