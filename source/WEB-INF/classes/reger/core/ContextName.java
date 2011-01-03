package reger.core;

import javax.servlet.ServletConfig;

/**
 * Holds this context's installation point
 */
public class ContextName {

    private static String name;

    public ContextName(javax.servlet.http.HttpServletRequest request){
         name = request.getSession().getServletContext().getServletContextName();
    }

    public ContextName(ServletConfig config){
         name = config.getServletContext().getServletContextName();
    }

    public static String getContextName(){
        return name;
    }

}
