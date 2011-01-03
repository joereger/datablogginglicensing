package reger.core;

import reger.core.db.Db;

/**
 * Manages the debug level of the application.  Persisted to the database.
 * 0 = off
 * 1 = low
 * 5 = high
 */
public class DegubLevel {

    private static int debugLevel = -1;

    public static int getDebugLevel(){
        if (debugLevel<0){
            //-----------------------------------
            //-----------------------------------
            String[][] rstDb= Db.RunSQL("SELECT debuglevel FROM debuglevel ORDER BY debuglevelid DESC LIMIT 0,1");
            //-----------------------------------
            //-----------------------------------
            if (rstDb!=null && rstDb.length>0){
            	debugLevel = Integer.parseInt(rstDb[0][0]);
            }
        }
        return debugLevel;
    }

    public static int getDebugLevelNoDBCallDontUseThisOneExceptInDbCode(){
        return debugLevel;
    }

    public static void setDebugLevel(int newDebugLevel){
        debugLevel =  newDebugLevel;

        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("DELETE FROM debuglevel");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int identity = Db.RunSQLInsert("INSERT INTO debuglevel(debuglevel) VALUES('"+newDebugLevel+"')");
        //-----------------------------------
        //-----------------------------------
    }

}
