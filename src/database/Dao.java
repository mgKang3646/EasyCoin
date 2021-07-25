package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Peer;

public class Dao {
	
	// 관심사 : UserName 중복 체크
	public int checkDuplicateUserName(String userName) {
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
				return 0; // 중복된 userName이 존재
			}else {
				return 1; // 중복이 안 됨.
			}
		} catch (SQLException e) {
			System.out.println("UserName 중복 체크 SQL문 실행 중 오류 발생");
			return -1; // SQL문 실행 중 문제 발생
		}	
	}
	
	// 관심사 : 회원가입 정보 DB 저장
	public int join(String localhost, String userName){
		
		int result=0;
		
		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "INSERT INTO PEERTABLE VALUES (?,?)";
			
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, localhost);
			pstmt.setString(2, userName);
			
			result = pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			System.out.println("회원가입 정보 DB 저장 중 SQL 실행 오류 발생");
			return result;
		}
	}
	
	// 관심사 : Peer 데이터 갖고 오기
	public Peer getPeer(String userName) {
		Peer peer = new Peer();
		
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
				
				peer.setUserName(username);
				peer.setLocalhost(localhost);
			}
			
			return peer;
			
		} catch (SQLException e) {
			System.out.println("로그인 시, Peer 객체 정보 추출 중 SQL 실행 오류 발생");
			return null;
		}
	}
	
	// 관심사 : DB에 저장된 전체 PEER 갖고 오기
	public ArrayList<Peer> getPeers() {
		ArrayList<Peer> peers = new ArrayList<Peer>();

		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Peer peer = new Peer();
				peer.setLocalhost(rs.getString("localhost"));
				peer.setUserName(rs.getString("username"));
				peers.add(peer);
			}
			return peers;
		} catch (SQLException e) {
			System.out.println("DB에 저장된 전체 Peer 추출 중 SQL 실행 오류 발생");
			return null;
		}
		
	}
	
	
	
	

}
