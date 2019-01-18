package com.example.gruppeb.madbestillingsapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.gruppeb.madbestillingsapp.Domain.JsonObserver;
import com.example.gruppeb.madbestillingsapp.FoodFragments.FragmentGenerator;
import com.example.gruppeb.madbestillingsapp.FoodFragments.SplashFragment;

public class JsonController implements JsonObserver {
    Context context;
    MainScreen mainScreen;
    FragmentGenerator fg;
    private final String url = "http://35.178.118.175/MadbestillingsappWebportal/dayMenuJSON.php";
    SplashFragment fragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fm;

    public JsonController(){}

    public JsonController(Context context, MainScreen mainScreen) {
        this.context = context;
        this.mainScreen = mainScreen;
    }

    public void doAction(){

        if (haveNetworkConnection()) {
            fg = FragmentGenerator.getInstance();
            fg.setContext(context);
            fg.getJsonFiles(url);
            fg.setController(this);
            beginTransaction();
        }
        else{
            Toast.makeText(context, "Der skete en fejl (intet internet)", Toast.LENGTH_SHORT).show();
        }
    }

    private void beginTransaction() {
        fm = mainScreen.getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        fragment = new SplashFragment();
        fragmentTransaction.add(R.id.mainscreen_full, fragment).commit();
    }

    @Override
    public void jsonCompleted() {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
        mainScreen.initialView();
    }

    private boolean haveNetworkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void setAdapter(ViewPager viewPager){
        fg.setAdapter(viewPager, mainScreen);
    }

    public String getLanguage(){
        return fg.getLanguage();
    }

    public String getFragmentTitle(int currentItem) {
        return fg.getFragmentTitle(currentItem);
    }

    public String getFragmentDescription(String language, int position){
        return fg.getFragmentDescription(language, position);
    }

    public String getFragmentNames(String language, int position){
        return fg.getFragmentTitle(position);
    }

}
