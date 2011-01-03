package reger.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Saves all incoming name/value pairs into a Hashmap
 */
public class RequestSaver {

    public HashMap nameValuePairs = null;

    public RequestSaver(javax.servlet.http.HttpServletRequest request){
        Enumeration enumer = request.getParameterNames();
		while (enumer.hasMoreElements()) {
			String name = (String)enumer.nextElement();
			String[] values = request.getParameterValues(name);
            //Store in the nameValuePairs
			nameValuePairs.put(name, values);
		}
    }

    public String getAsQueryString(){
        StringBuffer qs = new StringBuffer();
        if (nameValuePairs!=null){
            for (Iterator i=nameValuePairs.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                //out.println(e.getKey() + ": " + e.getValue());
                String name = (String)e.getKey();
                String[] values = (String[])e.getValue();

                for (int j = 0; j < values.length; j++) {
                    try{
                        if (j>0){
                            qs.append("&");
                        }
                        qs.append(java.net.URLEncoder.encode(name, "UTF-8")+"="+java.net.URLEncoder.encode(values[j], "UTF-8"));
                    } catch (Exception ex){
                        Debug.errorsave(ex, "");
                    }
                }
            }
        }
        return qs.toString();
    }

}
