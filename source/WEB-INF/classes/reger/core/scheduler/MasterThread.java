package reger.core.scheduler;

import reger.core.db.Db;
import reger.core.db.DbConfig;
import reger.core.Util;
import reger.core.Debug;

import java.util.Calendar;

/**
 * Sends automates email subscription emails
 */
public class MasterThread extends Thread {


    private ScheduledTask[] scheduledTask;
	public String threadstartedat;
	public int secondstosleep=60;
	public int masterthreadid=1;
	public int numberoftimesrun = 0;
	public String currentStatus = "New Thread.";

	private int threadCount = 0;



    public MasterThread() {
       threadCount = threadCount+1;
       System.out.println("Reger.com MasterThread Started:" + threadCount);
    }

    public MasterThread(ScheduledTask[] tasks){
        this.scheduledTask = tasks;
    }


    public void init() {
        this.setPriority(Thread.MIN_PRIORITY);
        run();
    }

    public void addScheduledTask(ScheduledTask task){
        if (scheduledTask==null){
            scheduledTask = new ScheduledTask[0];
        }
		ScheduledTask[] outArr = new ScheduledTask[scheduledTask.length+1];
		for(int i=0; i < scheduledTask.length; i++) {
			outArr[i]=scheduledTask[i];
		}
		outArr[scheduledTask.length]=task;
		scheduledTask = outArr;
    }


    public void run() {
		try{

			//Make sure we have a valid db connection
            if (!DbConfig.haveValidConfig()){
                return;
            }

		    //Check database version.  Runs one time.
		    currentStatus = "Running database upgrade check.";
		    reger.core.dbupgrade.UpgradeCheckAtStartup dbcheck = new reger.core.dbupgrade.UpgradeCheckAtStartup();
		    dbcheck.doCheck();

		    //If we don't have a valid database version, return
		    if (!reger.core.dbupgrade.RequiredDatabaseVersion.havecorrectversion){
                return;
            }

            //Setup the running vars for this thread
			setupThread();

            //Load the system properties
            try{
                reger.systemproperties.AllSystemProperties.loadProperties();
            } catch (Exception e){
                Debug.errorsave(e, "");
            }

            //Refresh the account cache, now that we have a database connection
            try{
                reger.cache.AccountCache.flush();
            } catch (Exception e){
                Debug.errorsave(e, "");
            }

            while (true) {
                //Increment the thread counter
                numberoftimesrun=numberoftimesrun+1;

                //Go through all of the tasks
                if(scheduledTask!=null){
                    for (int i = 0; i < scheduledTask.length; i++) {
                        try {
                            //If it's not a one-time run and it hasn't been run in the last x minutes OR if it is a one-time run and it's the first time the thread's been run
                            if ( (!wasTaskRunInLastXminutes(scheduledTask[i].getTaskName(), scheduledTask[i].getRunEveryXMinutes()) && !scheduledTask[i].getOnlyRunOnceAtStartup()) || (scheduledTask[i].getOnlyRunOnceAtStartup() && numberoftimesrun==1) ){
                                //Set the current status
                                currentStatus = scheduledTask[i].getTaskName();
                                //Start the stopwatch
                                reger.executionTime executionTime = new reger.executionTime();
                                //Run the task
                                scheduledTask[i].doTask();
                                //Stop the stopwatch
                                long time = executionTime.getElapsedMillis();
                                //Update the task status table in the database
                                updateTask(scheduledTask[i].getTaskName(), scheduledTask[i].getResult() + "<br><font face=arial size=-2>Execution Time: "+time+" ms</font>", masterthreadid);
                            }
                        } catch (Exception e) {
                            Debug.errorsave(e, "");
                        }
                    }
                }

                //Take a thread nap
                currentStatus = "Sleeping.";
                sleep(secondstosleep * 1000);

            }


        } catch (Exception e) {
            e.printStackTrace();
			Debug.errorsave(e, "");
			currentStatus = "Thread dead.";
        }
    }


	private boolean wasTaskRunInLastXminutes(String task, int xMinutes){
		int tasklastrun;
		boolean result=false;
		//-----------------------------------
		//-----------------------------------
		String[][] rstEmailSubSttus= Db.RunSQL("SELECT lastrun FROM scheduler WHERE task='"+reger.core.Util.cleanForSQL(task)+"' ORDER BY lastrun DESC LIMIT 0,1");
		//-----------------------------------
		//-----------------------------------
		if (rstEmailSubSttus!=null && rstEmailSubSttus.length>0){
			//Setup the calendars
			Calendar cal1=reger.core.TimeUtils.dbstringtocalendar(rstEmailSubSttus[0][0]);
			Calendar cal2=reger.core.TimeUtils.convertFromOneTimeZoneToAnother(Calendar.getInstance(), Calendar.getInstance().getTimeZone().getID(), "GMT");
			//Debug
			//String c1 = reger.core.TimeUtils.dateformatcompactwithtime(cal1);
			//String c2 = reger.core.TimeUtils.dateformatcompactwithtime(cal2);
			//reger.core.Util.logtodb("MasterThread.wasTaskRunInLastXMinutes()<br>cal1=" + c1 + "<br>cal2=" + c2);
			//Calculate the time diff
			tasklastrun=reger.core.DateDiff.dateDiff("minute", cal2, cal1);
			//reger.core.Util.logtodb("MasterThread.wasTaskRunInLastXMinutes()<br>tasklastrun=" + tasklastrun);
			//Determine the outcome
			if (tasklastrun<=xMinutes) {
				result = true;
            } else {
				result = false;
            }
		}
		return result;
    }

    public static void updateTask(String task, String comment, int masterthreadid){
        //-----------------------------------
        //-----------------------------------
        String[][] rstTask= Db.RunSQL("SELECT schedulerid FROM scheduler WHERE task='"+reger.core.Util.cleanForSQL(task)+"' AND masterthreadid='"+masterthreadid+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstTask!=null && rstTask.length>0){
        	//-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("UPDATE scheduler SET lastrun='"+reger.core.TimeUtils.nowInGmtString()+"', comment='"+reger.core.Util.cleanForSQL(comment)+"', timesrun=timesrun+1 WHERE schedulerid='"+rstTask[0][0]+"'");
            //-----------------------------------
            //-----------------------------------
        } else {
            //There isn't a record for this task on this masterthreadid yet so I need to create one
            //-----------------------------------
            //-----------------------------------
            int identity = Db.RunSQLInsert("INSERT INTO scheduler(masterthreadid, task, lastrun, timesrun, comment) VALUES('"+masterthreadid+"', '"+reger.core.Util.cleanForSQL(task)+"', '"+reger.core.TimeUtils.nowInGmtString()+"', '1', '"+Util.cleanForSQL(comment)+"')");
            //-----------------------------------
            //-----------------------------------
        }
    }

    public void setupThread(){
        //Make sure the scheduler table exists
        if (!checkThatSchedulerTableExists()){
            createSchedulerTable();
        }

        //Set the start time for this thread
        threadstartedat = reger.core.TimeUtils.dateformatfordb(Calendar.getInstance());

        //Get this masterthread's id
        //-----------------------------------
        //-----------------------------------
        String[][] rstMaster= Db.RunSQL("SELECT max(masterthreadid) FROM scheduler");
        //-----------------------------------
        //-----------------------------------
        if (rstMaster!=null && rstMaster.length>0 && reger.core.Util.isinteger(rstMaster[0][0])){
            masterthreadid = Integer.parseInt(rstMaster[0][0])+1;
        } else {
            masterthreadid = 0;
        }

        //Only keep the last one hundred threads, delete the rest
        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("DELETE FROM scheduler WHERE masterthreadid<'"+(masterthreadid-100)+"'");
        //-----------------------------------
        //-----------------------------------


        //Log the new thread to the DB
        Debug.logtodb("The wheels on the bus are turning.<br>Application startup at:" + reger.core.TimeUtils.dateformatfordb(Calendar.getInstance()) + "<br>This is not an error... it's a good thing.", "");

    }

    private static boolean checkThatSchedulerTableExists(){
        try{
            //-----------------------------------
            //-----------------------------------
            String[][] rstT = Db.RunSQL("SELECT COUNT(*) FROM scheduler");
            //-----------------------------------
            //-----------------------------------
            if (rstT!=null && rstT.length>0){
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

    private static void createSchedulerTable(){
        try{
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("CREATE TABLE `scheduler` (`schedulerid` int(11) NOT NULL auto_increment, `masterthreadid` int(11) NOT NULL default '0', `task` varchar(255) default null, lastrun datetime, timesrun int(11) default NULL, comment varchar(255), PRIMARY KEY  (`schedulerid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ScheduledTask[] getScheduledTask() {
        return scheduledTask;
    }
}
