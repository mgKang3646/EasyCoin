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
				// PeerThread 연결 요청이 들어온 경우
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
					
					System.out.println("연결 완료");
					System.out.println(peerModel.peerList.get(peerModel.peerList.size()-1).toString());
					System.out.println("리더여부 : "+ peerModel.amILeader);
					
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
				
				// 블럭 제공 요청이 들어온 경우
				if(jsonObject.containsKey("blockNum")) {
					ArrayList<Block> blocks = peerModel.blockchainModel.getBlocks();
					int blockNum = Integer.parseInt(jsonObject.getString("blockNum"));
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
				
				//채굴 후 합의 결과 리더임을 알리는 json 메세지
				if(jsonObject.containsKey("miningLeader")) {
						peerModel.amILeader = true;
						peerModel.initializeLeader();
				}
				
				// 새로 P2P에 참여한 PEER가 자신보다 블럭개수가 많은 경우 리더지위 내려놓기
				if(jsonObject.containsKey("biggerThanYou")) {					
					for(Peer peer : peerModel.peerList) {
						if(jsonObject.getString("username").equals(peer.getUserName())){
							peer.setLeader(true);
							peerModel.amILeader = false;
							System.out.println(peer.getUserName()+"이 리더로 선출(biggerThanYou)");
						}
						peer.setLeader(false); // 리더가 아닌 친구들은 fasle로 설정
					}	
				}
				
				//요청한 UTXO 받기
				if(jsonObject.containsKey("responseUTXO")) {
					System.out.println("[잔액] 결과 잘 받았음");
					float value = Float.parseFloat(jsonObject.getString("value"));
					int nonce = Integer.parseInt(jsonObject.getString("nonce"));
					String miner = jsonObject.getString("miner");
					String utxoHash = jsonObject.getString("utxoHash");
					PublicKey recipient = peerModel.walletModel.getPublicKey();
					String recipientEncoding = Base64.toBase64String(recipient.getEncoded());
					
					//UTXO 검증
					if(utxoHash.equals(DigestUtils.sha256Hex(miner+recipientEncoding+value+nonce))) {
						TransactionOutput UTXO = new TransactionOutput(recipient,value,miner);
						UTXO.setHash(utxoHash);
						UTXO.setNonce(nonce);
						peerModel.walletModel.getUTXOWallet().add(UTXO);
						System.out.println("[잔액] UTXO 검증 및 지갑에 저장 완료");
					}	
				}
				
				//기록 : 향상된 for문에서 list 자료구조를 사용할 때, for문안에서 remove() 실행하는 것은 오류를 발생시킨다. (이유는 모르겠음, 알아보기)
				if(jsonObject.containsKey("deleteUTXO")) {
					String utxoHash = jsonObject.getString("deleteUTXO");
					TransactionOutput UTXO=null;
					for(TransactionOutput tempUTXO : peerModel.UTXOs) {
						if(utxoHash.equals(tempUTXO.getTxoHash())) {
							UTXO = tempUTXO;
							break;
						}
					}
					System.out.println("UTXO 소지 측 : UTXO 삭제 완료");
					peerModel.UTXOs.remove(UTXO);
				}
				
			}
			// 상대방 연결이 끊겼을 시 대응
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
