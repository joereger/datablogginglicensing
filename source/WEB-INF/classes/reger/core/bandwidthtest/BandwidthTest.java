package reger.core.bandwidthtest;

import reger.core.scheduler.ScheduledTask;
import reger.core.Debug;

import java.util.Vector;
import java.util.Calendar;

/**
 * Tests internet connectivity
 */
public class BandwidthTest implements ScheduledTask {

    public Calendar timeTestDone;
    public double speedInKbps = 0;
    public double speedInKbytesPs = 0;
    public boolean wasSuccessfulTest = false;

    public static String downloadUrl = "http://reviews.cnet.com/i/is/image500.jpg";
    public static double dataSizeInKb = 500;
    public static Vector allTestResults = new Vector();

    public BandwidthTest(){

    }

    public BandwidthTest(BandwidthTest bw){
        this.timeTestDone = bw.timeTestDone;
        this.speedInKbps = bw.speedInKbps;
        this.speedInKbytesPs = bw.speedInKbytesPs;
        this.wasSuccessfulTest = bw.wasSuccessfulTest;
    }

    public String getTaskName() {
        return "Bandwidth Connectivity and Speed Test";
    }


    public String getResult() {
        if (wasSuccessfulTest){
            return "Test successful. speedInKbps=" + speedInKbps;
        } else {
            return "Test failed.  No connectivity to " + downloadUrl;
        }
    }

    public int getRunEveryXMinutes() {
        return 180;
    }

    public boolean getOnlyRunOnceAtStartup() {
        return false;
    }




    public void doTask(){

        timeTestDone = Calendar.getInstance();

        try{
            //Start time
            Calendar startTime = Calendar.getInstance();

            //Download the image
            reger.http.Http http = new reger.http.Http();
            http.getUrl(downloadUrl, -1);

            if (http.successfulCallWasMade){

                //End Time
                Calendar endTime = Calendar.getInstance();

                //Calculations
                long millisElapsed = endTime.getTimeInMillis() - startTime.getTimeInMillis();
                double downloadTime = (((double)millisElapsed)/(double)1000);
                double kbps1    = ((double)dataSizeInKb/(double)downloadTime);
                double kbpsA    = ((kbps1*8)*10*1.02);
                double kbyteA   = ((dataSizeInKb*10)/downloadTime);
                double kbps     = (Math.round(kbpsA)/10);
                double kbyte    = (Math.round(kbyteA)/10);

                //Save the results
                speedInKbps = kbps;
                speedInKbytesPs = kbyte;
                wasSuccessfulTest = true;
            }
        } catch (Exception e){
            Debug.errorsave(e, "");
        }

        //Add this to the results vector
        synchronized(allTestResults){
            allTestResults.add(new BandwidthTest(this));
        }
    }

    public static Vector getAllTestResults() {
        return allTestResults;
    }
}
