package com.myadridev.mypocketcave.uiTestsHelpers;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.myadridev.mypocketcave.uiTestsHelpers.ViewActionHelper.setRating;
import static org.hamcrest.core.AllOf.allOf;

public class BottleHelper {

    public static void createBottle(BottleModelV2 bottle) {
        onView(withId(R.id.bottle_edit_name)).perform(typeText(bottle.Name));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);
        onView(withId(R.id.bottle_edit_domain)).perform(typeText(bottle.Domain));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);
        onView(withId(R.id.bottle_edit_stock)).perform(typeText(String.valueOf(bottle.Stock)));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);
        onView(withId(R.id.bottle_edit_wine_color)).perform(click());
        onView(allOf(withId(R.id.wine_color_label), withText(bottle.WineColor.StringResourceId))).perform(click());

        onView(withId(R.id.bottle_edit_millesime)).perform(click());
        onView(allOf(withId(R.id.millesime_label), withText(String.valueOf(bottle.Millesime)))).perform(click());

        onView(withId(R.id.bottle_edit_person)).perform(typeText(bottle.PersonToShareWith));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);
        onView(withId(R.id.bottle_edit_comments)).perform(typeText(bottle.Comments));
        ControlHelper.hideKeyboardFromCreate(R.id.bottle_create_coordinator_layout);

        onView(withId(R.id.bottle_edit_rating)).perform(setRating(bottle.Rating));
        onView(withId(R.id.bottle_edit_price_rating)).perform(setRating(bottle.PriceRating));

        ClickHelper.clickOnMenuItem(R.id.save, R.string.save);
    }
}
