package com.myadridev.mypocketcave.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;
import com.myadridev.mypocketcave.models.v2.AboutItemV2;
import com.myadridev.mypocketcave.uiTestsHelpers.ClickHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);

    @Before
    public void before() {
        ClickHelper.clickOnMenuItem(R.id.about, R.string.about);
    }

    @Test
    public void isActivityInitialized() {
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(R.string.title_about)));
        onData(instanceOf(AboutItemV2.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()));
        onData(instanceOf(AboutItemV2.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(AboutFieldsEnumV2.v.StringResourceId)))));
        onData(instanceOf(AboutItemV2.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(AboutFieldsEnumV2.c.StringResourceId)))));
        onData(instanceOf(AboutItemV2.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
                .atPosition(2).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(AboutFieldsEnumV2.s.StringResourceId)))));
        onData(instanceOf(AboutItemV2.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
                .atPosition(3).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(AboutFieldsEnumV2.l.StringResourceId)))));
    }
}