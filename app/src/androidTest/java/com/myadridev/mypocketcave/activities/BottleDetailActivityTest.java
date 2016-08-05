package com.myadridev.mypocketcave.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.managers.BottleManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BottleDetailActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity activity;
    private boolean isBottleCreated;

    @Before
    public void before() {
        activity = activityTestRule.getActivity();
        onView(withId(R.id.container)).perform(swipeLeft());
        BottleUiTestHelper.tryCreateBottleFromList(activity);

        isBottleCreated = true;
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.defaultTestName)));
        onView(withId(R.id.fab_delete_bottle)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_edit_bottle)).check(matches(isDisplayed()));

        onView(withId(R.id.bottle_detail_name)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestName)));
        onView(withId(R.id.bottle_detail_domain)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestDomain)));
        onView(withId(R.id.bottle_detail_stock)).check(matches(isDisplayed())).check(matches(withText(activity.getString(R.string.bottles_stock, BottleUiTestHelper.defaultTestStock))));
        onView(withId(R.id.bottle_detail_wine_color)).check(matches(isDisplayed())).check(matches(withText(activity.getString(BottleUiTestHelper.defaultTestWineColor.stringResourceId))));
        onView(withId(R.id.bottle_detail_person)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestPerson)));
        onView(withId(R.id.bottle_detail_comments)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestComments)));
        onView(withId(R.id.bottle_detail_food)).check(matches(isDisplayed())).check(matches(withText(FoodToEatHelper.computeFoodViewText(activity, BottleUiTestHelper.defaultTestFood))));
        onView(withId(R.id.bottle_detail_millesime)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestMillesime)));
    }

    @Test
    public void isNavigationToEditBottlePossible() {
        onView(withId(R.id.fab_edit_bottle)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_edit_bottle))));
        pressBack();
    }

    @Test
    public void isNavigationToDeleteBottlePossible() {
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        onView(withText(R.string.global_yes)).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void isBottleNotDeletedWhenPressBack() {
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        pressBack();
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.defaultTestName)));
    }

    @Test
    public void isBottleNotDeletedWhenCancelDelete() {
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        onView(withText(R.string.global_no)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.defaultTestName)));
    }

    @Test
    public void isBottleDeletedWhenDelete() {
        int bottleCountBeforeDelete = BottleManager.Instance.getBottlesCount();
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        onView(withText(R.string.global_yes)).perform(click());
        isBottleCreated = false;
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_main))));
        onView(withId(R.id.bottles_count)).check(matches(withText(activity.getString(R.string.bottles_count, bottleCountBeforeDelete - BottleUiTestHelper.defaultTestStock))));
    }

    @Test
    public void isBottleNameChangedWhenEdited() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.defaultTestName)));
        BottleUiTestHelper.editBottle(BottleUiTestHelper.modifiedTestName);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.modifiedTestName)));
        onView(withId(R.id.bottle_detail_name)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.modifiedTestName)));
    }

    @After
    public void after() {
        if (isBottleCreated) {
            BottleUiTestHelper.tryDeleteBottleFromDetail();
        }
    }
}