package reger.licensingapi;

import reger.core.db.Db;
import reger.core.licensing.License;

import java.util.Hashtable;
import java.util.Calendar;
import java.util.Enumeration;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.Verisign.payment.PFProAPI;

/**
 * Communicates with Verisign.
 */
public class Verisign {

    private int licenseid = 0;

    private String verisignHostAddress = "test-payflow.verisign.com";
    private String verisignUser = "regercom";
    private String verisignPassword = "physics1";
    private String verisignVendor = "";
    private String verisignPartner = "verisign";
    private int    verisignHostPort = 443;
    private int    verisignTimeout = 30;
    private String verisignProxyAddress = "";
    private int    verisignProxyPort = 0;
    private String verisignProxyLogon = "";
    private String verisignProxyPassword = "";

    private Hashtable props;

    /**
     * You must have a licenseid before you can do any sort of charging
     * @param licenseid
     */
    public Verisign(int licenseid){
        this.licenseid = licenseid;
    }

    public Transaction chargeCard(String amt, String acct, String expmonth, String expyear, String street, String zip) throws VerisignException{
        if (expmonth.length()==1){
            expmonth = "0" + expmonth;
        }
        if (expyear.length()==1){
            expyear = "0" + expyear;
        }

        //Set the variables
        props = new Hashtable();
        props.put("AMT", String.valueOf(amt));
        props.put("ACCT", String.valueOf(acct));
        props.put("EXPDATE", expmonth + expyear);
        props.put("STREET", String.valueOf(street));
        props.put("ZIP", String.valueOf(zip));
        props.put("TRXTYPE", String.valueOf("S"));
        props.put("TENDER", String.valueOf("C"));
        //props.put("ACTION", String.valueOf("A"));

        //Make the call to verisign
        try{
            return transaction();
        } catch (VerisignException vex){
            throw vex;
        } catch (Exception e){
            reger.core.Util.errorsave(e);
            VerisignException ex = new VerisignException();
            ex.sentString = buildSubmitString();
            ex.receivedString = "";
            ex.recievedProps =  new Hashtable();
            throw ex;
        }
        
    }

    /**
     * This method creates a submitstring, sends it to Verisign, records the results to the database and returns the verisign result code
     */
    private Transaction transaction() throws VerisignException{

        //Submit the transaction to Verisign
        PFProAPI pn = new PFProAPI();
        pn.SetCertPath(reger.core.WebAppRootDir.getWebAppRootPath() + "/certs");
		pn.CreateContext(verisignHostAddress, verisignHostPort, verisignTimeout, verisignProxyAddress, verisignProxyPort, verisignProxyLogon, verisignProxyPassword);
		String VSResponsestring = pn.SubmitTransaction(buildSubmitString());
		pn.DestroyContext();

        //Put the response into a hash table of name/value pairs
        Hashtable VSResponseHash=parseNVpairs(VSResponsestring);

        //Record the details of the transaction to the database
        Transaction tr = new Transaction(reger.core.TimeUtils.nowInGmtCalendar(), buildSubmitString(), VSResponsestring, licenseid);
        tr.save();


        if (VSResponseHash==null || VSResponseHash.get("RESULT")==null || !VSResponseHash.get("RESULT").equals("0")){
            //Something went wrong... throw an exception
            VerisignException vex = new VerisignException();
            vex.sentString = buildSubmitString();
            vex.receivedString = VSResponsestring;
            vex.recievedProps =  VSResponseHash;
            if (VSResponseHash.get("RESPMSG")!=null){
                vex.errorMessage = (String)VSResponseHash.get("RESPMSG");
            } else {
                vex.errorMessage = "" + VSResponsestring;
            }
            throw vex;
        }

        //Get result to return
        return tr;
    }


    private String buildSubmitString(){
        StringBuffer submitString = new StringBuffer();
        //Start paramlist with authentication info
        submitString.append("USER=" + verisignUser + "&VENDOR=" + verisignVendor + "&PARTNER=" + verisignPartner + "&PWD=" + verisignPassword);
        //Add the props as a string
        if (!compilePropsString().equals("")){
            submitString.append("&"+compilePropsString());
        }
        return submitString.toString();
    }



    private String compilePropsString(){
        //Add each of the props
        StringBuffer tmp = new StringBuffer();
        if (props!=null){
            Enumeration keys = props.keys();
            while ( keys.hasMoreElements() ){
                String key = (String)keys.nextElement();
                String value = (String)props.get(key );
                tmp = addNameValuePair(tmp, key, value);
            }
        }

        return tmp.toString();
    }


    private StringBuffer addNameValuePair(StringBuffer tmpLic, String name, String value){
        if (name!=null && !name.equals("")){
            if (value!=null && !value.equals("")){
                if (tmpLic.length()>0){
                    tmpLic.append("&");
                }
                tmpLic.append(name+"="+value);
            }
        }
        return tmpLic;
    }




    public static Hashtable parseNVpairs(String instring){
        //This function parses a string of the format:
        //"name1=val1&name2=val2&name3=val3"
        //It returns a hash table of name/value pairs that can
        //be accessed with the syntax:
        //HashTable test=parseNVpairs("name1=val1&name2=val2&name3=val3")
        //out.println(test("name3"))

        Hashtable outHashtable = new Hashtable();
        reger.core.Util.debug(5, "Verisign.java parseNVPairs() instring=" + instring);

        if (instring!=null && !instring.equals("")){
            //Split on & to get nv pairs
            String[] splitarray = instring.split("&");
            //reger.core.Util.logStringArrayToDb("splitarray", splitarray);

            if (splitarray!=null && splitarray.length>0){
                for (int i = 0; i < splitarray.length; i++) {
                    //Split on = to split to name and value
                    String[] tmpArr = splitarray[i].split("=");
                    if (tmpArr!=null && tmpArr.length>=2){
                        //Add to the hashtable
                        reger.core.Util.debug(5, "Verisign.java parseNVPairs() outHashtable.put("+tmpArr[0]+", "+tmpArr[1]+")");
                        outHashtable.put(tmpArr[0], tmpArr[1]);
                    } else if (tmpArr!=null && tmpArr.length==1){
                        //Add to the hashtable
                        reger.core.Util.debug(5, "Verisign.java parseNVPairs() outHashtable.put("+tmpArr[0]+", )");
                        outHashtable.put(tmpArr[0], "");
                    }

                }
            }
        }
        //reger.core.Util.logHashTableToDb("outHashtable", outHashtable);
        return outHashtable;
    }








}

