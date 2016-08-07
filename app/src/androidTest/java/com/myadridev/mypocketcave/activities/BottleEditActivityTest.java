//package com.myadridev.mypocketcave.activities;
//
//import android.support.test.filters.LargeTest;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v7.widget.Toolbar;
//import android.widget.TextView;
//
//import com.myadridev.mypocketcave.R;
//import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.Espresso.pressBack;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.swipeLeft;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.AllOf.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class BottleEditActivityTest {
//
//    @Rule
//    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
//    private SplashScreenActivity activity;
//    private boolean isBottleCreated;
//
//    @Before
//    public void before() {
//        activity = activityTestRule.getActivity();
//        onView(withId(R.id.container)).perform(swipeLeft());
//        BottleUiTestHelper.tryCreateBottleFromList(activity);
//        onView(withId(R.id.fab_edit_bottle)).perform(click());
//
//        isBottleCreated = true;
//    }
//
//    @Test
//    public void isActivityInitialized() {
//        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_edit_bottle))));
//
//        onView(withId(R.id.bottle_edit_name)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestName)));
//        onView(withId(R.id.bottle_edit_domain)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestDomain)));
//        onView(withId(R.id.bottle_edit_stock)).check(matches(isDisplayed())).check(matches(withText(String.valueOf(BottleUiTestHelper.defaultTestStock))));
//        onView(allOf(withId(R.id.wine_color_label), withText(activity.getString(BottleUiTestHelper.defaultTestWineColor.stringResourceId)))).check(matches(isDisplayed()));
//        onView(withId(R.id.bottle_edit_person)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestPerson)));
//        onView(withId(R.id.bottle_edit_comments)).check(matches(isDisplayed())).check(matches(withText(BottleUiTestHelper.defaultTestComments)));
//        onView(withId(R.id.bottle_edit_food)).check(matches(isDisplayed())).check(matches(withText(FoodToEatHelper.computeFoodViewText(activity, BottleUiTestHelper.defaultTestFood))));
//        onView(withId(R.id.bottle_edit_millesime)).check(matches(isDisplayed())).check(matches(withSpinnerText(BottleUiTestHelper.defaultTestMillesime)));
//
//        pressBack();
//    }
//
//    @Test
//    public void isErrorSnackbarWhenEditingBottleWithoutName() {
//        BottleUiTestHelper.editBottleFromEdit("");
//        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(activity.getString(R.string.error_bottle_no_name)))).check(matches(isDisplayed()));
//        pressBack();
//    }
//
//    @Test
//    public void isErrorSnackbarWhenEditingExistingBottle() {
//        BottleUiTestHelper.editBottleFromEdit(BottleUiTestHelper.modifiedTestName);
//        pressBack();
//        BottleUiTestHelper.tryCreateBottleFromList(activity);
//        BottleUiTestHelper.editBottle(BottleUiTestHelper.modifiedTestName);
//        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(activity.getString(R.string.error_bottle_already_exists)))).check(matches(isDisplayed()));
//        pressBack();
//        BottleUiTestHelper.tryDeleteBottleFromDetail();
//        onView(allOf(withText(BottleUiTestHelper.defaultTestDomain + " - " + BottleUiTestHelper.modifiedTestName))).check(matches(isDisplayed())).perform(click());
//    }
//
//    @Test
//    public void isRedirectionToExistingBottleWhenEditingExistingBottleAndClickingYesOnErrorPopup() {
//        BottleUiTestHelper.editBottleFromEdit(BottleUiTestHelper.modifiedTestName);
//        pressBack();
//        BottleUiTestHelper.tryCreateBottleFromList(activity);
//        BottleUiTestHelper.editBottle(BottleUiTestHelper.modifiedTestName);
//        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(activity.getString(R.string.error_bottle_already_exists)))).check(matches(isDisplayed()));
//        onView(allOf(withId(android.support.design.R.id.snackbar_action), withText(activity.getString(R.string.global_merge)))).check(matches(isDisplayed())).perform(click());
//        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(BottleUiTestHelper.modifiedTestName)));
//    }
//
//    @After
//    public void after() {
//        if (isBottleCreated) {
//            BottleUiTestHelper.tryDeleteBottleFromDetail();
//        }
//    }
//}