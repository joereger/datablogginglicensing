package reger.core;

import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

/**
 * Gives passwords as a hash
 */
public class PasswordHash {


    public static String getHash(String plaintextPassword){
        MessageDigest md = null;

        try{
          md = MessageDigest.getInstance("SHA"); //step 1
        }catch(Exception e){
            Debug.errorsave(e, "");
        }

        try{
            if (plaintextPassword!=null && md!=null){
                md.update(plaintextPassword.getBytes("UTF-8")); //step 2
            }
        }catch(Exception e){
            Debug.errorsave(e, "");
        }

        byte raw[] = md.digest(); //step 3
        String hash = (new BASE64Encoder()).encode(raw); //step 4
        return hash; //step 5
    }

}
