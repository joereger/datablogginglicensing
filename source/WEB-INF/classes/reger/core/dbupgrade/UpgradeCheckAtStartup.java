package reger.core.dbupgrade;

import reger.core.db.Db;
import reger.core.Debug;
import reger.licensing.ServerLicense;

/**
 * This class is run once at startup to make sure the current app has the
 * correct database version running.
 */
public class UpgradeCheckAtStartup {

    public void doCheck(){



        //Calculate the max version that exists in the code classes
        int maxVer = 0;
        while(true){
            try{
                //Try to create an object
                UpgradeDatabaseOneVersion upg = (UpgradeDatabaseOneVersion)(Class.forName("reger.dbversion.Version"+maxVer+"ToVersion" + (maxVer+1)).newInstance());
            } catch (ClassNotFoundException ex){
                //If class isn't found, break
                break;
            } catch (Throwable e){
                Debug.debug(5, "", e);
                break;
            }
            maxVer = maxVer + 1;
        }
        reger.core.dbupgrade.RequiredDatabaseVersion.maxversion = maxVer;

        //Make sure we have the database table to support the database version status
        if (!checkThatDatabaseVersionTableExists()){
            createDbVersionTable();
        }

        //Get the highest version stored in the database version table
        int currentDatabaseVersion = getMaxVersionFromDatabase();

        //Figure out the target version
        if (reger.core.dbupgrade.RequiredDatabaseVersion.version<=0){
            reger.core.dbupgrade.RequiredDatabaseVersion.version = maxVer;
        }

        //Boolean to keep working
        boolean keepWorking = true;

        //Compare to the current required database version
        while (currentDatabaseVersion<reger.core.dbupgrade.RequiredDatabaseVersion.version && keepWorking && ServerLicense.licenseAllowsCurrentApplicationVersion()) {
            //Get the highest version stored in the database version table
            currentDatabaseVersion = getMaxVersionFromDatabase();

            //If, after checking it out in the database, we're still not on the correct version
            if (currentDatabaseVersion<reger.core.dbupgrade.RequiredDatabaseVersion.version){

                //@todo Backup the databases prior to changes.
                //Problem is that the database tier can be on another machine
                //which Java has no control of.  So we must rely on the database's
                //backup schemes or we must download entire database and write to file.
                //Both approaches are already implemented but never called.
                //Problem with relying on MySql is that it doesn't overwrite past backups.
                //Problem with relying on writing Db to file is that the characters in
                //the database may cause problems that make it unusable and the CPU
                //gets pinned for a while.  Writing to file is the most reasonable solution though.

                try{
                    //Do the upgrade
                    System.out.println("Reger.com UpgradeCheckAtStartup.java: Start Db Upgrade from "+currentDatabaseVersion+" to "+(currentDatabaseVersion+1)+".");
                    Debug.logtodb("Start upgrade database to version " + (currentDatabaseVersion+1), "");
                    UpgradeDatabaseOneVersion upg = (UpgradeDatabaseOneVersion)(Class.forName("reger.dbversion.Version"+currentDatabaseVersion+"ToVersion" + (currentDatabaseVersion+1)).newInstance());
                    upg.doUpgrade();
                    updateDatabase((currentDatabaseVersion+1));
                    Debug.logtodb("End upgrade database to version " + (currentDatabaseVersion+1), "");
                    System.out.println("Reger.com UpgradeCheckAtStartup.java: End Db Upgrade from "+currentDatabaseVersion+" to "+(currentDatabaseVersion+1)+".");
                } catch (ClassNotFoundException ex){
                    //Class isn't found, report it and exit
                    Debug.errorsave(ex, "");
                    ex.printStackTrace();
                    keepWorking = false;
                    reger.core.dbupgrade.RequiredDatabaseVersion.error = reger.core.dbupgrade.RequiredDatabaseVersion.error + reger.core.ErrorDissect.dissect(ex);
                    Debug.logtodb("Error: Upgrade database to version " + (currentDatabaseVersion+1) + ".  Class not found.", "");
                } catch (Exception e){
                    //Some other sort of error
                    System.out.println("Reger.com UpgradeCheckAtStartup.java: Error upgrading Db:" + e.getMessage());
                    e.printStackTrace();
                    Debug.errorsave(e, "");
                    keepWorking = false;
                    reger.core.dbupgrade.RequiredDatabaseVersion.error = reger.core.dbupgrade.RequiredDatabaseVersion.error + reger.core.ErrorDissect.dissect(e);
                    Debug.logtodb("Error: Upgrade database to version " + (currentDatabaseVersion+1) + " had issues recorded.", "");
                }

            } else {
                keepWorking = false;
            }

        }

        if (currentDatabaseVersion==reger.core.dbupgrade.RequiredDatabaseVersion.version){
            //Successful upgrade through all versions
            reger.core.dbupgrade.RequiredDatabaseVersion.havecorrectversion = true;
            Debug.logtodb("The correct database version is installed.  Version: " + currentDatabaseVersion, "");
        }

    }




    private static void updateDatabase(int version){
        try{
            //Update the database version
            //-----------------------------------
            //-----------------------------------
            int identity = Db.RunSQLInsert("INSERT INTO databaseversion(version, date) VALUES('"+version+"', '"+reger.core.TimeUtils.nowInGmtString()+"')");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int getMaxVersionFromDatabase(){
        try{
            //Go to the database and see what we've got.
            //-----------------------------------
            //-----------------------------------
            String[][] rstVersion= Db.RunSQL("SELECT max(version) FROM databaseversion");
            //-----------------------------------
            //-----------------------------------
            if (rstVersion!=null && rstVersion.length>0){
                if (reger.core.Util.isinteger(rstVersion[0][0])){
                    reger.core.dbupgrade.RequiredDatabaseVersion.currentversion = Integer.parseInt(rstVersion[0][0]);
                    return Integer.parseInt(rstVersion[0][0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            reger.core.dbupgrade.RequiredDatabaseVersion.error = reger.core.dbupgrade.RequiredDatabaseVersion.error + reger.core.ErrorDissect.dissect(e);
        }
        return 0;
    }

    private static boolean checkThatDatabaseVersionTableExists(){
        try{
            //-----------------------------------
            //-----------------------------------
            String[][] rstT = Db.RunSQL("SELECT COUNT(*) FROM databaseversion");
            //-----------------------------------
            //-----------------------------------
            if (rstT!=null && rstT.length>0){
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

    private static void createDbVersionTable(){
        try{
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("CREATE TABLE `databaseversion` (`databaseversionid` int(11) NOT NULL auto_increment, `version` int(11) NOT NULL default '0', `date` datetime default null, PRIMARY KEY  (`databaseversionid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
