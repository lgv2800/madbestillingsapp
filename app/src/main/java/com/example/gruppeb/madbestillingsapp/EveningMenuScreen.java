package com.example.gruppeb.madbestillingsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.gruppeb.madbestillingsapp.Adapter.EveningMenuScreenAdapter;
import com.example.gruppeb.madbestillingsapp.Domain.LeafClass;
import com.example.gruppeb.madbestillingsapp.Helper.EveningDishJSON;
import com.example.gruppeb.madbestillingsapp.Model.EveningMenuScreenModel;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import java.util.ArrayList;
import java.util.List;

public class EveningMenuScreen extends AppCompatActivity implements AsyncResponse {

    RecyclerView recyclerView;
    EveningMenuScreenAdapter adapter;

    List<EveningMenuScreenModel> eveningMenuList;

    Toolbar mToolbarOrders;
    ArrayList<String> eveningDishImagesJSON, eveningDishNamesJSON_DA, eveningDishDescriptionJSON_DA, eveningDishCommentJSON_DA, eveningDishNamesJSON_EN, eveningDishDescriptionJSON_EN, eveningDishCommentJSON_EN,eveningDishNamesJSON_AR, eveningDishDescriptionJSON_AR, eveningDishCommentJSON_AR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evening_menu_screen);
        setTitle(getString(R.string.evening_menu_title));


        /**
         * JSON stuff - https://www.youtube.com/watch?v=PRQvn__YkCM
         */
        PostResponseAsyncTask postResponseAsyncTask = new PostResponseAsyncTask(this);
        postResponseAsyncTask.execute(LeafClass.URL_EVENING);

        mToolbarOrders = findViewById(R.id.my_OrdersToolbar);
        setSupportActionBar(mToolbarOrders);

        //RecyclerView, dishesFromOrdersList
        eveningMenuList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView_EveningMenuRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        eveningDishImagesJSON = new ArrayList<>();

        eveningDishNamesJSON_DA = new ArrayList<>();
        eveningDishDescriptionJSON_DA = new ArrayList<>();
        eveningDishCommentJSON_DA = new ArrayList<>();

        eveningDishNamesJSON_EN = new ArrayList<>();
        eveningDishDescriptionJSON_EN = new ArrayList<>();
        eveningDishCommentJSON_EN = new ArrayList<>();

        eveningDishNamesJSON_AR = new ArrayList<>();
        eveningDishDescriptionJSON_AR = new ArrayList<>();
        eveningDishCommentJSON_AR = new ArrayList<>();

        for (EveningDishJSON value : EveningDishJSONList) {

            eveningDishNamesJSON_DA.add(value.evNameLangDA);
            eveningDishDescriptionJSON_DA.add(value.evDescriptionLangDA);
            eveningDishCommentJSON_DA.add(value.evCommentLangDA);

            eveningDishNamesJSON_EN.add(value.evNameLangEN);
            eveningDishDescriptionJSON_EN.add(value.evDescriptionLangEN);

            eveningDishNamesJSON_AR.add(value.evNameLangAR);
            eveningDishDescriptionJSON_AR.add(value.evDescriptionLangAR);

            eveningDishImagesJSON.add(value.evMenuImage);
        }

        for (int i = 0; i < eveningDishNamesJSON_DA.size(); i++) {
            String mEveningDishNameJSON_DA = eveningDishNamesJSON_DA.get(i);
            String mEveningDishDescriptionJSON_DA = eveningDishDescriptionJSON_DA.get(i);
            String mEveningDishCommentJSON_DA = eveningDishCommentJSON_DA.get(i);

            eveningMenuList.add(new EveningMenuScreenModel(mEveningDishNameJSON_DA, mEveningDishDescriptionJSON_DA, mEveningDishCommentJSON_DA));
        }

        adapter = new EveningMenuScreenAdapter(getApplicationContext(), eveningMenuList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * https://developer.android.com/training/appbar/actions#java
     */
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