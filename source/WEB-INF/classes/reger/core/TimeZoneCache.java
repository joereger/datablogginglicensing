package reger.core;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.base.NeedsRefreshException;

import java.util.TimeZone;

/**
 * Caches Account objects using OSCache
 */
public class TimeZoneCache {

    public static GeneralCacheAdministrator admin;


    public static TimeZone get(String id){
        if (admin==null){
            admin = new GeneralCacheAdministrator();
        }

        try {
            return (TimeZone) admin.getFromCache(id);
        } catch (NeedsRefreshException nre) {
            try {
                TimeZone tz = TimeZone.getTimeZone(id);
                admin.putInCache(id, tz);
                //Debug.debug(5, "TimeZoneCache.java", "In id: "+id+"<br>REFRESHING<br>Returning tz.getID(): " + tz.getID());
                return tz;
            } catch (Exception ex) {
                admin.cancelUpdate(id);
                Debug.errorsave(ex, "");
                //Debug.debug(5, "TimeZoneCache.java", "In id: "+id+"<br>REFRESHING<br>Returning the default GMT.");
                return TimeZone.getTimeZone("GMT");
            }
        }
    }

    


}
