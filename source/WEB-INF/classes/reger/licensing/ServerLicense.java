package reger.licensing;

/**
 * Spoofs the ServerLicense so that the database upgrade can happen.
 * No need for a server license on the licensing server.
 */
public class ServerLicense {

    public static boolean licenseAllowsCurrentApplicationVersion(){
        return true;
    }


}
