package reger.core.dbupgrade;

import reger.core.db.Db;
import reger.core.Debug;

/**
 * This class will create database backups.
 */
public class BackupDatabase {

    public static void doBackup(){
        //Date-based directory name
        String dirname = (String)reger.Vars.getEnvVar("DBBACKUPDIR");
        //java.Util.Calendar cal = java.Util.Calendar.getInstance();
        //dirname = dirname + reger.core.TimeUtils.dateformatfilestamp(cal) + "/";

        //Create a File Handle
        //java.io.File file = new java.io.File(dirname);
        //if (!file.exists()){
            //file.mkdirs();
        //}

        Debug.logtodb("Database Backup Directory: " + dirname, "");

        //-----------------------------------
        //-----------------------------------
        String[][] rstTables= Db.RunSQL("SHOW TABLES");
        //-----------------------------------
        //-----------------------------------
        if (rstTables!=null && rstTables.length>0){
            for(int i=0; i<rstTables.length; i++){

                //-----------------------------------
                //-----------------------------------
                String[][] rstBackup= Db.RunSQL("BACKUP TABLE "+rstTables[i][0]+" TO '" + dirname + "';");
                //-----------------------------------
                //-----------------------------------
                if (rstBackup!=null && rstBackup.length>0){
                    for(int j=0; j<rstBackup.length; j++){
                        Debug.logtodb("Table:" + rstBackup[j][0] + "<br>Operation:" + rstBackup[j][1] + "<br>MessageType:" + rstBackup[j][2] + "<br>MessageText:" + rstBackup[j][3], "");
                    }
                }



            }
        }
    }

}
