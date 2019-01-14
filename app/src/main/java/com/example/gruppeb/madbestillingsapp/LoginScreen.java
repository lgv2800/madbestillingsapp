package com.example.gruppeb.madbestillingsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import com.example.gruppeb.madbestillingsapp.Connector.Connector;
import com.example.gruppeb.madbestillingsapp.Domain.ILanguageSettings;
import com.example.gruppeb.madbestillingsapp.Domain.LanguageController;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private String languageFromLocalgetDefault, languageFromSharedPrefs, mRoomNumberFromSharedPrefs;

    private Boolean mBooleanRememberRoomNumber, mBooleanRememberRoomNumberFromSharedPrefs;
    private CheckBox mCheckBoxRememberRoomNumber;

    private ImageView mImageViewFlagDanish, mImageViewFlagEnglish, mImageViewFlagArabic;

    private final String TAG = "LoginScreen";

    Button mLoginButton;
    EditText mRoomNumberEnterField;
    com.airbnb.lottie.LottieAnimationView loadingAnimation;

    SharedPreferences settingsSharedPreferences;
    SharedPreferences.Editor editorSettings;
    LanguageController mLanguageController;

    Connector mConnector; //Database connector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playIntro();

        mLanguageController = new LanguageController();

        settingsSharedPreferences = getSharedPreferences("settingsPref", Context.MODE_PRIVATE);
        languageFromLocalgetDefault = Locale.getDefault().getLanguage();

        languageFromSharedPrefs = settingsSharedPreferences.getString("languagePref", languageFromLocalgetDefault);
        //languageFromSharedPrefs = settingsSharedPreferences.getString("languagePref", "");

        //https://blog.lokalise.co/android-app-localization/
        // Create a new Locale object
        Locale locale = new Locale(languageFromSharedPrefs);
        Locale.setDefault(locale);
        // Create a new configuration object
        Configuration config = new Configuration();
        // Set the locale of the new configuration
        config.locale = locale;
        // Update the configuration of the Accplication context
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );

        setContentView(R.layout.activity_login_screen);

        findViews();
        addOnClickListeners();


        mBooleanRememberRoomNumberFromSharedPrefs = settingsSharedPreferences.getBoolean("checkBoxRoomNumber", false);
        mRoomNumberFromSharedPrefs = settingsSharedPreferences.getString("roomNumberInput", "");

        if (mBooleanRememberRoomNumberFromSharedPrefs) {
            Intent intent = new Intent(this, MainScreen.class);
            this.startActivity(intent);
            this.finishActivity(0);
            System.out.println(mRoomNumberFromSharedPrefs);
            Order.ROOM_NUMBER = mRoomNumberFromSharedPrefs;
        }
        //Allows for enter taps on keyboard to play round.
        mRoomNumberEnterField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mLoginButton.performClick();
                return true;
            }
            return false;
        });

        //Database
        mConnector = new Connector();
    }

    private void addOnClickListeners() {
        mLoginButton.setOnClickListener(this);

        mImageViewFlagArabic.setOnClickListener(this);
        mImageViewFlagDanish.setOnClickListener(this);
        mImageViewFlagEnglish.setOnClickListener(this);
    }

    private void findViews() {
        mLoginButton = findViewById(R.id.login_button);
        loadingAnimation = findViewById(R.id.login_animation_loading);
        mCheckBoxRememberRoomNumber = findViewById(R.id.checkBox_rememberRoomNumber);
        mImageViewFlagDanish = findViewById(R.id.imageView_flag_danish);
        mImageViewFlagEnglish = findViewById(R.id.imageView_flag_english);
        mImageViewFlagArabic = findViewById(R.id.imageView_flag_arabic);
        mRoomNumberEnterField = findViewById(R.id.login_number);
    }


    private class loginAsyncTaskStatement extends AsyncTask<String, String, String> {
        //Database
        private String roomNumberString = mRoomNumberEnterField.getText().toString();
        private boolean isSuccess = false;

        private String roomNumberQuery;
        private String errorMessage = "";

        @Override
        protected String doInBackground(String... params) {
            if (roomNumberString.trim().equals(""))
                errorMessage = getString(R.string.login_login_message_fillvalue);
            else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = getString(R.string.login_login_message_checkinternet);
                    } else {

                        System.out.println("Forbindelse til DB er aktiv.");

                        //String query = " SELECT * FROM Orders WHERE roomNumber='" + roomNumberString + "'";
                        String query = " SELECT * FROM roomNumber WHERE roomNumber='" + roomNumberString + "'";

                        Statement stmt = con.createStatement();
                        // stmt.executeUpdate(query);

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next())

                        {
                            //i = Placement of column in table
                            roomNumberQuery = rs.getString(1);

                            if (roomNumberQuery.equals(roomNumberString)) {

                                isSuccess = true;
                                errorMessage = getString(R.string.login_login_message_loginsuccesswithroomnumber) + roomNumberString;

                            } else {

                                isSuccess = false;
                                errorMessage = getString(R.string.login_error_login);
                            }
                        }

                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    errorMessage = "Exceptions" + ex;
                }
            }
            return errorMessage;
        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {

                Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                startActivity(intent);
                setAnimation(false);
                Toast.makeText(getBaseContext(), "" + errorMessage, Toast.LENGTH_LONG).show();
                Order.ROOM_NUMBER = roomNumberString;
            }

            if (!isSuccess) {
                setAnimation(false);
                Toast wrongNrToast = Toast.makeText(LoginScreen.this,"Forkert værelsesnummer, prøv igen", Toast.LENGTH_LONG);
                wrongNrToast.show();
            }

        }
    }


    @Override
    public void onClick(View v) {

        if (v == mLoginButton) {
            loginAsyncTaskStatement mloginAsyncTaskStatement = new loginAsyncTaskStatement();
            mloginAsyncTaskStatement.execute();

            if (mCheckBoxRememberRoomNumber.isChecked()) {
                Toast.makeText(getBaseContext(), "Rum nr huskes.", Toast.LENGTH_SHORT).show();
                mBooleanRememberRoomNumber = true;
                setCheckBoxRememberRoomNumber();
            }

            setAnimation(true);
        }

        if (v == mImageViewFlagArabic){
            Log.d(TAG, "Language changed to ar");
            mLanguageController.changeLanguage("ar", this);
        }

        if (v == mImageViewFlagDanish){
            Log.d(TAG, "Language changed to da");
            mLanguageController.changeLanguage("da", this);
        }

        if (v == mImageViewFlagEnglish){
            Log.d(TAG, "Language changed to en");
            mLanguageController.changeLanguage("en", this);
        }
    }

    private void setAnimation(Boolean a) {
        if (a) {
            loadingAnimation.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.INVISIBLE);
        }
        if (!a) {
            loadingAnimation.setVisibility(View.INVISIBLE);
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }

    private void setCheckBoxRememberRoomNumber() {
        settingsSharedPreferences = getSharedPreferences("settingsPref", Context.MODE_PRIVATE);

        editorSettings = settingsSharedPreferences.edit();
        editorSettings.putBoolean("checkBoxRoomNumber", mBooleanRememberRoomNumber);
        editorSettings.putString("roomNumberInput", Order.ROOM_NUMBER);
        editorSettings.apply();
        editorSettings.commit();

        System.out.println(mBooleanRememberRoomNumber);
    }

    private void playIntro() {
        SharedPreferences sp = getSharedPreferences("first_time", MODE_PRIVATE);
        if (!sp.getBoolean("first_onboard", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first_onboard", true);
            editor.apply();
            Intent intent = new Intent(LoginScreen.this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }
    }

}
