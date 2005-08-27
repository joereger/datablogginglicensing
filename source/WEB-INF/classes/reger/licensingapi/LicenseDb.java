package reger.licensingapi;

import reger.core.db.Db;
import reger.core.licensing.License;

import java.util.Calendar;

/**
 * Represents the license in the database
 */
public class LicenseDb {

    private int licenseid = 0;
    private String encryptedlicense = "";
    private String decryptedlicense = "";
    private boolean isactive;
    private boolean isbillingok;
    private String name = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private String country = "";
    private String ccnum = "";
    private int ccexpmonth;
    private int ccexpyear;
    private String verisignkey = "";
    private String billingerror = "";
    private String phone = "";
    private String email = "";

    public LicenseDb(int licenseid){
        this.licenseid = licenseid;
        load();
    }

    public LicenseDb(String encryptedlicense, String decryptedlicense, boolean isactive, boolean isbillingok, String name, String address1, String address2, String city, String state, String zip, String country, String ccnum, String ccexpmonth, String ccexpyear, String verisignkey, String billingerror, String phone, String email){
        int tmpCcexpmonth = 0;
        if (reger.core.Util.isinteger(ccexpmonth)){
            tmpCcexpmonth = Integer.parseInt(ccexpmonth);
        }
        int tmpCcexpyear = 0;
        if (reger.core.Util.isinteger(ccexpyear)){
            tmpCcexpyear = Integer.parseInt(ccexpyear);
        }
        this.encryptedlicense = encryptedlicense;
        this.decryptedlicense = decryptedlicense;
        this.isactive = isactive;
        this.isbillingok = isbillingok;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.ccnum = ccnum;
        this.ccexpmonth = tmpCcexpmonth;
        this.ccexpyear = tmpCcexpyear;
        this.verisignkey = verisignkey;
        this.billingerror = billingerror;
        this.phone = phone;
        this.email = email;
    }

    public LicenseDb(String encryptedlicense, String decryptedlicense, boolean isactive, boolean isbillingok, String name, String address1, String address2, String city, String state, String zip, String country, String ccnum, int ccexpmonth, int ccexpyear, String verisignkey, String billingerror, String phone, String email){
        this.encryptedlicense = encryptedlicense;
        this.decryptedlicense = decryptedlicense;
        this.isactive = isactive;
        this.isbillingok = isbillingok;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.ccnum = ccnum;
        this.ccexpmonth = ccexpmonth;
        this.ccexpyear = ccexpyear;
        this.verisignkey = verisignkey;
        this.billingerror = billingerror;
        this.phone = phone;
        this.email = email;
    }

    public void setEncryptedlicense(String encryptedlicense) {
        this.encryptedlicense = encryptedlicense;
    }

    public void setDecryptedlicense(String decryptedlicense) {
        this.decryptedlicense = decryptedlicense;
    }

    private void load(){
        //-----------------------------------
        //-----------------------------------
        String[][] rstLic= Db.RunSQL("SELECT licenseid, encryptedlicense, decryptedlicense, isactive, isbillingok, name, address1, address2, city, state, zip, country, ccnum, ccexpmonth, ccexpyear, verisignkey, billingerror, phone, email FROM license WHERE licenseid='"+licenseid+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstLic!=null && rstLic.length>0){
            this.encryptedlicense = rstLic[0][1];
            this.decryptedlicense = rstLic[0][2];
            this.isactive = reger.core.Util.booleanFromSQLText(rstLic[0][3]);
            this.isbillingok = reger.core.Util.booleanFromSQLText(rstLic[0][4]);
            this.name = rstLic[0][5];
            this.address1 = rstLic[0][6];
            this.address2 = rstLic[0][7];
            this.city = rstLic[0][8];
            this.state = rstLic[0][9];
            this.zip = rstLic[0][10];
            this.country = rstLic[0][11];
            this.ccnum = rstLic[0][12];
            if (reger.core.Util.isinteger(rstLic[0][13])){
                this.ccexpmonth = Integer.parseInt(rstLic[0][13]);
            } else {
                this.ccexpmonth = 0;
            }
            if (reger.core.Util.isinteger(rstLic[0][14])){
                this.ccexpyear = Integer.parseInt(rstLic[0][14]);
            } else {
                this.ccexpyear = 0;
            }
            this.verisignkey = rstLic[0][15];
            this.billingerror = rstLic[0][16];
            this.phone = rstLic[0][17];
            this.email = rstLic[0][18];
        } else {
            licenseid = 0;
        }
    }

    public void populate(javax.servlet.http.HttpServletRequest request){
        encryptedlicense = reger.core.RequestParam.getString(request, "encryptedlicense", encryptedlicense);
        decryptedlicense = reger.core.RequestParam.getString(request, "decryptedlicense", decryptedlicense);
        isactive = reger.core.RequestParam.getBoolean(request, "isactive", true);
        isbillingok = reger.core.RequestParam.getBoolean(request, "isbillingok", true);
        name = reger.core.RequestParam.getString(request, "name", "");
        address1 = reger.core.RequestParam.getString(request, "address1", "");
        address2 = reger.core.RequestParam.getString(request, "address2", "");
        city = reger.core.RequestParam.getString(request, "city", "");
        state = reger.core.RequestParam.getString(request, "state", "");
        zip = reger.core.RequestParam.getString(request, "zip", "");
        country = reger.core.RequestParam.getString(request, "country", "");
        ccnum = reger.core.RequestParam.getString(request, "ccnum", "");
        ccexpmonth = reger.core.RequestParam.getInt(request, "ccexpmonth", 0);
        ccexpyear = reger.core.RequestParam.getInt(request, "ccexpyear", 0);
        verisignkey = reger.core.RequestParam.getString(request, "verisignkey", verisignkey);
        phone = reger.core.RequestParam.getString(request, "phone", phone);
        email = reger.core.RequestParam.getString(request, "email", email);
    }

    public void save(){
        if (licenseid>0){
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("UPDATE license SET encryptedlicense='"+reger.core.Util.cleanForSQL(encryptedlicense)+"', decryptedlicense='"+reger.core.Util.cleanForSQL(decryptedlicense)+"', isactive='"+reger.core.Util.booleanAsSQLText(isactive)+"', isbillingok='"+reger.core.Util.booleanAsSQLText(isbillingok)+"', name='"+reger.core.Util.cleanForSQL(name)+"', address1='"+reger.core.Util.cleanForSQL(address1)+"', address2='"+reger.core.Util.cleanForSQL(address2)+"', city='"+reger.core.Util.cleanForSQL(city)+"', state='"+reger.core.Util.cleanForSQL(state)+"', zip='"+reger.core.Util.cleanForSQL(zip)+"', country='"+reger.core.Util.cleanForSQL(country)+"', ccnum='"+reger.core.Util.cleanForSQL(ccnum)+"', ccexpmonth='"+ccexpmonth+"', ccexpyear='"+ccexpyear+"', verisignkey='"+reger.core.Util.cleanForSQL(verisignkey)+"', billingerror='"+reger.core.Util.cleanForSQL(billingerror)+"', phone='"+reger.core.Util.cleanForSQL(phone)+"', email='"+reger.core.Util.cleanForSQL(email)+"' WHERE licenseid='"+licenseid+"'");
            //-----------------------------------
            //-----------------------------------
        } else {
            //-----------------------------------
            //-----------------------------------
            licenseid = Db.RunSQLInsert("INSERT INTO license(encryptedlicense, decryptedlicense, isactive, isbillingok, name, address1, address2, city, state, zip, country, ccnum, ccexpmonth, ccexpyear, verisignkey, billingerror, phone, email) VALUES('"+reger.core.Util.cleanForSQL(encryptedlicense)+"', '"+reger.core.Util.cleanForSQL(decryptedlicense)+"', '"+reger.core.Util.booleanAsSQLText(isactive)+"', '"+reger.core.Util.booleanAsSQLText(isbillingok)+"', '"+reger.core.Util.cleanForSQL(name)+"', '"+reger.core.Util.cleanForSQL(address1)+"', '"+reger.core.Util.cleanForSQL(address2)+"', '"+reger.core.Util.cleanForSQL(city)+"', '"+reger.core.Util.cleanForSQL(state)+"', '"+reger.core.Util.cleanForSQL(zip)+"', '"+reger.core.Util.cleanForSQL(country)+"', '"+reger.core.Util.cleanForSQL(ccnum)+"', '"+ccexpmonth+"', '"+ccexpyear+"', '"+reger.core.Util.cleanForSQL(verisignkey)+"', '"+reger.core.Util.cleanForSQL(billingerror)+"', '"+reger.core.Util.cleanForSQL(phone)+"', '"+reger.core.Util.cleanForSQL(email)+"')");
            //-----------------------------------
            //-----------------------------------
        }
    }

    /**
     * Checks current billing status for this license, works with Verisign and updates status
     */
    public void doBilling() throws VerisignException{
        License lic = new License(null, encryptedlicense);
        if (lic.getProperty(License.PROPSTRINGISCHARGEDTOCREDITCARD)!=null && String.valueOf(lic.getProperty(License.PROPSTRINGISCHARGEDTOCREDITCARD)).equals("1")){
            //If we need to charge the license
            if (requiresAnotherCharge()){
                    //Get the amt
                    double amt = 0;
                    if (reger.core.Util.isnumeric((String)lic.getProperty(License.PROPSTRINGAMOUNT))){
                        amt = Double.parseDouble((String)lic.getProperty(License.PROPSTRINGAMOUNT));
                    }
                    //Charge the card
                    try{
                        //Call verisign
                        Verisign vs = new Verisign(licenseid);
                        Transaction tr = vs.chargeCard(String.valueOf(amt), String.valueOf(ccnum), String.valueOf(ccexpmonth), String.valueOf(ccexpyear), String.valueOf(address1), String.valueOf(zip));
                        this.isbillingok = true;
                        this.billingerror = "";
                        save();
                    } catch (VerisignException vex){
                        this.isbillingok = false;
                        this.billingerror = vex.getUserFriendlyMessage();
                        save();
                        throw vex;
                    }
            }
        }
    }

    private boolean requiresAnotherCharge(){

        //Load up the license
        License lic = new License(null, encryptedlicense);
        //Does this license require another charge?
        Transaction mostRecentSuccessfulCharge = mostRecentSuccessfulCharge();
        if (isactive && mostRecentSuccessfulCharge!=null){
            //Get the charging frequency
            int chargeevery = 0;
            if (reger.core.Util.isinteger(lic.getProperty(License.PROPSTRINGCHARGEEVERY))){
                chargeevery = Integer.parseInt((String)lic.getProperty(License.PROPSTRINGCHARGEEVERY));
            }
            //Get the charging frequency units (weeks, months, years, etc)
            int chargeeveryunits = 0;
            if (reger.core.Util.isinteger(lic.getProperty(License.PROPSTRINGCHARGEEVERYUNITS))){
                chargeeveryunits = Integer.parseInt((String)lic.getProperty(License.PROPSTRINGCHARGEEVERYUNITS));
            }
            //Get the date of the transaction
            Calendar nextCharge = Calendar.getInstance();
            if (chargeeveryunits==License.CHARGEEVERYMONTHS){
                nextCharge = reger.core.TimeUtils.AddOneMonth(mostRecentSuccessfulCharge.getTransactiondatetime());
            } else if (chargeeveryunits==License.CHARGEEVERYYEARS){
                nextCharge = reger.core.TimeUtils.addOneYear(mostRecentSuccessfulCharge.getTransactiondatetime());
            }
            //If the current time is after the next transaction time then another charge is in order
            if (reger.core.TimeUtils.nowInGmtCalendar().after(nextCharge)){
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    private Transaction mostRecentSuccessfulCharge(){
        //-----------------------------------
        //-----------------------------------
        String[][] rstTrans= Db.RunSQL("SELECT transactionid FROM transaction WHERE licenseid='"+licenseid+"' ORDER BY transactionid DESC");
        //-----------------------------------
        //-----------------------------------
        if (rstTrans!=null && rstTrans.length>0){
            for(int i=0; i<rstTrans.length; i++){
                //Load the transaction
                Transaction tr = new Transaction(Integer.parseInt(rstTrans[i][0]));
                if (tr.getResulthash().get("RESULT")!=null && tr.getResulthash().get("RESULT").equals("0")){
                    //It was a successful transaction
                    if (tr.getSenthash().get("TRXTYPE")!=null && tr.getSenthash().get("TRXTYPE").equals("S")){
                        //It was a charge
                        return tr;
                    } else if (tr.getSenthash().get("TRXTYPE")!=null && tr.getSenthash().get("TRXTYPE").equals("C")){
                        //It was a credit
                    }
                 } else {
                    //It was a failed transaction

                 }

            }
        }
        return null;
    }

    public int getLicenseid() {
        return licenseid;
    }

    public String getEncryptedlicense() {
        return encryptedlicense;
    }

    public String getDecryptedlicense() {
        return decryptedlicense;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public boolean isIsbillingok() {
        return isbillingok;
    }

    public String getName() {
        return name;
    }


    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getCcnum() {
        return ccnum;
    }

    public int getCcexpmonth() {
        return ccexpmonth;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public String getVerisignkey() {
        return verisignkey;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getBillingerror() {
        return billingerror;
    }

    public void setLicenseid(int licenseid) {
        this.licenseid = licenseid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
