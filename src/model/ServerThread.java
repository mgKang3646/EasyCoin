package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;

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
				// PeerThread 연결 요청이 들어온 경우
				if(jsonObject.containsKey("localhost")) {
					System.out.println("연결 요청 : "	+ jsonObject.toString());
					String hostAddress = jsonObject.getString("localhost");
					String[] address = hostAddress.split(":");
					
					Socket newSocket = new Socket(InetAddress.getByName(address[0]),Integer.valueOf(address[1]));
					//////////////////////////////////////////blocking///////////////////////////////////////////
					
					PeerThread peerThread = new PeerThread(newSocket,peerModel);
					peerThread.start();
					
					//연결 요청이 들어온 Peer에게 자신이 리더임을 알리기
					if(peerModel.amILeader) {
						StringWriter sw = new StringWriter();
						Json.createWriter(sw).writeObject(Json.createObjectBuilder()
														.add("leader",true)
														.add("leaderBlockNum", Block.count)
														.build());
						printWriter.println(sw);
					}
					
				}
				// 새로 P2P에 참여한 PEER가 자신보다 블럭개수가 많은 경우 리더지위 내려놓기
				if(jsonObject.containsKey("biggerThanYou")) {
					System.out.println("너가 나보다 더 크구나!");
					peerModel.amILeader = false;
				}
				// 블럭 제공 요청이 들어온 경우
				if(jsonObject.containsKey("blockNum")) {
					ArrayList<Block> blocks = peerModel.blockchainModel.getBlocks();
					int blockNum = jsonObject.getInt("blockNum");
					String blockHash = jsonObject.getString("blockHash");
					int startIndex =0;
					boolean requestVerified = false;
					
					System.out.println("요청 블럭넘버 : " + blockNum);
					System.out.println("요청 블럭해시 : " + blockHash);
					System.out.println("비교 블럭해시 : " + blocks.get(blockNum).getHash());
					
					//검증 단계 : 해쉬가 일치하면 다음 블럭부터 블럭을 제공, 일치하지 않으면 제네시스부터 새로 제공
					if(blocks.get(blockNum).getHash().equals(blockHash)) {
						System.out.println("요청블럭해시와 비교 블럭해시가 서로 같음");
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
				//자신이 리더임을 인정하는 flag
				if(jsonObject.containsKey("leader")) {
						peerModel.amILeader = true;
				}
				
				
			}
			// 
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			serverListener.getServerThreadThreads().remove(this);
			interrupt();
		}
	}
	
	public PrintWriter getPrintWriter() { return printWriter;}
	public String toString() { return socket.getInetAddress().getHostAddress()+":"+socket.getPort();}
}
