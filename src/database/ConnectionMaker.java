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
			System.out.println("����̹� Ȯ�� ����");
		} catch (SQLException e) {
			System.out.println("Ŀ�ؼ� Ȯ�� ����");
		}
		return null;
	}

}
