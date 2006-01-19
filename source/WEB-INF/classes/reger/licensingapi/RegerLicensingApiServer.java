package reger.licensingapi;

import reger.core.licensing.License;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Handles licensing api requests
 */
public class RegerLicensingApiServer {

    //@todo Log all requests to DB for security/auditing capability

    public Hashtable createLicense(String encryptedCurrentLicense, String encryptedLicense, String name, String address1, String address2, String city, String state, String zip, String country, String ccnum, String ccexpmonth, String ccexpyear, String phone, String email){
        Hashtable out = new Hashtable();

        //@todo Check requiredness of fields

        reger.core.Debug.debug(3, "RegerLicensingApiServer.java", "Incoming RegerLicensingApiServer.java request.");

        //Get the name/value pairs out of the license
        License proposedLic = new License(null, encryptedLicense);
        HashMap licenseProps = proposedLic.getProps();

        //Create the license
        License lic = new License(null, licenseProps);

        //Create the new license
        LicenseDb licDb = new LicenseDb(lic.getEncryptedLicense(), lic.getDecryptedLicense(), true, true, name, address1, address2, city, state, zip, country, ccnum, ccexpmonth, ccexpyear, "", "", phone, email);

        //Get the name/value pairs out of the license
        License currentLic = new License(null, encryptedCurrentLicense);
        HashMap currentlicenseProps = proposedLic.getProps();

        //Get the licenseid of the current license
        int currentlicenseid = 0;
        if(reger.core.Util.isinteger(currentLic.getProperty(License.PROPSTRINGLICENSEID))){
            currentlicenseid = Integer.parseInt(currentLic.getProperty(License.PROPSTRINGLICENSEID));
        }


        //Compare by decrypting each string and examining
        LicenseDb testLicDb = new LicenseDb(currentlicenseid);
        License lic1 = new License(null, testLicDb.getEncryptedlicense());
        License lic2 = new License(null, encryptedCurrentLicense);

        reger.core.Debug.debug(3, "RegerLicensingApiServer.java", "RegerLicensingApiServer.java <br>currentlicenseid=" + currentlicenseid +"<br>testLicDb.getEncryptedlicense()=<br>" + testLicDb.getEncryptedlicense() + "<br>encryptedCurrentLicense=<br>"+encryptedCurrentLicense);
        if (License.licensesAreEqual(lic1, lic2)){
            //We have a valid incoming license
            //Set the licenseid of the licenseDb object so that we're editing, not creating
            licDb.setLicenseid(currentlicenseid);
            reger.core.Debug.debug(3, "RegerLicensingApiServer.java", "RegerLicensingApiServer.java <br>currentlicenseid=" + currentlicenseid +"<br>licDb.setLicenseid() was called.");
        } else {
            reger.core.Debug.debug(3, "RegerLicensingApiServer.java", "RegerLicensingApiServer.java <br>currentlicenseid=" + currentlicenseid +"<br>licDb.setLicenseid() was NOT called.");
        }

        //Save to db
        licDb.save();

        //Add the licenseid to the license props
        licenseProps.put(License.PROPSTRINGLICENSEID, String.valueOf(licDb.getLicenseid()));

        //Recreate the license
        License licWithId = new License(null, licenseProps);

        //Set the enc and dec license
        licDb.setEncryptedlicense(licWithId.getEncryptedLicense());
        licDb.setDecryptedlicense(licWithId.getDecryptedLicense());
        licDb.save();

        //Handle billing
        try{
            licDb.doBilling();
        } catch (VerisignException vex){
            return errorMessage(vex.getUserFriendlyMessage());
        }

        //Build the success response
        out = new Hashtable();
        out.put("successful", "true");
        out.put("encryptedlicense", licDb.getEncryptedlicense());



        return out;

    }



    public Hashtable createLicense(String encryptedLicense, LicenseDb licDb){
        return createLicense("", encryptedLicense, licDb.getName(), licDb.getAddress1(), licDb.getAddress2(), licDb.getCity(), licDb.getState(), licDb.getZip(), licDb.getCountry(), licDb.getCcnum(), String.valueOf(licDb.getCcexpmonth()), String.valueOf(licDb.getCcexpyear()), licDb.getPhone(), licDb.getEmail());
    }

    public Hashtable isLicenseBillingOk(String encryptedLicense){
        //Note that this calls the database to check on the last billing update.
        //This does not call the Verisign billing system.

        Hashtable out = new Hashtable();

        //Get the name/value pairs out of the license
        License proposedLic = new License(null, encryptedLicense);
        HashMap licenseProps = proposedLic.getProps();

        //Get licenseid
        int licenseid=0;
        if (licenseProps.get(License.PROPSTRINGLICENSEID)!=null){
            if (reger.core.Util.isinteger(String.valueOf(licenseProps.get(License.PROPSTRINGLICENSEID)))){
                licenseid = Integer.parseInt(String.valueOf(licenseProps.get(License.PROPSTRINGLICENSEID)));
            }
        }

        //Handle billing
        if (licenseid>0){
            LicenseDb licDb = new LicenseDb(licenseid);
            if (licDb.isIsbillingok()){
                out.put("successful", "true");
                return out;
            } else {
                out.put("successful", "false");
                out.put("errormessage", "There has been an error with your license: " + licDb.getBillingerror());
                return out;
            }
        }

        //Build the response
        out.put("successful", "false");
        out.put("errormessage", "Your license was not found in the system.");
        return out;
    }


    public Hashtable cancelLicense(String encryptedLicense){
        Hashtable out = new Hashtable();

        //Get the name/value pairs out of the license
        License lic = new License(null, encryptedLicense);
        if (reger.core.Util.isinteger(lic.getProperty(License.PROPSTRINGLICENSEID))){
            LicenseDb licDb = new LicenseDb(Integer.parseInt(lic.getProperty(License.PROPSTRINGLICENSEID)));

            //Make sure the incoming license matches the one in the database
            License lic1 = new License(null, licDb.getEncryptedlicense());
            License lic2 = new License(null, encryptedLicense);
            if (License.licensesAreEqual(lic1, lic2)){
                //Update the record to make it inactive
                licDb.setIsactive(false);
                licDb.save();
            }
        } else {
            return errorMessage("Sorry, license not found.");
        }

        //Build the success response
        out = new Hashtable();
        out.put("successful", "true");
        out.put("message", "The license has been canceled.");

        return out;
    }

    private Hashtable errorMessage(String errorMessage){
        //Build the fail response
        Hashtable out = new Hashtable();
        out.put("successful", "false");
        out.put("errormessage", errorMessage);
        return out;
    }

}
