package controller;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Dao;
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
	
	@FXML TextArea progressTextArea;
	@FXML ProgressBar progressBar;
	@FXML Label progressLabel;
	
	NewPage newPage;
	Stage parentStage;
	Peer peer;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
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
				//P2P 네트워크 연결 과정
					try {
						//1. 서버리스너 생성
						//2. PeerThread 생성하여 DB에 저장된 서버리스너와 연결
						//3. 상대측에서 PeerThread 만들도록 유도
						
						// 관심사 : 포트번호 추출
						String[] address = peer.getLocalhost().split(":");
						String portNum = address[1];
						// 1. 서버리스너 생성
						ServerListener serverListener = new ServerListener(portNum);
						serverListener.start();
						// 2. PeerThread 생성하여 DB에 저장된 서버리스너와 연결
						// 관심사 : DB에 저장되 Peer 갖고오기
						Dao dao = new Dao();
						ArrayList<Peer> peers = dao.getPeers(peer.getUserName());
						
						if(peers != null) {
							//3. Peer 스레드 생성하여 ServerListener와 연결
							for(int i =0;i<peers.size();i++) {
								// 관심사 : 소켓 어드레스 생성
								SocketUtil socketUtil = new SocketUtil();
								SocketAddress socketAddress = socketUtil.makeSocketAddress(peers.get(i).getLocalhost());
								if(socketUtil.connectToSocketAddress(socketAddress)) {
									// 관심사 : PeerThread 생성
									PeerThread peerThread = new PeerThread(socketUtil.getSocket());
									peerThread.start();
									System.out.println("이름 : "+ peers.get(i).getUserName() + " true");
								}else {
									// 소켓 연결 실패한 경우
									System.out.println("이름 : "+ peers.get(i).getUserName()+" FALSE");
									continue;
								}
							}
						}
					} catch (IOException e) {
						System.out.println("서버리스너 생성 중 오류 발생");
					}	
				}
			};
			
			progressThread.start();
		}	
	}
