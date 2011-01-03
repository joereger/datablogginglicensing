package reger.core.licensing;

import reger.core.Debug;
import reger.cache.providers.jboss.Cacheable;

import java.util.*;

/**
 * This class handles the main license rules for the installed version of the application.
 * The LM uses a single encoded string in the context.xml file to determine licensing properties.
 */
@Cacheable
public class License implements java.io.Serializable {

    //This absolutely can't change because old licenses won't work.
    private static String passPhrase = "pupper";
    public static int LICENSETYPEACCOUNTSUBSCRIPTION = 1;
    public static int LICENSETYPEPRIVATELABEL = 2;
    public static int LICENSETYPESERVER = 3;
    public static int CHARGEEVERYMONTHS = 1;
    public static int CHARGEEVERYYEARS = 2;

    //These are the properties that are encoded into each license
    //The PROPSTRING values can't change because old licenses will break
    public static String PROPSTRINGLICENSEID = "licenseid";
    public static String PROPSTRINGLICENSETYPE = "licensetype";
    public static String PROPSTRINGMAXUSERS = "maxusers";
    public static String PROPSTRINGMAXPRIVATELABELS = "maxprivatelabels";
    public static String PROPSTRINGISCOMMERCIAL = "iscommercial";
    public static String PROPSTRINGMINBASEACCOUNTPRICE = "minbaseaccountprice";
    public static String PROPSTRINGMINPRICEPER100MBSTORAGE = "minpriceper100mbstorage";
    public static String PROPSTRINGMINPRICEPERGBBANDWIDTH = "minpricepergbbandwidth";
    public static String PROPSTRINGMAXSPACEINBYTES = "maxspaceinbytes";
    public static String PROPSTRINGMAXBANDWIDTH = "maxbandwidth";
    public static String PROPSTRINGEXPDATEGMT = "expdategmt";
    public static String PROPSTRINGRANDOMSALT = "randomsalt";
    public static String PROPSTRINGISCHARGEDTOCREDITCARD = "ischargedtocreditcard";
    public static String PROPSTRINGAMOUNT = "amount";
    public static String PROPSTRINGCHARGEEVERY = "chargeevery";
    public static String PROPSTRINGCHARGEEVERYUNITS = "chargeeveryunits";
    public static String PROPSTRINGINDIVIDUALUSERSPAYTOUPGRADEACCOUNTS = "individualuserspaytoupgradeaccounts";
    public static String PROPSTRINGISRECURRINGBILLING = "isrecurringbilling";
    //End properties encoded into each license


    private HashMap props;
    private static HashMap allPropTypes;
    private String encryptedLicense = "";
    private String decryptedLicense = "";
    private License parentLicense;
    private boolean isvalidlicense = true;


    private static void loadPropTypes(){
        allPropTypes = new HashMap();
        synchronized(allPropTypes){
            allPropTypes = new HashMap();
            allPropTypes.put(PROPSTRINGLICENSEID, new LicenseProp(PROPSTRINGLICENSEID, "0"));
            allPropTypes.put(PROPSTRINGLICENSETYPE, new LicenseProp(PROPSTRINGLICENSETYPE, "0"));
            allPropTypes.put(PROPSTRINGMAXUSERS, new LicenseProp(PROPSTRINGMAXUSERS, "3"));
            allPropTypes.put(PROPSTRINGMAXPRIVATELABELS, new LicenseProp(PROPSTRINGMAXPRIVATELABELS, "1"));
            allPropTypes.put(PROPSTRINGISCOMMERCIAL, new LicenseProp(PROPSTRINGISCOMMERCIAL, "0"));
            allPropTypes.put(PROPSTRINGMINBASEACCOUNTPRICE, new LicenseProp(PROPSTRINGMINBASEACCOUNTPRICE, "2.95"));
            allPropTypes.put(PROPSTRINGMINPRICEPER100MBSTORAGE, new LicenseProp(PROPSTRINGMINPRICEPER100MBSTORAGE, ".35"));
            allPropTypes.put(PROPSTRINGMINPRICEPERGBBANDWIDTH, new LicenseProp(PROPSTRINGMINPRICEPERGBBANDWIDTH, ".5"));
            allPropTypes.put(PROPSTRINGMAXSPACEINBYTES, new LicenseProp(PROPSTRINGMAXSPACEINBYTES, "0"));
            allPropTypes.put(PROPSTRINGMAXBANDWIDTH, new LicenseProp(PROPSTRINGMAXBANDWIDTH, "0"));
            allPropTypes.put(PROPSTRINGEXPDATEGMT, new LicenseProp(PROPSTRINGEXPDATEGMT, reger.core.TimeUtils.dateformatfordb(reger.core.TimeUtils.nowInGmtCalendar())));
            allPropTypes.put(PROPSTRINGRANDOMSALT, new LicenseProp(PROPSTRINGRANDOMSALT, reger.core.RandomString.randomAlphanumeric(10)));
            allPropTypes.put(PROPSTRINGISCHARGEDTOCREDITCARD, new LicenseProp(PROPSTRINGISCHARGEDTOCREDITCARD, "0"));
            allPropTypes.put(PROPSTRINGAMOUNT, new LicenseProp(PROPSTRINGAMOUNT, "2.95"));
            allPropTypes.put(PROPSTRINGCHARGEEVERY, new LicenseProp(PROPSTRINGCHARGEEVERY, "1"));
            allPropTypes.put(PROPSTRINGCHARGEEVERYUNITS, new LicenseProp(PROPSTRINGCHARGEEVERYUNITS, String.valueOf(License.CHARGEEVERYMONTHS)));
            allPropTypes.put(PROPSTRINGINDIVIDUALUSERSPAYTOUPGRADEACCOUNTS, new LicenseProp(PROPSTRINGINDIVIDUALUSERSPAYTOUPGRADEACCOUNTS, "0"));
            allPropTypes.put(PROPSTRINGISRECURRINGBILLING, new LicenseProp(PROPSTRINGISRECURRINGBILLING, "0"));
        }
    }


    public License(License parentLicense, String encryptedLicense){
        if (allPropTypes==null){
            loadPropTypes();
        }
        loadEncryptedLicense(parentLicense, encryptedLicense);
    }

    public License(License parentLicense, HashMap props){
        if (allPropTypes==null){
            loadPropTypes();
        }
        createLicense(parentLicense, props);
    }

    public License(HashMap props){
        if (allPropTypes==null){
            loadPropTypes();
        }
        createLicense(null, props);
    }



    public static HashMap getAllPropTypes(){
        if (allPropTypes==null){
            loadPropTypes();
        }
        return allPropTypes;
    }


    /**
     * Populates the license with an encryptedLicense
     */
    private void loadEncryptedLicense(License parentLicense, String encryptedLicense){
        this.encryptedLicense = encryptedLicense;
        this.decryptedLicense = "";
        this.parentLicense = parentLicense;

        if (encryptedLicense!=null && !encryptedLicense.equals("")){
            //First three characters are the encryption encoding version of the key.
            //This is done for future enhancement and backwards-compatibility.
            String versionString = encryptedLicense.substring(0,3);
            int version = 1;
            if (reger.core.Util.isinteger(versionString)){
                version = Integer.parseInt(versionString);
            }

            //For future expansion I'll deal with other versioninfo
            if (version==1){
                decryptLicenseV1();
            }

            //Break props into Hashmap
            props = putLicensePropsIntoHashmap(decryptedLicense);
        }
    }

    private void createLicense(License parentLicense, HashMap props){
        this.encryptedLicense = "";
        this.decryptedLicense = "";
        this.parentLicense = parentLicense;
        this.props = props;

        //Start with an empty stringbuffer
        StringBuffer tmpLic = new StringBuffer();

        //Add props
        tmpLic.append(addHashtablePropsToLongString(props));

        //Add the randomSalt
        tmpLic = addNameValuePairToLicense(tmpLic, PROPSTRINGRANDOMSALT, reger.core.RandomString.randomAlphanumeric(6));

        //Set the decrypted version of the license
        this.decryptedLicense = tmpLic.toString();

        //Encrypt the license
        encryptLicenseV1();
    }

    public static StringBuffer addHashtablePropsToLongString(HashMap props){
        //Add each of the props
        StringBuffer tmpLic = new StringBuffer();
        Iterator keyValuePairs = props.entrySet().iterator();
        for (int i = 0; i < props.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String value = (String)mapentry.getValue();

            tmpLic = addNameValuePairToLicense(tmpLic, key, value);
        }
        return tmpLic;
    }


    public static StringBuffer addNameValuePairToLicense(StringBuffer tmpLic, String name, String value){
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

    /**
     * Decrypts en encrypted v1 license
     */
    private void decryptLicenseV1(){
        //Strip the version info off the front
        String encryptedLicenseMinusVersion = encryptedLicense.substring(3, encryptedLicense.length());
        //Create a decrypter object
        DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
        //Do the decryption
        decryptedLicense = encrypter2.decrypt(encryptedLicenseMinusVersion);
    }

    private void encryptLicenseV1(){
        //Create a decrypter object
        DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
        encryptedLicense = "001" + encrypter2.encrypt(decryptedLicense);
    }

    public static HashMap putLicensePropsIntoHashmap(String namevaluepairsaslongstring){
        Debug.debug(5, "", "License.java - putLicensePropsIntoHashmap()");
        HashMap out = null;
        if (namevaluepairsaslongstring!=null && !namevaluepairsaslongstring.equals("")){
            Debug.debug(5, "", "License.java - decryptedLicense!=null");
            out = new HashMap();
            //Now we have a string like: maxusers=50&maxlogs=45&maxsites=78&owner=Bob
            //Split on &
            String[] keyValuePairs = namevaluepairsaslongstring.split("&");
            for (int i = 0; i < keyValuePairs.length; i++) {
                try{
                    String key = keyValuePairs[i].split("=")[0];
                    String value = keyValuePairs[i].split("=")[1];
                    Debug.debug(5, "", "License.java - adding key=" + key + ", value=" + value);
                    out.put(key, value);
                } catch (Exception e){
                    Debug.errorsave(e, "");
                }
            }
        }
        return out;
    }

    public String getPropertyWithoutInheritingFromParent(String PROPSTRING){
        try{
            //Try this license
            if(props!=null && props.get(PROPSTRING)!=null && !(props.get(PROPSTRING)).equals("")){
                return (String)props.get(PROPSTRING);
            }
        } catch (Exception e){
            Debug.errorsave(e, "");
        }
        return "";    
    }

    public String getProperty(String PROPSTRING){
        try{
            //Try this license
            if(props!=null && props.get(PROPSTRING)!=null && !(props.get(PROPSTRING)).equals("")){
                return (String)props.get(PROPSTRING);
            //Try the parent license
            } else if (parentLicense!=null){
                return parentLicense.getProperty(PROPSTRING);
            }
            //Find the default
            if (allPropTypes!=null && allPropTypes.get(PROPSTRING)!=null){
                LicenseProp licenseProp = (LicenseProp)allPropTypes.get(PROPSTRING);
                return licenseProp.defaultValue;
            }
        } catch (Exception e){
            Debug.errorsave(e, "");
        }
        return "";
    }

    public String getEncryptedLicense() {
        return encryptedLicense;
    }

    public String getDecryptedLicense() {
        return decryptedLicense;
    }

    public HashMap getProps() {
        return props;
    }


    public License getParentLicense() {
        return parentLicense;
    }

    public static boolean licensesAreEqual(String encryptedLicense1, String encryptedLicense2){
        License lic1 = new License(null, encryptedLicense1);
        License lic2 = new License(null, encryptedLicense2);
        return licensesAreEqual(lic1, lic2);
    }

    public static boolean licensesAreEqual(License lic1, License lic2){
        StringBuffer debug = new StringBuffer();
        boolean theyMatch = true;
        HashMap props1 = new HashMap();
        HashMap props2 = new HashMap();
        if (lic1!=null){
            props1 = lic1.getProps();
            if (props1==null){
                props1 = new HashMap();
            }
        }
        if (lic2!=null){
            props2 = lic2.getProps();
            if (props2==null){
                props2 = new HashMap();
            }
        }

        //Start with lic1's props
        Iterator keyValuePairsA = props1.entrySet().iterator();
        for (int i = 0; i < props1.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairsA.next();
            String key = (String)mapentry.getKey();
            String value1 = (String)props1.get(key);
            String value2 = (String)props2.get(key);
            if (value1==null){
                value1="";
            }
            if (value2==null){
                value2="";
            }
            debug.append("<br>key=" + key + " value1=" + value1 + " value2=" + value2);
            if (!value1.equals(value2)){
                debug.append(" !!!!No Match!!!!");
                theyMatch = false;
            }
        }

        //Start with lic2's props
        Iterator keyValuePairsB = props2.entrySet().iterator();
        for (int i = 0; i < props2.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairsB.next();
            String key = (String)mapentry.getKey();
            String value1 = (String)props1.get(key);
            String value2 = (String)props2.get(key);
            if (value1==null){
                value1="";
            }
            if (value2==null){
                value2="";
            }
            debug.append("<br>key=" + key + " value1=" + value1 + " value2=" + value2);
            if (!value1.equals(value2)){
                debug.append(" !!!!No Match!!!!");
                theyMatch = false;
            }
        }

        Debug.debug(5, "", "License.java licensesAreEqual()<br>" + debug + "<br><br>Returning: " +theyMatch);

        return theyMatch;
    }

}
