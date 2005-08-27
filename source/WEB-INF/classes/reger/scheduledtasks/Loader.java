package reger.scheduledtasks;

import reger.core.db.DbConfig;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This servlet is started at application start and loads email threads and other threads that need to run.
 */

public class Loader extends HttpServlet {

    public static reger.core.scheduler.MasterThread tThr;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Must be here so that Tomcat considers this to be a servlet and will load it at Tomcat startup
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Must be here so that Tomcat considers this to be a servlet and will load it at Tomcat startup
    }

    public void init(ServletConfig config){
        System.out.println("Reger.com Licensing: Loader.java init() method called.");
        reger.core.WebAppRootDir ward = new reger.core.WebAppRootDir(config);
        reger.core.ContextName ctname = new reger.core.ContextName(config);
        System.out.println("Reger.com Licensing: reger.core.WebAppRootDir=" + reger.core.WebAppRootDir.getWebAppRootPath());
        System.out.println("Reger.com Licensing: reger.core.ContextName=" + reger.core.ContextName.getContextName());
        //Make sure we have a valid db connection
        if (!DbConfig.haveValidConfig()){
            System.out.println("Reger.com Licensing: Loader.java init() method, no valid dbconfig found, returning.");
            return;
        }
        startAMasterThread();
        System.out.println("Reger.com Licensing: Loader.java init() method succesfully processed.");
    }

    public static void startAMasterThread(){
        if (reger.scheduledtasks.Loader.tThr!=null){
            reger.scheduledtasks.Loader.tThr = null;
        }
        //Master thread
        //The master thread is created
        tThr = new reger.core.scheduler.MasterThread();
        //Add scheduled tasks
        tThr.addScheduledTask(new BillingTask());
        //@todo Allow plugin tasks here
        //Run the thread
        tThr.setPriority(Thread.MIN_PRIORITY);
        tThr.start();
    }


}


