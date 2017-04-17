//package com.myadridev.mypocketcave.activities;
//
//import android.support.test.filters.LargeTest;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v7.widget.Toolbar;
//import android.widget.TextView;
//
//import com.myadridev.mypocketcave.R;
//import com.myadridev.mypocketcave.enums.AboutFieldsEnumV1;
//import com.myadridev.mypocketcave.models.v1.AboutItemV1;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
//import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.instanceOf;
//import static org.hamcrest.core.AllOf.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class AboutActivityTest {
//
//    @Rule
//    public ActivityTestRule<AboutActivity> aboutActivityTestRule = new ActivityTestRule<>(AboutActivity.class);
//    private AboutActivity aboutActivity;
//
//    @Before
//    public void before() {
//        aboutActivity = aboutActivityTestRule.getActivity();
//    }
//
//    @Test
//    public void isActivityInitialized() {
//        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(aboutActivity.getString(R.string.title_about))));
//        onData(instanceOf(AboutItemV1.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()));
//        onData(instanceOf(AboutItemV1.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
//                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(aboutActivity.getString(AboutFieldsEnumV1.VERSION.getStringResource()))))));
//        onData(instanceOf(AboutItemV1.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
//                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(aboutActivity.getString(AboutFieldsEnumV1.CONTACT.getStringResource()))))));
//        onData(instanceOf(AboutItemV1.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
//                .atPosition(2).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(aboutActivity.getString(AboutFieldsEnumV1.SOURCES.getStringResource()))))));
//        onData(instanceOf(AboutItemV1.class)).inAdapterView(allOf(withId(R.id.about_list_view), isDisplayed()))
//                .atPosition(3).check(matches(hasDescendant(allOf(withId(R.id.about_label), withText(aboutActivity.getString(AboutFieldsEnumV1.LICENSE.getStringResource()))))));
//    }
//}