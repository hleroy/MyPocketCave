package com.myadridev.mypocketcave.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
    private SplashScreenActivity activity;

    @Before
    public void before() {
        activity = activityTestRule.getActivity();
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_main))));
        onView(withId(R.id.container)).check(matches(isDisplayed()));
        onView(withId(R.id.tabs)).check(matches(isDisplayed()));
        onView(withText(activity.getString(R.string.title_caves))).check(matches(isDisplayed()));
        onView(withText(activity.getString(R.string.title_bottles))).check(matches(isDisplayed()));
        onView(withId(R.id.about)).check(matches(isDisplayed()));
        onView(withId(R.id.sync)).check(matches(isDisplayed()));
        onView(withId(R.id.suggest)).check(matches(isDisplayed()));

        onView(withId(R.id.fab_menu_main)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_cave)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fab_add_bottle)).check(matches(not(isDisplayed())));
    }

    @Test
    public void isNavigationToAboutPossible() {
        onView(withId(R.id.about)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_about))));
    }

    @Test
    public void isNavigationToSyncPossible() {
        onView(withId(R.id.sync)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_sync))));
    }

    @Test
    public void isNavigationToSuggestBottlePossible() {
        onView(withId(R.id.suggest)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_suggest_bottle_search))));
    }

    @Test
    public void isNavigationToCreateBottlePossible() {
        onView(withId(R.id.fab_menu_main)).perform(click());

        onView(withId(R.id.fab_add_bottle)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_cave)).check(matches(isDisplayed()));

        onView(withId(R.id.fab_add_bottle)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_create_bottle))));
    }

    @Test
    public void isNavigationToCreateCavePossible() {
        onView(withId(R.id.fab_menu_main)).perform(click());

        onView(withId(R.id.fab_add_bottle)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_cave)).check(matches(isDisplayed()));

        onView(withId(R.id.fab_add_cave)).perform(click());
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_create_cave))));
    }
}