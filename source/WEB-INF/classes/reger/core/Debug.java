package reger.core;

import java.util.*;

/**
 * Debug Class
 */
public class Debug {

    public static void logtodb(String whattolog, String label){
        System.out.println(label + ": " + whattolog);
        //-----------------------------------
        //-----------------------------------
        int identity = reger.core.db.Db.RunSQLInsert("INSERT INTO error(date, description, label) VALUES('"+ TimeUtils.nowInGmtString()+"', '"+ Util.cleanForSQL(whattolog) +"', '"+reger.core.Util.cleanForSQL(label)+"')");
        //-----------------------------------
        //-----------------------------------
    }

    public static void logtodb(byte[] whattolog, String label){
        StringBuffer mb = new StringBuffer();
        for (int i = 0; i < whattolog.length; i++) {
            mb.append(Byte.toString(whattolog[i]));
        }
        logtodb(mb.toString(), label);
    }

    /*
      * Saves an exception to the database with a recorded accountid.
      * Note that I only use DbNoErrorsave class here.  Otherwise an infinite loop can develop.
      */
    public static void errorsave(Throwable e, String label, int accountid, String message, javax.servlet.http.HttpServletRequest request){
        try {
            boolean doRecord = true;
            String prettyError = "";

            //Make the error pretty
            prettyError = ErrorDissect.dissect(e, request, message);

            try{
                if (e instanceof java.net.SocketException){
                    doRecord=false;
                }
            }catch (Throwable ee){
                //Do nothing
            }

            if (prettyError!=null && prettyError.indexOf("getOutputStream() has already been called")>-1){
                doRecord = false;
            }

            if (doRecord){

                //Try to find a current error with the same description
                //-----------------------------------
                //-----------------------------------
                String[][] rstCurrErr= reger.core.db.DbNoErrorsave.RunSQL("SELECT errorid FROM error WHERE description='"+ Util.cleanForSQL(prettyError)+"' AND label='"+reger.core.Util.cleanForSQL(label)+"' ORDER BY errorid DESC LIMIT 0,1");
                //-----------------------------------
                //-----------------------------------
                if (rstCurrErr!=null && rstCurrErr.length>0){
                    //Just update the count
                    //-----------------------------------
                    //-----------------------------------
                    int count = reger.core.db.DbNoErrorsave.RunSQLUpdate("UPDATE error SET count=count+1 WHERE errorid='"+rstCurrErr[0][0]+"'");
                    //-----------------------------------
                    //-----------------------------------
                } else {
                    //Couldn't find one... just add a new one
                    //-----------------------------------
                    //-----------------------------------
                    int identity = reger.core.db.DbNoErrorsave.RunSQLInsert("INSERT INTO error(date, description, accountid, count, label) VALUES('"+ TimeUtils.nowInGmtString()+"', '"+ Util.cleanForSQL(prettyError) +"', '"+accountid+"', '1', '"+reger.core.Util.cleanForSQL(label)+"')");
                    //-----------------------------------
                    //-----------------------------------
                }
            }
        } catch (Exception f){
            f.printStackTrace();
        }

    }

    public static void errorsave(Throwable e, String label, String message){
        errorsave(e, label, -1, message, null);
    }


    public static void errorsave(Throwable e, String label, String message, javax.servlet.http.HttpServletRequest request){
        errorsave(e, label, -1, message, request);
    }


    public static void errorsave(Throwable e, String label, int accountid){
        errorsave(e, label, accountid, "", null);
    }

    public static void errorsave(Throwable e, String label){
        errorsave(e, label, -1, "", null);
    }


    public static void errorsave(Throwable e, String label, int accountid, String message){
        errorsave(e, label, accountid, message, null);
    }







    public static void debug(int debugLevel, String label, String whatToLog){
        if (debugLevel<= DegubLevel.getDebugLevel()){
            logtodb(whatToLog, label);
        }
    }


    public static void debug(int debugLevel, String label, Throwable e){
        if (debugLevel<= DegubLevel.getDebugLevel()){
            errorsave(e, label, -1, "", null);
        }
    }








    //Log various things to db


    public static void logDoubleIntArrayToDb(String desc, int[][] array){
        StringBuffer tst = new StringBuffer();
        if (array==null){
            array = new int[0][0];
        }
        for (int i = 0; i < array.length; i++) {
            StringBuffer tstTmp = new StringBuffer();
            for (int j = 0; j < array[i].length; j++) {
                tstTmp.append("<br>" + array[i][j]);
            }
            tst.append("<br><b>Contents of Array["+i+"][]:</b>" + tstTmp);
        }
        logtodb(desc + "<br><b>Contents of the Double Array:</b>" + tst, "");
    }


    public static void logRequestObjectToDb(javax.servlet.http.HttpServletRequest request){
        String tmp="";

        tmp=tmp+"<br>" +"getCharacterEncoding: " + request.getCharacterEncoding();
        tmp=tmp+"<br>" +"getContentLength: " + request.getContentLength();
        tmp=tmp+"<br>" +"getContentType: " + request.getContentType();
        tmp=tmp+"<br>" +"getProtocol: " + request.getProtocol();
        tmp=tmp+"<br>" +"getRemoteAddr: " + request.getRemoteAddr();
        tmp=tmp+"<br>" +"getRemoteHost: " + request.getRemoteHost();
        tmp=tmp+"<br>" +"getScheme: " + request.getScheme();
        tmp=tmp+"<br>" +"getServerName: " + request.getServerName();
        tmp=tmp+"<br>" +"getServerPort: " + request.getServerPort();
        tmp=tmp+"<br>" +"getAuthType: " + request.getAuthType();
        tmp=tmp+"<br>" +"getMethod: " + request.getMethod();
        tmp=tmp+"<br>" +"getPathInfo: " + request.getPathInfo();
        tmp=tmp+"<br>" +"getPathTranslated: " + request.getPathTranslated();
        tmp=tmp+"<br>" +"getQueryString: " + request.getQueryString();
        tmp=tmp+"<br>" +"getRemoteUser: " + request.getRemoteUser();
        tmp=tmp+"<br>" +"getRequestURI: " + request.getRequestURI();
        tmp=tmp+"<br>" +"getServletPath: " + request.getServletPath();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
        String name = (String) headerNames.nextElement();
        String value = request.getHeader(name);
        tmp=tmp+"<br>request.getHeader(" + name + ") : " + value;
        }
        logtodb(tmp, "");
    }


    public static void logStringArrayToDb(String desc, String[] array){
        if (array!=null){
            StringBuffer tst = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                tst.append("<br>" + array[i]);
            }
            logtodb(desc + "<br>Contents of Array:" + tst, "");
        }
    }


    public static void logTreeMapToDb(String desc, TreeMap hash){
        StringBuffer tst = new StringBuffer();
        for (Iterator i=hash.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            tst.append("<br>key=" + e.getKey() + " - value=" + e.getValue());
        }
        logtodb(desc + "<br>Contents of TreeMap:" + tst, "");
    }


    public static void logHashTableToDb(String desc, Hashtable hash){
        StringBuffer tst = new StringBuffer();
        for ( Enumeration e = hash.keys() ; e.hasMoreElements() ; ) {
            // retrieve the object_key
            String object_key = (String) e.nextElement();
            // retrieve the object associated with the key
            String value = (String) hash.get ( object_key );
            tst.append("<br>key=" + object_key + " - value=" + value);
        }
        logtodb(desc + "<br>Contents of HashTable:" + tst, "");
    }


    public static void logHashMapToDb(String desc, HashMap hash){
        StringBuffer tst = new StringBuffer();
        for (Iterator i=hash.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            tst.append("<br>key=" + e.getKey() + " - value=" + e.getValue());
        }
        logtodb(desc + "<br>Contents of HashMap:" + tst, "");
    }


    public static void logDoubleStringArrayToDb(String desc, String[][] array){
        StringBuffer tst = new StringBuffer();
        tst.append("<table cellpadding=0 cellspacing=0 border=1>");
        for (int i = 0; i < array.length; i++) {
            String[] row = array[i];
            tst.append("<tr>");
            for (int j = 0; j < row.length; j++) {
                tst.append("<td valign=top>");
                tst.append(row[j]);
                tst.append("</td>");
            }
            tst.append("</tr>");
        }
        tst.append("</table>");
        logtodb(desc + "<br>Contents of Double Array:" + tst, "");
    }


    public static void logIntArrayToDb(String desc, int[] array){
        StringBuffer tst = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            tst.append("<br>" + array[i]);
        }
        logtodb(desc + "<br>Contents of Array:" + tst, "");
    }
}
