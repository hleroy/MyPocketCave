package com.myadridev.mypocketcave.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.managers.BottleManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BottleCreateActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity activity;

    @Before
    public void before() {
        activity = activityTestRule.getActivity();
        onView(withId(R.id.container)).perform(swipeLeft());
        onView(withId(R.id.fab_add_bottle)).perform(click());
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_create_bottle))));
        onView(withId(R.id.action_save)).check(matches(isDisplayed()));
        onView(withId(R.id.action_cancel)).check(matches(isDisplayed()));

        onView(withId(R.id.bottle_edit_name)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_domain)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_stock)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_wine_color)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_person)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_comments)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_food)).check(matches(isDisplayed()));
        onView(withId(R.id.bottle_edit_millesime)).check(matches(isDisplayed()));
    }

    @Test
    public void isClickOnCancelPossible() {
        int stockBottlesBefore = BottleManager.Instance.getBottlesCount();
        int maxBottleIdBefore = BottleManager.MaxBottleId();
        int numberBottlesBefore = BottleManager.Instance.getBottles().size();
        onView(withId(R.id.action_cancel)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_main))));
        int stockBottlesAfter = BottleManager.Instance.getBottlesCount();
        int maxBottleIdAfter = BottleManager.MaxBottleId();
        int numberBottlesAfter = BottleManager.Instance.getBottles().size();

        Assert.assertEquals(stockBottlesAfter, stockBottlesBefore);
        Assert.assertEquals(maxBottleIdAfter, maxBottleIdBefore);
        Assert.assertEquals(numberBottlesAfter, numberBottlesBefore);
    }

    @Test
    public void isBottleCreatedWhenCreatingBottleWithCorrectName() {
        BottleUiTestHelper.tryCreateBottleFromCreatePage(activity);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.defaultTestName)));
        pressBack();

        BottleUiTestHelper.tryDeleteBottleFromList(BottleUiTestHelper.defaultTestName);
    }

    @Test
    public void isNoBottleCreatedWhenCreatingBottleButClickingCancel() {
        BottleUiTestHelper.tryCreateBottleButCancel(activity);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_main))));

        onView(allOf(withText(BottleUiTestHelper.defaultTestDomain + " - " + BottleUiTestHelper.defaultTestName))).check(doesNotExist());
    }

    @Test
    public void isErrorSnackbarWhenCreatingBottleWithoutName() {
        BottleUiTestHelper.tryCreateBottleNoName(activity);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(activity.getString(R.string.error_bottle_no_name)))).check(matches(isDisplayed()));
    }

    @Test
    public void isErrorSnackbarWhenCreatingExistingBottle() {
        BottleUiTestHelper.tryCreateBottleFromCreatePage(activity);
        pressBack();
        BottleUiTestHelper.tryCreateBottleFromList(activity);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(activity.getString(R.string.error_bottle_already_exists)))).check(matches(isDisplayed()));
        onView(withId(R.id.action_cancel)).perform(click());

        BottleUiTestHelper.tryDeleteBottleFromList(BottleUiTestHelper.defaultTestName);
    }

    @Test
    public void isRedirectionToExistingBottleWhenCreatingExistingBottleAndClickingYesOnErrorPopup() {
        BottleUiTestHelper.tryCreateBottleFromCreatePage(activity);
        pressBack();
        BottleUiTestHelper.tryCreateBottleFromList(activity);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(activity.getString(R.string.error_bottle_already_exists)))).check(matches(isDisplayed()));
        onView(allOf(withId(android.support.design.R.id.snackbar_action), withText(activity.getString(R.string.global_merge)))).check(matches(isDisplayed())).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.defaultTestName)));

        BottleUiTestHelper.tryDeleteBottleFromDetail();
    }
}

