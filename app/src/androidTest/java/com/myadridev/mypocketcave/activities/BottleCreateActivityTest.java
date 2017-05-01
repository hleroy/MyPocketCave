package com.myadridev.mypocketcave.activities;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.uiTestsHelpers.BottleHelper;
import com.myadridev.mypocketcave.uiTestsHelpers.ClickHelper;
import com.myadridev.mypocketcave.uiTestsHelpers.ContextHelper;
import com.myadridev.mypocketcave.uiTestsHelpers.ControlHelper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BottleCreateActivityTest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
    private Activity activity;

    @Before
    public void before() {
        activity = activityTestRule.getActivity();
        onView(withId(R.id.fab_menu_main)).perform(click());
        onView(withId(R.id.fab_add_bottle)).perform(click());
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(R.string.title_create_bottle)));

        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_name);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_domain);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_stock);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_wine_color);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_millesime);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_rating);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_price_rating);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_food);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_person);
        ControlHelper.checkIfDisplayedWithScroll(R.id.bottle_edit_comments);
    }

    @Test
    public void isBottleCreatedWhenCreatingBottle() throws InterruptedException {
        int totalBottlesBefore = BottleManager.getBottlesCount();

        BottleModelV2 bottle = new BottleModelV2();
        bottle.Name = " AA";
        bottle.Domain = "AAAA";
        bottle.Stock = 2;
        bottle.WineColor = WineColorEnumV2.w;
        bottle.Millesime = 2015;
        bottle.PersonToShareWith = "person";
        bottle.Comments = " default TestComments";
        bottle.Rating = 4;
        bottle.PriceRating = 2;

        BottleHelper.createBottle(bottle);

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(bottle.Name.trim())));
        Assert.assertEquals(totalBottlesBefore + bottle.Stock, BottleManager.getBottlesCount());

        Activity currentActivity = ContextHelper.getCurrentActivity();
        assertTrue(currentActivity instanceof BottleDetailActivity);
        BottleDetailActivity bottleDetailActivity = (BottleDetailActivity) currentActivity;
        int bottleId = bottleDetailActivity.bottleId;

        assertNotEquals(0, bottleId);
        BottleManager.removeBottle(activity, bottleId);
        assertNull(BottleManager.getBottle(bottleId));
    }

    @Test
    public void isBottleNotCreatedWhenCancellingBottleCreation() throws InterruptedException {
        int totalBottlesBefore = BottleManager.getBottlesCount();
        onView(withId(R.id.bottle_edit_name)).perform(typeText(" Name"));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);
        onView(withId(R.id.bottle_edit_domain)).perform(typeText("Doma"));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);
        onView(withId(R.id.bottle_edit_stock)).perform(typeText("2"));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);

        pressBack();
        onView(withText(R.string.global_exit)).check(matches(isDisplayed())).perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(R.string.title_main)));
        Assert.assertEquals(totalBottlesBefore, BottleManager.getBottlesCount());
    }

    @Test
    public void isNoBottleCreatedWhenNoName() {
        int totalBottlesBefore = BottleManager.getBottlesCount();
        ClickHelper.clickOnMenuItem(R.id.save, R.string.save);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.error_bottle_no_name))).check(matches(isDisplayed()));
        Assert.assertEquals(totalBottlesBefore, BottleManager.getBottlesCount());
    }

    @Test
    public void isErrorSnackbarWhenCreatingExistingBottle() {
        BottleModelV2 bottle = new BottleModelV2();
        bottle.Name = " AAA";
        bottle.Domain = "AAAA";
        bottle.Stock = 2;
        bottle.WineColor = WineColorEnumV2.w;
        bottle.Millesime = 2015;
        bottle.PersonToShareWith = "person";
        bottle.Comments = " default TestComments";
        bottle.Rating = 4;
        bottle.PriceRating = 2;
        bottle.trimAll();

        int bottleId = BottleManager.addBottle(activity, new BottleModelV2(bottle));

        bottle.Id = 0;
        bottle.Name = " AAA";
        BottleHelper.createBottle(bottle);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.error_bottle_already_exists))).check(matches(isDisplayed()));

        BottleManager.removeBottle(activity, bottleId);
    }
}
