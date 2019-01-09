package com.example.gruppeb.madbestillingsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Skelet fra https://github.com/PaoloRotolo/AppIntro
 */
public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        /**
        addSlide(SampleSlide.newInstance(R.layout.your_slide_here));
        addSlide(SampleSlide.newInstance(R.layout.your_slide_here));
        addSlide(SampleSlide.newInstance(R.layout.your_slide_here));
*/
        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("Test1");
        sliderPage1.setDescription("testdeskription");
        sliderPage1.setImageDrawable(R.drawable.ballerup);
        sliderPage1.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Test1");
        sliderPage2.setDescription("testdeskription");
        sliderPage2.setImageDrawable(R.drawable.ballerup);
        sliderPage2.setBgColor(getResources().getColor(R.color.colorComplementary1));
        addSlide(AppIntroFragment.newInstance(sliderPage2));


        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Test1");
        sliderPage.setDescription("testdeskription");
        sliderPage.setImageDrawable(R.drawable.ballerup);
        sliderPage.setBgColor(getResources().getColor(R.color.colorComplementary2));
        addSlide(AppIntroFragment.newInstance(sliderPage));
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
