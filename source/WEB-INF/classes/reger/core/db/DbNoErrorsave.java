package reger.core.db;


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

  /*
  * Run SQL, return a String Array
  */
  public static String[][] RunSQL(String sql, int recordstoreturn) {
    return reger.core.db.proxool.DbNoErrorsave.RunSQL(sql, recordstoreturn);
  }
  
  /**
  * Overload to allow just sql, with no specification of # records to return
  */
  public static String[][] RunSQL(String sql) {
  		return RunSQL(sql, 50000);
  }

  //Run Update SQL, return the number of rows affected
  public static int RunSQLUpdate(String sql){
    return reger.core.db.proxool.DbNoErrorsave.RunSQLUpdate(sql);
  }
  

  //Run Insert SQL, return the unique autonumber of the row inserted
  public static int RunSQLInsert(String sql){
    return reger.core.db.proxool.DbNoErrorsave.RunSQLInsert(sql);
  }
  

}