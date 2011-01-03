package reger.core.scheduler;

/**
 * Interface implemented for scheduled tasks
 */
public interface ScheduledTask {

    public String getTaskName();

    public void doTask();

    public String getResult();

    public int getRunEveryXMinutes();
    
    public boolean getOnlyRunOnceAtStartup();

}
