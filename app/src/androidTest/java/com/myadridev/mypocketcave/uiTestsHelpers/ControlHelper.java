package com.myadridev.mypocketcave.uiTestsHelpers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

public class ControlHelper {

    public static void checkIfDisplayed(int controlId) {
        checkIfDisplayed(controlId, true);
    }

    public static void checkIfNotDisplayed(int controlId) {
        checkIfDisplayed(controlId, false);
    }

    private static void checkIfDisplayed(int controlId, boolean isDisplayed) {
        if (isDisplayed) {
            onView(withId(controlId)).perform(scrollTo()).check(matches(isDisplayed()));
        } else {
            onView(withId(controlId)).perform(scrollTo()).check(matches(not(isDisplayed())));
        }
    }

    public static void hideKeyboardFromCreate(int coordinatorLayoutId) {
        onView(withId(coordinatorLayoutId)).perform(closeSoftKeyboard());
    }
}
