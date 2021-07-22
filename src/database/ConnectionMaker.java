package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMaker {
	
	public Connection getConnection() {
		try {
			String url = "jdbc:mysql://localhost:3306/BLOCKCHAIN";
			String id = "root";
			String pw = "325264";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url,id,pw);
			
			return connection;

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 확보 실패");
		} catch (SQLException e) {
			System.out.println("커넥션 확보 실패");
		}
		return null;
	}

}
