package reger.core.db.proxool;


import java.sql.*;
import java.util.Vector;
import java.util.Properties;

import reger.core.Debug;
import reger.core.db.DbConfig;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.StatisticsIF;
import org.logicalcobwebs.proxool.admin.SnapshotIF;

public class Db {

  public static final int defaultRecordstoreturn=50000;
  public static boolean driverHasBeenConfigured = false;
  public static String alias = "reger-"+reger.core.RandomString.randomAlphabetic(4);
  public static int configCount = 0;


    public static void setupDataSource(){
        configCount = configCount + 1;
        System.out.println("Reger.com alias="+alias+": Start proxool configuration configCount="+configCount);
        try{

            try{
                System.out.println("Reger.com alias="+alias+": Shutting down all pools.");
                ProxoolFacade.shutdown(0);
            } catch (Exception e){
                System.out.println("Reger.com alias="+alias+": Error shutting down pool.");
                e.printStackTrace();
            }

            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
            Properties info = new Properties();
            info.setProperty("proxool.maximum-connection-count", String.valueOf(DbConfig.getDbMaxActive()));
            info.setProperty("proxool.minimum-connection-count", String.valueOf(DbConfig.getDbMinIdle()));
            info.setProperty("proxool.house-keeping-test-sql", "select CURRENT_DATE");
            info.setProperty("user", DbConfig.getDbUsername());
            info.setProperty("password", DbConfig.getDbPassword());
            String driverClass = DbConfig.getDbDriverName();
            String driverUrl = DbConfig.getDbConnectionUrl();
            String url = "proxool." + alias + ":" + driverClass + ":" + driverUrl;
            ProxoolFacade.registerConnectionPool(url, info);

            //Set the driver flag first
            driverHasBeenConfigured = true;
            System.out.println("Reger.com alias="+alias+": Ds setup appears successful.");
            System.out.println("Reger.com alias="+alias+": Ds url="+url);

        } catch (Exception e){
                System.out.println("Reger.com alias="+alias+": Error configuring proxool connection.");
                e.printStackTrace();
        }
        System.out.println("Reger.com alias="+alias+": End ds setup.");
    }

  /**
   * Get a connection object.
   */
  public static Connection getConnection(){
        try{
            if (!driverHasBeenConfigured || DbConfig.haveNewConfigToTest()){
                System.out.println("Reger.com alias="+alias+": proxool driverHasBeenConfigured is false or we have a new dbconfig to test.");
                setupDataSource();
            }
            try{
                //Return the connection
                return DriverManager.getConnection("proxool."+alias);
            } catch (Exception e){
                System.out.println("Reger.com alias="+alias+": Error getting connection from the dataSource ds.");
                e.printStackTrace();
            }
        } catch (Exception e){
            System.out.println("Reger.com alias="+alias+": Error setting up.");
            e.printStackTrace();
        }
        System.out.println("Reger.com alias="+alias+": Returning null connection.");
        return null;

  }

  public static void shutdownDriver() {

    }

  public static String printDriverStats() throws Exception {
        StringBuffer mb = new StringBuffer();
        mb.append("<br><br>");

        //Get current status
        SnapshotIF snapshot = ProxoolFacade.getSnapshot(alias, true);
        mb.append("<table cellpadding=0 cellspacing=5 border=0>");
        mb.append("<tr>");
        mb.append("<td valign=top bgcolor=#cccccc>");
        mb.append("<font face=arial size=-1>Active Conn</font>");
        mb.append("</td>");
        mb.append("<td valign=top bgcolor=#cccccc>");
        mb.append("<font face=arial size=-1>Available Conn</font>");
        mb.append("</td>");
        mb.append("<td valign=top bgcolor=#cccccc>");
        mb.append("<font face=arial size=-1>Conn Served Since Pool Start</font>");
        mb.append("</td>");
        mb.append("<td valign=top bgcolor=#cccccc>");
        mb.append("<font face=arial size=-1>Conn Refused Since Pool Start</font>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("<tr>");
        mb.append("<td valign=top bgcolor=#ffffff>");
        mb.append("<font face=arial size=-1>"+snapshot.getActiveConnectionCount()+"</font>");
        mb.append("</td>");
        mb.append("<td valign=top bgcolor=#ffffff>");
        mb.append("<font face=arial size=-1>"+snapshot.getAvailableConnectionCount()+"</font>");
        mb.append("</td>");
        mb.append("<td valign=top bgcolor=#ffffff>");
        mb.append("<font face=arial size=-1>"+snapshot.getServedCount()+"</font>");
        mb.append("</td>");
        mb.append("<td valign=top bgcolor=#ffffff>");
        mb.append("<font face=arial size=-1>"+snapshot.getRefusedCount()+"</font>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("</table>");


        //Get back an array of all the statistics for a particular pool
        StatisticsIF[] statisticsArray = ProxoolFacade.getStatistics(alias);
          for (int i = 0; i < statisticsArray.length; i++) {
              StatisticsIF statisticsIF = statisticsArray[i];
                mb.append("<table cellpadding=5 cellspacing=2 border=0>");
                mb.append("<tr>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Time Period</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Start Date</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Stop Date</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Avg Active Conn</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Avg Active Time</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Served Count</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Served Per Second</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Refused Count</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#cccccc>");
                mb.append("<font face=arial size=-1>Refused Per Second</font>");
                mb.append("</td>");
                mb.append("</tr>");

                mb.append("<tr>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getPeriod()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getStartDate()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getStopDate()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getAverageActiveCount()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getAverageActiveTime()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getServedCount()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getServedPerSecond()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getRefusedCount()+"</font>");
                mb.append("</td>");
                mb.append("<td valign=top bgcolor=#e6e6e6>");
                mb.append("<font face=arial size=-1>"+statisticsIF.getRefusedPerSecond()+"</font>");
                mb.append("</td>");
                mb.append("</tr>");
                mb.append("</table>");

          }



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