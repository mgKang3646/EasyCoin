package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	public ArrayList<Peer> getPeers(String userName) {
		ArrayList<Peer> peers = new ArrayList<Peer>();

		try {
			ConnectionMaker connectionMaker = new ConnectionMaker();
			Connection conn = connectionMaker.getConnection();
			
			String sql = "SELECT * FROM PEERTABLE";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("username").equals(userName)) continue; // 자기 자신은 제외
				
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
