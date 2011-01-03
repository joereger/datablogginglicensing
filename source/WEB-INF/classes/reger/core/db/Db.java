package reger.core.db;

import java.sql.*;


//When it's time to cluster:
//http://c-jdbc.objectweb.org/


public class Db {

  /**
   * Get a connection object.  Don't use this.  Only here for the DatabaseToString backup code
   */
  public static Connection getConnection(){
        return reger.core.db.proxool.Db.getConnection();
  }
  
  /*
  * Run SQL, return a String Array
  */
  public static String[][] RunSQL(String sql, int recordstoreturn) {
    if (DbConfig.haveValidConfig() || DbConfig.haveNewConfigToTest()){
        return reger.core.db.proxool.Db.RunSQL(sql, recordstoreturn);
    } else {
        return new String[0][];
    }
  }
  
  /**
  * Overload to allow just sql, with no specification of # records to return
  */
  public static String[][] RunSQL(String sql) {
  		return RunSQL(sql, 50000);
  }

  //Run Update SQL, return the number of rows affected
  public static int RunSQLUpdate(String sql){
    if (DbConfig.haveValidConfig() || DbConfig.haveNewConfigToTest()){
       return reger.core.db.proxool.Db.RunSQLUpdate(sql);
    } else {
        return 0;
    }
  }
  

  //Run Insert SQL, return the unique autonumber of the row inserted
  public static int RunSQLInsert(String sql){
    if (DbConfig.haveValidConfig() || DbConfig.haveNewConfigToTest()){
      return reger.core.db.proxool.Db.RunSQLInsert(sql);
    } else {
        return 0;
    }
  }

  public static String printDriverStats() throws Exception {
        return reger.core.db.proxool.Db.printDriverStats();
   }

  public static void recordToSqlDebugCache(String sql){
      if (reger.core.DegubLevel.getDebugLevelNoDBCallDontUseThisOneExceptInDbCode()>=4){
         System.out.println("REGERCOM-SQL: " +  sql);
      }
  }


             

}