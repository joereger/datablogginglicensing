package reger.dbversion;

import reger.core.dbupgrade.UpgradeDatabaseOneVersion;
import reger.core.db.Db;

/**
 * Upgrades the DB One version
 */
public class Version1ToVersion2 implements UpgradeDatabaseOneVersion{
    public void doUpgrade(){

        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("CREATE TABLE `transaction` (`transactionid` int(11) NOT NULL auto_increment, transactiondatetime datetime, sentstring text, resultstring text, PRIMARY KEY  (`transactionid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
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
