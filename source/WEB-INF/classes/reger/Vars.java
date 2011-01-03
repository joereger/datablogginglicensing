package reger;

import reger.core.Debug;
import reger.systemproperties.AllSystemProperties;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * A set of global static vars for use in the app to avoid any hard-coding.
 * Any time I have to hard-code anything in the system I use this class to store it
 * for easy configuration later on.
 */
public class Vars {



    //Accountlimits - Hardcode until a better way is found
    //public static final long ACCOUNTLIMITSPACE = 10000000;
    //public static final long ACCOUNTLIMITMAXBANDWIDTH = 1000000000;

    //Base account price
    //public static final double PRICEPER100MBSTORAGE = .35;
    //public static final double PRICEPERGBTRAFFIC = .50;

    //Default email from address
    public static final String EMAILDEFAULTFROM = "weblog.server@some.place.com";

    //Default email to address
    public static final String EMAILDEFAULTTO = "weblog.server@some.place.com";

    //Default log to create if the user doesn't choose one
    public static final int DEFAULTLOGEVENTTYPEID = 13;
    public static final String DEFAULTLOGNAME = "My Log";

    //Default Timezoneid
    public static final String TIMEZONEIDDEFAULT = "US/Eastern";

    //Default Entrymode
    public static final int ENTRYMODEADVANCED = 1;
    public static final int ENTRYMODESIMPLE = 0;


    //Traffic types - these must correlate to traffictype table in database
    public static final int TRAFFICTYPEDONTRECORD = 0;
    public static final int TRAFFICTYPEPUBLICHOMEPAGE = 1;
    public static final int TRAFFICTYPEADMINHOMEPAGE = 2;
    public static final int TRAFFICTYPEPUBLICLOGSECTION = 3;
    public static final int TRAFFICTYPEADMINLOGSECTION = 4;
    public static final int TRAFFICTYPEPUBLICENTRYDETAIL = 5;
    public static final int TRAFFICTYPEIMAGE = 6;
    public static final int TRAFFICTYPEPUBLICMISC = 7;
    public static final int TRAFFICTYPEADMINMISC = 8;
    public static final int TRAFFICTYPEMOBILE = 10;
    public static final int TRAFFICTYPEMARKETINGSITEMISC = 11;
    //New... not yet implemented in code or in database
    public static final int TRAFFICTYPEJAVASCRIPTEMBEDDED = 12;
    public static final int TRAFFICTYPEEMAILAPI = 13;
    public static final int TRAFFICTYPERSSFEED = 14;
    public static final int TRAFFICTYPEGROUPS = 15;
    public static final int TRAFFICTYPEGROUPSRSS = 16;
    public static final int TRAFFICTYPEAPI = 17;

    //Line Break Character  - This is what Java recognizes in a regular expression as a line end and break
    public static final String LINEBREAKCHAR = "\\n\\r";
    public static final String LINEBREAK = "\\n";
    public static final String CARRIAGERETURN = "\\r";
    public static final String LINEBREAKCHARFOREMAIL = "\n\r";
    public static final String LINEBREAKCHARFORHTML = "\n";

    //Friend Invitation Statuses
    public static final int INVITATIONSTATUSOPEN = 0;
    public static final int INVITATIONSTATUSVIEWED = 1;
    public static final int INVITATIONSTATUSACCEPTED = 2;
    public static final int INVITATIONSTATUSDECLINED = 3;

    //Default PLID.  This chooses the default marketing template and default signup association.  Should be the same as reger.com's plid in the database.
    public static final int PLIDDEFAULT = 1;

    //Number of days in a trial account
    public static final int TRIALACCOUNTDAYS = 15;

    //Template statuses
    public static final int TEMPLATESTATUSDELETED = 0;
    public static final int TEMPLATESTATUSPUBLIC = 1;
    public static final int TEMPLATESTATUSPRIVATE = 2;

    //Help Sections
    public static final int HELPSECTIONSIGNUPSITE = 1;
    public static final int HELPSECTIONADMINSITE = 2;
    public static final int HELPSECTIONLOGSPECIFIC = 3;
    public static final int HELPSECTIONPUBLICSITE = 4;

    //Account types - must correlate with accounttype table in database
    public static final int ACCTYPEFREE = 0;
    public static final int ACCTYPETRIAL = 1;
    public static final int ACCTYPEPRO = 2;

    //Megadatatype.  The name is the data type.  The value is the megadatatypeid in the megadatatype table in the database.
    //public static final int DATATYPENUMBER = 1;
    //public static final int DATATYPEINTEGER = 2;
    //public static final int DATATYPEALPHANUMERIC = 3;
    //public static final int DATATYPEDATE = 5;

    //Custom chart math constants
    public static final int CUSTOMMATHADD = 1;
    public static final int CUSTOMMATHSUBTRACT = 2;
    public static final int CUSTOMMATHMULTIPLY = 3;
    public static final int CUSTOMMATHDIVIDE = 4;

    //Custom chart math choose custom number
    public static final int CUSTOMMATHNUMBER = -2;

    //Charting y Axis settings to determine how y axis is calculated
    public static final int YAXISWHATTODOAVG = 1;
    public static final int YAXISWHATTODOSUM = 2;

    //Chart size settings
    public static final int CHARTSIZEMINISCULE = 1;
    public static final int CHARTSIZESMALL = 2;
    public static final int CHARTSIZEMEDIUM = 3;
    public static final int CHARTSIZELARGE = 4;
    public static final int CHARTSIZEMASSIVE = 5;

    //Chart Types
    public static final int CHARTTYPELINE = 1;
    public static final int CHARTTYPEBAR = 2;
    public static final int CHARTTYPEHORIZONTALBAR = 3;
    public static final int CHARTTYPE3DBAR = 4;
    public static final int CHARTTYPEHORIZONTAL3DBAR = 5;
    public static final int CHARTTYPEPIE = 6;
    public static final int CHARTTYPE3DPIE = 7;
    public static final int CHARTTYPESCATTERPLOT = 8;
    public static final int CHARTTYPESTEPCHART = 9;
    public static final int CHARTTYPEAREACHART = 10;
    public static final int CHARTTYPESTACKEDBARCHART = 11;
    public static final int CHARTTYPESTACKEDBARCHART3D = 12;
    public static final int CHARTTYPESTACKEDBARCHARTHORIZONTAL = 13;
    public static final int CHARTTYPESTACKEDBARCHART3DHORIZONTAL = 14;
    public static final int CHARTTYPESTACKEDAREA = 15;

    //Date Ranges
    public static final int DATERANGEALLTIME = 1;
    public static final int DATERANGETHISWEEK = 2;
    public static final int DATERANGETHISMONTH = 3;
    public static final int DATERANGETHISYEAR = 4;
    public static final int DATERANGELASTWEEK = 5;
    public static final int DATERANGELASTMONTH = 6;
    public static final int DATERANGELASTYEAR = 7;
    public static final int DATERANGELASTXDAYS = 8;
    public static final int DATERANGELASTXWEEKS = 9;
    public static final int DATERANGELASTXMONTHS = 10;
    public static final int DATERANGELASTXYEARS = 11;
    public static final int DATERANGESPECIFIED = 12;
    public static final int DATERANGELASTXUNITS = 13;
    public static final int DATERANGESAVEDSEARCH = 14;

    //Log access
    public static final int LOGACCESSPRIVATE = 0;
    public static final int LOGACCESSPUBLIC = 1;

    //Calendar types
    public static final int CALENDARTYPEEMPTYEVENTFINDER = 1;
    public static final int CALENDARTYPEEMPTYEVENTLINKS = 2;

    //The generic thumbnail copied to create thumbnails for uploaded media that is not thumbnailable
    public static final String THUMBNAILGENERIC = reger.core.WebAppRootDir.getWebAppRootPath() + "images"+java.io.File.separator+"mediatypeicons"+java.io.File.separator+"icon-unidentifiedfile.gif";

    //The image to display if a user doesn't have permission or if the file is not found
    public static final String ERRORIMAGE = reger.core.WebAppRootDir.getWebAppRootPath() + "images"+java.io.File.separator+"imageerror.jpg";

    //The default profile image for users without image profiles
    public static final String PROFILEGENERICIMAGE = "images/profilegenericimage.gif";

    //Gets environment vars from context
    public static Object getEnvVar(String varName){
        try {
            Context ctx = new InitialContext();
            if(ctx != null){
                //reger.core.Util.debug(5, "Reger.com: Environment var "+varName+" requested: " + ctx.lookup("java:comp/env/" + varName));
                //System.out.println("Reger.com: Environment var "+varName+" requested: " + ctx.lookup("java:comp/env/" + varName));
                return ctx.lookup("java:comp/env/" + varName);
            }
        } catch (Exception e){
            //System.out.println("Reger.com: Failed getting environment variable '"+varName+"' from context.");
            e.printStackTrace();
            Debug.debug(5, "", e);
        }

        return new String();
    }

    //http:// or https:// is determined by the context.xml
    private static String httpUrlPrefix = null;

    public static String getHttpUrlPrefix(){
        if (httpUrlPrefix==null){
            if (AllSystemProperties.getProp("SSLISON").equals("1")){
                httpUrlPrefix = "https://";
            } else {
                httpUrlPrefix = "http://";
            }
        }
        return httpUrlPrefix;
    }



}
