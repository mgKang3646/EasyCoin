package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

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
					peerThread.setPeer(new Peer(username, hostAddress,peerThread));
					peerModel.peerList.add(peerThread.getPeer());
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
				
				// �� ���� ��û�� ���� ���
				if(jsonObject.containsKey("blockNum")) {
					ArrayList<Block> blocks = peerModel.blockchainModel.getBlocks();
					int blockNum = Integer.parseInt(jsonObject.getString("blockNum"));
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
				
				//ä�� �� ���� ��� �������� �˸��� json �޼���
				if(jsonObject.containsKey("miningLeader")) {
						peerModel.amILeader = true;
						peerModel.initializeLeader();
				}
				
				// ���� P2P�� ������ PEER�� �ڽź��� �������� ���� ��� �������� ��������
				if(jsonObject.containsKey("biggerThanYou")) {					
					for(Peer peer : peerModel.peerList) {
						if(jsonObject.getString("username").equals(peer.getUserName())){
							peer.setLeader(true);
							peerModel.amILeader = false;
							System.out.println(peer.getUserName()+"�� ������ ����(biggerThanYou)");
						}
						peer.setLeader(false); // ������ �ƴ� ģ������ fasle�� ����
					}	
				}
				
				//��û�� UTXO �ޱ�
				if(jsonObject.containsKey("responseUTXO")) {
					System.out.println("��� �� �޾���");
					float value = Float.parseFloat(jsonObject.getString("value"));
					double nonce = Double.parseDouble(jsonObject.getString("nonce"));
					String miner = jsonObject.getString("miner");
					String utxoHash = jsonObject.getString("utxoHash");
					PublicKey recipient = peerModel.walletModel.getPublicKey();
					
					//UTXO ����
					if(utxoHash.equals(DigestUtils.sha256(value+nonce+miner+recipient))) {
						TransactionOutput UTXO = new TransactionOutput(recipient,value,miner);
						UTXO.setHash(utxoHash);
						UTXO.setNonce(nonce);
						peerModel.walletModel.getUTXOWallet().add(UTXO);
					}	
				}
				
				if(jsonObject.containsKey("getUTXO")) {
					for(TransactionOutput UTXO : peerModel.UTXOs) {
						if(UTXO.getTxoHash().equals(jsonObject.getString("utxoHash"))) {
							System.out.println("UTXO ���� �� : UTXO �۽� ��û �� ����");
							HashMap<String,String> additems = new HashMap<String,String>();
							additems.put("sendUTXO", UTXO.getTxoHash());
							additems.put("nonce",UTXO.getNonce()+"");
							additems.put("miner", UTXO.getMiner());
							additems.put("recipient",Base64.toBase64String(UTXO.getRecipient().getEncoded()));
							additems.put("value", UTXO.value+"");
							additems.put("txHash", jsonObject.getString("txHash"));
							getPrintWriter().println(peerModel.makeJsonObject(additems)); // �۽����� �ٽ� ��� ������
							System.out.println("UTXO ���� �� : UTXO �۽� �Ϸ�");
						}
					}
				}
				
				if(jsonObject.containsKey("deleteUTXO")) {
					String utxoHash = jsonObject.getString("deleteUTXO");
					for(TransactionOutput UTXO : peerModel.UTXOs) {
						if(utxoHash.equals(UTXO.getTxoHash())) {
							peerModel.UTXOs.remove(UTXO);
							System.out.println("UTXO ���� �� : UTXO ���� �Ϸ�");
						}
					}
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
