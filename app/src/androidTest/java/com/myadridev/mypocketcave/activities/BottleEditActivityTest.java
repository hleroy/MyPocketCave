package com.myadridev.mypocketcave.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.helpers.FoodToEatHelper;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.uiTestsHelpers.ClickHelper;
import com.myadridev.mypocketcave.uiTestsHelpers.ControlHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.myadridev.mypocketcave.uiTestsHelpers.MatcherHelper.withRating;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BottleEditActivityTest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
    private SplashScreenActivity activity;
    private int bottleId;
    private BottleModelV2 bottle;

    @Before
    public void before() {
        activity = activityTestRule.getActivity();

        bottle = new BottleModelV2();
        bottle.Name = "AAA";
        bottle.Domain = "AAAAA";
        bottle.Stock = 2;
        bottle.WineColor = WineColorEnumV2.w;
        bottle.Millesime = 2015;
        bottle.PersonToShareWith = "person";
        bottle.Comments = "default TestComments";
        bottle.Rating = 4;
        bottle.FoodToEatWithList.clear();
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.cho);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.fi);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.ex);
        bottle.PriceRating = 2;

        bottleId = BottleManager.addBottle(activity, new BottleModelV2(bottle));
        onView(withId(R.id.container)).perform(swipeLeft());
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withText(bottle.Domain + " - " + bottle.Name))).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.fab_menu_bottle)).perform(click());
        onView(withId(R.id.fab_edit_bottle)).perform(click());
    }

    @After
    public void after() {
        BottleManager.removeBottle(activity, bottleId);
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_edit_bottle))));

        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_name).check(matches(withText(bottle.Name)));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_domain).check(matches(withText(bottle.Domain)));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_stock).check(matches(withText(String.valueOf(bottle.Stock))));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_wine_color);
        onView(allOf(withId(R.id.wine_color_label), withText(bottle.WineColor.StringResourceId))).check(matches(isDisplayed()));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_millesime);
        onView(allOf(withId(R.id.millesime_label), withText(String.valueOf(bottle.Millesime)))).check(matches(isDisplayed()));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_rating).check(matches(withRating(bottle.Rating)));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_price_rating).check(matches(withRating(bottle.PriceRating)));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_food).check(matches(withText(FoodToEatHelper.computeFoodViewText(activity, bottle.FoodToEatWithList))));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_person).check(matches(withText(bottle.PersonToShareWith)));
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_comments).check(matches(withText(bottle.Comments)));
    }

    @Test
    public void isBottleEditedWhenEditingBottle() {
        int totalBottlesBefore = BottleManager.getBottlesCount();

        onView(withId(R.id.bottle_edit_name)).perform(replaceText("AAAAAA"));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_edit_coordinator_layout);
        onView(withId(R.id.bottle_edit_stock)).perform(replaceText(String.valueOf(bottle.Stock + 1)));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_edit_coordinator_layout);

        ClickHelper.clickOnMenuItem(R.id.save, R.string.save);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText("AAAAAA")));
        Assert.assertEquals(totalBottlesBefore + 1, BottleManager.getBottlesCount());
    }

    @Test
    public void isErrorSnackbarWhenEditingBottleWithoutName() {
        int totalBottlesBefore = BottleManager.getBottlesCount();

        onView(withId(R.id.bottle_edit_name)).perform(replaceText(" "));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_edit_coordinator_layout);
        onView(withId(R.id.bottle_edit_stock)).perform(replaceText(String.valueOf(bottle.Stock + 1)));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_edit_coordinator_layout);

        ClickHelper.clickOnMenuItem(R.id.save, R.string.save);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.error_bottle_no_name))).check(matches(isDisplayed()));
        Assert.assertEquals(totalBottlesBefore, BottleManager.getBottlesCount());
    }

    @Test
    public void isErrorSnackbarWhenEditingExistingBottle() {
        BottleModelV2 bottle2 = new BottleModelV2(bottle);
        bottle2.Name = "AAAA";

        int bottle2Id = BottleManager.addBottle(activity, new BottleModelV2(bottle2));

        int totalBottlesBefore = BottleManager.getBottlesCount();

        onView(withId(R.id.bottle_edit_name)).perform(replaceText(bottle2.Name));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_edit_coordinator_layout);
        onView(withId(R.id.bottle_edit_stock)).perform(replaceText(String.valueOf(bottle.Stock + 1)));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_edit_coordinator_layout);

        ClickHelper.clickOnMenuItem(R.id.save, R.string.save);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.error_bottle_already_exists))).check(matches(isDisplayed()));
        Assert.assertEquals(totalBottlesBefore, BottleManager.getBottlesCount());

        BottleManager.removeBottle(activity, bottle2Id);
    }
}