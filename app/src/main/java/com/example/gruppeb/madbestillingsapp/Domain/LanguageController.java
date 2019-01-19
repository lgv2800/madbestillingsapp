package com.example.gruppeb.madbestillingsapp.Domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gruppeb.madbestillingsapp.R;

public class LanguageController implements ILanguageSettings{
    @Override
    public void changeLanguage(String language, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_settings), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString("languagePref", language).apply();
    }

    @Override
    public String getLanguage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("settingsPref",Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("languagePref","da");
        return language;
    }
}
