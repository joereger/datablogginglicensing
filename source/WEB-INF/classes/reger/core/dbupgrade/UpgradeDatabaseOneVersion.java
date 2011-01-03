package reger.core.dbupgrade;

/**
 * Classes that upgrade the database from one version to another must
 * adhere to this interface.
 */
public interface UpgradeDatabaseOneVersion {

    /**
     * Does the upgrade.
     */
    void doUpgrade();
    

}
