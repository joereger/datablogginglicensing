package reger.scheduledtasks;

import reger.core.scheduler.ScheduledTask;
import reger.core.db.Db;
import reger.licensingapi.LicenseDb;
import reger.licensingapi.VerisignException;

/**
 * Does billing for licensing server
 */
public class BillingTask implements ScheduledTask{

    private int numbercompleted = 0;

    public String getTaskName() {
        return "Billing";
    }

    public void doTask() {

        //-----------------------------------
        //-----------------------------------
        String[][] rstLic= Db.RunSQL("SELECT licenseid FROM license", 5000000);
        //-----------------------------------
        //-----------------------------------
        if (rstLic!=null && rstLic.length>0){
            for(int i=0; i<rstLic.length; i++){
                LicenseDb licDb = new LicenseDb(Integer.parseInt(rstLic[i][0]));
                try{
                    licDb.doBilling();

                } catch (VerisignException vex){
                    reger.core.Debug.debug(3, "BillingTask.java", vex);
                } catch (Exception e){
                    reger.core.Debug.errorsave(e, "BillingTask.java");
                }
            }
        }

    }

    public String getResult() {
        return String.valueOf(numbercompleted);
    }

    public int getRunEveryXMinutes() {
        return 60;
    }

    public boolean getOnlyRunOnceAtStartup() {
        return false;
    }

}
