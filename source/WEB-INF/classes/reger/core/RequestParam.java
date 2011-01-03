package reger.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Safely gets request params
 */
public class RequestParam {

    public static int getInt(HttpServletRequest request, String paramName, int defaultValue){
        if (request.getParameter(paramName)!=null){
            if (reger.core.Util.isinteger(request.getParameter(paramName))){
                return Integer.parseInt(request.getParameter(paramName));
            }
        }
        return defaultValue;
    }

    public static String getString(HttpServletRequest request, String paramName, String defaultValue){
        if (request.getParameter(paramName)!=null){
            return request.getParameter(paramName);
        }
        return defaultValue;
    }

    public static boolean getBoolean(HttpServletRequest request, String paramName, boolean defaultValue){
        if (request.getParameter(paramName)!=null){
            if (request.getParameter(paramName).equals("1") || request.getParameter(paramName).equals("true")){
                return true;
            } else {
                return false;
            }
        }
        return defaultValue;
    }

    public static double getDouble(HttpServletRequest request, String paramName, double defaultValue){
        if (request.getParameter(paramName)!=null){
            if (reger.core.Util.isnumeric(request.getParameter(paramName))){
                return Double.parseDouble(request.getParameter(paramName));
            }
        }
        return defaultValue;
    }

    public static long getLong(HttpServletRequest request, String paramName, long defaultValue){
        if (request.getParameter(paramName)!=null){
            if (reger.core.Util.isnumeric(request.getParameter(paramName))){
                return Long.parseLong(request.getParameter(paramName));
            }
        }
        return defaultValue;
    }



}
