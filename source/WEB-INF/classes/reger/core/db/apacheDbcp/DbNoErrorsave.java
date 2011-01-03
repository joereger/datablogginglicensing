package reger.core.db.apacheDbcp;

import java.sql.*;
import java.util.Vector;

import reger.core.db.apacheDbcp.*;
import reger.core.db.apacheDbcp.Db;

/**
 * This special version of the database access code does not contain errorsave calls.
 * Doing this is important because it allows the main Db.java to try
 * one time... just one time... with this class to save its own errors and then after that it fails.
 * If I don't have this class there is the possibility of circular references
 * where the Db.java calls itself, continually failing.
 *
 * Note: This does save the error to the system stack.
 */

public class DbNoErrorsave {

  public static final int defaultRecordstoreturn=5000;


  
  /*
  * Run SQL, return a String Array
  */
  public static String[][] RunSQL(String sql, int recordstoreturn) {
  	Connection conn=null;
	Statement stmt=null;
	ResultSet rs=null;
	String[][] results=null;
	try{

	        conn = reger.core.db.apacheDbcp.Db.getConnection();
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
	        }


    }catch(Exception e) {
      	e.printStackTrace();
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


	        conn = reger.core.db.apacheDbcp.Db.getConnection();
	        if(conn != null){
	            stmt = conn.createStatement();
	            count = stmt.executeUpdate(sql);
	        }


    }catch(Exception e) {
      e.printStackTrace();
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


	        conn = reger.core.db.apacheDbcp.Db.getConnection();
	        if(conn != null)  {
	            stmt = conn.createStatement();
	            count = stmt.executeUpdate(sql);
				tmpRst = stmt.executeQuery("SELECT LAST_INSERT_ID()");
				if(tmpRst.next()) {
					myidentity = tmpRst.getInt(1);
				}
				if(tmpRst!=null) tmpRst.close();
	        }


    }catch(Exception e) {
	  e.printStackTrace();
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