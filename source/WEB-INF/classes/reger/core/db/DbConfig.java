package reger.core.db;

import reger.core.licensing.DesEncrypter;
import reger.core.Debug;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Holds database configuration parameters
 */
public class DbConfig {

    private static String dbConnectionUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String dbMaxActive;
    private static String dbMaxIdle;
    private static String dbMinIdle;
    private static String dbMaxWait;
    private static String dbDriverName;

    private static String passPhrase = "pupper";

    private static boolean haveValidConfig = false;
    private static boolean haveNewConfigToTest = false;
    private static boolean haveAttemptedToLoadDefaultPropsFile = false;
    private static String dbPropsInternalFilename = reger.core.WebAppRootDir.getWebAppRootPath() + "conf/instance.props";
    private static String dbPropsExternalFilename = "datablogging-"+reger.core.WebAppRootDir.getUniqueContextId()+"-dbconfig.txt";

    public DbConfig(){
        System.out.println("DbConfig instanciated");
    }

    public static void load(){
        if (!haveValidConfig && !haveAttemptedToLoadDefaultPropsFile){
            try {

                boolean gotFile = false;

                //Look in the /conf directory
                try{
                    System.out.println("Looking for instance.props dbPropsInternalFilename: "+dbPropsInternalFilename);
                    File internalFile = new File(dbPropsInternalFilename);
                    if (internalFile!=null && internalFile.exists() && internalFile.canRead() && internalFile.isFile()){
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(internalFile));
                        loadPropsFile(properties);
                        gotFile = true;
                        System.out.println("Found instance props from dbPropsInternalFilename="+dbPropsInternalFilename);
                    } else {
                        System.out.println("dbPropsInternalFilename is null, doesn't exist, can't be read, etc");
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

                //If we don't have one in the conf directory, look to the system default dir
                if (!gotFile){
                    try{
                        System.out.println("Looking for instance.props dbPropsExternalFilename: "+dbPropsExternalFilename);
                        File externalFile = new File("", dbPropsExternalFilename);
                        if (externalFile!=null && externalFile.exists() && externalFile.canRead() && externalFile.isFile()){
                            Properties properties = new Properties();
                            properties.load(new FileInputStream(externalFile));
                            loadPropsFile(properties);
                            gotFile = true;
                            System.out.println("Found instance props from dbPropsExternalFilename");
                        } else {
                            System.out.println("dbPropsExternalFilename is null, doesn't exist, can't be read, etc");
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadPropsFile(Properties properties){
        try{
            dbConnectionUrl = properties.getProperty("dbConnectionUrl", "jdbc:mysql://localhost:3306/regerdatabase?autoReconnect=true");
            dbUsername = properties.getProperty("dbUsername", "username");
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //dbPassword = encrypter2.decrypt(properties.getProperty("dbPassword", "password"));
            dbPassword = properties.getProperty("dbPassword", "password");
            dbMaxActive = properties.getProperty("dbMaxActive", "100");
            dbMaxIdle = properties.getProperty("dbMaxIdle", "15");
            dbMinIdle = properties.getProperty("dbMinIdle", "10");
            dbMaxWait = properties.getProperty("dbMaxWait", "10000");
            dbDriverName = properties.getProperty("dbDriverName", "com.mysql.jdbc.Driver");

            haveAttemptedToLoadDefaultPropsFile = true;
            haveNewConfigToTest = true;
        } catch (Exception e){
            e.printStackTrace();
        }

        testConfig();
    }

    public static void save(){
        if (!haveNewConfigToTest){
            Properties properties = new Properties();
            try {
                //Make sure we test the next time around
                haveNewConfigToTest = true;
                
                if (dbConnectionUrl!=null){
                    properties.setProperty("dbConnectionUrl", dbConnectionUrl);
                }
                if (dbUsername!=null){
                    properties.setProperty("dbUsername", dbUsername);
                }
                if (dbPassword!=null){
                    //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
                    //String encDbPassword = encrypter2.encrypt(dbPassword);
                    //properties.setProperty("dbPassword", encDbPassword);
                    properties.setProperty("dbPassword", dbPassword);
                }
                if (dbMaxActive!=null){
                    properties.setProperty("dbMaxActive", dbMaxActive);
                }
                if (dbMaxIdle!=null){
                    properties.setProperty("dbMaxIdle", dbMaxIdle);
                }
                if (dbMinIdle!=null){
                    properties.setProperty("dbMinIdle", dbMinIdle);
                }
                if (dbMaxWait!=null){
                    properties.setProperty("dbMaxWait", dbMaxWait);
                }
                if (dbDriverName!=null){
                    properties.setProperty("dbDriverName", dbDriverName);
                }
                //Save to the file system in the conf directory
                File fil = new File(dbPropsInternalFilename);
                FileOutputStream fos = new FileOutputStream(fil);
                properties.store(fos, "DbConfig");
                fos.close();
                fil = null;
                fos = null;

                //Store to default system location
                File fil2 = new File("", dbPropsExternalFilename);
                FileOutputStream fos2 = new FileOutputStream(fil2);
                properties.store(fos2, "DbConfig for " + reger.core.WebAppRootDir.getUniqueContextId());
                fos2.close();
                fil2 = null;
                fos2 = null;

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Test the new values
            testConfig();
        }
    }

    public static boolean testConfig(){
        if (!haveValidConfig && haveNewConfigToTest){

            //-----------------------------------
            //-----------------------------------
            //int count = DbNoErrorsave.RunSQLUpdate("DROP TABLE test");
            //-----------------------------------
            //-----------------------------------

            //-----------------------------------
            //-----------------------------------
            //int count1 = DbNoErrorsave.RunSQLUpdate("CREATE TABLE `test` (`testid` int(11) NOT NULL auto_increment, `test` varchar(255) NOT NULL default '0', PRIMARY KEY  (`testid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
            //-----------------------------------
            //-----------------------------------

            //-----------------------------------
            //-----------------------------------
            //int identity = Db.RunSQLInsert("INSERT INTO test(test) VALUES('This is a test: "+reger.core.TimeUtils.nowInGmtString()+"')");
            //-----------------------------------
            //-----------------------------------

            //-----------------------------------
            //-----------------------------------
            //String[][] rstTest= DbNoErrorsave.RunSQL("SELECT testid FROM test");
            String[][] rstTest= DbNoErrorsave.RunSQL("SELECT 1");
            //-----------------------------------
            //-----------------------------------
            if (rstTest!=null && rstTest.length>0){
                //-----------------------------------
                //-----------------------------------
                //int count2 = DbNoErrorsave.RunSQLUpdate("DROP TABLE test");
                //-----------------------------------
                //-----------------------------------

                haveValidConfig = true;

            }
        }
        haveNewConfigToTest = false;
        return haveValidConfig;
    }

    public static String getDbConnectionUrl() {
        load();
        return dbConnectionUrl;
    }

    public static void setDbConnectionUrl(String dbConnectionUrl) {
        DbConfig.dbConnectionUrl = dbConnectionUrl;
    }

    public static String getDbUsername() {
        load();
        return dbUsername;
    }

    public static void setDbUsername(String dbUsername) {
        DbConfig.dbUsername = dbUsername;
    }

    public static String getDbPassword() {
        load();
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        DbConfig.dbPassword = dbPassword;
    }

    public static int getDbMaxActive() {
        load();
        if (reger.core.Util.isinteger(dbMaxActive)){
            return Integer.parseInt(dbMaxActive);
        } else {
            return 100;
        }
    }

    public static void setDbMaxActive(String dbMaxActive) {
        DbConfig.dbMaxActive = dbMaxActive;
    }

    public static int getDbMaxIdle() {
        load();
        if (reger.core.Util.isinteger(dbMaxIdle)){
            return Integer.parseInt(dbMaxIdle);
        } else {
            return 15;
        }
    }

    public static void setDbMaxIdle(String dbMaxIdle) {
        DbConfig.dbMaxIdle = dbMaxIdle;
    }

    public static int getDbMinIdle() {
        load();
        if (reger.core.Util.isinteger(dbMinIdle)){
            return Integer.parseInt(dbMinIdle);
        } else {
            return 10;
        }
    }

    public static void setDbMinIdle(String dbMinIdle) {
        DbConfig.dbMinIdle = dbMinIdle;
    }

    public static int getDbMaxWait() {
        load();
        if (reger.core.Util.isinteger(dbMaxWait)){
            return Integer.parseInt(dbMaxWait);
        } else {
            return 10000;
        }
    }

    public static void setDbMaxWait(String dbMaxWait) {
        DbConfig.dbMaxWait = dbMaxWait;
    }

    public static boolean haveValidConfig() {
        load();
        return haveValidConfig;
    }

    public static void setHaveValidConfig(boolean haveValidConfig) {
        DbConfig.haveValidConfig = haveValidConfig;
    }

    public static String getDbPropsInternalFilename() {
        load();
        return dbPropsInternalFilename;
    }

    public static void setDbPropsInternalFilename(String dbPropsInternalFilename) {
        DbConfig.dbPropsInternalFilename = dbPropsInternalFilename;
    }

    public static String getDbDriverName(){
        load();
        return dbDriverName;
    }

    public static void setDbDriverName(String dbDriverName) {
        DbConfig.dbDriverName = dbDriverName;
    }

    public static boolean haveNewConfigToTest() {
        return haveNewConfigToTest;
    }

    public static void setHaveNewConfigToTest(boolean haveNewConfigToTest) {
        DbConfig.haveNewConfigToTest = haveNewConfigToTest;
    }


}
