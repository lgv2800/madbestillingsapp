package com.example.gruppeb.madbestillingsapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gruppeb.madbestillingsapp.Helper.DishJSON;
import com.example.gruppeb.madbestillingsapp.Helper.EveningDishJSON;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.MainActivity;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EveningMenuScreen extends AppCompatActivity implements AsyncResponse {

    Toolbar mToolbarOrders;
    ListView mEvDishNameDescriptionList;

    private String languageFromSharedPrefs;
    SharedPreferences settingsSharedPreferences;

    List<String> evDishNameListString;
    List<String> evDishDescriptionListString;
    String[] evDishNameArrayString;
    String[] evDishDescriptionArrayString;

    ArrayList<String> eveningDishImagesJSON, eveningDishNamesJSON_DA, eveningDishDescriptionJSON_DA, eveningDishNamesJSON_EN, eveningDishDescriptionJSON_EN, eveningDishNamesJSON_AR, eveningDishDescriptionJSON_AR;
    ArrayList<HashMap<String, String>> evDishNameDescriptionArrayList;
    HashMap<String, String> evDishNameDescriptionHashMap;

    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evening_menu_screen);
        setTitle(getString(R.string.evening_menu_title));

        mEvDishNameDescriptionList = findViewById(R.id.listView_EveningMenuListView);

        //JSON stuff - https://www.youtube.com/watch?v=PRQvn__YkCM
        PostResponseAsyncTask postResponseAsyncTask = new PostResponseAsyncTask(this);
        postResponseAsyncTask.execute("http://35.178.118.175/MadbestillingsappWebportal/eveningMenuJSON.php");

        mToolbarOrders = findViewById(R.id.my_OrdersToolbar);
        setSupportActionBar(mToolbarOrders);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public void processFinish(String result) {
        ArrayList<EveningDishJSON> EveningDishJSONList =
                new JsonConverter<EveningDishJSON>().toArrayList(result, EveningDishJSON.class);

        eveningDishImagesJSON = new ArrayList<String>();

        eveningDishNamesJSON_DA = new ArrayList<String>();
        eveningDishDescriptionJSON_DA = new ArrayList<String>();

        eveningDishNamesJSON_EN = new ArrayList<String>();
        eveningDishDescriptionJSON_EN = new ArrayList<String>();

        eveningDishNamesJSON_AR = new ArrayList<String>();
        eveningDishDescriptionJSON_AR = new ArrayList<String>();

        for (EveningDishJSON value : EveningDishJSONList) {

            eveningDishNamesJSON_DA.add(value.evNameLangDA);
            System.out.print(value.evNameLangDA);

            eveningDishDescriptionJSON_DA.add(value.evDescriptionLangDA);

            System.out.println(value.evDescriptionLangDA);

            eveningDishNamesJSON_EN.add(value.evNameLangEN);
            eveningDishDescriptionJSON_EN.add(value.evDescriptionLangEN);

            eveningDishNamesJSON_AR.add(value.evNameLangAR);
            eveningDishDescriptionJSON_AR.add(value.evDescriptionLangAR);

            eveningDishImagesJSON.add(value.evMenuImage);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.evening_dishname_dishdescription_list, eveningDishNamesJSON_DA); //eveningDishNamesJSON_DA

        mEvDishNameDescriptionList.setAdapter(adapter);

        //readEvDishNamesDescriptionList();

    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}