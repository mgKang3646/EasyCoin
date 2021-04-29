package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import model.Block;
import model.WalletModel;

public class DAO {
	
	// 가입 시 DB 접근
	public int join(String userLocalHost, String username) {
		String SQL = "INSERT INTO PEERTABLE VALUES(?,?)";
		
		try {
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userLocalHost);
			pstmt.setString(2, username);

			return pstmt.executeUpdate(); // SQL를 실행해서 성공여부를 반환 데이터 삽입이 성공한 개수를 반환
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("DAO DB SQL 삽입과정에서 문제 발생");
			e.printStackTrace();
		}
		
		return -1; // 오류가 발생하는 경우
	}
	
	// P2P 네트워크 연동을 위한 DB 안 host 주소 
	public ArrayList<DTO> getPeers() {
		ArrayList<DTO> peers = new ArrayList<DTO>(); // DB안에 있는 PEER들 주소를 담을 리스트

		try {
			
			String SQL = "SELECT * From PEERTABLE";
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) { // ResultSet은 일반적인 set가 조금 다르다.
				DTO dto = new DTO();
				dto.setLocalhost(rs.getString("localhost"));
				dto.setUsername(rs.getString("username"));
				peers.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return peers;
	}
	
	// 특정 개인키에 해당하는 데이터 갖고 오기
	public WalletModel getPeer(String username){
		WalletModel walletModel = new WalletModel();
		try {
			
			String SQL = "SELECT * FROM PEERTABLE WHERE username='"+username+"';";
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) { // ResultSet은 일반적인 set가 조금 다르다.
				walletModel.setUserLocalHost(rs.getString("localhost"));
				walletModel.setUsername(rs.getString("username"));
			}
			
			return walletModel;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//호스트 주소 중복체크
	public int registerCheck(String username) {
		
		try {
			String SQL ="SELECT * FROM PEERTABLE WHERE username = ?";
			
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return 1; // 중복되는 값이 있음
			}else {
				return 0; // 중복되는 값이 없음
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("중복체크 SQL 삽입 오류 발생!");
			e.printStackTrace();
		}
		
		return -1; // 에러 발생
	}
	
	public int storeBlock(Block block, String username) {
		String SQL = "INSERT INTO BLOCKTABLE VALUES( ?, ?,?, ?, ?, ?,?)";
		
		try {
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, block.getNum()+"");
			pstmt.setString(2, block.getNonce());
			pstmt.setString(3, block.getTimestamp());
			pstmt.setString(4, "거래정보");
			pstmt.setString(5, block.getPreviousBlockHash());
			pstmt.setString(6, block.getHash());
			pstmt.setString(7, username);
			
			int result = pstmt.executeUpdate();
			return result; // 영향을 끼친 튜플 수를 반환
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("DB 블럭 삽입 오류!");
			e.printStackTrace();
		}
		return -1; // DB 오류 발생 시
	}
	
	public int deleteAllBlock(String username) {
		String SQL = "DELETE FROM BLOCKTABLE WHERE username = ?";
		
		
		try {
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, username);
			
			int result = pstmt.executeUpdate();
			System.out.println(result +"개 DB에서 삭제");
			
			return result;
		} catch (SQLException e) {
			System.out.println("블럭 삭제 과정 중 SQL 삽입 오류 발생");
		}
		
		return -1; //DB 에러 
	}
	
	public LinkedList<Block> getBlocks(String username) {
		String SQL = "SELECT * FROM BLOCKTABLE WHERE username = ?";
		LinkedList<Block> blocks = new LinkedList<Block>(); // 중간 삽입이 자주 일어날 것이므로 링크드리스트를 써준다.
		Block block=null;
		
		try {
			
			Connection conn = util.DatabaseUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			// 리스트 정렬하기 
			while(rs.next()) {
				block = new Block(rs.getString("previoushash"),rs.getString("nonce"),rs.getString("timestamp"),Integer.valueOf(rs.getString("num")));
				if(blocks.size()==0) {
					blocks.add(block);
				}
				
				else {
					for(int i = 0; i< blocks.size(); i++) {
						if(block.getNum() < blocks.get(i).getNum()) {
							blocks.add(i,block);
							break;
						}
						if(i==(blocks.size()-1)) {
							blocks.add(block);
							break; // 왜 브레이크를 안하면 무한 루프를 도는가?
						}
					}
				}
			}
			
			return blocks;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("DB에서 블럭 가져오는 중 오류 발생!");
			e.printStackTrace();
		}
		
		return null; // db 오류 
		
		
	}
	
	

}
