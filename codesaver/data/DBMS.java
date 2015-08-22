package data;
import java.sql.*;

import utils.Constants;
import obj.Snippet;

public class DBMS {
	private static DBMS dbms = new DBMS();
	
	public DBMS() {
		initialize();
	}
	
	public static DBMS getInstance() {
		return dbms;
	}
	
	public void initialize() {
		Constants.setProperties();

		//Register driver
		try {
			Class.forName(Constants.DATABASE_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Database driver not found.");
			e.printStackTrace();
			System.exit(1);
		}
		
		//Create snippets table if it doesn't already exist
		if (!tableExists()) {
			createTable();
		}
	}
	
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(Constants.DATABASE_URL, Constants.USER_INFO);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public boolean tableExists() {
		Connection conn;
		try {
			conn = getConnection();
			DatabaseMetaData dmd = conn.getMetaData();
			ResultSet rs;
			rs = dmd.getTables(null, null, "Snippets", null);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void createTable() {
		Statement stm;
		Connection conn;
		try {
			conn = getConnection();
			stm = conn.createStatement();
			
			stm.execute("CREATE TABLE Snippets ("
					+ "SnippetID INT NOT NULL AUTO_INCREMENT,"
					+ "Name VARCHAR(50) UNIQUE NOT NULL,"
					+ "Category VARCHAR(50) NOT NULL,"
					+ "Code VARCHAR(1500) NOT NULL,"
					+ "Comment VARCHAR(500),"
					+ "Syntax VARCHAR(50),"
					+ "PRIMARY KEY (SnippetID))");
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot create table. Table may already exists.");
		}
	}
	
	public boolean addSnippet(Snippet s) {
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String insertSQL = "INSERT INTO snippets(Name, Category, Code, Comment, Syntax) " 
							+ "VALUES (?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(insertSQL);
			stm.setString(1, s.name());
			stm.setString(2, s.category());
			stm.setString(3, s.code());
			stm.setString(4, s.comment());
			stm.setString(5, s.syntax());
			stm.executeUpdate();
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean changeSnippet(Snippet oldS, Snippet newS) {
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String updateSQL = "UPDATE Snippets SET "
							+ "Name = ? ,"
							+ "Category = ? ,"
							+ "Code = ? ," 
							+ "Comment = ? ,"
							+ "Syntax = ? ,"
							+ "WHERE Name = ? ";
			stm = conn.prepareStatement(updateSQL);
			stm.setString(1, newS.name());
			stm.setString(2, newS.category());
			stm.setString(3, newS.code());
			stm.setString(4, newS.comment());
			stm.setString(5, newS.syntax());
			stm.setString(6, oldS.name());
			stm.executeUpdate();
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean deleteSnippet(Snippet s) {
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String deleteSQL = "DELETE FROM Snippets WHERE Name = ? ";
			stm = conn.prepareStatement(deleteSQL);
			stm.setString(1, s.name());
			stm.executeUpdate();
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public Snippet getSnippetInstance(String name) {
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String getRowQuery = "SELECT Name, Category, Code, Comment, Syntax "
						+ "FROM Snippets WHERE Name = ? ";
			stm = conn.prepareStatement(getRowQuery);
			stm.setString(1, name);
			ResultSet rs = stm.executeQuery();
			String sName = null, sCat = null, sCode = null, sComment = null, syntax = null;
			while (rs.next()) {
				sName = rs.getString("Name");
				sCat = rs.getString("Category");
				sCode = rs.getString("Code");
				sComment = rs.getString("Comment");
				syntax = rs.getString("Syntax");
			}
			conn.close();
			return new Snippet(sName, sCat, sCode, sComment, syntax);
			
		} catch (SQLException e) {
			return null;
		}
	}
	
}