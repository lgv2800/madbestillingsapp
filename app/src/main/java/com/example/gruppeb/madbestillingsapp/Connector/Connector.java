package com.example.gruppeb.madbestillingsapp.Connector;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    String jdbcClass = "com.mysql.jdbc.Driver";

    /*
    //Madbestilling root user
    String url = "jdbc:mysql://10.16.231.123:8889/Madbestilling_BallerupKommune";
    String username = "root";
    String password = "root";*/

    //Madbestilling DB user
    //String url = "jdbc:mysql://IP-address:PORT/DB_Name
    //String url = "jdbc:mysql://10.16.231.123:8889/Madbestilling_BallerupKommune";
    String url = "jdbc:mysql://192.168.0.43:8889/Madbestilling_BallerupKommune";
    String username = "madbestilling_ballerup";
    String password = "mdballerup";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(jdbcClass);

            conn = DriverManager.getConnection(url, username, password);

            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

}