package com.myadridev.mypocketcave.uiTestsHelpers;

import android.support.test.espresso.NoMatchingViewException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ClickHelper {

    public static void clickOnMenuItem(int menuItemId, int menuItemTextResourceId) {
        try {
            onView(withId(menuItemId)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(menuItemTextResourceId)).perform(click());
        }
    }
}
