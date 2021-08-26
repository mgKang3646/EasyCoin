package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Block;
import model.BlockMaker;
import model.OtherPeer;
import model.Peer;

public class Dao {
	
	public boolean isUserNameExisted(String userName) {
		try {
			// 관심사 : 커넥션 객체 생성
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE WHERE username = ?";
			
			// 관심사 : SQL문 실행 객체 생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userName);
			
			// 관심사 : userName 중복 체크
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return true; // 중복된 userName이 존재
			}else {
				return false; // 중복이 안 됨.
			}
		} catch (SQLException e) {
			System.out.println("UserName 중복 체크 SQL문 실행 중 오류 발생");
			return false; // SQL문 실행 중 문제 발생
		}	
	}
	
	// 관심사 : 회원가입 정보 DB 저장
	public boolean join(String localhost, String userName){
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "INSERT INTO PEERTABLE VALUES (?,?)";
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, localhost);
			pstmt.setString(2, userName);
			if(pstmt.executeUpdate() > 0 ) return true;
			else return false;
			
		} catch (SQLException e) {
			System.out.println("회원가입 정보 DB 저장 중 SQL 실행 오류 발생");
			return false;
		}
	}
	
	// 관심사 : Peer 데이터 갖고 오기
	public PeerDto getPeer(String userName) {
		PeerDto dto = new PeerDto();
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE WHERE username = ?";
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String username = rs.getString("username");
				String localhost = rs.getString("localhost");
				
				dto.setUserName(username);
				dto.setLocalhost(localhost);
			}
			return dto;
			
		} catch (SQLException e) {
			System.out.println("로그인 시, Peer 객체 정보 추출 중 SQL 실행 오류 발생");
			return null;
		}
	}
	
	// 관심사 : DB에 저장된 전체 PEER 갖고 오기
	public ArrayList<OtherPeer> getPeers(String userName) {
		ArrayList<OtherPeer> otherPeers = new ArrayList<OtherPeer>();

		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("username").equals(userName)) continue; // 자기 자신은 제외
				
				OtherPeer otherPeer = new OtherPeer();
				otherPeer.setLocalhost(rs.getString("localhost"));
				otherPeer.setUserName(rs.getString("username"));
				otherPeers.add(otherPeer);
			}
			return otherPeers;
		} catch (SQLException e) {
			System.out.println("DB에 저장된 전체 Peer 추출 중 SQL 실행 오류 발생");
			return null;
		}
	}
	
	public void storeBlock(Block block) {
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String SQL = "INSERT INTO BLOCKTABLE VALUES( ?, ?,?, ?, ?, ?,?)";
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, block.getNum()+"");
			pstmt.setString(2, block.getNonce()+"");
			pstmt.setString(3, block.getTimestamp());
			pstmt.setString(4, "거래정보");
			pstmt.setString(5, block.getPreviousBlockHash());
			pstmt.setString(6, block.getHash());
			pstmt.setString(7, Peer.myPeer.getUserName());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("DB 블럭 삽입 오류!");
			e.printStackTrace();
		}
	}
	
	public ArrayList<Block> getBlocks() {
		String SQL = "SELECT * FROM BLOCKTABLE WHERE username = ?";
		ArrayList<Block> blocks = new ArrayList<Block>();
		BlockMaker blockMaker = new BlockMaker();
		Block block;
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, Peer.myPeer.getUserName());
			blocks.sort(null);
			ResultSet rs = pstmt.executeQuery();
			// 리스트 정렬하기 
			while(rs.next()) {
				block = blockMaker.makeDBBlock(rs);
				blocks.add(block);
			}
			return blocks;
		} catch (SQLException e) {
			System.out.println("DB에서 블럭 가져오는 중 오류 발생!");
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteAllBlock(String username) {
		String SQL = "DELETE FROM BLOCKTABLE WHERE username = ?";
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, username);
			int result = pstmt.executeUpdate();
			System.out.println("DB에서 블럭 " + result+"개 삭제");
			
		} catch (SQLException e) {
			System.out.println("블럭 삭제 과정 중 SQL 삽입 오류 발생");
		}
	}
}
