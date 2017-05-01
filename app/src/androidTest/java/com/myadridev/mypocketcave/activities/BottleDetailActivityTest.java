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

import junit.framework.Assert;

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
import static com.myadridev.mypocketcave.uiTestsHelpers.MatcherHelper.withRating;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BottleDetailActivityTest {

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
    }

    @After
    public void after() {
        BottleManager.removeBottle(activity, bottleId);
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(bottle.Name)));

        onView(withId(R.id.fab_menu_bottle)).check(matches(isDisplayed()));

        onView(withId(R.id.bottle_detail_domain)).check(matches(isDisplayed())).check(matches(withText(bottle.Domain)));
        onView(withId(R.id.bottle_detail_stock)).check(matches(isDisplayed())).check(matches(withText(activity.getString(R.string.bottles_stock, bottle.Stock))));
        onView(withId(R.id.bottle_detail_placed)).check(matches(isDisplayed())).check(matches(withText(activity.getResources().getQuantityString(R.plurals.bottles_placed, bottle.NumberPlaced, bottle.NumberPlaced))));
        onView(withId(R.id.bottle_detail_wine_color)).check(matches(isDisplayed())).check(matches(withText(bottle.WineColor.StringResourceId)));
        onView(withId(R.id.bottle_detail_person)).check(matches(isDisplayed())).check(matches(withText(bottle.PersonToShareWith)));
        onView(withId(R.id.bottle_detail_comments)).check(matches(isDisplayed())).check(matches(withText(bottle.Comments)));
        onView(withId(R.id.bottle_detail_food)).check(matches(isDisplayed())).check(matches(withText(FoodToEatHelper.computeFoodViewText(activity, bottle.FoodToEatWithList))));
        onView(withId(R.id.bottle_detail_millesime)).check(matches(isDisplayed())).check(matches(withText(String.valueOf(bottle.Millesime))));
        onView(withId(R.id.bottle_detail_rating)).check(matches(isDisplayed())).check(matches(withRating(bottle.Rating)));
        onView(withId(R.id.bottle_detail_price_rating)).check(matches(isDisplayed())).check(matches(withRating(bottle.PriceRating)));
    }

    @Test
    public void isBottleDeletedWhenDelete() {
        int totalBottlesBefore = BottleManager.getBottlesCount();

        onView(withId(R.id.fab_menu_bottle)).perform(click());
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        onView(withText(R.string.global_yes)).perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_main))));
        Assert.assertEquals(totalBottlesBefore - bottle.Stock, BottleManager.getBottlesCount());
    }

    @Test
    public void isBottleNotDeletedWhenPressBack() {
        int totalBottlesBefore = BottleManager.getBottlesCount();

        onView(withId(R.id.fab_menu_bottle)).perform(click());
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        pressBack();

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(bottle.Name)));
        Assert.assertEquals(totalBottlesBefore, BottleManager.getBottlesCount());
    }

    @Test
    public void isBottleNotDeletedWhenCancelDelete() {
        int totalBottlesBefore = BottleManager.getBottlesCount();

        onView(withId(R.id.fab_menu_bottle)).perform(click());
        onView(withId(R.id.fab_delete_bottle)).perform(click());
        onView(withText(R.string.global_no)).perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(bottle.Name)));
        Assert.assertEquals(totalBottlesBefore, BottleManager.getBottlesCount());
    }
}