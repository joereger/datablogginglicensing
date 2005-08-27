package reger.systemproperties;

import reger.core.SystemProperty;

/**
 * This class uses the SystemProperty object to hold the system properties
 */
public class AllSystemProperties {

    private static SystemProperty[] properties;

    public static void loadProperties(){
        properties = new SystemProperty[0];
        //properties[0] = new PathUploadMedia();

    }

    public static SystemProperty[] getAllSystemProperties(){
        if (properties==null){
            loadProperties();
        }
        return properties;
    }

    public static String getProp(String propertyName){
        if (properties!=null && propertyName!=null){
            for (int i = 0; i < properties.length; i++) {
                if (properties[i].getPropertyName()!=null && properties[i].getPropertyName().equals(propertyName)){
                    return properties[i].getPropertyValue();
                }
            }
        }
        return "";
    }




}