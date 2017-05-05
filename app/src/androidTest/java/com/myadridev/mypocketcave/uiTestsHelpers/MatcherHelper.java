package com.myadridev.mypocketcave.uiTestsHelpers;

import android.view.View;

import com.myadridev.mypocketcave.matchers.WithRating;

import org.hamcrest.Matcher;

public class MatcherHelper {

    public static Matcher<View> withRating(float value) {
        return new WithRating(value);
    }
}
