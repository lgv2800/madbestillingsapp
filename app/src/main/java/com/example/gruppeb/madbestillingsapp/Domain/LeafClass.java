package com.example.gruppeb.madbestillingsapp.Domain;

/**
 * Class that contains static fields only
 */
public class LeafClass {

    /**
     * JSON-urls
     */
    public static final String URL = "http://35.178.118.175/MadbestillingsappWebportal/dayMenuJSON.php";
    public static final String URL_EVENING = "http://35.178.118.175/MadbestillingsappWebportal/eveningMenuJSON.php";

    /**
     * Database connection
     */
    public static final String URL_DATABASE = "jdbc:mysql://ahemdb.cug8phllmerd.eu-west-2.rds.amazonaws.com:3306/Madbestilling_BallerupKommune";
    public static final String USER_DATABASE = "balkom_madbestilling";
    public static final String PW_DATABASE = "8yH1OEPeZVhp";
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
}
