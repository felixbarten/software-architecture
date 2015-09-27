package nl.uva.se.funcpipes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBCreation {
	private static DBCreation instance = null ;
    private String url; 				
    private String driver; 		
    private String userName; 	 
    private String password; 	
    private String dbName; 		
    
    private DBCreation() {								
    	fixDbProperties();
    }
    
    public static synchronized DBCreation getInstance() {
		if(instance == null)
			instance = new DBCreation();
		return instance;
	}
	
    /*** returns a connection with mysql ***/
    
    public Connection connectWithMysql() {
    	Connection conn;
    	try {
    		Class.forName(driver).newInstance();
    		conn = DriverManager.getConnection(url, userName, password);
    	}
    	catch (Exception e){
    		System.out.println("***Cannot connect with mysql***");
    		//e.printStackTrace();
    		conn=null;
    	}
    	return conn;
    }
    
    /*** Returns a connection with POIDB ***/
    
    public Connection connect() {
    	Connection conn;
    	try {
    		Class.forName(driver).newInstance();
    		conn = DriverManager.getConnection(url+dbName, userName, password);
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		conn=null;
    	}
    	return conn;
    }
 
    public static void createPOIDB() {
    	DBCreation sql = DBCreation.getInstance();
        Connection conn;
    	PreparedStatement st;
    	conn = sql.connectWithMysql();

    	try {
       		st = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS POIDB");
    		st.executeUpdate();	
    	}
    	catch (SQLException e) {
    		System.out.println("Error while creating POIDB");
    		return;
    	}
    	finally {
    		try { 
    			conn.close(); 
    		}
    		catch (SQLException e) { 
        		e.printStackTrace(); 
    		}    		
    	}
    	
    	DBCreation.createPOITables();
    }
    
    
    /****** location_points not needed . . . ? *****/
    
    
    public static void createPOITables() {
        DBCreation sql = DBCreation.getInstance();
        Connection conn;
        PreparedStatement st;
        conn = sql.connect();
        
        try {
        	
        	System.out.println("Checking 'users' table");                                    
            st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users (username VARCHAR(30) NOT NULL PRIMARY KEY, password VARCHAR(30) NOT NULL, stayPointRequest TIMESTAMP, stayRegionRequest TIMESTAMP)");
            st.executeUpdate();
            st.clearParameters();

             /****************** insert admin's account into database ******************************/
            st = conn.prepareStatement("insert into users (username, password) values (?, ?)");
            st.setString(1, "admin");
            st.setString(2, "1234");
            st.executeUpdate();
            st.clearParameters();
            
          /*  System.out.println("Checking 'location_points' table");                                    
            st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS location_points (latitude DOUBLE NOT NULL, longitude DOUBLE NOT NULL, username VARCHAR(30) NOT NULL, locationPointTimestamp VARCHAR(50),"
            		+ "FOREIGN KEY(username) REFERENCES users(username))");
            st.executeUpdate();
            st.clearParameters(); */
            
            System.out.println("Checking 'stay_points' table");                                    
            st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS stay_points (latitude DOUBLE NOT NULL, longitude DOUBLE NOT NULL, time_start TIMESTAMP NOT NULL,time_end TIMESTAMP NOT NULL, username VARCHAR(30) NOT NULL, stayPointTimestamp TIMESTAMP NOT NULL,"
            		+ "FOREIGN KEY(username) REFERENCES users(username))");
            st.executeUpdate();
            st.clearParameters();
            
            System.out.println("Checking 'stay_regions' table");                                  
            st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS stay_regions (cen_latitude DOUBLE NOT NULL, cen_longitude DOUBLE NOT NULL,"
            		+ "min_latitude DOUBLE NOT NULL, max_latitude DOUBLE NOT NULL,"
            		+ "min_longitude DOUBLE NOT NULL, max_longitude DOUBLE NOT NULL, stayRegionTimestamp TIMESTAMP)");
            st.executeUpdate();
            st.clearParameters();
                        	
            
        }
        catch (SQLException e) {
            System.out.println("Error while creating tables!");
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
        		System.out.println("** Mysql connection closed **");
            }
            catch (SQLException se) {
               se.printStackTrace();
            }
        }

    }
    
    private void fixDbProperties() {
    	File propertiesFile = new File("dbConfig.properties");
		if(!propertiesFile.exists()) {
			try {
				propertiesFile.createNewFile();
				FileWriter buffer = new FileWriter("dbConfig.properties");
		    	this.url = "jdbc:mysql://localhost:3306/";
				buffer.write("URL=" + this.url + "\n");
				this.driver = "com.mysql.jdbc.Driver";
				buffer.write("DRIVER=" + this.driver + "\n");
				this.userName = "root";
				buffer.write("USERNAME=" + this.userName + "\n");
				this.password = "1234";
				buffer.write("PASSWORD=" + this.password + "\n");
				this.dbName = "employeeDB";
				buffer.write("DBNAME=" + this.dbName + "\n");
				buffer.close();
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(propertiesFile));
				this.url = properties.getProperty("URL");
				this.driver = properties.getProperty("DRIVER");
				this.userName = properties.getProperty("USERNAME");
				this.password = properties.getProperty("PASSWORD");
				this.dbName = properties.getProperty("DBNAME");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
		}
    } 
}



