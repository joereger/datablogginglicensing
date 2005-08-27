package reger.licensingapi;

import java.util.Hashtable;

/**
 * An exception talking to Verisign
 */
public class VerisignException extends Throwable{

    public String sentString;
    public String receivedString;
    public Hashtable recievedProps;
    public String errorMessage;

    public String getUserFriendlyMessage(){
        //Custom errors
        if (recievedProps.get("RESULT")!=null){
            if (recievedProps.get("RESULT").equals("2")){
                return "Invalid tender type. Your merchant bank account does not support the credit card type that was submitted.";
            } else if (recievedProps.get("RESULT").equals("12")){
                return "Declined. Check the credit card number and transaction information to make sure they were entered correctly. If this does not resolve the problem, please call the credit card issuer to resolve.";
            } else if (recievedProps.get("RESULT").equals("13")){
                return "Referral. Transaction was declined but could be approved with a verbal authorization from the bank that issued the card. Please use another card or contact customer support.";
            } else if (recievedProps.get("RESULT").equals("23")){
                return "Invalid credit card number. Check and re-submit.";
            } else if (recievedProps.get("RESULT").equals("24")){
                return "Invalid expiration date. Check and re-submit.";
            }
        }
        //Go with the error msg sent
        if (recievedProps.get("RESPMSG")!=null){
            return String.valueOf(recievedProps.get("RESPMSG"));
        }
        //Unknown error
        return "An error has occurred.  Please try again.";
    }



}
