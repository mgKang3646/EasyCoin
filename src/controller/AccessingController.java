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
				//P2P ��Ʈ��ũ ���� ����
					try {
						//1. ���������� ����
						//2. PeerThread �����Ͽ� DB�� ����� ���������ʿ� ����
						//3. ��������� PeerThread ���鵵�� ����
						
						// ���ɻ� : ��Ʈ��ȣ ����
						String[] address = peer.getLocalhost().split(":");
						String portNum = address[1];
						// 1. ���������� ����
						ServerListener serverListener = new ServerListener(portNum);
						serverListener.start();
						// 2. PeerThread �����Ͽ� DB�� ����� ���������ʿ� ����
						// ���ɻ� : DB�� ����� Peer �������
						Dao dao = new Dao();
						ArrayList<Peer> peers = dao.getPeers(peer.getUserName());
						
						if(peers != null) {
							//3. Peer ������ �����Ͽ� ServerListener�� ����
							for(int i =0;i<peers.size();i++) {
								// ���ɻ� : ���� ��巹�� ����
								SocketUtil socketUtil = new SocketUtil();
								SocketAddress socketAddress = socketUtil.makeSocketAddress(peers.get(i).getLocalhost());
								if(socketUtil.connectToSocketAddress(socketAddress)) {
									// ���ɻ� : PeerThread ����
									PeerThread peerThread = new PeerThread(socketUtil.getSocket());
									peerThread.start();
									System.out.println("�̸� : "+ peers.get(i).getUserName() + " true");
								}else {
									// ���� ���� ������ ���
									System.out.println("�̸� : "+ peers.get(i).getUserName()+" FALSE");
									continue;
								}
							}
						}
					} catch (IOException e) {
						System.out.println("���������� ���� �� ���� �߻�");
					}	
				}
			};
			
			progressThread.start();
		}	
	}
