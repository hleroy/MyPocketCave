package com.myadridev.mypocketcave.uiTestsHelpers;

import android.support.test.espresso.ViewAction;

import com.myadridev.mypocketcave.viewActions.SetRating;

public class ViewActionHelper {

    public static ViewAction setRating(int value) {
        return new SetRating(value);
    }
}
