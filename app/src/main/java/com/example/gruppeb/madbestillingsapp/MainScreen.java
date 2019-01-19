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
import android.os.Handler;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.example.gruppeb.madbestillingsapp.Connector.Connector;
import com.example.gruppeb.madbestillingsapp.Domain.BreadType;
import com.example.gruppeb.madbestillingsapp.Domain.FragmentPage;
import com.example.gruppeb.madbestillingsapp.Domain.JsonObserver;
import com.example.gruppeb.madbestillingsapp.Domain.LanguageController;
import com.example.gruppeb.madbestillingsapp.Domain.Order;
import com.example.gruppeb.madbestillingsapp.FoodFragments.*;
import com.example.gruppeb.madbestillingsapp.Helper.DishJSON;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import io.fabric.sdk.android.Fabric;

public class MainScreen extends AppCompatActivity implements View.OnClickListener, BreadType, NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    FloatingActionButton fab;
    ViewPager viewPager;
    private boolean isLight = false;
    private DrawerLayout drawer;
    private TextView badgeCount;
    private ImageView imageView_page1;
    Order order;
    ImageButton play;

    IntroGuide intro;

    ProgressDialog progressDialog;

    private String newLanguageValue;
    private String languageFromSharedPrefs;
    SharedPreferences settingsSharedPreferences;
    SharedPreferences.Editor editorSettings;

    JsonController jsonController;
    private Context mContext = MainScreen.this;
    private CheckBox mCheckBoxVoiceOver;
    private Boolean mBooleanVoiceOverIfChecked;
    TextToSpeech mTextToSpeech;
    LanguageController languageController;

    //ArrayList for dishNamesJSON, dishDescriptionJSON for languages DA, EN and AR.
    ArrayList<String> dishNamesJSON_DA, dishDescriptionJSON_DA, dishNamesJSON_EN, dishDescriptionJSON_EN, dishNamesJSON_AR, dishDescriptionJSON_AR, dishImagesJSON;
    public List<String> mFragmentTitleList;
    Connector mConnector; //Database connector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //https://docs.google.com/document/d/10H-RIjr2M6xhAIlb52mhRZdbc4dtuCgcdJBDlPAr_mM/edit#
        boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator");
        if (!EMULATOR) {
            Fabric.with(this, new Crashlytics());
        }
        setContentView(R.layout.activity_main_screen);
        order = new Order();
        jsonController = new JsonController(this, MainScreen.this);
        jsonController.doAction();
    }

    public void initialView() {
        viewPager = findViewById(R.id.pager);
        jsonController.setAdapter(viewPager);


        //Shared preferences
        settingsSharedPreferences = getSharedPreferences("settingsPref", Context.MODE_PRIVATE);


        //Text To Speech stuff
        //https://www.tutorialspoint.com/android/android_text_to_speech.htm
        //https://developer.android.com/reference/android/speech/tts/TextToSpeech
        //https://stackoverflow.com/questions/3058919/text-to-speechtts-android
        //https://android-developers.googleblog.com/2009/09/introduction-to-text-to-speech-in.html
        //https://stacktips.com/tutorials/android/android-texttospeech-example
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int TTS_Status) {
                if (TTS_Status == TextToSpeech.SUCCESS) {
                    int TTS_Result = mTextToSpeech.setLanguage(new Locale(jsonController.getLanguage(), ""));
                    if (TTS_Result == TextToSpeech.LANG_MISSING_DATA || TTS_Result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    }
                } else {
                    Log.e("error", "Initialization Failed!");
                }

                //mTextToSpeech.speak(getString(R.string.startup_welcome), TextToSpeech.QUEUE_FLUSH, null, null);

            }

        });

        imageView_page1 = findViewById(R.id.page1_image);

        //Order logic
        intro = new IntroGuide();

        //Add toolbar
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        drawer = findViewById(R.id.drawer_layout);

        progressDialog = new ProgressDialog(this);

        //https://stackoverflow.com/a/38418531/8968120
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView roomNumberNavbar = headerView.findViewById(R.id.nav_header_roomNumber);
        roomNumberNavbar.setText(getString(R.string.drawermenu_header_roomNumber) + " " + Order.ROOM_NUMBER);
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

        play = findViewById(R.id.tts_play);
        play.setOnClickListener(this);

        updateView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_myOrders:
                Intent openMyOrdersScreenIntent = new Intent(MainScreen.this, MyOrdersScreen.class);
                startActivity(openMyOrdersScreenIntent);
                break;
            case R.id.nav_myEveningMenu:
                Intent openEveningMenuScreenIntent = new Intent(MainScreen.this, EveningMenuScreen.class);
                startActivity(openEveningMenuScreenIntent);
                break;
            case R.id.nav_mySettingsLanguage:
                LanguageAlertDialog();
                break;
            case R.id.nav_logout:
                settingsSharedPreferences = getSharedPreferences("settingsPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = settingsSharedPreferences.edit();
                edit.putBoolean("checkBoxRoomNumber", false);
                edit.apply();
                Intent i = new Intent(MainScreen.this, LoginScreen.class);
                startActivity(i);
                break;
            case R.id.nav_mySettingsVoiceOverSwitch:
                break;
            case R.id.nav_introplay:
                intro.playGuide(this, MainScreen.this);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void LanguageAlertDialog() {
        String[] languageList = {"Dansk", "English", "العربية"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.drawermenu_language_title));
        builder.setItems(languageList, (dialog, intFromList) -> {

            switch (intFromList) {
                case 0:
                    newLanguageValue = "da";
                    editorSettings = settingsSharedPreferences.edit();
                    editorSettings.putString("languagePref", newLanguageValue);
                    editorSettings.apply();
                    LanguageChangedToast("Sprog ændret. Genstart venligst applikationen.");
                    break;
                case 1:
                    newLanguageValue = "en";
                    editorSettings = settingsSharedPreferences.edit();
                    editorSettings.putString("languagePref", newLanguageValue);
                    editorSettings.apply();
                    LanguageChangedToast("Language changed. Please restart application.");
                    break;
                case 2:
                    newLanguageValue = "ar";
                    editorSettings = settingsSharedPreferences.edit();
                    editorSettings.putString("languagePref", newLanguageValue);
                    editorSettings.apply();
                    LanguageChangedToast("تغيرت اللغة. يرجى إعادة تشغيل التطبيق.");
                    break;
            }

        });
        builder.show();
    }

    private void LanguageChangedToast(String text) {
        Toast.makeText(this,text , Toast.LENGTH_SHORT).show();
    }

    private void VoiceOverChangedToast() {
        if (mBooleanVoiceOverIfChecked) {
            // Do your coding
            Toast.makeText(this, "VoiceOver er slået til.", Toast.LENGTH_SHORT).show();
        } else {
            // Do your coding
            Toast.makeText(this, "VoiceOver er slået fra.", Toast.LENGTH_SHORT).show();
        }
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
            /**
             * kilde
             *  https://stackoverflow.com/questions/31891062/animate-the-cart-icon-on-button-click
             */
            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
            scale.setDuration(500);
            scale.setInterpolator(new OvershootInterpolator());
            badgeCount.startAnimation(scale);
            order.order(jsonController.getFragmentTitle(viewPager.getCurrentItem()), isLight, getApplication());
            updateView();
        }
        if (v == play) {
            textToSpeech();
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

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    public void textToSpeech() {
        int position = viewPager.getCurrentItem();
        String read = jsonController.getFragmentTitle(languageController.getLanguage(this), position) + " " + jsonController.getFragmentDescription(languageController.getLanguage(this), position);
        mTextToSpeech.speak(read, TextToSpeech.QUEUE_FLUSH, null);
    }
}