package reger.core.licensing;

import reger.core.Debug;
import reger.systemproperties.AllSystemProperties;

import java.util.Vector;
import java.util.Hashtable;
import java.net.MalformedURLException;
import java.io.IOException;

import org.apache.xmlrpc.XmlRpcClient;

/**
 * Talks to the remote licensing server at reger.com to create and manage licenses
 */
public class RegerLicensingApiClient {

    private static String licensingServerUrl = AllSystemProperties.getProp("LICENSINGSERVERURL");

    public static Hashtable createLicense(String currentLicenseEncrypted, String proposedLicense, String name, String address1, String address2, String city, String state, String zip, String country, String ccnum, String ccexpmonth, String ccexpyear, String phone, String email){
        try{

            //Build up the parameters
            Vector params = new Vector ();
            if (currentLicenseEncrypted==null){
                currentLicenseEncrypted = "";
            }
            params.addElement(currentLicenseEncrypted);
            if (proposedLicense==null){
                proposedLicense = "";
            }
            params.addElement(proposedLicense);
            if (name==null){
                name = "";
            }
            params.addElement(name);
            if (address1==null){
                address1 = "";
            }
            params.addElement(address1);
            if (address2==null){
                address2 = "";
            }
            params.addElement(address2);
            if (city==null){
                city = "";
            }
            params.addElement(city);
            if (state==null){
                state = "";
            }
            params.addElement(state);
            if (zip==null){
                zip = "";
            }
            params.addElement(zip);
            if (country==null){
                country = "";
            }
            params.addElement(country);
            if (ccnum==null){
                ccnum = "";
            }
            params.addElement(ccnum);
            if (ccexpmonth==null){
                ccexpmonth = "";
            }
            params.addElement(ccexpmonth);
            if (ccexpyear==null){
                ccexpyear = "";
            }
            params.addElement(ccexpyear);
            if (phone==null){
                phone = "";
            }
            params.addElement(phone);
            if (email==null){
                email = "";
            }
            params.addElement(email);

            //Call the remote method
            XmlRpcClient xmlrpc = new XmlRpcClient(licensingServerUrl);
            Hashtable result = new Hashtable();
            //try{
                result = (Hashtable) xmlrpc.execute("regerlicensingapi.createLicense", params);
            //} catch (Exception e){
                //reger.core.Util.errorsave(e, "RegerLicensingApiClient.java");
            //}

            Debug.logHashTableToDb("Result after calling regerlicensingapi.createLicense", result);
            return result;

        } catch (MalformedURLException urle){
            Debug.errorsave(urle, "");
            return errorMessage("The URL for the licensing server is invalid.  Please contact reger.com for a fix.");
        } catch (IOException ioex){
            Debug.errorsave(ioex, "");
            return errorMessage("The datablogging server was unable to connect to the internet in order to communicate with the licensing server.");
        } catch (Exception e){
            Debug.errorsave(e, "");
            return errorMessage("There was an unknown error: " + e.getMessage());
        }
    }

    public static Hashtable isLicenseBillingOk(String encryptedLicense){
        try{

            //Build up the parameters
            Vector params = new Vector ();
            if (encryptedLicense==null){
                encryptedLicense = "";
            }
            params.addElement(encryptedLicense);

            //Call the remote method
            XmlRpcClient xmlrpc = new XmlRpcClient(licensingServerUrl);
            Hashtable result = (Hashtable) xmlrpc.execute("regerlicensingapi.isLicenseBillingOk", params);

            Debug.logHashTableToDb("Result after calling regerlicensingapi.isLicenseBillingOk", result);

            return result;
        } catch (MalformedURLException urle){
            Debug.errorsave(urle, "");
            return errorMessage("The URL for the licensing server is invalid.  Please contact reger.com for a fix.");
        } catch (IOException ioex){
            Debug.errorsave(ioex, "");
            return errorMessage("The datablogging server was unable to connect to the internet in order to communicate with the licensing server.");
        } catch (Exception e){
            Debug.errorsave(e, "");
            return errorMessage("There was an unknown error: " + e.getMessage());
        }

    }

    public static Hashtable cancelLicense(String encryptedLicense){
        try{

            //Build up the parameters
            Vector params = new Vector ();
            if (encryptedLicense==null){
                encryptedLicense = "";
            }
            params.addElement(encryptedLicense);

            //Call the remote method
            XmlRpcClient xmlrpc = new XmlRpcClient(licensingServerUrl);
            Hashtable result = (Hashtable) xmlrpc.execute("regerlicensingapi.cancelLicense", params);

            Debug.logHashTableToDb("Result after calling regerlicensingapi.cancelLicense", result);

            return result;
        } catch (MalformedURLException urle){
            Debug.errorsave(urle, "");
            return errorMessage("The URL for the licensing server is invalid.  Please contact reger.com for a fix.");
        } catch (IOException ioex){
            Debug.errorsave(ioex, "");
            return errorMessage("The datablogging server was unable to connect to the internet in order to communicate with the licensing server.");
        } catch (Exception e){
            Debug.errorsave(e, "");
            return errorMessage("There was an unknown error: " + e.getMessage());
        }
    }

    private static Hashtable errorMessage(String errorMessage){
        //Build the fail response
        Hashtable out = new Hashtable();
        out.put("successful", "false");
        out.put("errormessage", errorMessage);
        return out;
    }

}
