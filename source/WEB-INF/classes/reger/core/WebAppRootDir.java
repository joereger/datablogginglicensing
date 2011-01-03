package reger.core;

import javax.servlet.ServletConfig;

/**
 * Holds this context's installation point
 */
public class WebAppRootDir {

    private static String root;
    private static String uniqueContextId;


    public WebAppRootDir(javax.servlet.http.HttpServletRequest request){
         setUniqueContextId(request.getSession().getServletContext());
         root = request.getSession().getServletContext().getRealPath("/");
    }

    public WebAppRootDir(ServletConfig config){
        setUniqueContextId(config.getServletContext());
        root = config.getServletContext().getRealPath("/");
    }

    private static void setUniqueContextId(javax.servlet.ServletContext context){
        String pathSeparator = System.getProperty("file.separator");
       if (pathSeparator.equals("\\")){
            pathSeparator = "\\\\";
       }
       String realPath = context.getRealPath(pathSeparator);
       String[] realPathParts = realPath.split(pathSeparator);
        for (int i = 0; i < realPathParts.length; i++) {
            java.lang.String realPathPart = realPathParts[i];
        }
        String uniqueEngineName = realPathParts[realPathParts.length-1];
        if (realPathParts[realPathParts.length-2]!=null){
            uniqueEngineName = realPathParts[realPathParts.length-2] + uniqueEngineName;
        }


        uniqueContextId = uniqueEngineName;
    }

    public static String getWebAppRootPath(){
        return root;
    }

    public static String getUniqueContextId() {
        return uniqueContextId;
    }

}
