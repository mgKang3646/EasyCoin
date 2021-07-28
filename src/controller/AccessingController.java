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
import util.SocketUtil;

public class AccessingController implements Controller {
	
	private @FXML TextArea progressTextArea;
	private @FXML ProgressBar progressBar;
	private @FXML Label progressLabel;
	
	private Stage parentStage;
	private Dao dao;
	private Peer peer;
	private ServerListener serverListener;
	SocketUtil socketUtil;
	private double progress;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeObjects();
		initializeComponents();
	}
	public void initializeObjects() {
		this.dao = new Dao();
		this.socketUtil = new SocketUtil();
	}
	public void initializeComponents() {
		progressBar.setStyle("-fx-accent : #58FA82;");
		progressTextArea.setEditable(false);
	}
	@Override
	public void setStage(Stage stageValue) {	
		this.parentStage = stageValue;
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
	
	///////////////////////////////////////////// LEVEL 1 ///////////////////////////////////////////////////


	// ���ɻ� : ���������� �����
	private void createServerListener() {
		runServerListener(getPortNum());
	}
	// ���ɻ� : �ٸ� Peer�� ���������ʿ� �����ϱ�
	private void connectToAnotherServerListener() {
		connectToServerListenerOfAnotherPeers(getPeersInDB());
	}
	

	///////////////////////////////////////////// LEVEL 2 ///////////////////////////////////////////////////

	
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
				try {
					if(connectServerListener(getSocketAddress(peerValue))) {
						processUI(getConnectionMsg(peerValue,true), getProgress(getConnectionPercent(peers.size()))); // ���ɻ� : UI ó��
					}else {
						processUI(getConnectionMsg(peerValue,false), getProgress(getConnectionPercent(peers.size()))); // ���ɻ� : UI ó��
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}	
	}
	
	///////////////////////////////////////////// LEVEL 3 ///////////////////////////////////////////////////
	
	
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
	// ���ɻ� : ���� ��巹�� ����
	private SocketAddress getSocketAddress(Peer peer) throws UnknownHostException {
		SocketAddress socketAddress = socketUtil.makeSocketAddress(peer.getLocalhost());
		return socketAddress;
	}
	// ���ɻ� : ��� ServerListener�� ����
	private boolean connectServerListener(SocketAddress socketAddress) {
		Socket socket = socketUtil.getSocket();
		if(socketUtil.connectToSocketAddress(socketAddress,socket)) {
			createPeerThread();
			return true;
		}
		return false;
	}
	// ���ɻ� : PeerThread ����
	private void createPeerThread() {
		try {
			PeerThread peerThread = new PeerThread(socketUtil.getSocket());
			peerThread.setPeer(this.peer);
			peerThread.start();
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
