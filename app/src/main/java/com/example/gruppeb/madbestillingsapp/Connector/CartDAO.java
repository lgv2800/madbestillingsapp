package com.example.gruppeb.madbestillingsapp.Connector;

import android.os.AsyncTask;

import com.example.gruppeb.madbestillingsapp.Domain.Dishes.Order;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class CartDAO implements IDAO {
    private Connector mConnector; //Database connector
    private ArrayList<String> orderItem;
    private ArrayList<String> breadType;

    public CartDAO(ArrayList<String> orderItem, ArrayList<String> breadType) {
        this.orderItem = orderItem;
        this.breadType = breadType;

    }

    @Override
    public void executeAction() {
        mConnector = new Connector();
        CartToDBAsyncTaskStatement mCartToDBAsyncTaskStatement = new CartToDBAsyncTaskStatement();
        mCartToDBAsyncTaskStatement.execute();
    }

    public class CartToDBAsyncTaskStatement extends AsyncTask<String, String, String> {
        private boolean isSuccess = false;

        //Variables of dish
        String breadColor;
        String orderMenu;


        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < breadType.size(); i++) {
                breadColor = breadType.get(i);
                orderMenu = orderItem.get(i);
                Connection con;

                try {
                    con = mConnector.CONN();
                        String query = " INSERT INTO Orders (roomNumber, orderMenu, breadType) values('" + Order.ROOM_NUMBER + "','" + orderMenu + "','" + breadColor + "')";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                        isSuccess = true;
                         con.close();
                } catch (Exception ex) {
                    isSuccess = false;
                }
            }
            return null;
        }

    }

}
