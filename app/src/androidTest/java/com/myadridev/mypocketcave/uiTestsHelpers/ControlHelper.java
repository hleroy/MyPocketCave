package com.myadridev.mypocketcave.uiTestsHelpers;

import android.support.test.espresso.ViewInteraction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

public class ControlHelper {

    public static ViewInteraction checkIfDisplayedWithScroll(int controlId) {
        return checkIfDisplayedWithScroll(controlId, true);
    }

    public static ViewInteraction checkIfNotDisplayedWithScroll(int controlId) {
        return checkIfDisplayedWithScroll(controlId, false);
    }

    private static ViewInteraction checkIfDisplayedWithScroll(int controlId, boolean isDisplayed) {
        if (isDisplayed) {
            return onView(withId(controlId)).perform(scrollTo()).check(matches(isDisplayed()));
        } else {
            return onView(withId(controlId)).perform(scrollTo()).check(matches(not(isDisplayed())));
        }
    }

    public static void hideKeyboardFromCreate(int coordinatorLayoutId) {
        onView(withId(coordinatorLayoutId)).perform(closeSoftKeyboard());
    }
}
