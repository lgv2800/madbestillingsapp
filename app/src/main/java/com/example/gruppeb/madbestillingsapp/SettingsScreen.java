package com.example.gruppeb.madbestillingsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Reader;

public class SettingsScreen extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private Preference aboutScreen;
    private Preference roomNumberInput;
    private Preference voiceOverSwitch;
    private ListPreference listPreference;

    private Boolean voiceOverStatus;

    SharedPreferences settingsSharedPreferences;
    SharedPreferences.Editor editorSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        settingsSharedPreferences = getSharedPreferences("settingsPref", Context.MODE_PRIVATE);

        aboutScreen = findPreference("aboutScreen");
        aboutScreen.setOnPreferenceClickListener(this);

        voiceOverSwitch = findPreference("changeVoiceOver");
        voiceOverSwitch.setOnPreferenceClickListener(this);

        roomNumberInput = findPreference("roomNumberInput");
        roomNumberInput.setOnPreferenceClickListener(this);

        //https://stackoverflow.com/a/8155029/8968120
        listPreference = (ListPreference) findPreference("changeDisplayLanguage");
        if (listPreference.getValue() == null) {
            listPreference.setValueIndex(0);
        }
        listPreference.setSummary(listPreference.getValue().toString());
        listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(newValue.toString());

            editorSettings = settingsSharedPreferences.edit();
            editorSettings.putString("languagePref", newValue.toString());
            editorSettings.apply();

            switch (newValue.toString()) {
                case "da":
                    Toast.makeText(this, "Sprog ændret. Genstart venligst applikationen.", Toast.LENGTH_SHORT).show();
                    ;
                    break;
                case "en":
                    Toast.makeText(this, "Language changed. Please restart application.", Toast.LENGTH_SHORT).show();
                    ;
                    break;
                case "ar":
                    Toast.makeText(this, "تغيرت اللغة. يرجى إعادة تشغيل التطبيق.", Toast.LENGTH_SHORT).show();

                    ;
                    break;
                default:
                    Toast.makeText(this, "Sprog ændret. Genstart venligst applikationen.", Toast.LENGTH_SHORT).show();
                    ;
                    break;

        }

            System.out.print(newValue.toString());

            return true;
        });
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == aboutScreen) {
            showAbout();
        }

        if (preference == voiceOverSwitch) {
            voiceOverStatusSwitch();
        }

        if (preference == roomNumberInput) {
            roomNumberInput();
        }

        return true;
    }

    private void voiceOverStatusSwitch() {
        SwitchPreference soundSwitch = (SwitchPreference) findPreference("soundEffectsSwitch");
        if (soundSwitch != null) {
            soundSwitch.setOnPreferenceChangeListener((preference, o) -> {
                voiceOverStatus = settingsSharedPreferences.getBoolean("soundPref", true);

                if (voiceOverStatus) {
                    editorSettings = settingsSharedPreferences.edit();
                    editorSettings.putBoolean("soundPref", voiceOverStatus);
                    editorSettings.apply();
                } else {
                    editorSettings = settingsSharedPreferences.edit();
                    editorSettings.putBoolean("soundPref", voiceOverStatus);
                    editorSettings.apply();
                }
                return true;
            });
        }
    }

    private void showAbout() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Madbestillingsappen er udviklet af Gruppe B, ITØ 2018.");
        dlgAlert.setTitle("Om appen");
        dlgAlert.create().show();
    }

    private void roomNumberInput() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String roomNumberFromPrefsManager = preferences.getString("roomNumberInput", "");

        editorSettings = settingsSharedPreferences.edit();
        editorSettings.putString("roomNumberInput", roomNumberFromPrefsManager);
        editorSettings.apply();
        editorSettings.commit();
        System.out.println(roomNumberFromPrefsManager);
    }
}
