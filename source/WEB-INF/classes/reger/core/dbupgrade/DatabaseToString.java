package reger.core.dbupgrade;

import reger.core.Debug;

import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.Properties;

/**
 * This class connects to a database and dumps all the tables and contents out to stdout in the form of
 * a set of SQL executable statements
 */
public class DatabaseToString {


//    public static boolean backupDatabase(){
//        //Date-based directory name
//        String dirname = (String)reger.Vars.getEnvVar("DBBACKUPDIR");
//        java.Util.Calendar cal = java.Util.Calendar.getInstance();
//        dirname = dirname + "backup-" + reger.core.TimeUtils.dateformatfilestamp(cal) + ".sql";
//
//        //Create a File Handle
//        java.io.File file = new java.io.File(dirname);
//        if (!file.exists()){
//            try{
//                file.createNewFile();
//            } catch (Exception e){
//                reger.core.Util.errorsave(e);
//            }
//        }
//
//        //Write the database to a file
//        //return reger.core.Util.textFileOverwrite(file, dumpDB());
//
//    }


    /** Dump the whole database to an SQL string */
    public static boolean dumpDB() throws java.io.IOException {

        Properties props = new Properties();
//        props.setProperty("driver.url", "jdbc:mysql://localhost:3306/qloggerupgradetest?autoReconnect=true");
//        props.setProperty("driver.class", "com.mysql.jdbc.Driver");
//        props.setProperty("user", "webapp");
//        props.setProperty("password", "r3g3r");


         //Date-based directory name
        String dirname = (String)reger.Vars.getEnvVar("DBBACKUPDIR");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        dirname = dirname + "backup-" + reger.core.TimeUtils.dateformatfilestamp(cal) + ".sql";

        //Create a File Handle
        java.io.File file = new java.io.File(dirname);
        if (!file.exists()){
            try{
                file.createNewFile();
            } catch (Exception e){
                Debug.errorsave(e, "");
            }
        }

        //Create a filewriter
        java.io.FileWriter fw = new java.io.FileWriter(file);


        //String driverClassName = props.getProperty("driver.class");
        //String driverURL = props.getProperty("driver.url");
        // Default to not having a quote character
        String columnNameQuote = props.getProperty("columnName.quoteChar", "");
        DatabaseMetaData dbMetaData = null;
        Connection dbConn = null;
        try {
            //Class.forName(driverClassName);
            //dbConn = DriverManager.getConnection(driverURL, props);

            dbConn=reger.core.db.Db.getConnection();
            dbMetaData = dbConn.getMetaData();


        }
        catch( Exception e ) {
            Debug.errorsave(e, "");
            return false;
        }

        try {
            //StringBuffer result = new StringBuffer();
            String catalog = props.getProperty("catalog");
            String schema = props.getProperty("schemaPattern");
            String tables = props.getProperty("tableName");
            ResultSet rs = dbMetaData.getTables(catalog, schema, tables, null);
            if (! rs.next()) {
                Debug.logtodb("Unable to find any tables matching: catalog="+catalog+" schema="+schema+" tables="+tables, "");
                rs.close();
            } else {
                // Right, we have some tables, so we can go to work.
                // the details we have are
                // TABLE_CAT String => table catalog (may be null)
                // TABLE_SCHEM String => table schema (may be null)
                // TABLE_NAME String => table name
                // TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
                // REMARKS String => explanatory comment on the table
                // TYPE_CAT String => the types catalog (may be null)
                // TYPE_SCHEM String => the types schema (may be null)
                // TYPE_NAME String => type name (may be null)
                // SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
                // REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
                // We will ignore the schema and stuff, because people might want to import it somewhere else
                // We will also ignore any tables that aren't of type TABLE for now.
                // We use a do-while because we've already caled rs.next to see if there are any rows
                do {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    if ("TABLE".equalsIgnoreCase(tableType)) {
                        fw.write("\n\n-- "+tableName);
                        fw.write("\nCREATE TABLE "+tableName+" (\n");
                        ResultSet tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");
                        boolean firstLine = true;
                        while (tableMetaData.next()) {
                            if (firstLine) {
                                firstLine = false;
                            } else {
                                // If we're not the first line, then finish the previous line with a comma
                                fw.write(",\n");
                            }
                            String columnName = tableMetaData.getString("COLUMN_NAME");
                            String columnType = tableMetaData.getString("TYPE_NAME");
                            // WARNING: this may give daft answers for some types on some databases (eg JDBC-ODBC link)
                            int columnSize = tableMetaData.getInt("COLUMN_SIZE");
                            String nullable = tableMetaData.getString("IS_NULLABLE");
                            String nullString = "NULL";
                            if ("NO".equalsIgnoreCase(nullable)) {
                                nullString = "NOT NULL";
                            }
                            fw.write("    "+columnNameQuote+columnName+columnNameQuote+" "+columnType+" ("+columnSize+")"+" "+nullString);
                        }
                        tableMetaData.close();

                        // Now we need to put the primary key constraint
                        try {
                            ResultSet primaryKeys = dbMetaData.getPrimaryKeys(catalog, schema, tableName);
                            // What we might get:
                            // TABLE_CAT String => table catalog (may be null)
                            // TABLE_SCHEM String => table schema (may be null)
                            // TABLE_NAME String => table name
                            // COLUMN_NAME String => column name
                            // KEY_SEQ short => sequence number within primary key
                            // PK_NAME String => primary key name (may be null)
                            String primaryKeyName = null;
                            StringBuffer primaryKeyColumns = new StringBuffer();
                            while (primaryKeys.next()) {
                                String thisKeyName = primaryKeys.getString("PK_NAME");
                                if ((thisKeyName != null && primaryKeyName == null)
                                        || (thisKeyName == null && primaryKeyName != null)
                                        || (thisKeyName != null && ! thisKeyName.equals(primaryKeyName))
                                        || (primaryKeyName != null && ! primaryKeyName.equals(thisKeyName))) {
                                    // the keynames aren't the same, so output all that we have so far (if anything)
                                    // and start a new primary key entry
                                    if (primaryKeyColumns.length() > 0) {
                                        // There's something to output
                                        fw.write(",\n    PRIMARY KEY ");
                                        if (primaryKeyName != null) { fw.write(primaryKeyName); }
                                        fw.write("("+primaryKeyColumns.toString()+")");
                                    }
                                    // Start again with the new name
                                    primaryKeyColumns = new StringBuffer();
                                    primaryKeyName = thisKeyName;
                                }
                                // Now append the column
                                if (primaryKeyColumns.length() > 0) {
                                    primaryKeyColumns.append(", ");
                                }
                                primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
                            }
                            if (primaryKeyColumns.length() > 0) {
                                // There's something to output
                                fw.write(",\n    PRIMARY KEY ");
                                if (primaryKeyName != null) { fw.write(primaryKeyName); }
                                fw.write(" ("+primaryKeyColumns.toString()+")");
                            }
                        } catch (SQLException e) {
                            // NB you will get this exception with the JDBC-ODBC link because it says
                            // [Microsoft][ODBC Driver Manager] Driver does not support this function
                            Debug.errorsave(e, "", "Unable to get primary keys for table "+tableName+" because ");
                        }

                        fw.write("\n);\n");

                        // Right, we have a table, so we can go and dump it
                        dumpTable(dbConn, fw, tableName);
                    }
                } while (rs.next());
                rs.close();
            }
            dbConn.close();
            fw.close();
            return true;
        } catch (SQLException e) {
            Debug.errorsave(e, "");  //To change body of catch statement use Options | File Templates.
        } finally {
            try{
                if (dbConn!=null && dbConn.isClosed()==false){
                    dbConn.close();
                }
                if (fw!=null){
                    fw.close();
                }
            } catch (Throwable e){
                Debug.errorsave(e, "");
            }
        }
        return false;
    }

    /** dump this particular table to the string buffer */
    private static void dumpTable(Connection dbConn, java.io.FileWriter fw, String tableName) throws java.io.IOException {
        try {
            // First we output the create table stuff
            PreparedStatement stmt = dbConn.prepareStatement("SELECT * FROM "+tableName);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Now we can output the actual data
            fw.write("\n\n-- Data for "+tableName+"\n");
            while (rs.next()) {
                fw.write("INSERT INTO "+tableName+" VALUES (");
                for (int i=0; i<columnCount; i++) {
                    if (i > 0) {
                        fw.write(", ");
                    }
                    Object value = rs.getObject(i+1);
                    if (value == null) {
                        fw.write("NULL");
                    } else {
                        String outputValue = value.toString();
                        outputValue = outputValue.replaceAll("'","''");
                        fw.write("'"+outputValue+"'");
                    }
                }
                fw.write(");\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            Debug.errorsave(e, "", "Unable to dump table "+tableName+" because: ");
        }
    }











}
