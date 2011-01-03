package reger.core;

import reger.core.db.Db;

/**
 * Holds a single system property.
 * The nature of a system property is that there is only
 * a single value for the entire instance.
 */
public class SystemProperty {

    protected String propertyName;
    protected String propertyValue;
    protected String propertyDefault;
    protected String propertyDescription;

    public SystemProperty(){

    }

    public SystemProperty(String propertyName, String propertyDefault){
        this.propertyName = propertyName;
        this.propertyDefault = propertyDefault;
        load();
    }

    public SystemProperty(String propertyName){
        this.propertyName = propertyName;
        load();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPropertyDefault(String propertyDefault) {
        this.propertyDefault = propertyDefault;
    }

    public String getPropertyValue() {
        if (propertyValue!=null && !propertyValue.equals("")){
            return propertyValue;
        } else {
            return propertyDefault;
        }
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void save(){
        if (!checkThatSystemPropertyTableExists()){
            createSystemPropertyTable();
        }


        //-----------------------------------
        //-----------------------------------
        String[][] rstProp= Db.RunSQL("SELECT systempropertyid FROM systemproperty WHERE propertyname='"+reger.core.Util.cleanForSQL(propertyName)+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstProp!=null && rstProp.length>0){
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("UPDATE systemproperty SET propertyvalue='"+reger.core.Util.cleanForSQL(propertyValue)+"' WHERE systempropertyid='"+rstProp[0][0]+"'");
            //-----------------------------------
            //-----------------------------------
        } else {
            //-----------------------------------
            //-----------------------------------
            int identity = Db.RunSQLInsert("INSERT INTO systemproperty(propertyname, propertyvalue) VALUES('"+reger.core.Util.cleanForSQL(propertyName)+"', '"+reger.core.Util.cleanForSQL(propertyValue)+"')");
            //-----------------------------------
            //-----------------------------------
        }

    }

    public void load(){
        if (!checkThatSystemPropertyTableExists()){
            createSystemPropertyTable();
        }

        //-----------------------------------
        //-----------------------------------
        String[][] rstData= Db.RunSQL("SELECT propertyvalue FROM systemproperty WHERE propertyname='"+reger.core.Util.cleanForSQL(propertyName)+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstData!=null && rstData.length>0){
            for(int i=0; i<rstData.length; i++){
                this.propertyValue = rstData[0][0];
            }
        }
    }

    private static boolean checkThatSystemPropertyTableExists(){
        try{
            //-----------------------------------
            //-----------------------------------
            String[][] rstT = Db.RunSQL("SELECT COUNT(*) FROM systemproperty");
            //-----------------------------------
            //-----------------------------------
            if (rstT!=null && rstT.length>0){
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

    private static void createSystemPropertyTable(){
        try{
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("CREATE TABLE `systemproperty` (`systempropertyid` int(11) NOT NULL auto_increment, `propertyname` varchar(255) default null, `propertyvalue` varchar(255) default null, PRIMARY KEY  (`systempropertyid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception e){
            e.printStackTrace();
            Debug.errorsave(e, "");
        }
    }


}
