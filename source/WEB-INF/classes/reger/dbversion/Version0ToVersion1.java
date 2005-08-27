package reger.dbversion;

import reger.core.dbupgrade.UpgradeDatabaseOneVersion;
import reger.core.db.Db;

/**
 * Upgrades the DB One version
 */
public class Version0ToVersion1 implements UpgradeDatabaseOneVersion{
    public void doUpgrade(){

        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("CREATE TABLE `license` (`licenseid` int(11) NOT NULL auto_increment, encryptedlicense text, decryptedlicense text, isactive int(11), isbillingok varchar(255), firstname varchar(255), lastname varchar(255), address1 varchar(255), address2 varchar(255), city varchar(255), state varchar(255), zip varchar(255), country varchar(255), ischargedtocreditcard int(11), ccnum varchar(255), ccexpmonth varchar(255), ccexpyear varchar(255), verisignkey varchar(255),  PRIMARY KEY  (`licenseid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count1 = Db.RunSQLUpdate("CREATE TABLE `billingactivity` (`billingactivityid` int(11) NOT NULL auto_increment, date datetime, vssentstring text, vsresultstring test, hostaddress varchar(255), result varchar(255), amt varchar(255), rpref varchar(255), profileid varchar(255),  PRIMARY KEY  (`billingactivityid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2 = Db.RunSQLUpdate("CREATE TABLE `error` ( `errorid` int(11) NOT NULL auto_increment, `date` datetime default NULL, `url` longtext, `description` longtext, `status` varchar(50) default NULL, `accountid` int(11) default NULL, `count` int(11) NOT NULL default '1', PRIMARY KEY  (`errorid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count3 = Db.RunSQLUpdate("CREATE TABLE `pagenotfound` (`pagenotfoundid` int(11) NOT NULL auto_increment, `pagename` varchar(255) default NULL, `count` int(11), PRIMARY KEY  (`pagenotfoundid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count4 = Db.RunSQLUpdate("ALTER TABLE pagenotfound ADD mostrecentreferer varchar(255)");
        //-----------------------------------
        //-----------------------------------


    }

        //Sample sql statements

        //-----------------------------------
        //-----------------------------------
        //int count = Db.RunSQLUpdate("CREATE TABLE `pltemplate` (`pltemplateid` int(11) NOT NULL auto_increment, logid int(11), plid int(11), type int(11), templateid int(11), PRIMARY KEY  (`pltemplateid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        //int count = Db.RunSQLUpdate("ALTER TABLE megachart CHANGE daterangesavedsearchid daterangesavedsearchid int(11) NOT NULL default '0'");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        //int count = Db.RunSQLUpdate("ALTER TABLE account DROP gps");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        //int count = Db.RunSQLUpdate("ALTER TABLE megalogtype ADD isprivate int(11) NOT NULL default '0'");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        //int count = Db.RunSQLUpdate("DROP TABLE megafielduser");
        //-----------------------------------
        //-----------------------------------

}
