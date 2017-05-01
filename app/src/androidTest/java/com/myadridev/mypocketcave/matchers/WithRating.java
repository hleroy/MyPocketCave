package com.myadridev.mypocketcave.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.RatingBar;

import org.hamcrest.Description;

public class WithRating extends BoundedMatcher<View, RatingBar> {

    private float value;

    public WithRating(float value) {
        super(RatingBar.class);
        this.value = value;
    }

    @Override
    protected boolean matchesSafely(RatingBar item) {
        return value == item.getRating();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with rating value: " + value);
    }
}