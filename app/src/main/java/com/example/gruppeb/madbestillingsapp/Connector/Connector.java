package com.example.gruppeb.madbestillingsapp.Connector;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.example.gruppeb.madbestillingsapp.Domain.LeafClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    String jdbcClass = LeafClass.JDBC_DRIVER;

    //Amazon Web Server (AWS) RDS database
    String url = LeafClass.URL_DATABASE;
    String username = LeafClass.USER_DATABASE;
    String password = LeafClass.PW_DATABASE;

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