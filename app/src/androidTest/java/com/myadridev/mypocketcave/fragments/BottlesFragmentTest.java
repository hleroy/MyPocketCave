//package com.myadridev.mypocketcave.fragments;
//
//import android.support.test.filters.LargeTest;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v7.widget.Toolbar;
//import android.widget.TextView;
//
//import com.myadridev.mypocketcave.R;
//import com.myadridev.mypocketcave.activities.BottleUiTestHelper;
//import com.myadridev.mypocketcave.activities.SplashScreenActivity;
//import com.myadridev.mypocketcave.enums.WineColorEnum;
//import com.myadridev.mypocketcave.managers.BottleManager;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.HashMap;
//import java.util.Map;
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
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.not;
//import static org.hamcrest.core.AllOf.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class BottlesFragmentTest {
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
//        pressBack();
//
//        isBottleCreated = true;
//    }
//
//    @Test
//    public void isActivityInitialized() {
//        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_main))));
//        onView(withId(R.id.bottles_recyclerview)).check(matches(isDisplayed()));
//        onView(withId(R.id.bottles_count)).check(matches(isDisplayed()));
//        onView(withId(R.id.bottles_count_detail)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.bottles_count)).check(matches(withText(activity.getString(R.string.bottles_count, BottleManager.Instance.getBottlesCount()))));
//    }
//
//    @Test
//    public void isClickOnTotalBottlesPossible() {
//        onView(withId(R.id.bottles_count)).perform(click());
//        onView(withId(R.id.bottles_count)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.bottles_count_detail)).check(matches(isDisplayed()));
//
//        Map<WineColorEnum, Integer> bottlesCountByColor = new HashMap<>(WineColorEnum.number);
//        for (WineColorEnum color : WineColorEnum.values()) {
//            bottlesCountByColor.put(color, BottleManager.Instance.getBottlesCount(color));
//        }
//
//        onView(withId(R.id.bottles_count_detail)).check(matches(withText(activity.getString(R.string.bottles_count_detail,
//                bottlesCountByColor.get(WineColorEnum.RED),
//                bottlesCountByColor.get(WineColorEnum.WHITE),
//                bottlesCountByColor.get(WineColorEnum.ROSE),
//                bottlesCountByColor.get(WineColorEnum.CHAMPAGNE)))));
//    }
//
//    @Test
//    public void isClickOnTotalBottlesPossibleThenClickOnDetailTotalPossible() {
//        onView(withId(R.id.bottles_count)).perform(click());
//
//        Map<WineColorEnum, Integer> bottlesCountByColor = new HashMap<>(WineColorEnum.number);
//        for (WineColorEnum color : WineColorEnum.values()) {
//            bottlesCountByColor.put(color, BottleManager.Instance.getBottlesCount(color));
//        }
//
//        onView(withId(R.id.bottles_count_detail)).check(matches(withText(activity.getString(R.string.bottles_count_detail,
//                bottlesCountByColor.get(WineColorEnum.RED),
//                bottlesCountByColor.get(WineColorEnum.WHITE),
//                bottlesCountByColor.get(WineColorEnum.ROSE),
//                bottlesCountByColor.get(WineColorEnum.CHAMPAGNE)))));
//
//        onView(withId(R.id.bottles_count_detail)).perform(click());
//        onView(withId(R.id.bottles_count)).check(matches(isDisplayed()));
//        onView(withId(R.id.bottles_count_detail)).check(matches(not(isDisplayed())));
//
//        onView(withId(R.id.bottles_count)).check(matches(withText(activity.getString(R.string.bottles_count, BottleManager.Instance.getBottlesCount()))));
//    }
//
//    @After
//    public void after() {
//        if (isBottleCreated) {
//            BottleUiTestHelper.tryDeleteBottleFromList(BottleUiTestHelper.defaultTestName);
//        }
//    }
//}