package com.example.gruppeb.madbestillingsapp.FoodFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.gruppeb.madbestillingsapp.Domain.FragmentPage;
import com.example.gruppeb.madbestillingsapp.Domain.JsonObserver;
import com.example.gruppeb.madbestillingsapp.Helper.DishJSON;
import com.example.gruppeb.madbestillingsapp.JsonController;
import com.example.gruppeb.madbestillingsapp.LoginScreen;
import com.example.gruppeb.madbestillingsapp.MainScreen;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class FragmentGenerator implements AsyncResponse {
    private Context context;
    private String url;
    private ArrayList<String> dishNamesJSON_DA, dishDescriptionJSON_DA, dishNamesJSON_EN, dishDescriptionJSON_EN, dishNamesJSON_AR, dishDescriptionJSON_AR, dishImagesJSON, fragmentTitle;
    private ArrayList<Fragment> fragments;
    private ViewPagerAdapter adapter;
    JsonController jsonController;

    public static volatile FragmentGenerator sSoleInstance = new FragmentGenerator();

    private FragmentGenerator(){}

    public static FragmentGenerator getInstance(){
        return sSoleInstance;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void getJsonFiles(String url){
        this.url = url;
        //JSON stuff - https://www.youtube.com/watch?v=PRQvn__YkCM
        PostResponseAsyncTask postResponseAsyncTask = new PostResponseAsyncTask(context, this );
        postResponseAsyncTask.execute(url);
    }


    @Override
    public void processFinish(String result) {
        ArrayList<DishJSON> dishJSONList =
                new JsonConverter<DishJSON>().toArrayList(result, DishJSON.class);

        dishImagesJSON = new ArrayList<>();

        dishNamesJSON_DA = new ArrayList<>();
        dishDescriptionJSON_DA = new ArrayList<>();

        dishNamesJSON_EN = new ArrayList<>();
        dishDescriptionJSON_EN = new ArrayList<>();

        dishNamesJSON_AR = new ArrayList<>();
        dishDescriptionJSON_AR = new ArrayList<>();

        for (DishJSON value : dishJSONList) {
            dishNamesJSON_DA.add(value.daNameLangDA);
            dishDescriptionJSON_DA.add(value.daDescriptionLangDA);

            dishNamesJSON_EN.add(value.daNameLangEN);
            dishDescriptionJSON_EN.add(value.daDescriptionLangEN);

            dishNamesJSON_AR.add(value.daNameLangAR);
            dishDescriptionJSON_AR.add(value.daDescriptionLangAR);

            dishImagesJSON.add(value.daMenuImage);
        }
        jsonController.jsonCompleted();

    }

    public void fragmentGenerator(MainScreen mainScreen) {
        String languageFromSharedPrefs = getLanguage();
        fragments = new ArrayList<>();
        fragmentTitle = new ArrayList<>();
        //Add viewpager
        adapter = new ViewPagerAdapter(mainScreen.getSupportFragmentManager());

        switch (languageFromSharedPrefs) {
            case "da":
                for (int i = 0; i < dishNamesJSON_DA.size(); i++) {
                    String title = dishNamesJSON_DA.get(i);
                    String description = dishDescriptionJSON_DA.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    bundle.putString("image", internetURL);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                }
                break;
            case "en":
                for (int i = 0; i < dishNamesJSON_EN.size(); i++) {
                    String title = dishNamesJSON_EN.get(i);
                    String description = dishDescriptionJSON_EN.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    bundle.putString("image", internetURL);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                }
                break;
            case "ar":
                for (int i = 0; i < dishNamesJSON_AR.size(); i++) {
                    String title = dishNamesJSON_AR.get(i);
                    String description = dishDescriptionJSON_AR.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    bundle.putString("image", internetURL);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                }
                break;
            default:
                for (int i = 0; i < dishNamesJSON_DA.size(); i++) {
                    String title = dishNamesJSON_DA.get(i);
                    String description = dishDescriptionJSON_DA.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    bundle.putString("image", internetURL);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                }
                break;
        }

    }

    /**
     *
     * @return language, default is sys_default
     */
    public String getLanguage() {
        SharedPreferences settingsSharedPreferences = context.getSharedPreferences("settingsPref", Context.MODE_PRIVATE);
        String languageFromLocalgetDefault = Locale.getDefault().getLanguage();
        return settingsSharedPreferences.getString("languagePref", languageFromLocalgetDefault);
    }

    public ViewPagerAdapter getAdapter(){
        return adapter;
    }

    public ArrayList<Fragment> getFragments(){
        return fragments;
    }

    public String getFragmentTitle(int position){
        return fragmentTitle.get(position);
    }

    public void setController(JsonController jsonController) {
        this.jsonController = jsonController;
    }

    public void setAdapter(ViewPager viewPager, MainScreen main) {
        fragmentGenerator(main);
        viewPager.setAdapter(getAdapter());
    }

    public String getFragmentDescription(String language, int position) {
        switch(language){
            case "da" : return dishDescriptionJSON_DA.get(position);
            case "en" : return dishDescriptionJSON_EN.get(position);
            case "ar" : return dishDescriptionJSON_AR.get(position);
        }
        return dishDescriptionJSON_DA.get(position);
    }

    public String getFragmentTitle(String language, int position){
        switch (language){
            case "da" : return dishNamesJSON_DA.get(position);
            case "en" : return dishNamesJSON_EN.get(position);
            case "ar" : return dishNamesJSON_AR.get(position);
        }
        return dishNamesJSON_DA.get(position);
    }

    //Code skeleton from http://www.gadgetsaint.com/android/create-viewpager-tabs-android/

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

    }

}
