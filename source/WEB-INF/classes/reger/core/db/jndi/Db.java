package reger.core.db.jndi;

import reger.core.Util;

import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import java.util.Vector;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.FileOutputStream;


////When it's time to cluster:
////http://c-jdbc.objectweb.org/
//
//
public class Db {
//
//
//
//
//  //Works with Tomcat 5.5.x
//  //private static final String jndiPrePend = "java:comp/env/";
//  //Works with Tomcat 5.0.x
//  private static final String jndiPrePend = "java:comp/env/";
//  private static final String jndiDB = "jdbc/db";
//  public static final int defaultRecordstoreturn=50000;
//
//
//  /**
//   * Get a connection object.  Don't use this.  Only here for the DatabaseToString backup code
//   */
//  public static Connection getConnection(){
//    Connection conn=null;
//    try{
//      Context ctx = new InitialContext();
//      if(ctx != null){
//        DataSource ds = (DataSource)ctx.lookup(jndiPrePend + jndiDB);
//        if(ds != null){
//            conn = ds.getConnection();
//            return conn;
//        }
//      }
//    } catch (Exception e){
//        e.printStackTrace();
//      	Util.errorsave(e);
//    }
//    return null;
//  }
//
//  /*
//  * Run SQL, return a String Array
//  */
//  public static String[][] RunSQL(String sql, int recordstoreturn) {
//    recordToSqlDebugCache(sql);
//
//  	Connection conn=null;
//	Statement stmt=null;
//	ResultSet rs=null;
//	String[][] results=null;
//	try{
//      Context ctx = new InitialContext();
//      if(ctx != null){
//      	DataSource ds = (DataSource)ctx.lookup(jndiPrePend + jndiDB);
//		if(ds != null){
//	        conn = ds.getConnection();
//	        if(conn != null){
//	            stmt = conn.createStatement();
//	            rs = stmt.executeQuery(sql);
//
//				//Get metadata
//				Vector rows = new Vector();
//				int columnCount = rs.getMetaData().getColumnCount();
//				int recCount=0;
//				boolean tmp = rs.next();
//				if (recordstoreturn<0){
//					recordstoreturn=defaultRecordstoreturn;
//				}
//				while(tmp && recCount < recordstoreturn){
//					String[] row = new String[columnCount];
//					for(int c=0; c<columnCount; c++){
//						row[c] = rs.getString(c+1);
//						if(row[c] == null){
//							row[c]="";
//						} else {
//							row[c] = row[c].trim();
//						}
//					}
//					rows.addElement(row);
//					recCount++;
//					tmp = rs.next();
//				}
//
//				//Put records into a string array
//				results = new String[rows.size()][];
//				for(int i=0; i < results.length; i++){
//					results[i] = (String[]) rows.get(i);
//				}
//	        }
//      	}
//	  }
//    }catch(Exception e) {
//      	e.printStackTrace();
//      	Util.errorsave(e, sql);
//    } finally {
//		try {
//			//Close the connections
//			if(rs!=null) rs.close();
//			if(stmt!=null) stmt.close();
//			if(conn!=null) conn.close();
//		}catch(Exception e) {
//      		e.printStackTrace();
//		}
//	}
//
//	//Return results to caller
//	return results;
//  }
//
//  /**
//  * Overload to allow just sql, with no specification of # records to return
//  */
//  public static String[][] RunSQL(String sql) {
//  		return RunSQL(sql, defaultRecordstoreturn);
//  }
//
//
//
//  //Run Update SQL, return the number of rows affected
//  public static int RunSQLUpdate(String sql){
//    recordToSqlDebugCache(sql);
//  	int count=0;
//	Connection conn=null;
//	Statement stmt=null;
//  	try{
//      Context ctx = new InitialContext();
//      if(ctx != null){
//      	DataSource ds = (DataSource)ctx.lookup(jndiPrePend + jndiDB);
//		if (ds != null){
//	        conn = ds.getConnection();
//	        if(conn != null){
//	            stmt = conn.createStatement();
//	            count = stmt.executeUpdate(sql);
//	        }
//      	}
//	   }
//    }catch(Exception e) {
//      e.printStackTrace();
//      Util.errorsave(e, sql);
//	  return 0;
//    } finally {
//		try {
//			//Close the connections
//			if(stmt!=null) stmt.close();
//			if(conn!=null) conn.close();
//		}catch(Exception e) {
//      		e.printStackTrace();
//		}
//	}
//	//Return the count to the caller
//  	return count;
//  }
//
//
//
//
//
//
//  //Run Insert SQL, return the unique autonumber of the row inserted
//  public static int RunSQLInsert(String sql){
//    recordToSqlDebugCache(sql);
//  	int count=0;
//	int myidentity=0;
//	Connection conn=null;
//	Statement stmt=null;
//	ResultSet tmpRst=null;
//  	try{
//      Context ctx = new InitialContext();
//      if(ctx != null) {
//      	DataSource ds = (DataSource)ctx.lookup(jndiPrePend + jndiDB);
//		if (ds != null) {
//	        conn = ds.getConnection();
//	        if(conn != null)  {
//	            stmt = conn.createStatement();
//	            count = stmt.executeUpdate(sql);
//				tmpRst = stmt.executeQuery("SELECT LAST_INSERT_ID()");
//				if(tmpRst.next()) {
//					myidentity = tmpRst.getInt(1);
//				}
//				if(tmpRst!=null) tmpRst.close();
//	        }
//      	}
//	   }
//    }catch(Exception e) {
//	  e.printStackTrace();
//	  Util.errorsave(e, sql);
//	  return 0;
//    } finally {
//		try {
//			//Close the connections
//			if(tmpRst!=null) tmpRst.close();
//			if(stmt!=null) stmt.close();
//			if(conn!=null) conn.close();
//		}catch(Exception e) {
//      		e.printStackTrace();
//		}
//	}
//  	//Return the identity to the caller
//	return myidentity;
//  }
//
//  public static void recordToSqlDebugCache(String sql){
//      if (reger.core.DegubLevel.getDebugLevelNoDBCallDontUseThisOneExceptInDbCode()>=4){
//         System.out.println("REGERCOM-SQL: " +  sql);
////         String toWrite = "REGERCOM-SQL: " +  sql + System.getProperty("line.separator");
////         try{
////            File outputFile = new File("sql.txt");
////            FileOutputStream out = new FileOutputStream(outputFile, true);
////            out.write(toWrite.getBytes());
////         } catch (Exception e){
////            e.printStackTrace();
////         }
//      }
//  }


}