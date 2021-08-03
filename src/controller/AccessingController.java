package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Dao;
import factory.UtilFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Peer;
import model.PeerThread;
import model.ServerListener;
import util.JsonUtil;
import util.NewPage;
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
	private UtilFactory utilFactory;
	private SocketUtil socketUtil;
	private double progress;
	private JsonUtil jsonUtil;
	private ArrayList<Peer> peers;
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeObjects();
		initializeComponents();
	}
	public void initializeObjects() {
		this.utilFactory = new UtilFactory();
		this.dao = new Dao();
		this.socketUtil = utilFactory.getSocketUtil();
		this.jsonUtil = utilFactory.getJsonUtil();
	}
	public void initializeComponents() {
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}
	@Override
	public void setStage(Stage stageValue) {	
		this.parentStage = stageValue;
		newPage = utilFactory.getNewPage(stageValue);
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
					createServerListener();
					processUI("���� ���� �Ϸ� : " + serverListener.toString(), getProgress(0.1)); // ���ɻ� : UI ó��
					connectToAnotherServerListener(); // 2. PeerThread �����Ͽ� DB ����� Peer��� ���Ͽ���
					processUI("P2P ��Ʈ��ũ�� ����Ϸ�",1); // ���ɻ� : UI ó��	
				} catch (IOException e) {}
			}
		};
		progressThread.start();
	}
	
	// ���ɻ� : ���������� �����
	private void createServerListener() throws IOException {
		runServerListener(getPortNum());
	}
	// ���ɻ� : �ٸ� Peer�� ���������ʿ� �����ϱ�
	private void connectToAnotherServerListener() throws IOException {
		setPeersFromDB();
		connectToServerListenerOfAnotherPeers();
	}
	// ���ɻ� : ���������� ����
	private void runServerListener(String portNum) throws IOException {
		makeServerListener(portNum);
		serverListener.start();
	}
	
	// ���ɻ� : DB�� ����� Peer�� ���� �������
	private void setPeersFromDB() {
		this.peers = dao.getPeers(peer.getUserName());
	}
	
	// ���ɻ� : �ٸ� Peer�� ���������ʿ� �����ϱ� + ���ɻ� : UI ó���ϱ� ( �� ���� �޼ҵ� �ȿ� �� ���� ���ɻ� )
	private void connectToServerListenerOfAnotherPeers() throws IOException {
			for(Peer peerValue : this.peers) {
				doConnect(peerValue);
			}	
	}
	
	// ���ɻ� : ��Ʈ��ȣ ����
	private String getPortNum() {
		String[] address = peer.getLocalhost().split(":");
		String portNum = address[1];
		return portNum;
	}
	
	// ���ɻ� : ���������� ����
	private void makeServerListener(String portNum) throws IOException {
			this.serverListener = new ServerListener(portNum);
			this.serverListener.setPeer(peer);
	}
	
	// ���ɻ� : Socket ���� �����ϱ�
	private void doConnect(Peer peerValue) throws IOException {
			if(connectServerListener(getSocketAddress(peerValue))) {
				processUI(getConnectionMsg(peerValue,true), getProgress(getConnectionPercent(this.peers.size()))); // ���ɻ� : UI ó��
			}else {
				processUI(getConnectionMsg(peerValue,false), getProgress(getConnectionPercent(this.peers.size()))); // ���ɻ� : UI ó��
			}
	}
	
	// ���ɻ� : ���� ��巹�� ����
	private SocketAddress getSocketAddress(Peer peer) throws UnknownHostException {
		return socketUtil.makeSocketAddress(peer.getLocalhost());
	}
	
	// ���ɻ� : ��� ServerListener�� ����
	private boolean connectServerListener(SocketAddress socketAddress) throws IOException {
		Socket socket = socketUtil.getSocket();
		if(socketUtil.connectToSocketAddress(socketAddress,socket)) {
			createPeerThread(socket);
			return true;
		}
		return false;
	}
	
	// ���ɻ� : PeerThread ����
	private void createPeerThread(Socket socket) throws IOException {
			PeerThread peerThread = new PeerThread(socket);
			peerThread.setPeer(this.peer);
			peerThread.start();
			peerThread.send(jsonUtil.sendLocalhost(serverListener.toString()));// ���ɻ簡 �ٸ� �и��ؾ� ��
	}
	
	// ���ɻ� : �߰��� progress ����
	private double getProgress(double value) {
		this.progress += value;
		return this.progress;
	}
	
	// ���ɻ� : ���� ��� �޼��� ��ȯ
	private String getConnectionMsg(Peer peer, boolean isConnect) {
		return peer.getUserName()+" ���� ��� : " + isConnect;
	}
	
	// ���ɻ� : ���� �ۼ�Ʈ �� ��ȯ
	private double getConnectionPercent(int size) {
		return 0.9/size;
	}
	
	// ���ɻ� : UI ó���ϱ�
	private void processUI(String msg, double progress) {
		Platform.runLater(()->{
			progressBar.setProgress(progress);
			progressLabel.setText("P2P�� ������("+(int)(progress*100)+"%)");
			progressTextArea.appendText(msg+"\n");
		});
	}
		
}
