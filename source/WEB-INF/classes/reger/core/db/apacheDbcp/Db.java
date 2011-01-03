package reger.core.db.apacheDbcp;

import org.apache.commons.dbcp.*;
import javax.sql.*;
import java.sql.*;
import java.util.Vector;
import reger.core.Debug;
import reger.core.db.DbConfig;

public class Db {

  public static final int defaultRecordstoreturn=50000;
  private static DataSource ds;


    public static void setupDataSource(){
        System.out.println("Reger.com: Start ds setup.");
        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(DbConfig.getDbDriverName());
        ds.setUsername(DbConfig.getDbUsername());
        ds.setPassword(DbConfig.getDbPassword());
        ds.setUrl(DbConfig.getDbConnectionUrl());
        ds.setMaxActive(DbConfig.getDbMaxActive());
        ds.setMaxIdle(DbConfig.getDbMaxIdle());
        ds.setMaxWait(DbConfig.getDbMaxWait());
        ds.setMinIdle(DbConfig.getDbMinIdle());

        Db.ds = ds;
        System.out.println("Reger.com: End ds setup.");
    }

  /**
   * Get a connection object.
   */
  public static Connection getConnection(){
        try{
            if (ds==null || DbConfig.haveNewConfigToTest()){
                System.out.println("Reger.com: ds is null or we have a new dbconfig to test.");
                setupDataSource();
            }
            try{
                //Return the connection
                return ds.getConnection();
            } catch (Exception e){
                System.out.println("Reger.com: Error getting connection from the dataSource ds.");
                e.printStackTrace();
            }
        } catch (Exception e){
            System.out.println("Reger.com: Error setting up.");
            e.printStackTrace();
        }
        System.out.println("Reger.com: Returning null connection.");
        return null;

  }

  public static void shutdownDriver() {
        try {
            BasicDataSource bds = (BasicDataSource) ds;
            bds.close();
        } catch (SQLException ex){
            System.out.println("Reger.com: Error shutting down pool, short error note to follow.");
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println("Reger.com: Error shutting down pool.");
            e.printStackTrace();
        }
    }

  public static String printDriverStats() throws Exception {
        StringBuffer mb = new StringBuffer();
        BasicDataSource bds = (BasicDataSource) ds;
        mb.append("NumActive: " + bds.getNumActive());
        mb.append("<br>");
        mb.append("NumIdle: " + bds.getNumIdle());
        return mb.toString();

    }
  
  /*
  * Run SQL, return a String Array
  */
  public static String[][] RunSQL(String sql, int recordstoreturn) {
    //reger.core.Util.logtodb("SQL: " + sql);
    //System.out.println("Reger.com: RunSql called: " + sql);

  	Connection conn=null;
	Statement stmt=null;
	ResultSet rs=null;
	String[][] results=null;
	try{


	        conn = getConnection();
	        if(conn != null){
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);

				//Get metadata
				Vector rows = new Vector();
				int columnCount = rs.getMetaData().getColumnCount();
				int recCount=0;
				boolean tmp = rs.next();
				if (recordstoreturn<0){
					recordstoreturn=defaultRecordstoreturn;
				}
				while(tmp && recCount < recordstoreturn){
					String[] row = new String[columnCount];
					for(int c=0; c<columnCount; c++){
						row[c] = rs.getString(c+1);
						if(row[c] == null){
							row[c]="";
						} else {
							row[c] = row[c].trim();
						}
					}
					rows.addElement(row);
					recCount++;
					tmp = rs.next();
				}

				//Put records into a string array
				results = new String[rows.size()][];
				for(int i=0; i < results.length; i++){
					results[i] = (String[]) rows.get(i);
				}
	        } else {
                Debug.errorsave(null, "", "Db.java: conn==null.<br>sql=" + sql);
            }


    }catch(Exception e) {
      	e.printStackTrace();
      	Debug.errorsave(e, "", sql);
    } finally {
		try {
			//Close the connections
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(conn!=null) conn.close();
		}catch(Exception e) {
      		e.printStackTrace();
		}
	}

	//Return results to caller
	return results;
  }
  
  /**
  * Overload to allow just sql, with no specification of # records to return
  */
  public static String[][] RunSQL(String sql) {
  		return RunSQL(sql, defaultRecordstoreturn);
  }
  

  
  //Run Update SQL, return the number of rows affected
  public static int RunSQLUpdate(String sql){


  	int count=0;
	Connection conn=null;
	Statement stmt=null;
  	try{


	        conn = getConnection();
	        if(conn != null){
	            stmt = conn.createStatement();
	            count = stmt.executeUpdate(sql);
	        } else {
                Debug.errorsave(null, "", "Db.java: conn==null.<br>sql=" + sql);
            }


    }catch(Exception e) {
      e.printStackTrace();
      Debug.errorsave(e, "", sql);
	  return 0;
    } finally {
		try {
			//Close the connections
			if(stmt!=null) stmt.close();
			if(conn!=null) conn.close();
		}catch(Exception e) {
      		e.printStackTrace();
		}
	}
	//Return the count to the caller
  	return count;
  }
  
  
  
  
 
  
  //Run Insert SQL, return the unique autonumber of the row inserted
  public static int RunSQLInsert(String sql){
  	int count=0;
	int myidentity=0;
	Connection conn=null;
	Statement stmt=null;
	ResultSet tmpRst=null;
  	try{


	        conn = getConnection();
	        if(conn != null)  {
	            stmt = conn.createStatement();
	            count = stmt.executeUpdate(sql);
				tmpRst = stmt.executeQuery("SELECT LAST_INSERT_ID()");
				if(tmpRst.next()) {
					myidentity = tmpRst.getInt(1);
				}
				if(tmpRst!=null) tmpRst.close();
	        } else {
                Debug.errorsave(null, "", "Db.java: conn==null.<br>sql=" + sql);
            }


    }catch(Exception e) {
	  e.printStackTrace();
	  Debug.errorsave(e, "", sql);
	  return 0;
    } finally {
		try {
			//Close the connections
			if(tmpRst!=null) tmpRst.close();
			if(stmt!=null) stmt.close();
			if(conn!=null) conn.close();
		}catch(Exception e) {
      		e.printStackTrace();
		}
	}
  	//Return the identity to the caller
	return myidentity;

  }


}