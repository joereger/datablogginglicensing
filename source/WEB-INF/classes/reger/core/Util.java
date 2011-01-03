package reger.core;


import reger.Account;

import java.util.*;
import java.io.*;
import java.util.regex.*;
import javax.servlet.http.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;

public class Util {

    /**
    * This function accepts a string and removes/replaces special
    * characters to make sure that the string can be displayed inside
    * of form elements and other html
    */
    public static String cleanForHtml(String instring){
        if (instring!=null){
            instring=instring.replaceAll("\"", "&quot;");
            instring=instring.replaceAll("<", "&lt;");
            return instring;
        }
        return "";
    }



   

    public static String cleanDirectorySlashesUseSystemFileSeparator(String directoryOrFileName){
        return FilenameUtils.normalize(directoryOrFileName);
//        reger.core.Debug.debug(5, "Util.cleanDirectorySlashesUseSystemFileSeparator()", "File.separator=" + File.separator);
//        reger.core.Debug.debug(5, "Util.cleanDirectorySlashesUseSystemFileSeparator()", "directoryOrFileName before replace="+directoryOrFileName);
//        if (File.separator.equals("\\\\")){
//            reger.core.Debug.debug(5, "Util.cleanDirectorySlashesUseSystemFileSeparator()", "File.separator is backslash.");
//            directoryOrFileName.replaceAll("/", "\\\\");
//        } else if (File.separator.equals("/")){
//            reger.core.Debug.debug(5, "Util.cleanDirectorySlashesUseSystemFileSeparator()", "File.separator is forwardslash.");
//            directoryOrFileName.replaceAll("\\\\", "/");
//        }
//        reger.core.Debug.debug(5, "Util.cleanDirectorySlashesUseSystemFileSeparator()", "directoryOrFileName after replace="+directoryOrFileName);
//        return directoryOrFileName;
    }

  

    /**
    * This function accepts a string and removes/replaces special
    * characters to make sure that the string can be displayed inside
    * of form elements and other javascript
    */
    public static String cleanForjavascript(String instring){
        if (instring!=null){
            instring=instring.replaceAll("\"", "&quot;");
            instring=instring.replaceAll("'", "\\\\'");
            //instring=instring.replaceAll("<", "&lt;");
            return instring;
        }
        return "";
    }

    /**
    * Returns a String truncated to the specified max length
    */
    public static String truncateString(String instring, int maxlength){
        String outstring="";
        try{
            outstring = instring.substring(0,maxlength-1);
        } catch (Exception e) {
            outstring = instring;
        }
        return outstring;
    }

    /**
    * This function accepts a string and removes/replaces special
    * characters to make sure that the string can be displayed inside
    * of form elements and other html
    */
    public static String cleanForSQL(String instring){
        if (instring!=null && !instring.equals("")) {
            instring=instring.replaceAll("'", "''");
            instring=instring.replaceAll("\\\\", "\\\\\\\\");
            return instring;
        } else {
            return "";
        }
    }

    /**
    * This function accepts a string and removes/replaces special
    * characters to make sure that the string can be displayed inside
    * of form elements and other html.
    * This version uses a forward slash instead of double quotes
    *
    */
    public static String cleanForSQLWithSlashes(String instring){
        if (instring!=null && !instring.equals("")) {
            instring=instring.replaceAll("'", "\\'");
            return instring;
        } else {
            return "";
        }
    }

    /*
	 * Takes uri and returns name of script
	 */
    public static String getJspName(String uri){
        try{
            reger.core.Debug.debug(5, "Util.getJspName()", "uri=" + uri);
            String tmp[]=uri.split("/");
            if (tmp.length>1){
                reger.core.Debug.debug(5, "Util.getJspName()", "uri=" + uri + " length>1 so returning:" + tmp[tmp.length-1]);
                return tmp[tmp.length-1];
            }
        } catch (Exception e){
            Debug.debug(5, "Util.getJspName()", e);
        }
        reger.core.Debug.debug(5, "Util.getJspName()", "uri=" + uri+ "length not > 1 so returning uri:" + uri);
        return uri;
    }

    /*
      * Determines whether a string can be converted to an int
      */
    public static boolean isinteger(String str){
        if (str==null){
            return false;
        }
        try{
            Integer.parseInt(str);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /*
      * Determines whether a string can be converted to a long
      */
    public static boolean islong(String str){
        try{
            Long.parseLong(str);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    /*
      * Determines whether a string is numeric.
      */
    public static boolean isnumeric(String str){
        try{
            Double.parseDouble(str);
            return true;
        }catch(NumberFormatException e){
            return false;
        }catch(Exception e){
            return false;
        }
    }

    /*
      * Determines whether a particular int value is in an array.
      */
    public static boolean intIsInArray(int[] myArray, int value){
        for(int i=0; i<myArray.length; i++){
            if (myArray[i]==value){
                return true;
            }
        }
        return false;
    }

    /*
      * Accepts possibly null request.getParameter("page") and returns the correct
      * page number as an int
      */
    public static int getCurrentPage(String currpagein){
        int result=1;
        try {
            if (currpagein!=null){
                result=Integer.parseInt(currpagein);
            }
        } catch (Exception e) {
            result=1;
        }
        return result;
    }

    /*
      * Accepts possibly null request.getParameter("xxx") and returns the correct
      * page number as an int
      */
    public static int stringToInt(String in){
        int result=0;
        try {
            if (in!=null){
                result=Integer.parseInt(in);
            }
        } catch (Exception e) {
            result=0;
        }
        return result;
    }

    public static boolean arrayContainsValue(int[] array, int value){
       if (array!=null){
            for (int i = 0; i < array.length; i++) {
                if (array[i]==value){
                    return true;
                }
            }
       }
       return false;
    }


    /*
      * Determines whether yyyy, mm, dd is a valid date.
      */
    public static boolean isDate(int yyyy, int mm, int dd){
        try{
            Calendar cal = Calendar.getInstance();
            cal.set(yyyy,mm,dd);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /*
      * Overridde of isDate to accept strings
      */
    public static boolean isDate(String yyyy, String mm, String dd){
            int yyyyInt=Integer.parseInt(yyyy);
            int mmInt=Integer.parseInt(mm);
            int ddInt=Integer.parseInt(dd);
            return isTime(yyyyInt, mmInt, ddInt);
    }




    /*
      * Determines whether hour, min, sec is a valid time.
      */
    public static boolean isTime(int hour, int min, int sec){
        try{
            if(hour<0 || hour>23)return false;
            if(min<0 || min>59)return false;
            if(sec<0 || sec>59)return false;
            return true;
        }catch(RuntimeException e){
            return false;
        }
    }

    /*
      * Overridde of isTime to accept strings
      */
    public static boolean isTime(String hourStr, String minStr, String secStr){
            int hour=Integer.parseInt(hourStr);
            int min=Integer.parseInt(minStr);
            int sec=Integer.parseInt(secStr);
            return isTime(hour, min, sec);
    }




    /*
      * Returns the absolute value of a number
      */
   public static long qAbs(long num){
        if (num < 0) {
           return -1*num;
       } else {
           return num;
       }
   }


    /*
     * Count images for a particular event
     */
    public static int imageCount(int eventid){
        int imagecount=0;
        //-----------------------------------
        //-----------------------------------
        String[][] rs= reger.core.db.Db.RunSQL("SELECT count(*) FROM image WHERE eventid='" + eventid + "'");
        //-----------------------------------
        //-----------------------------------
        if(rs.length>0){
            imagecount=Integer.parseInt(rs[0][0]);
        }
        return imagecount;
    }

    /*
     * Count messages for a particular event
     */
    public static int messageCount(int eventid){
        int messagecount=0;
        //-----------------------------------
        //-----------------------------------
        String[][] rs= reger.core.db.Db.RunSQL("SELECT count(*) FROM message WHERE eventid='" + eventid + "'");
        //-----------------------------------
        //-----------------------------------
        if(rs.length>0){
            messagecount=Integer.parseInt(rs[0][0]);
        }
        return messagecount;
    }


    /*
     * Get a logid from an eventid
     */
    public static int getLogidFromEventid(int eventid, int accountid){
        int logid=-1;
        //-----------------------------------
        //-----------------------------------
        String[][] rs= reger.core.db.Db.RunSQL("SELECT logid FROM event WHERE eventid='"+ eventid +"' AND accountid='"+ accountid +"'");
        //-----------------------------------
        //-----------------------------------
        if (rs!=null && rs.length>0){
            if (isinteger(rs[0][0])) {
                logid=Integer.parseInt(rs[0][0]);
            }
        }
        return logid;
    }

    /*
     * Get a logid from an imageid
     */
    public static int getLogidFromImageid(int imageid, int accountid){
        int logid=-1;
        //-----------------------------------
        //-----------------------------------
        String[][] rs= reger.core.db.Db.RunSQL("SELECT logid FROM event, image WHERE event.eventid=image.eventid AND event.accountid='"+ accountid +"' AND image.imageid='"+imageid+"'");
        //-----------------------------------
        //-----------------------------------
        if (rs!=null){
            if (isinteger(rs[0][0])) {
                logid=Integer.parseInt(rs[0][0]);
            }
        }
        return logid;
    }



    /*
     * Convert a database 0/1 to a boolean
     */
    public static boolean dbToBoolean(String str){
        if (str.equals("1")) {
            return true;
        } else {
            return false;
        }
    }





    /*
     * Get a permission friendly name from a permission string
     */
    public static String getFriendlyNameFromAclobjectname(String aclobjectname){
        return reger.acl.AllAclObjects.getAclFriendlyNameByName(aclobjectname);
    }


    /*
      * Adds a row to the String array src with the value of String str
      */
    public static String[] addToStringArray(String[] src, String str){
        if (src==null){
            src=new String[0];
        }

        String[] outArr = new String[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }

    /**
     * reverses the contents of an array
     */
    public static String[] reverseStringArray(String[] src){
        if (src!=null){
            String[] out = new String[src.length];
            for (int i = 0; i < src.length; i++) {
                out[(src.length - 1) - i] =  src[i];
            }
            return out;
        } else {
            return null;
        }
    }

    /*
	 * Adds a row to the Object array src with the value of String str
	 */
    public static Object[] addToObjectArray(Object[] src, Object str){
        if (src==null){
            src=new Object[0];
        }

        Object[] outArr = new Object[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }


    /*
	 * Adds a row to the Object array src with the value of String str
	 */
    public static Cookie[] addToCookieArray(Cookie[] src, Cookie str){
        if (src==null){
            src=new Cookie[0];
        }

        Cookie[] outArr = new Cookie[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }



    /*
      * Adds a row to the String array src with the value of String str
      */
    public static int[] addToIntArray(int[] src, int str){
        //If the source isn't null
        if (src!=null){
            int[] outArr = new int[src.length+1];
            for(int i=0; i < src.length; i++) {
                outArr[i]=src[i];
            }
            outArr[src.length]=str;
            return outArr;
        //If the source is null, create an array, append str and return
        } else {
            int[] outArr = new int[1];
            outArr[0]=str;
            return outArr;
        }
    }

//	public static NewLogHolder[] addToNewLogHolderArray(NewLogHolder[] src, NewLogHolder str){
//		//If the source isn't null
//		if (src!=null){
//            NewLogHolder[] outArr = new NewLogHolder[src.length+1];
//            for(int i=0; i < src.length; i++) {
//                outArr[i]=src[i];
//            }
//            outArr[src.length]=str;
//            return outArr;
//        //If the source is null, create an array, append str and return
//        } else {
//            NewLogHolder[] outArr = new NewLogHolder[1];
//            outArr[0]=str;
//            return outArr;
//        }
//	}

    /*
	 * Contract an int array
	 */
    public static int[] contractIntArray(int[] src, int amounttocontract){
        //If the source isn't null
        if (src!=null){
            //Make sure we're not contracting too much
            if (amounttocontract>src.length){
                amounttocontract=src.length;
            }
            int[] outArr = new int[src.length-amounttocontract];
            for(int i=0; i < (src.length-amounttocontract); i++) {
                outArr[i]=src[i];
            }
            return outArr;
        //If the source is null, just return an empty array
        } else {
            int[] outArr = new int[0];
            return outArr;
        }
    }



    public static boolean isIntInIntArray(int lookingfor, int[] arraytosearch){
        if (arraytosearch!=null){
            for (int i = 0; i < arraytosearch.length; i++) {
                if (arraytosearch[i]==lookingfor){
                    return true;
                }
            }
        }
        return false;
    }

    /*
      * Appends to a hashmap value.  Assumes strings.
      */
    public static HashMap hashMapAppendValue(HashMap inHash, String key, String valuetoappend) {
        if (inHash.get(key)!=null) {
            //We must append
            inHash.put(key, inHash.get(key) + valuetoappend);
        } else {
            //Simply add
            inHash.put(key, valuetoappend);
        }
        return inHash;
    }

    /*
      * Prefill zeroes.  Example: 1 becomes 001.
      */
    public static String prefillZeroes(String inString, int finalLength) {
        String outString = "";
        if (inString.length()<finalLength) {
            int zeroesToAdd = finalLength - inString.length();
            for(int i=1; i<=zeroesToAdd; i++){
                outString=outString + "0";
            }
        }
        return outString + inString;
    }

    /*
      * Adds a row to the array, preserving existing data
      */
    public static byte[] extendByteArray(byte[] in){
        byte[] out = new byte[in.length+1];
        for(int i=0; i < in.length; i++){
            out[i]=in[i];
        }
        return out;
    }

    /**
     * Add single byte to a byte array
     */
    public static byte[] addByteToByteArray(byte[] in, byte newbyte){
        byte[] out = extendByteArray(in);
        out[out.length-1] = newbyte;
        return out;
    }

    /*
      * Adds a row to the double array src, which is indexed as [row][column]
      */
    public static String[][] extendArray(String[][] src, int columnCount){
        String[][] dest = new String[src.length+1][];
        for(int i=0; i < src.length; i++){
            dest[i]=src[i];
        }

        dest[dest.length-1]= new String[columnCount];
        for(int i=0; i < columnCount; i++){
            dest[dest.length-1][i]="";
        }

        return dest;
    }

    /*
      * Adds a row to the double array src, which is indexed as [row][column]
      */
    public static int[][] extendArray(int[][] src, int rowsToAdd){
        int[][] dest = new int[0][0];
        if (src!=null){
            dest = new int[src.length+rowsToAdd][];
            for(int i=0; i < src.length; i++){
                dest[i]=src[i];
            }

            //dest[dest.length-1]= new int[0];
            //dest[dest.length-1][i]=0;
        } else {
            dest = new int[rowsToAdd][0];
        }
        return dest;
    }

    /**
     * Combines two double int arrays by appending b to a.
     */
    public static int[][] combineTwoDoubleIntArrays(int[][] a, int[][] b){
        //Fix nulls
        if (a==null && b==null){
            return new int[0][0];
        }
        if (a!=null && b==null){
            return a;
        }
        if (a==null && b!=null){
            return b;
        }

        //Record the original size of a
        int origSize = a.length;
        if (b.length>0){
            //Extend a by the length of b
            a = reger.core.Util.extendArray(a, b.length);
            //Debug
            //reger.core.Util.logtodb("a.length.origSize:" + origSize + "<br>b.length:" + b.length + "<br>a.length.new:" + a.length);
            //Now add the values of b to a
            for(int i=origSize; i<a.length; i++){
                a[i] = b[i-origSize];
            }
        }
        return a;
    }

    /*
      * Removes a row from the double array src, which is indexed as [row][column]
      */
    public static String[][] contractArray(String[][] src, int rowToRemove)
    {
        if(src.length>0 && rowToRemove <= src.length){
            String[][] dest = new String[src.length-1][];
            int offset=0;
            for(int i=0; i < dest.length; i++){
                if(i==rowToRemove)
                    offset =1;
                dest[i]=src[i+offset];
            }

            return dest;
        }
        return src;
    }

    /*
      Takes aray and removes any spaces between rows
     */
    public static String[][] crunchArray(String[][] aray)
    {
      if (aray == null)
        return null;
      int x = 0;
      int y = 0;
      String[][] temp = new String[aray.length][aray[0].length];
      temp = cleanArray(temp);

      for (int i = 0; i < aray.length; i++){
        boolean doCopy = false;

        for (int j = 0; j < aray[i].length; j++){
          if (aray[i][j].trim().length() > 0)
            doCopy = true;
        }

        if (doCopy){
        for (int j = 0; j < aray[i].length; j++){
          temp[x][y] = aray[i][j];
          y++;
        }
        x++;
      }
        y=0;
      }
      return temp;
    }

    /**
     * Cleans an array.
     */
    public static String[][] cleanArray(String[][] aray)
    {
      if (aray == null)
        return null;
    for (int i = 0; i < aray.length; i++)
      for (int j = 0; j < aray[i].length; j++)
        aray[i][j]="";
    return aray;
    }

    /**
     * Gets the string representation of a day of the week from a number
     */
     public static String getDayOfWeek(int dayofweek){


        if (dayofweek==Calendar.SUNDAY){
            return "Sunday";
        } else if (dayofweek==Calendar.MONDAY){
            return "Monday";
        } else if (dayofweek==Calendar.TUESDAY){
            return "Tuesday";
        } else if (dayofweek==Calendar.WEDNESDAY){
            return "Wednesday";
        } else if (dayofweek==Calendar.THURSDAY){
            return "Thursday";
        } else if (dayofweek==Calendar.FRIDAY){
            return "Friday";
        } else if (dayofweek==Calendar.SATURDAY){
            return "Saturday";
        }

        return "NA";
     }

     /**
     * Gets the string representation of a day of the week from a number
     */
     public static String getDayOfWeekAbbreviated(int dayofweek){


        if (dayofweek==Calendar.SUNDAY){
            return "Sun";
        } else if (dayofweek==Calendar.MONDAY){
            return "Mon";
        } else if (dayofweek==Calendar.TUESDAY){
            return "Tue";
        } else if (dayofweek==Calendar.WEDNESDAY){
            return "Wed";
        } else if (dayofweek==Calendar.THURSDAY){
            return "Thu";
        } else if (dayofweek==Calendar.FRIDAY){
            return "Fri";
        } else if (dayofweek==Calendar.SATURDAY){
            return "Sat";
        }

        return "NA";
     }


    /**
     * Returns the last file extension of a file.
     */
    public static String getFilenameExtension(String infilename){
//        if (infilename!=null){
//            String[] parts=infilename.split("\\.");
//            int numofparts=parts.length;
//            if (numofparts>0){
//                String lastextension=parts[parts.length-1];
//                return lastextension;
//            }
//        }
//        return "";
        return FilenameUtils.getExtension(infilename);
    }

    /**
     * Returns the base filename of a file.  This is essentially the filename minus the extension.
     */
    public static String getFilenameBase(String infilename){
//	    if (infilename!=null && !infilename.equals("")){
//            String parts[]=infilename.split("\\.");
//            int numofparts=parts.length;
//            if (numofparts>1 && infilename.length()>0){
//                String lastextension=parts[parts.length-1];
//                //Calculate the starting point of the extention by subtracting the extension from the entire string
//                int startcharofextension = infilename.length() - (lastextension.length() + 1);
//                //Get the substring extension
//                String out=infilename.substring(0, startcharofextension);
//                return out;
//            }
//        }
//        return infilename;
        String filename = FilenameUtils.getName(infilename);
        String filenamebase = FilenameUtils.removeExtension(filename);
        return filenamebase;
    }



    /**
     * Simple file copy
     * @param source
     * @param dest
     */
    public static boolean copyFile(File source, File dest){
        try{
            FileUtils.copyFile(source, dest, true);
//             FileInputStream in = new FileInputStream(source);
//             FileOutputStream  out = new FileOutputStream(dest);
//             byte[] buffer = new byte[64000];
//
//             int read = 0;
//             while(read != -1){
//                read = in.read(buffer,0,buffer.length);
//                if(read!=-1){
//                    out.write(buffer,0,read);
//                }
//             }
//             out.flush();
//             in.close();
//             out.close();
        } catch (Exception e){
            Debug.errorsave(e, "");
            //@todo Uncomment this prior to production release.
            Debug.logtodb("Error copying file.<br>Source=" + source.getAbsolutePath() + "<br>Dest=" + dest.getAbsolutePath(), "");
            return false;
        }
        return true;
    }


    /**
     * Simple file copy method signature override
     * @param source
     * @param dest
     */
    public static boolean copyFile(String source, String dest){
        File src = new File(source);
        File dst = new File(dest);
        return copyFile(src, dst);
    }

    /**
     * Delete a file and fail with an error if necessary
     */
//     public static void deleteFile(String filePath) {
//        try {
//            //This is just a safety check to make sure I don't try to delete root or something crazy like that
//            if (!filePath.equals("") && !filePath.equals("thumbnails") && !filePath.equals("\\")){
//                //Get a file handler
//                File filehandler = new File(filePath);
//                filehandler.delete();
//            }
//        } catch (Exception e) {
//            Debug.errorsave(e, "");
//        }
//    }

    /**
     * Return the size in bytes of an incoming String.
     */
     public static int sizeInBytes(String instring) {
        int size=0;
        try {
            if (instring!=null){
                size = instring.length();
            }
        } catch (Exception e){
            //Do nothing
        }

        return size;
    }

    /**
     * Calculate the size in pixels that a graphic should be
     */
     public static int getImageIndicatorWidth(int indicator, int max, int desiredwidth){
         float ratio = 0;
         ratio= indicator/max;
         int outwidth=(int)(desiredwidth*ratio);
         if (outwidth>desiredwidth){
             outwidth=desiredwidth;
         }
         return outwidth;
     }

     public static StringBuffer getHtmlBarChart(float value, float max){
         return getHtmlBarChart(value, max, "");
     }


     /**
      * Output a stringbuffer with a table representing a bar graph.
      */
     public static StringBuffer getHtmlBarChart(float value, float max, String pathToAppRoot){
         StringBuffer bc = new StringBuffer();

         int leftwidth=0;
         int rightwidth=0;

         if (value<0){
            value=0;
         }

         leftwidth=(int)((value/max)*100);
         rightwidth=100-leftwidth;

         bc.append("<table cellpadding=0 cellspacing=0 border=0 width=100%>");
         bc.append("<tr>");
         bc.append("<td valign=top width="+leftwidth+"% align=left bgcolor=#ff0000>");
         bc.append("<img src='"+pathToAppRoot+"images/clear.gif' width=1 height=10>");
         bc.append("</td>");
         bc.append("<td valign=top width="+rightwidth+"% align=left bgcolor=#00ff00>");
         bc.append("<img src='"+pathToAppRoot+"images/clear.gif' width=1 height=10>");
         bc.append("</td>");
         bc.append("</tr>");
         bc.append("</table>");

         return bc;
     }





    public static StringBuffer pageFooter(String pathToAppRoot, reger.PrivateLabel pl){
        StringBuffer f = new StringBuffer();

//        f.append("<br>");
//
//        f.append("<table cellspacing='0' cellpadding='0' width=100% border='0'>");
//        f.append("<tr>");
//        f.append("<td valign='top' align=right bgcolor=#ffffff>");
//        f.append("<font face=arial size=-2><a href='"+pathToAppRoot+"about/terms-of-service.log'>"+pl.getTermsofuselinktext()+"</a>  &nbsp;&nbsp;  <a href='"+pathToAppRoot+"about/feedback.log' target=feedbackform>"+pl.getFeedbacklinktext()+"</a></font>");
//        f.append("</td>");
//        f.append("</tr>");
//        f.append("</table>");
//
//        f.append("<br><br><br>");

        return f;
    }

    public static StringBuffer poweredby(String pathToAppRoot){
        StringBuffer f = new StringBuffer();

//        f.append("<br clear=all>");
//
//        //f.append("<table cellspacing='0' cellpadding='0' width=100% border='0'>");
//        //f.append("<tr>");
//        //f.append("<td align=right>");
//        //f.append("<a href='http://www.reger.com/'><img src=images/reger-poweredby.gif border=0 align=right></a>");
//        //f.append("</td>");
//        //f.append("</tr>");
//        //f.append("</table>");
//
//        f.append("<br><br>");

        return f;
    }

    /**
     * Simply checks to see if this string is a null and if so converts it to a blank string.
     * @param in
     */
    public static String getParameterClean(String in){
        if (in==null){
            return "";
        }
        return in;
    }

    /**
     * Returns javascript html code for popups.
     * Calling code:
     * "<a href='someurl.html' onclick=\"javascript:NewWindow(this.href,'name','0','0','yes');return false;\">"
     */
     public static StringBuffer popup(){
        StringBuffer pp = new StringBuffer();

        pp.append("\n" + "<script language=\"JavaScript\"><!--" + "\n");
        pp.append("function NewWindow(mypage, myname, w, h, scroll) {" + "\n");
        //pp.append("var w = 850, h = 750;" + "\n");
        //pp.append("if (window.screen) {" + "\n");



        pp.append("if (w>0) {" + "\n");
        pp.append("//Do nothing... we're using the input values" + "\n");
        pp.append("} else if (window.screen) {" + "\n");
        pp.append("w = window.screen.availWidth - 50;" + "\n");
        pp.append("} else {" + "\n");
        pp.append("w = 800" + "\n");
        pp.append("}" + "\n");

        pp.append("if (h>0) {" + "\n");
        pp.append("//Do nothing... we're using the input values" + "\n");
        pp.append("} else if (window.screen) {" + "\n");
        pp.append("h = window.screen.availHeight - 100;" + "\n");
        pp.append("} else {" + "\n");
        pp.append("h = 600" + "\n");
        pp.append("}" + "\n");



        pp.append("var percent = 90;" + "\n");

        pp.append("winprops = 'height='+ h +',width='+ w +',top=25,left=25,scrollbars='+ scroll +',resizable';" + "\n");
        pp.append("win = window.open(mypage, myname, winprops)" + "\n");
        pp.append("if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }" + "\n");
        pp.append("}" + "\n");
        pp.append("//--></script>" + "\n");

        return pp;
    }


    public static StringBuffer popupCloseReturnToEntryJavascript(){
        StringBuffer mb = new StringBuffer();
        mb.append("\n" + "<script language=\"JavaScript\"><!--" + "\n");
        mb.append("function returnToEntryPage() {" + "\n");
        mb.append("    opener.document.entryform.action.value = \"refresh\";" + "\n");
        mb.append("    opener.document.entryform.submit();" + "\n");
        mb.append("    window.close();" + "\n");
        mb.append("}" + "\n");
        mb.append("//--></script>" + "\n");
        return mb;
    }

//    public static StringBuffer leaveEntryPageJavascript(){
//        StringBuffer mb = new StringBuffer();
//        mb.append("\n" + "<script language=\"JavaScript\"><!--" + "\n");
//        mb.append("function leaveEntryPage(href) {" + "\n");
//        mb.append("    document.entryform.action.value = \"leavepage\";" + "\n");
//        mb.append("    document.entryform.gotopage.value = href;" + "\n");
//        //This submitPost() is a javascript method required by any editor that I put in place.  It can be a simple wrapper method that just calls document.entryform.submit()
//        mb.append("    submitPost();");
//        mb.append("}" + "\n");
//        mb.append("//--></script>" + "\n");
//        return mb;
//    }








    /**
     * Cleans a string for proper xml presentation
     */
    public static String xmlclean(String instring){
        String xmlclean="";
        if (instring!=null) {
            if (!instring.equals("")) {

                instring=instring.replaceAll("<", "&lt;");
                instring=instring.replaceAll(">", "&gt;");
                instring=instring.replaceAll("&nbsp;", " ");
                instring=instring.replaceAll("&", "&amp;");
                instring=instring.replaceAll("’", "'");



                xmlclean=instring;
            } else {
                xmlclean=" ";
            }
        } else {
            xmlclean=" ";
        }
        return xmlclean;
    }

    /**
     * Cleans a string to make it work as an xml <fieldname></fieldname>.
     * Removes spaces and any non-alphanumeric chars
     */
    public static String xmlFieldNameClean(String in) {
        String out="";

        Pattern p = Pattern.compile("\\W");
        Matcher m = p.matcher(in);
        out = m.replaceAll("");

        return out;
    }

    /**
     * Gets a value from a cookie array
     * @param cookies
     * @param cookieName
     * @param defaultValue
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        try{
            if (cookies!=null){
                for(int i=0; i<cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookieName.equals(cookie.getName())){
                        return(cookie.getValue());
                    }
                }
            } else {
                return(defaultValue);
            }
        } catch (Exception e){
            Debug.debug(5, "", e);
            return(defaultValue);
        }
        return(defaultValue);
    }

    /**
     * Tells me whether the supercookie is on.
     * @param request
     * @param accountid
     */
    public static boolean isSupercookieOn(javax.servlet.http.HttpServletRequest request, int accountid) {
        try{
            if (request!=null && request.getCookies()!=null){
                if (getCookieValue(request.getCookies(), "supercookie-" + accountid, "off").equals("supercookieon")){
                    return true;
                }
            }
        } catch (Exception e){
            Debug.debug(5, "", e);
        }
        return false;
    }

    /**
     * Tells me whether the mastercookie is on.
     * @param request
     */
    public static boolean isMastercookieOn(javax.servlet.http.HttpServletRequest request) {
        try {
            if (getCookieValue(request.getCookies(), "mastercookie", "off").equals("mastercookieon")){
                return true;
            }
        } catch (Exception e){
            Debug.errorsave(e, "");
        }
        return false;
    }





    /**
     * Javascript to disable all links on a page
     */
     public static StringBuffer disableLinksJavascript(String pathToAppRoot){
        StringBuffer mb = new StringBuffer();

        mb.append("<script language=\"Javascript\">");
        mb.append("function makeLinksDead(){");
            mb.append("var allLinks = document.links;");
            mb.append("var msg=document.getElementById('alertMSG').onclick;");
            mb.append("for(i=0;i<allLinks.length;i++){");
                mb.append("allLinks[i].href='#';");
                mb.append("allLinks[i].disabled=true;");
                mb.append("allLinks[i].onclick=msg;");
            mb.append("}");
        mb.append("}");
        mb.append("</script>");
        mb.append("<img src='"+pathToAppRoot+"images/clear.gif' width=0 height=0 onLoad=\"makeLinksDead()\">");
        mb.append("<a href=\"#\" name=\"alertMSG\" onclick=\"alert('Links on this preview page have been disabled.');return false;\" style=\"display:none;\"></");

        return mb;
    }

    /**
     * Get a random number between zero and X
     *
     */
     public static int randomInt(int max){
        return (int)(Math.random()*(max+1));
    }

    /**
     * Clean for appendreplacement.  Java is a real pain in the arse when it comes to dollar signs and dashes.
     * This method must be called on any appendreplacement string.
     */
     public static String cleanForAppendreplacement(String in){
        String out = "";
        if(in!=null){
            out=in.replaceAll("\\\\","\\\\\\\\").replaceAll("\\$", "\\\\\\$");
        }

        return out;
    }

    /**
     * Outputs the html to display a screenshot popup.
     * Note that the page that uses this must have a single instance of popup()
     * so that the script is available.
     */
    public static StringBuffer screenshotWithThumbnail(String imagename){
        StringBuffer mb = new StringBuffer();

        mb.append("<table cellpadding=2 cellspacing=3 border=0 bgcolor=#cccccc>");
        mb.append("<tr>");
        mb.append("<td align=center bgcolor=#ffffff>");
        mb.append("<a href='imagedisplay.log?imagename="+imagename+"' onclick=\"javascript:NewWindow(this.href,'name','0','0','yes');return false;\">");
        mb.append("<img src='images/logscreenshots/thumbnails/"+imagename+"' alt='' border='0'>");
        mb.append("<br>");
        mb.append("<font face=arial size=-2 style=\"font-size: 11px; font-weight:900;\">");
        mb.append("+ ZOOM SCREENSHOT");
        mb.append("</font>");
        mb.append("</a>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("</table>");

        return mb;
    }

    public static StringBuffer screenshotBizWithThumbnail(String imagename){
        StringBuffer mb = new StringBuffer();

        mb.append("<table cellpadding=2 cellspacing=3 border=0 bgcolor=#cccccc>");
        mb.append("<tr>");
        mb.append("<td align=center bgcolor=#ffffff>");
        mb.append("<a href='imagedisplay.log?imagename="+imagename+"' onclick=\"javascript:NewWindow(this.href,'name','0','0','yes');return false;\">");
        mb.append("<img src='images/databloggingscreens/thumbs/"+imagename+"' alt='' border='0'>");
        mb.append("<br>");
        mb.append("<font face=arial size=-2 style=\"font-size: 11px; font-weight:900;\">");
        mb.append("+ ZOOM SCREENSHOT");
        mb.append("</font>");
        mb.append("</a>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("</table>");

        return mb;
    }



    /**
     * Outputs the html to display a screenshot popup.
     * Note that the page that uses this must have a single instance of popup()
     * so that the script is available.
     */
    public static StringBuffer screenshotTextLink(String imagename, String pathToAppRoot){
        StringBuffer mb = new StringBuffer();
        mb.append("<font face=arial size=-2 style=\"font-size: 8px; font-weight:900;\">");
        mb.append("<a href='"+pathToAppRoot+"about/imagedisplay.log?imagename="+imagename+"' onclick=\"javascript:NewWindow(this.href,'name','0','0','yes');return false;\">");

        mb.append("SCREENSHOT");

        mb.append("</a>");
        mb.append("</font>");


        return mb;
    }

    /**
     * Reads the contents of a text file and puts it into a StringBuffer
     */
     public static StringBuffer textFileRead(String filename){
        StringBuffer sb = new StringBuffer();

        File file = new File(filename);
        char[] chars = new char[(int) file.length()];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            for(int i = 0; i < 10000; i++) {
                reader.read(chars);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Debug.errorsave(e, "");
        } catch (IOException e) {
            Debug.errorsave(e, "");
        }

        sb.append(new String(chars));

        return sb;
    }



    /**
     * Reads the contents of a text file and puts it into a StringBuffer
     */
     public static StringBuffer textFileRead(File file){
        StringBuffer sb = new StringBuffer();

        //File file = new File(filename);
        char[] chars = new char[(int) file.length()];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            for(int i = 0; i < 10000; i++) {
                reader.read(chars);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Debug.errorsave(e, "");
        } catch (IOException e) {
            Debug.errorsave(e, "");
        }

        sb.append(new String(chars));

        return sb;
    }








    /**
     * Verifies that a Email is of the correct form... not that it matches any account.
     */
    public static String validateEmail(String email){
        String errortext = "";

        //Make sure email isn't blank
        if (email==null || email.equals("")) {
            return "Your email can't be blank.<br>";
        }
        //Email can't have spaces
        if (email.split(" ").length>1){
            return "No spaces or other characters are allowed in the email.<br>";
        }
        int index = email.indexOf("@");
        // Make sure if EMail has @ symbol
        if (index > -1) {
            // Make sure if EMail has . symbol
            if (email.indexOf(".", index) == -1) {
                return "Please enter valid email.<br>";
            }
        } else {
           return "Please enter valid email.<br>";
        }
        return errortext;
    }

    public static String validateServername(String accounturl){
        return Account.validateServername(accounturl, -1);
    }


    public static TreeMap requestToTreeMap(javax.servlet.http.HttpServletRequest request){
        TreeMap out = new TreeMap();

        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            out.put(name, request.getParameter(name));
        }

        return out;
    }




    /**
     * Looks for a word in context, similar to the way that search engines find the word you've searched for in context.
     * If no match is found it returns the top X chars of the string.
     * @param stringToSearch
     * @param stringToMatch
     * @param charsAroundToReturn
     */
    public static String returnSubstringAroundMatch(String stringToSearch, String stringToMatch, int charsAroundToReturn){
        //Find the start of the match
        int center = stringToSearch.indexOf(stringToMatch);
        //Add half of the length of the match so that we're choosing a range around the center
        center = center + stringToMatch.length();
        //Find the start of what we're going to return
        int start = center - charsAroundToReturn;
        //Find the end
        int end = center + charsAroundToReturn;
        //Deal with limits of string
        if (start<0){
            start=0;
        }
        if (end>stringToSearch.length()){
            end = stringToSearch.length();
        }
        //Get the return
        String tmp = stringToSearch.substring(start, end);

        return tmp;
    }


    public static String cleanForHtmlAdvanced(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++)
            {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                    }
                else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                    }
                }
            else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("&lt;br/&gt;");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160 )
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                        }
                    }
                }
            }
        return sb.toString();
    }

    /**
     * Determines whether to output an http:// or https:// prefix based on whether or not the current request is secure now.
     */
//    public static String getHttpOrHttpsString(javax.servlet.http.HttpServletRequest request){
//        if (request.isSecure()){
//            return "https://";
//        }
//        return "http://";
//    }


    public static String stripHttpHttpsAndTrailingSlashFromUrl(String siteUrl){
        //Trailing slashes
        if (siteUrl.length()>=2){
            String lastChar = siteUrl.substring(siteUrl.length()-1, siteUrl.length());
            if (lastChar.equals("/") || lastChar.equals("\\")){
                siteUrl = siteUrl.substring(0, siteUrl.length()-1);
            }
        }
        //http://
        if (siteUrl.length()>=7){
            String firstSevenChars = siteUrl.substring(0, 7);
            if (firstSevenChars.equals("http://")){
                siteUrl = siteUrl.substring(7, siteUrl.length());
            }
        }
        //https://
        if (siteUrl.length()>=7){
            String firstEightChars = siteUrl.substring(0, 8);
            if (firstEightChars.equals("https://")){
                siteUrl = siteUrl.substring(8, siteUrl.length());
            }
        }
        return siteUrl;
    }

    public static String serializeToString(Object obj){
        String str = "";
        try{
            ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(arrayOut);
            objOut.writeObject(obj);
            objOut.close();
            str = arrayOut.toString();
        } catch (Exception e){
            Debug.errorsave(e, "");
        }
        return str;
    }




//    /**
//     * Returns the bytes of an image.
//     *
//     * @param image to be converted to bytes
//     * @param bkg the color to be used for alpha-multiplication
//     * @param code ARGB, A, or BGR, ... you may also use *ARGB to pre-multiply with alpha
//     * @param pad number of bytes to pad the scanline with (1=byte, 2=short, 4=int, ...)
//     */
//    public static byte[] getBytesFromBufferedImage(RenderedImage image, Color bkg, String code, int pad) {
//        if (pad < 1) pad = 1;
//
//        Raster raster = image.getData();
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        boolean preMultiply = (code.charAt(0) == '*');
//        if (preMultiply) code = code.substring(1);
//
//        int pixelSize = code.length();
//
//        int size = width*height*pixelSize;
//        size += (width % pad)*height;
//        int index = 0;
//        byte[] bytes = new byte[size];
//
//        for (int y=0; y<height; y++) {
//            for (int x=0; x<width; x++) {
//
//                int[] rgba = raster.getPixel(x, y, (int[])null);
//
//                // Check the transparancy. If transparent substitute
//                // the background color.
//                if (preMultiply && (rgba.length > 3)) {
//                    if (bkg == null) bkg = Color.BLACK;
//                    double alpha = rgba[3]/255.0;
//                    rgba[0] = (int)(alpha*rgba[0]+(1-alpha)*bkg.getRed());
//                    rgba[1] = (int)(alpha*rgba[1]+(1-alpha)*bkg.getGreen());
//                    rgba[2] = (int)(alpha*rgba[2]+(1-alpha)*bkg.getBlue());
//                }
//
//                for (int i=0; i<code.length(); i++) {
//                    switch (code.charAt(i)) {
//                        case 'a':
//                        case 'A':
//                            bytes[index] = (rgba.length > 3) ? (byte)rgba[3] : (byte)0xFF;
//                            break;
//
//                        case 'r':
//                        case 'R':
//                            bytes[index] = (byte)rgba[0];
//                            break;
//
//                        case 'g':
//                        case 'G':
//                            bytes[index] = (byte)rgba[1];
//                            break;
//
//                        case 'b':
//                        case 'B':
//                            bytes[index] = (byte)rgba[2];
//                            break;
//
//                        default:
//                            reger.core.Util.logtodb("Util.java - getBytesFromBufferedImage(). Invalid code in '"+code+"'");
//                            break;
//                    }
//                    index++;
//                }
//            }
//            for (int i=0; i<(width % pad); i++) {
//                bytes[index] = 0;
//                index++;
//            }
//        }
//
//        return bytes;
//    }


    public static boolean valueIsInIntArray(int[] array, int value){
        for (int i = 0; i < array.length; i++) {
            if(array[i]==value){
                return true;
            }
        }
        return false;
    }

    public static String booleanAsSQLText(boolean bool){
        if (bool){
            return "1";
        } else {
            return "0";
        }
    }

    public static boolean booleanFromSQLText(String text){
        if (text!=null && text.equals("1")){
            return true;
        }
        return false;
    }

    public static String parseInputStringToString(java.io.InputStream is){
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try{
            String line = null;
            while((line=din.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){

            }
        }
        return sb.toString();
    }




}