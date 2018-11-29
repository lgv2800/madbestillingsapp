package com.example.gruppeb.madbestillingsapp.Domain;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.gruppeb.madbestillingsapp.Connector.Connector;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class CartDAO implements IDAO {
    Connector mConnector; //Database connector
    ArrayList<String> orderItem;
    ArrayList<String> breadType;

    public CartDAO(ArrayList<String> orderItem, ArrayList<String> breadType){
        this.orderItem=orderItem;
        this.breadType=breadType;

    }

    @Override
    public void executeAction(){
        mConnector = new Connector();
        CartToDBAsyncTaskStatement mCartToDBAsyncTaskStatement = new CartToDBAsyncTaskStatement();
        mCartToDBAsyncTaskStatement.execute();
    }

    public class CartToDBAsyncTaskStatement extends AsyncTask<String, String, String> {
        private boolean isSuccess = false;

        /*public CartToDBAsyncTaskStatement(Context mContext) {
            //mContext = this.Context;
        }*/

        //Variables of dish
        //private int dishAmount = Order.getCount(mContext);
        private int dishAmount = 1;
        String breadColor;
        String orderMenu;

        /*ArrayList <String> itemOrdered = new ArrayList<String>();
        ArrayList <String> breadType = new ArrayList<String>();*/

        //private String itemOrdered = new String[]{"Lys", "Mørk"};


        @Override
        protected String doInBackground(String... params) {
            for (int i = 0 ; i<breadType.size(); i++) {
                breadColor = breadType.get(i);
                orderMenu = orderItem.get(i);

                    try {
                        Connection con = mConnector.CONN();
                        if (con == null) {

                        } else {
                            String query = " INSERT INTO Orders (roomNumber, orderMenu, breadType) values('" + Order.ROOM_NUMBER + "','" + orderMenu + "','" + breadColor + "')";

                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                            isSuccess = true;
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                    }
                }
                return null;
            }
        }

    }
