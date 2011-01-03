package reger.core.dbupgrade;

/**
 * This class has a simple role.  It tells the app what version of the
 * database is required.
 */
public class RequiredDatabaseVersion {

    //Set version to -1 to always use the max available
    public static int version = -1;
    public static boolean havecorrectversion = false;
    public static int currentversion = -1;
    public static String error = "";
    public static int maxversion = -1;
    

}
