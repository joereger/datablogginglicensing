package reger.pageFramework;

import reger.core.db.DbConfig;
import reger.core.WebAppRootDir;
import reger.core.ContextName;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Header
 */
public class GlobalHeader {

    public static void get(HttpServletRequest request, HttpServletResponse response, javax.servlet.jsp.JspWriter out, String pathToAppRoot){
        try{

            //Get the webapp root
            if (WebAppRootDir.getWebAppRootPath()==null){
                new WebAppRootDir(request);
            }

            //Get the context name
            if (ContextName.getContextName()==null){
                new ContextName(request);
            }

            //Make sure we have a valid db connection
            if (!DbConfig.haveValidConfig()){
                response.sendRedirect(pathToAppRoot + "setup/setup-00.log");
                return;
            }

            //Make sure we have the correct database version
            if (!reger.core.dbupgrade.RequiredDatabaseVersion.havecorrectversion){
                response.sendRedirect(pathToAppRoot + "/error/invaliddbversion.jsp");
                return;
            }


        } catch (Exception e){
            reger.core.Debug.errorsave(e, "GlobalHeader.java");
        }

    }


}
