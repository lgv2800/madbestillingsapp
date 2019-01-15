package com.example.gruppeb.madbestillingsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.example.gruppeb.madbestillingsapp.Domain.BreadType;
import com.example.gruppeb.madbestillingsapp.Domain.FragmentPage;
import com.example.gruppeb.madbestillingsapp.Domain.Order;
import com.example.gruppeb.madbestillingsapp.FoodFragments.*;
import com.example.gruppeb.madbestillingsapp.Helper.DishJSON;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import io.fabric.sdk.android.Fabric;

public class MainScreen extends AppCompatActivity implements View.OnClickListener, BreadType, NavigationView.OnNavigationItemSelectedListener, AsyncResponse {

    Toolbar mToolbar;
    FloatingActionButton fab;
    ViewPager viewPager;
    private boolean isLight = false;
    private DrawerLayout drawer;
    private TextView badgeCount;
    private ImageView imageView_page1;
    Order order;

    private String languageFromSharedPrefs;
    SharedPreferences settingsSharedPreferences;

    private Context mContext = MainScreen.this;
    ViewPagerAdapter adapter;

    TextToSpeech mTextToSpeech;

    //ArrayList for dishNamesJSON, dishDescriptionJSON for languages DA, EN and AR.
    ArrayList<String> dishNamesJSON_DA;
    ArrayList<String> dishDescriptionJSON_DA;
    ArrayList<String> dishNamesJSON_EN;
    ArrayList<String> dishDescriptionJSON_EN;
    ArrayList<String> dishNamesJSON_AR;
    ArrayList<String> dishDescriptionJSON_AR;

    //ArrayList for dishImagesJSON.
    ArrayList<String> dishImagesJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //https://docs.google.com/document/d/10H-RIjr2M6xhAIlb52mhRZdbc4dtuCgcdJBDlPAr_mM/edit#
        boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator");
        if (!EMULATOR) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_main_screen);

        //Shared preferences
        settingsSharedPreferences = getSharedPreferences("settingsPref", Context.MODE_PRIVATE);
        languageFromSharedPrefs = settingsSharedPreferences.getString("languagePref", "");

        //JSON stuff - https://www.youtube.com/watch?v=PRQvn__YkCM
        PostResponseAsyncTask postResponseAsyncTask = new PostResponseAsyncTask(this);
        postResponseAsyncTask.execute("http://35.178.118.175/MadbestillingsappWebportal/dayMenuJSON.php");

        //Text To Speech stuff
        //https://www.tutorialspoint.com/android/android_text_to_speech.htm
        //https://developer.android.com/reference/android/speech/tts/TextToSpeech
        //https://stackoverflow.com/questions/3058919/text-to-speechtts-android
        //https://android-developers.googleblog.com/2009/09/introduction-to-text-to-speech-in.html
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int TTS_Status) {
                if (TTS_Status == TextToSpeech.SUCCESS) {
                    int TTS_Language = mTextToSpeech.setLanguage(new Locale(languageFromSharedPrefs, ""));

                    mTextToSpeech.speak(getString(R.string.startup_welcome),TextToSpeech.QUEUE_FLUSH,null,null);

                }
            }
        });

        imageView_page1 = findViewById(R.id.page1_image);

        //Order logic
        order = new Order();
        IntroGuide intro = new IntroGuide();

        //Add viewpager
        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add toolbar
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        drawer = findViewById(R.id.drawer_layout);

        //https://stackoverflow.com/a/38418531/8968120
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView roomNumberNavbar = headerView.findViewById(R.id.nav_header_roomNumber);
        roomNumberNavbar.setText(getString(R.string.drawermenu_header_roomNumber) + Order.ROOM_NUMBER);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sp = getSharedPreferences("first_time", MODE_PRIVATE);
        if (!sp.getBoolean("first_prompt", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first_prompt", true);
            editor.apply();
            intro.playGuide(this, MainScreen.this);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Add tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        updateView();
    }

    @Override
    public void processFinish(String result) {
        ArrayList<DishJSON> dishJSONList =
                new JsonConverter<DishJSON>().toArrayList(result, DishJSON.class);

        dishImagesJSON = new ArrayList<String>();

        dishNamesJSON_DA = new ArrayList<String>();
        dishDescriptionJSON_DA = new ArrayList<String>();

        dishNamesJSON_EN = new ArrayList<String>();
        dishDescriptionJSON_EN = new ArrayList<String>();

        dishNamesJSON_AR = new ArrayList<String>();
        dishDescriptionJSON_AR = new ArrayList<String>();

        for (DishJSON value : dishJSONList) {
            dishNamesJSON_DA.add(value.daNameLangDA);
            dishDescriptionJSON_DA.add(value.daDescriptionLangDA);

            dishNamesJSON_EN.add(value.daNameLangEN);
            dishDescriptionJSON_EN.add(value.daDescriptionLangEN);

            dishNamesJSON_AR.add(value.daNameLangAR);
            dishDescriptionJSON_AR.add(value.daDescriptionLangAR);

            dishImagesJSON.add(value.daMenuImage);
        }

        fragmentGenerator();
    }

    private void fragmentGenerator() {
        languageFromSharedPrefs = settingsSharedPreferences.getString("languagePref", "");

        switch (languageFromSharedPrefs) {
            case "da":
                for (int i = 0; i < dishNamesJSON_DA.size(); i++) {
                    String title = dishNamesJSON_DA.get(i);
                    String description = dishDescriptionJSON_DA.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                    viewPager.setAdapter(adapter);

                    /*
                    //Set image
                    Glide
                            .with(this)
                            .load(internetURL)
                            .into(imageView_page1);
                    */
                }
                ;
                break;
            case "en":
                for (int i = 0; i < dishNamesJSON_EN.size(); i++) {
                    String title = dishNamesJSON_EN.get(i);
                    String description = dishDescriptionJSON_EN.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                    viewPager.setAdapter(adapter);

                    /*
                    //Set image
                    Glide
                            .with(this)
                            .load(internetURL)
                            .into(imageView_page1);*/
                }
                ;
                break;
            case "ar":
                for (int i = 0; i < dishNamesJSON_AR.size(); i++) {
                    String title = dishNamesJSON_AR.get(i);
                    String description = dishDescriptionJSON_AR.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                    viewPager.setAdapter(adapter);

                    /*
                    //Set image
                    Glide
                            .with(this)
                            .load(internetURL)
                            .into(imageView_page1);*/
                }
                ;
                break;
            default:
                for (int i = 0; i < dishNamesJSON_DA.size(); i++) {
                    String title = dishNamesJSON_DA.get(i);
                    String description = dishDescriptionJSON_DA.get(i);
                    String internetURL = dishImagesJSON.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("description", description);
                    FragmentPage fragment = new FragmentPage();
                    fragment.setArguments(bundle);
                    adapter.addFragment(fragment, title);
                    viewPager.setAdapter(adapter);

                    /*
                    //Set image
                    Glide
                            .with(this)
                            .load(internetURL)
                            .into(imageView_page1);*/
                }
                ;
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_myOrders:
                //TO BE handled
                Intent openMyOrdersScreenIntent = new Intent(MainScreen.this, MyOrdersScreen.class);
                startActivity(openMyOrdersScreenIntent);
                break;
            case R.id.nav_mySettings:
                //TO BE handled
                Intent openSettingsScreenIntent = new Intent(MainScreen.this, SettingsScreen.class);
                startActivity(openSettingsScreenIntent);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.shopping_cart);
        View view = menuItem.getActionView();
        badgeCount = view.findViewById(R.id.cart_badge);
        badgeCount.setText(Integer.toString(0));
        updateView();
        view.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }

    @Override
    public void setBreadType(boolean a) {
        this.isLight = a;
    }

    //Code skeleton from http://www.gadgetsaint.com/android/create-viewpager-tabs-android/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                // User chose the "Settings" item, show the app settings UI...
                Intent openCart = new Intent(this, CartScreen.class);
                startActivity(openCart);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Snackbar.make(v, R.string.action_added, Snackbar.LENGTH_LONG)
                        .setActionTextColor(getColor(R.color.white))
                        .setAction(R.string.action_cart, v1 -> {
                            Intent openCart = new Intent(getApplicationContext(), CartScreen.class);
                            startActivity(openCart);
                        }).show();
            }
            //https://stackoverflow.com/questions/31891062/animate-the-cart-icon-on-button-click
            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
            scale.setDuration(500);
            scale.setInterpolator(new OvershootInterpolator());
            badgeCount.startAnimation(scale);
            order.order(viewPager.getCurrentItem(), isLight, getApplication());
            updateView();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        int count = order.getCount(this);
        if (badgeCount != null) {
            if (count == 0) {
                badgeCount.setVisibility(View.GONE);
            } else {
                badgeCount.setText(Integer.toString(count));
                badgeCount.setVisibility(View.VISIBLE);
            }
        }
    }

}
