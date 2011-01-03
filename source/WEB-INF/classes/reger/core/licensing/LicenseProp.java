package reger.core.licensing;

/**
 * Defines a single license property
 */
public class LicenseProp {

    public String name;
    public String defaultValue;

    public LicenseProp(String name, String defaultValue){
        this.name = name;
        this.defaultValue = defaultValue;
    }

}
