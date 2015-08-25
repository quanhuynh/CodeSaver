package data;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import utils.Constants;
import obj.Category;
import obj.Snippet;

public class DBMS {
	//Static instance of DBMS
	private static DBMS dbms = new DBMS();
	
	/**
	 * Constructor for DMBS
	 */
	public DBMS() {
		initialize();
	}
	
	/**
	 * Get the instance of DBMS
	 * @return DBMS object
	 */
	public static DBMS getInstance() {
		return dbms;
	}
	
	/**
	 * Initialize the DMBS
	 * Registers the driver
	 * Create the snippets table
	 */
	public void initialize() {
		Constants.setProperties();

		//Register driver
		try {
			Class.forName(Constants.DATABASE_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//Create snippets table if it doesn't already exist
		if (!tableExists()) {
			createTable();
		}
	}
	
	/**
	 * Establish a connection to the MySQL database
	 * @return Connection object
	 */
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(Constants.DATABASE_URL, Constants.USER_INFO);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * Check if a table exists
	 * @return true if the table exists
	 */
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
	
	/**
	 * Create the snippets table
	 */
	public void createTable() {
		Statement stm;
		Connection conn;
		try {
			conn = getConnection();
			stm = conn.createStatement();
			
			stm.execute("CREATE TABLE Snippets ("
					+ "SnippetID INT NOT NULL AUTO_INCREMENT,"
					+ "Name VARCHAR(50) NOT NULL,"
					+ "Category VARCHAR(50) NOT NULL,"
					+ "Code VARCHAR(1500) NOT NULL,"
					+ "Comment VARCHAR(500),"
					+ "Syntax VARCHAR(50),"
					+ "PRIMARY KEY (SnippetID))");
			conn.close();
		} catch (SQLException e) {
			
		}
	}
	
	/**
	 * Add a snippet to the table
	 * @param s Snippet object
	 * @return true if snippet was created successfully
	 */
	public boolean addSnippet(Snippet s) {
		PreparedStatement stm;
		Connection conn;
		if (snippetExists(s.name(), s.category())) {
			return changeSnippet(getSnippetInstance(s.name()), s);
		}
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
	
	public boolean snippetExists(String name, String category) {
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String checkSQL = "SELECT Name FROM Snippets WHERE Name = ? AND Category = ? ";
			stm = conn.prepareStatement(checkSQL);
			stm.setString(1, name);
			stm.setString(2, category);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Change a snippet's information
	 * @param oldS old Snippet object
	 * @param newS new Snippet object
	 * @return true if Snippet was changed
	 */
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
	
	/**
	 * Delete a snippet from the table
	 * @param s Snippet object
	 * @return true if snippet was deleted successfully
	 */
	public boolean deleteSnippet(Snippet s) {
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String deleteSQL = "DELETE FROM Snippets WHERE Name = ? AND Category = ? ";
			stm = conn.prepareStatement(deleteSQL);
			stm.setString(1, s.name());
			stm.setString(2, s.category());
			stm.executeUpdate();
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public Set<String> getAllCategories() {
		Set<String> categories = new HashSet<String>();
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String getAllCatsSQL = "SELECT Category FROM Snippets";
			stm = conn.prepareStatement(getAllCatsSQL);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String sCat = rs.getString("Category");
				categories.add(sCat);
			}
			return categories;
		} catch (SQLException e) {
			return categories;
		}
	}
	
	public Set<Snippet> getAllSnippets() {
		Set<Snippet> snippets = new HashSet<Snippet>();
		PreparedStatement stm;
		Connection conn;
		try {
			conn = getConnection();
			String getAllSQL = "SELECT Name, Category FROM Snippets";
			stm = conn.prepareStatement(getAllSQL);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String sName = rs.getString("Name");
				Snippet s = getSnippetInstance(sName);
				snippets.add(s);
			}
			return snippets;
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * Get a Snippet object from the table 
	 * @param name Name of Snippet
	 * @return Snippet object
	 */
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
