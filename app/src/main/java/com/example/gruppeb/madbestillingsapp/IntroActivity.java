package com.example.gruppeb.madbestillingsapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FontRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Skelet fra https://github.com/PaoloRotolo/AppIntro
 * <p>
 * Billeder lånt af Pixabay.com og Wikimedia.org (fri deling, både privat og kommercielt)
 */
public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * i det følgende produceres siderne automatisk vha. klassen SampleSlides konstruktør
         */


        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle(getString(R.string.onboard_first_title));
        sliderPage.setDescription(getString(R.string.onboard_first_desc));
        sliderPage.setImageDrawable(R.mipmap.oboard_food1);
        sliderPage.setBgColor(getResources().getColor(R.color.colorComplementary1));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle(getString(R.string.onboard_second_title));
        sliderPage1.setDescription(getString(R.string.onboard_second_desc));
        sliderPage1.setImageDrawable(R.mipmap.onboard_food3);
        sliderPage1.setBgColor(getResources().getColor(R.color.colorComplementary2));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getString(R.string.onboard_third_title));
        sliderPage2.setDescription(getString(R.string.onboard_third_desc));
        sliderPage2.setImageDrawable(R.mipmap.onboard_food2);
        sliderPage2.setBgColor(getResources().getColor(R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        // OPTIONAL METHODS

        setColorTransitionsEnabled(true);

        // Hide Skip/Done button.
        setProgressButtonEnabled(true);
        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
