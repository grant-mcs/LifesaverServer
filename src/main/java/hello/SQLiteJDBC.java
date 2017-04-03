package hello;

import java.sql.*;

public class SQLiteJDBC
{
  public Connection connectToDB()
  {
    Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:lifesaver.db");
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    }
    System.out.println("Opened database successfully");
    return c;
  }

  public int executeSQL(String sqlString)
  {
	  Connection c = connectToDB();
	  Statement stmt = null;
	  int executeValue = 0;
	  try {
	      stmt = c.createStatement();
	      executeValue = stmt.executeUpdate(sqlString);
	      stmt.close();
	      c.close();
	  } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() + ": " + sqlString );
	  }
	  return executeValue;
  }

  public void createTables() 
  {
	  String userSQL = "(ID INTEGER PRIMARY KEY," +
			  		   " NAME           TEXT    NOT NULL," + 
			  		   " PHONE          CHAR(50))";
	  createTable("USER", userSQL);
	  
	  String requestSQL = "(ID INTEGER PRIMARY KEY," +
			  			   "USER_ID INTEGER," +
			  			   "NOTIFY_RADIUS INTEGER," +
			  			   "CALL_911 BOOL," +
			  			   "EMERGENCY_REASON INTEGER," +
			  			   "OTHER_INFO TEXT," +
			  			   "TIMESTAMP DOUBLE," +
			  			   "LATITUDE FLOAT," +
			  			   "LONGITUDE FLOAT," +
			  			   "FOREIGN KEY(USER_ID) REFERENCES USER(ID))";
	  createTable("REQUEST", requestSQL);
  }

  public void createTable(String tableName, String columnSQL)
  {
	  String sql = "CREATE TABLE IF NOT EXISTS " + tableName + columnSQL;
	  executeSQL(sql);
	  System.out.println("Table created successfully");
  }

  public int addUser(String userName, String phoneNumber)
  {
	  Connection c = connectToDB();
	  PreparedStatement stmt = null;
	  int executeValue = 0;
	  try {
	      stmt = c.prepareStatement("INSERT INTO USER (NAME, PHONE) VALUES (?, ?)");
	      stmt.setString(1, userName);
	      stmt.setString(2, phoneNumber);
	      
	      executeValue = stmt.executeUpdate();
	      stmt.close();
	      c.close();
	  } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	  }
	  System.out.println("User inserted successfully");
	  return executeValue;
  }

  public int addRequest(int userId, int notifyRadius, boolean call911, int emergencyReason,
		  				String otherInfo, double timestamp, float latitude, float longitude)
  {
	  Connection c = connectToDB();
	  PreparedStatement stmt = null;
	  int executeValue = 0;
	  try {
	      stmt = c.prepareStatement("INSERT INTO REQUEST (USER_ID, NOTIFY_RADIUS, " +
	    		  "CALL_911, EMERGENCY_REASON, OTHER_INFO, TIMESTAMP, LATITUDE, " +
	    		  "LONGITUDE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
	      stmt.setInt(1, userId);
	      stmt.setInt(2, notifyRadius);
	      stmt.setBoolean(3, call911);
	      stmt.setInt(4, emergencyReason);
	      stmt.setString(5, otherInfo);
	      stmt.setDouble(6, timestamp);
	      stmt.setFloat(7, latitude);
	      stmt.setFloat(8, longitude);
	      
	      executeValue = stmt.executeUpdate();
	      stmt.close();
	      c.close();
	  } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	  }
	  System.out.println("Request inserted successfully");
	  return executeValue;
  }
}