//package com.myadridev.mypocketcave.activities;
//
//import android.support.test.filters.LargeTest;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v7.widget.Toolbar;
//import android.widget.TextView;
//
//import com.myadridev.mypocketcave.R;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.List;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.swipeLeft;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.AllOf.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class SuggestBottleSearchActivityTest {
//
//    @Rule
//    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
//    private SplashScreenActivity activity;
//
//    private List<Integer> bottleCreatedIndexList;
//
//    @Before
//    public void before() {
//        bottleCreatedIndexList = BottleUiTestHelper.createSearchBottleSet();
//        activity = activityTestRule.getActivity();
//        onView(withId(R.id.container)).perform(swipeLeft());
//        onView(withId(R.id.fab_suggest_bottle)).perform(click());
//    }
//
//    @Test
//    public void isActivityInitialized() {
//        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(activity.getString(R.string.title_suggest_bottle_search))));
//
//        onView(withId(R.id.suggest_bottle_search_criteria)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_must_have)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_wine_color)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_wine_color_must_have)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_domain)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_domain_must_have)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_millesime)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_millesime_must_have)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_food)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_food_must_have)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_person)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_person_must_have)).check(matches(isDisplayed()));
//        onView(withId(R.id.suggest_bottle_search_button)).check(matches(isDisplayed()));
//    }
//
//    @After
//    public void after() {
//        BottleUiTestHelper.deleteBottleSet(bottleCreatedIndexList);
//    }
//}