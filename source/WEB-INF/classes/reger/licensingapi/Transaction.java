package reger.licensingapi;

import reger.core.db.Db;

import java.util.Calendar;
import java.util.Hashtable;

/**
 *
 */
public class Transaction {


    private int transactionid;
    private Calendar transactiondatetime;
    private String sentstring;
    private Hashtable senthash;
    private String resultstring;
    private Hashtable resulthash;
    private int licenseid;


    public Transaction(int transactionid){
        this.transactionid = transactionid;
        load();
    }



    public Transaction(Calendar transactiondatetime, String sentstring, String resultstring, int licenseid){
        this.transactiondatetime = transactiondatetime;
        this.sentstring = sentstring;
        this.senthash = Verisign.parseNVpairs(sentstring);
        this.resultstring = resultstring;
        this.resulthash = Verisign.parseNVpairs(resultstring);
        this.licenseid = licenseid;

    }



    private void load(){
        //-----------------------------------
        //-----------------------------------
        String[][] rstLic= Db.RunSQL("SELECT transactionid, transactiondatetime, sentstring, resultstring, licenseid FROM transaction WHERE transactionid='"+transactionid+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstLic!=null && rstLic.length>0){
            this.transactionid = Integer.parseInt(rstLic[0][0]);
            this.transactiondatetime = reger.core.TimeUtils.dbstringtocalendar(rstLic[0][1]);
            this.sentstring = rstLic[0][2];
            this.senthash = Verisign.parseNVpairs(sentstring);
            this.resultstring = rstLic[0][3];
            this.resulthash = Verisign.parseNVpairs(resultstring);
            if (reger.core.Util.isinteger(rstLic[0][4])){
                this.licenseid = Integer.parseInt(rstLic[0][4]);
            } else {
                this.licenseid = 0;
            }
        } else {
            transactionid = 0;
        }
    }

    public void save(){
        if (transactionid>0){
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("UPDATE transaction SET transactiondatetime='"+reger.core.TimeUtils.dateformatfordb(transactiondatetime)+"', sentstring='"+reger.core.Util.cleanForSQL(sentstring)+"', resultstring='"+reger.core.Util.cleanForSQL(resultstring)+"', licenseid='"+licenseid+"' WHERE transactionid='"+transactionid+"'");
            //-----------------------------------
            //-----------------------------------
        } else {
            //-----------------------------------
            //-----------------------------------
            licenseid = Db.RunSQLInsert("INSERT INTO transaction(transactiondatetime, sentstring, resultstring, licenseid) VALUES('"+reger.core.TimeUtils.dateformatfordb(transactiondatetime)+"', '"+reger.core.Util.cleanForSQL(sentstring)+"', '"+reger.core.Util.cleanForSQL(resultstring)+"', '"+licenseid+"')");
            //-----------------------------------
            //-----------------------------------
        }
    }

    public int getTransactionid() {
        return transactionid;
    }

    public Calendar getTransactiondatetime() {
        return transactiondatetime;
    }

    public String getSentstring() {
        return sentstring;
    }

    public String getResultstring() {
        return resultstring;
    }

    public int getLicenseid() {
        return licenseid;
    }

    public Hashtable getSenthash() {
        return senthash;
    }

    public Hashtable getResulthash() {
        return resulthash;
    }
}
