package reger.core.dbupgrade;

import java.io.BufferedReader;
import java.io.FileReader;
import reger.core.db.Db;
import reger.core.Debug;

/**
 * This class will execute a file of SQL commands
 */
public class ExecuteSQLFile {

    public static boolean execute(String filename){

        try{
            //Data reading buffer
            BufferedReader br = new BufferedReader(new FileReader(filename));
            //Temp storage line
            String line = "";
            String statement = "";
            while ((line = br.readLine()) != null) {

                //Append this line
                statement = statement + line;

                //Collect lines until we see a ; which indicates the end of a statement
                if (statement.endsWith(";")){
                    //Remove comments
                    //System.out.println("Reger.com DbUpgrade Pre :"+statement);
                    statement = statement.replaceAll("/\\*(.|\\n)*?\\*/", "");
                    //System.out.println("Reger.com DbUpgrade Post:"+statement);

                    //Execute the sucker
                    //-----------------------------------
                    //-----------------------------------
                    int count = Db.RunSQLUpdate(statement);
                    //-----------------------------------
                    //-----------------------------------

                    //Clear out the statement
                    statement = "";
                }

            }
        } catch (Exception e){
            System.out.println("Reger.com DbUpgrade: Problem running sql file for db version 0:" + e.getMessage());
            Debug.errorsave(e, "", "Problem running sql file:" + filename);
            return false;
        }
        return true;
    }

}
