package com.example.gruppeb.madbestillingsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.gruppeb.madbestillingsapp.Domain.LanguageController;
import com.example.gruppeb.madbestillingsapp.Domain.Order;
import com.example.gruppeb.madbestillingsapp.FoodFragments.FragmentGenerator;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private String languageFromLocalgetDefault, languageFromSharedPrefs, mRoomNumberFromSharedPrefs;

    private Boolean mBooleanRememberRoomNumber, mBooleanRememberRoomNumberFromSharedPrefs;
    private CheckBox mCheckBoxRememberRoomNumber;

    private TextView mImageViewFlagDanish, mImageViewFlagEnglish, mImageViewFlagArabic;

    private final String TAG = "LoginScreen";
    private final String url = "http://35.178.118.175/MadbestillingsappWebportal/dayMenuJSON.php";

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
        updateChosenLanguage();

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

    /**
     * TODO: Den ændrer ikke til bold. ved ikke helt hvorfor. Har også prøvet setTextSize, men den ændrer heller intet
     */
    private void updateChosenLanguage() {
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        mImageViewFlagArabic.setTypeface(null, Typeface.NORMAL);
        mImageViewFlagEnglish.setTypeface(null, Typeface.NORMAL);
        mImageViewFlagDanish.setTypeface(null, Typeface.NORMAL);
        switch (languageFromSharedPrefs) {
            case "ar":
                mImageViewFlagArabic.setTypeface(boldTypeface);
                break;
            case "en":
                mImageViewFlagArabic.setTypeface(boldTypeface);
                break;
            case "da":
                mImageViewFlagArabic.setTypeface(boldTypeface);
                break;

        }
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
        mImageViewFlagDanish = findViewById(R.id.login_language_danish);
        mImageViewFlagEnglish = findViewById(R.id.login_language_english);
        mImageViewFlagArabic = findViewById(R.id.login_language_arabic);
        mRoomNumberEnterField = findViewById(R.id.login_number);
    }


    private class loginAsyncTaskStatement extends AsyncTask<String, String, String> {
        //Database
        private String roomNumberString = mRoomNumberEnterField.getText().toString();
        private String roomNumberStatus = "0";
        private boolean isSuccess = false;

        private String roomNumberQuery;
        private String roomNumberStatusQuery;
        private String errorMessage = "";

        @Override
        protected String doInBackground(String... params) {
            if (roomNumberString.equals("")) {
                errorMessage = getString(R.string.login_login_message_fillvalue);
            } else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = getString(R.string.login_login_message_checkinternet);
                    } else {

                        System.out.println("Forbindelse til DB er aktiv.");

                        String query = " SELECT * FROM roomNumber WHERE roomNumber='" + roomNumberString + "' AND roomNumberStatus='" + roomNumberStatus + "'";

                        Statement stmt = con.createStatement();
                        // stmt.executeUpdate(query);

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            //i = Placement of column in table
                            roomNumberQuery = rs.getString(1);
                            roomNumberStatusQuery = rs.getString(2);

                            if (roomNumberQuery.equals(roomNumberString) && roomNumberStatusQuery.equals(roomNumberStatus)) {

                                isSuccess = true;
                                errorMessage = getString(R.string.login_login_message_loginsuccesswithroomnumber) + " " + roomNumberString;

                            }
                        }
                        if (!isSuccess) {
                            errorMessage = getString(R.string.login_error_login);

                        }
                    }


            } catch(Exception ex){
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
                Order.ROOM_NUMBER = roomNumberString;
                finish();
            }

            if (!isSuccess) {
                setAnimation(false);
                mRoomNumberEnterField.setError(errorMessage);
            }

        }
    }


    @Override
    public void onClick(View v) {

        if (v == mLoginButton) {
            FragmentGenerator frag = FragmentGenerator.getInstance();
            frag.setContext(this);
            frag.getJsonFiles(url);
            loginAsyncTaskStatement mloginAsyncTaskStatement = new loginAsyncTaskStatement();
            mloginAsyncTaskStatement.execute();

            if (mCheckBoxRememberRoomNumber.isChecked()) {
                Toast.makeText(getBaseContext(), "Rum nr huskes.", Toast.LENGTH_SHORT).show();
                mBooleanRememberRoomNumber = true;
                setCheckBoxRememberRoomNumber();
            }

            setAnimation(true);
        }

        if (v == mImageViewFlagArabic) {
            Log.d(TAG, "Language changed to ar");
            mLanguageController.changeLanguage("ar", this);
            updateChosenLanguage();
            Toast.makeText(this, "تغيرت اللغة. يرجى إعادة تشغيل التطبيق.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, getString(R.string.language_changed_setting), Toast.LENGTH_SHORT).show();
        }

        if (v == mImageViewFlagDanish) {
            Log.d(TAG, "Language changed to da");
            mLanguageController.changeLanguage("da", this);
            updateChosenLanguage();
            Toast.makeText(this, "Sprog ændret. Genstart venligst applikationen.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, getString(R.string.language_changed_setting), Toast.LENGTH_SHORT).show();
        }

        if (v == mImageViewFlagEnglish) {
            Log.d(TAG, "Language changed to en");
            mLanguageController.changeLanguage("en", this);
            updateChosenLanguage();
            Toast.makeText(this, "Language changed. Please restart application.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, getString(R.string.language_changed_setting), Toast.LENGTH_SHORT).show();
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
