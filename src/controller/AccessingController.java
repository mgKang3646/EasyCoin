package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Dao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.NewPage;
import model.Peer;
import model.PeerThread;
import model.ServerListener;
import util.JsonUtil;
import util.SocketUtil;

public class AccessingController implements Controller {
	
	private @FXML TextArea progressTextArea;
	private @FXML ProgressBar progressBar;
	private @FXML Label progressLabel;
	
	private Stage parentStage;
	private Dao dao;
	private Peer peer;
	private ServerListener serverListener;
	private NewPage newPage;
	private SocketUtil socketUtil;
	private double progress;
	private JsonUtil jsonUtil;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeObjects();
		initializeComponents();
	}
	public void initializeObjects() {
		this.dao = new Dao();
		this.socketUtil = new SocketUtil();
		this.jsonUtil = new JsonUtil();
	}
	public void initializeComponents() {
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}
	@Override
	public void setStage(Stage stageValue) {	
		this.parentStage = stageValue;
		newPage = new NewPage(parentStage);
	}
	@Override
	public void setObject(Object object) {
		this.peer = (Peer)object;
	}
	@Override
	public void mainButtonAction() {}
	@Override
	public void subButtonAction() {}
	
	@Override
	public void mainThreadAction() {
		Thread progressThread = new Thread() {
			public void run() {
				try {
					
					createServerListener(); // 1. 서버리스너 생성
					processUI("서버 생성 완료 : " + serverListener.toString(), getProgress(0.1)); // 관심사 : UI 처리
					connectToAnotherServerListener(); // 2. PeerThread 생성하여 DB 저장된 Peer들과 소켓연결
					Thread.sleep(500);
					processUI("P2P 네트워크망 연결완료",1); // 관심사 : UI 처리		
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		progressThread.start();
	}
	
	// 관심사 : 서버리스너 만들기
	private void createServerListener() {
		runServerListener(getPortNum());
	}
	// 관심사 : 다른 Peer의 서버리스너와 연결하기
	private void connectToAnotherServerListener() {
		connectToServerListenerOfAnotherPeers(getPeersInDB());
	}
	// 관심사 : 서버리스너 실행
	private void runServerListener(String portNum) {
		makeServerListener(portNum);
		serverListener.start();
	}
	
	// 관심사 : DB에 저장된 Peer들 정보 갖고오기
	private ArrayList<Peer> getPeersInDB() {
		ArrayList<Peer> peers = dao.getPeers(peer.getUserName());
		return peers;
	}
	
	// 관심사 : 다른 Peer의 서버리스너와 연결하기 + 관심사 : UI 처리하기 ( 한 가지 메소드 안에 두 가지 관심사 )
	private void connectToServerListenerOfAnotherPeers(ArrayList<Peer> peers) {
			for(Peer peerValue : peers) {
				doConnect(peerValue, peers.size());
			}	
	}
	
	// 관심사 : 포트번호 추출
	private String getPortNum() {
		String[] address = peer.getLocalhost().split(":");
		String portNum = address[1];
		return portNum;
	}
	
	// 관심사 : 서버리스너 생성
	private void makeServerListener(String portNum) {
		try {
			this.serverListener = new ServerListener(portNum);
			this.serverListener.setPeer(peer);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// 관심사 : Socket 연결 시작하기
	private void doConnect(Peer peerValue, int length) {
		try {
			if(connectServerListener(getSocketAddress(peerValue))) {
				processUI(getConnectionMsg(peerValue,true), getProgress(getConnectionPercent(length))); // 관심사 : UI 처리
			}else {
				processUI(getConnectionMsg(peerValue,false), getProgress(getConnectionPercent(length))); // 관심사 : UI 처리
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	// 관심사 : 소켓 어드레스 생성
	private SocketAddress getSocketAddress(Peer peer) throws UnknownHostException {
		SocketAddress socketAddress = socketUtil.makeSocketAddress(peer.getLocalhost());
		return socketAddress;
	}
	
	// 관심사 : 상대 ServerListener와 연결
	private boolean connectServerListener(SocketAddress socketAddress) {
		Socket socket = socketUtil.getSocket();
		if(socketUtil.connectToSocketAddress(socketAddress,socket)) {
			createPeerThread(socket);
			return true;
		}
		return false;
	}
	
	// 관심사 : PeerThread 생성
	private void createPeerThread(Socket socket) {
		try {
			PeerThread peerThread = new PeerThread(socket);
			peerThread.setPeer(this.peer);
			peerThread.start();
			peerThread.send(jsonUtil.sendLocalhost(serverListener.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
}
