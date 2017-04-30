package com.myadridev.mypocketcave.viewActions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.RatingBar;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public final class SetRating implements ViewAction {

    private final int value;

    public SetRating(int value) {
        this.value = value;
    }

    public Matcher<View> getConstraints() {
        return isAssignableFrom(RatingBar.class);
    }

    public String getDescription() {
        return "Custom view action to set rating.";
    }

    public void perform(UiController uiController, View view) {
        RatingBar ratingBar = (RatingBar) view;
        ratingBar.setRating(value);
    }
}