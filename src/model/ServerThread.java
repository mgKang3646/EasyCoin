package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;

import model.PeerModel.Peer;

public class ServerThread extends Thread {

	private ServerListener serverListener;
	private Socket socket;
	private PrintWriter printWriter;
	private PeerModel peerModel;
	
	public ServerThread(Socket socket, ServerListener serverListener,PeerModel peerModel)throws UnknownHostException{
		this.peerModel = peerModel;
		this.serverListener = serverListener;
		this.socket = socket;
	}
	
	public void run() {
	
		boolean flag = true;
		try {
			this.printWriter = new PrintWriter(socket.getOutputStream(),true);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			
			
			while(flag) {
				JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
				// PeerThread ���� ��û�� ���� ���
				if(jsonObject.containsKey("localhost")) {
					String hostAddress = jsonObject.getString("localhost");
					String username = jsonObject.getString("username");
					String[] address = hostAddress.split(":");
					
					Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1]));
					//////////////////////////////////////////blocking///////////////////////////////////////////
					
					PeerThread peerThread = new PeerThread(newSocket,peerModel);
					peerModel.peerList.add(new Peer(username, hostAddress,peerThread));
					peerThread.start();
					
					System.out.println("���� �Ϸ�");
					System.out.println(peerModel.peerList.get(peerModel.peerList.size()-1).toString());
					
					System.out.println("�������� : "+ peerModel.amILeader);
					//���� ��û�� ���� Peer���� �ڽ��� �������� �˸���
					if(peerModel.amILeader) {
						StringWriter sw = new StringWriter();
						Json.createWriter(sw).writeObject(Json.createObjectBuilder()
														.add("leader",true)
														.add("leaderBlockNum", Block.count)
														.build());
						printWriter.println(sw);
					}
				}
				// ���� P2P�� ������ PEER�� �ڽź��� �������� ���� ��� �������� ��������
				if(jsonObject.containsKey("biggerThanYou")) {
					System.out.println("�ʰ� ������ �� ũ����!");
					peerModel.amILeader = false;
				}
				// �� ���� ��û�� ���� ���
				if(jsonObject.containsKey("blockNum")) {
					ArrayList<Block> blocks = peerModel.blockchainModel.getBlocks();
					int blockNum = jsonObject.getInt("blockNum");
					String blockHash = jsonObject.getString("blockHash");
					int startIndex =0;
					boolean requestVerified = false;
					
					System.out.println("��û ���ѹ� : " + blockNum);
					System.out.println("��û ���ؽ� : " + blockHash);
					System.out.println("�� ���ؽ� : " + blocks.get(blockNum).getHash());
					
					//���� �ܰ� : �ؽ��� ��ġ�ϸ� ���� ������ ���� ����, ��ġ���� ������ ���׽ý����� ���� ����
					if(blocks.get(blockNum).getHash().equals(blockHash)) {
						System.out.println("��û���ؽÿ� �� ���ؽð� ���� ����");
						startIndex = blockNum+1;
						requestVerified = true;
					}
					
					for(int i = startIndex; i<blocks.size();i++) {
							System.out.println("requestVerfied"+i+" : "+ requestVerified);
							StringWriter sw = new StringWriter();
							Json.createWriter(sw).writeObject(Json.createObjectBuilder()
												.add("requestVerified",requestVerified)
												.add("RpreviousHash",blocks.get(i).getPreviousBlockHash())
												.add("Rnonce",blocks.get(i).getNonce())
												.add("Rtimestamp", blocks.get(i).getTimestamp())
												.build());
							printWriter.println(sw.toString());
							
							Thread.sleep(500);
					}
				}
				//�ڽ��� �������� �����ϴ� flag
				if(jsonObject.containsKey("leader")) {
						peerModel.amILeader = true;
				}
				
				//��û�� UTXO �ޱ�
				if(jsonObject.containsKey("responseUTXO")) {
					System.out.println("��� �� �޾���");
					float value = Float.parseFloat(jsonObject.getString("value"));  
					TransactionOutput UTXO = new TransactionOutput(peerModel.walletModel.getPublicKey(),value);
					peerModel.walletModel.getUTXOWallet().add(UTXO);
				}
				
				
			}
			// ���� ������ ������ �� ����
		} catch (Exception e) {
			try {
				flag = false;
				serverListener.getServerThreadThreads().remove(this);
				socket.close();
			} catch (IOException e1) {e1.printStackTrace();}	
		}
	}
	
	public PrintWriter getPrintWriter() { return printWriter;}
	public String toString() { return socket.getInetAddress().getHostAddress()+":"+socket.getPort();}
}
