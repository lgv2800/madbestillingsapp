package com.example.gruppeb.madbestillingsapp;

import android.content.Context;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

class IntroGuide {
    public void playGuide(Context context, MainScreen mainScreen){
        new MaterialTapTargetPrompt.Builder(mainScreen)
                .setTarget(R.id.tabs)
                .setPrimaryText(context.getString(R.string.intro_guide_first_step_title))
                .setSecondaryText(context.getString(R.string.intro_guide_first_step_desc))
                .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                .setPromptBackground(new RectanglePromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        // User has pressed the prompt target

                        new MaterialTapTargetPrompt.Builder(mainScreen)
                                .setTarget(R.id.page1_chiplayout)
                                .setPrimaryText(context.getString(R.string.intro_guide_second_step_title))
                                .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                                .setPromptBackground(new RectanglePromptBackground())
                                .setPromptFocal(new RectanglePromptFocal())
                                .setSecondaryText(context.getString(R.string.intro_guide_second_step_desc))
                                .setPromptStateChangeListener((prompt13, state13) -> {
                                    if (state13 == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                        // User has pressed the prompt target

                                        new MaterialTapTargetPrompt.Builder(mainScreen)
                                                .setTarget(R.id.fab)
                                                .setPrimaryText(context.getString(R.string.intro_guide_third_step_title))
                                                .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                                                .setSecondaryText(context.getString(R.string.intro_guide_third_step_desc))
                                                .setPromptStateChangeListener((prompt12, state12) -> {
                                                    if (state12 == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                                        // User has pressed the prompt target

                                                        new MaterialTapTargetPrompt.Builder(mainScreen)
                                                                .setTarget(R.id.shopping_cart)
                                                                .setIcon(R.drawable.ic_shopping_cart_white_24dp)
                                                                .setPrimaryText(context.getString(R.string.intro_guide_fourth_step_title))
                                                                .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                                                                .setSecondaryText(context.getString(R.string.intro_guide_fourth_step_desc))
                                                                .setPromptStateChangeListener((prompt1, state1) -> {
                                                                    if (state1 == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                                                        // User has pressed the prompt target
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                })
                                                .show();
                                    }
                                })
                                .show();
                    }
                })
                .show();

    }
}
