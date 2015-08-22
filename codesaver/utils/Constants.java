package utils;

import java.awt.Color;
import java.util.Properties;

public class Constants {
	public static final Properties USER_INFO = new Properties();
	public static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver"; 
	public static final String DATABASE_URL = "jdbc:mysql://localhost/mysql";
	public static final String APP_NAME = "CodeSaver";
	public static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);
	
	
	public static void setProperties() {
		USER_INFO.setProperty("user", "root");
		USER_INFO.setProperty("password", "password");
	}

}
