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
					
					createServerListener(); // 1. ���������� ����
					processUI("���� ���� �Ϸ� : " + serverListener.toString(), getProgress(0.1)); // ���ɻ� : UI ó��
					connectToAnotherServerListener(); // 2. PeerThread �����Ͽ� DB ����� Peer��� ���Ͽ���
					Thread.sleep(500);
					processUI("P2P ��Ʈ��ũ�� ����Ϸ�",1); // ���ɻ� : UI ó��		
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		progressThread.start();
	}
	
	// ���ɻ� : ���������� �����
	private void createServerListener() {
		runServerListener(getPortNum());
	}
	// ���ɻ� : �ٸ� Peer�� ���������ʿ� �����ϱ�
	private void connectToAnotherServerListener() {
		connectToServerListenerOfAnotherPeers(getPeersInDB());
	}
	// ���ɻ� : ���������� ����
	private void runServerListener(String portNum) {
		makeServerListener(portNum);
		serverListener.start();
	}
	
	// ���ɻ� : DB�� ����� Peer�� ���� �������
	private ArrayList<Peer> getPeersInDB() {
		ArrayList<Peer> peers = dao.getPeers(peer.getUserName());
		return peers;
	}
	
	// ���ɻ� : �ٸ� Peer�� ���������ʿ� �����ϱ� + ���ɻ� : UI ó���ϱ� ( �� ���� �޼ҵ� �ȿ� �� ���� ���ɻ� )
	private void connectToServerListenerOfAnotherPeers(ArrayList<Peer> peers) {
			for(Peer peerValue : peers) {
				doConnect(peerValue, peers.size());
			}	
	}
	
	// ���ɻ� : ��Ʈ��ȣ ����
	private String getPortNum() {
		String[] address = peer.getLocalhost().split(":");
		String portNum = address[1];
		return portNum;
	}
	
	// ���ɻ� : ���������� ����
	private void makeServerListener(String portNum) {
		try {
			this.serverListener = new ServerListener(portNum);
			this.serverListener.setPeer(peer);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// ���ɻ� : Socket ���� �����ϱ�
	private void doConnect(Peer peerValue, int length) {
		try {
			if(connectServerListener(getSocketAddress(peerValue))) {
				processUI(getConnectionMsg(peerValue,true), getProgress(getConnectionPercent(length))); // ���ɻ� : UI ó��
			}else {
				processUI(getConnectionMsg(peerValue,false), getProgress(getConnectionPercent(length))); // ���ɻ� : UI ó��
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	// ���ɻ� : ���� ��巹�� ����
	private SocketAddress getSocketAddress(Peer peer) throws UnknownHostException {
		SocketAddress socketAddress = socketUtil.makeSocketAddress(peer.getLocalhost());
		return socketAddress;
	}
	
	// ���ɻ� : ��� ServerListener�� ����
	private boolean connectServerListener(SocketAddress socketAddress) {
		Socket socket = socketUtil.getSocket();
		if(socketUtil.connectToSocketAddress(socketAddress,socket)) {
			createPeerThread(socket);
			return true;
		}
		return false;
	}
	
	// ���ɻ� : PeerThread ����
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
