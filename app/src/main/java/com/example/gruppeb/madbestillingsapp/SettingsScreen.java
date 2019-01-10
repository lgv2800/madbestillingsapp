package com.example.gruppeb.madbestillingsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Reader;

public class SettingsScreen extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private Preference languagePicker;
    private Preference aboutScreen;
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

        //https://stackoverflow.com/a/8155029/8968120
        listPreference = (ListPreference) findPreference("changeDisplayLanguage");
        if (listPreference.getValue() == null) {
            listPreference.setValueIndex(0);
        }
        listPreference.setSummary(listPreference.getValue().toString());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());

                editorSettings = settingsSharedPreferences.edit();
                editorSettings.putString("languagePref", newValue.toString());
                editorSettings.apply();

                System.out.print(newValue.toString());

                return true;
            }
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

        return true;
    }

    private void voiceOverStatusSwitch() {
        SwitchPreference soundSwitch = (SwitchPreference) findPreference("soundEffectsSwitch");
        if (soundSwitch != null) {
            soundSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
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
                }
            });
        }
    }

    private void showAbout() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Madbestillingsappen er udviklet af Gruppe B, ITØ 2018.");
        dlgAlert.setTitle("Om appen");
        dlgAlert.create().show();
    }
}
