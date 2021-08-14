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
	private @FXML Label percentLabel;
	private @FXML Label titleLabel;
	
	private Dao dao;
	private Peer peer;
	private Stage parentStage;
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
	public void setPeer(Peer peer) {	
		this.peer = peer;
	}
	
	@Override
	public void setObject(Object object) {
		parentStage = (Stage)object;
	}
	
	@Override
	public void execute() {
		runAccessingThread();
	}
	
	private void setNewPage(NewPage newPage) {
		this.newPage = newPage;
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
		processUI("���� ���� �Ϸ� : " + serverListener.toString(), getProgress(0.1)); // ���ɻ� : UI ó��
		connectToAnotherServerListener(); // 2. PeerThread �����Ͽ� DB ����� Peer��� ���Ͽ���
		processUI("P2P ��Ʈ��ũ�� ����Ϸ�",1); // ���ɻ� : UI ó��	
	}
	
	private void sleepMoment() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			SocketThread socketThread = socketThreadFactory.getPeerThread(socket,peer);
			socketThread.start();
			socketThread.send(jsonSend.requestPeerThread(serverListener.toString()));// ���ɻ簡 �ٸ� �и��ؾ� ��
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
			percentLabel.setText("("+(int)(progress*100)+"%)");
			progressTextArea.appendText(msg+"\n");
		});
	}
	
	private void moveToMypage() {
		Platform.runLater(()->{
			setNewPage(utilFactory.getNewScene(parentStage,peer));
			newPage.makePage("/view/mypage.fxml");
		});
	}
	
	private void openErrorPopup() {
		Platform.runLater(()->{
			setNewPage(utilFactory.getNewStage(getStage()));
			newPage.makePage("/view/popup.fxml","�̹� ���� ���� ����Ű�Դϴ�.");
			newPage.show();
		});
	}
	
	private void closeStage() {
		Platform.runLater(()->{
			getStage().close();
		});
	}
	
}
