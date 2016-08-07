//package com.myadridev.mypocketcave.activities;
//
//import android.app.Activity;
//import android.support.test.espresso.core.deps.guava.collect.ImmutableList;
//
//import com.myadridev.mypocketcave.R;
//import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
//import com.myadridev.mypocketcave.enums.WineColorEnum;
//import com.myadridev.mypocketcave.managers.BottleManager;
//import com.myadridev.mypocketcave.models.BottleModel;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.Espresso.pressBack;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.AllOf.allOf;
//
//public class BottleUiTestHelper {
//
//    public static final String defaultTestName = "Bouteille pour les tests";
//    public static final String modifiedTestName = "Bouteille modifiée pour les tests";
//    public static final String defaultTestDomain = "AAA Domaine pour les tests";
//    public static final int defaultTestStock = 42;
//    public static final WineColorEnum defaultTestWineColor = WineColorEnum.RED;
//    public static final String defaultTestPerson = "Avec qui la boire";
//    public static final String defaultTestComments = "Commentaire";
//    public static final ImmutableList<FoodToEatWithEnum> defaultTestFood = ImmutableList.of(FoodToEatWithEnum.Aperitif, FoodToEatWithEnum.Fish);
//    public static final String defaultTestMillesime = "2015";
//
//    public static void tryCreateBottleFromCreatePage(Activity activity) {
//        createBottleInner(activity, defaultTestName);
//        pressBack();
//    }
//
//    public static void tryCreateBottleFromList(Activity activity) {
//        onView(withId(R.id.fab_add_bottle)).perform(click());
//        createBottleInner(activity, defaultTestName);
//        pressBack();
//    }
//
//    public static void tryCreateBottleNoName(Activity activity) {
//        createBottleInner(activity, "");
//        pressBack();
//    }
//
//    public static void tryCreateBottleButCancel(Activity activity) {
//        createBottleInner(activity, defaultTestName);
//        pressBack();
//    }
//
//    private static void hideKeyboardFromCreate() {
//        onView(withId(R.id.bottle_create_coordinator_layout)).perform(closeSoftKeyboard());
//    }
//
//    public static void editBottle(String newName) {
//        onView(withId(R.id.fab_edit_bottle)).perform(click());
//        editBottleFromEdit(newName);
//    }
//
//    public static void editBottleFromEdit(String newName) {
//        onView(withId(R.id.bottle_edit_name)).perform(replaceText(newName));
//        hideKeyboardFromEdit();
//        pressBack();
//    }
//
//    private static void hideKeyboardFromEdit() {
//        onView(withId(R.id.bottle_edit_coordinator_layout)).perform(closeSoftKeyboard());
//    }
//
//    public static void tryDeleteBottleFromList(String bottleName) {
//        tryDeleteBottleFromList(bottleName, defaultTestDomain);
//    }
//
//    public static void tryDeleteBottleFromList(String bottleName, String bottleDomaine) {
//        onView(allOf(withText(bottleDomaine + " - " + bottleName))).check(matches(isDisplayed())).perform(click());
//        tryDeleteBottleFromDetail();
//    }
//
//    public static void tryDeleteBottleFromDetail() {
//        onView(withId(R.id.fab_delete_bottle)).perform(click());
//        onView(withText(R.string.global_yes)).check(matches(isDisplayed())).perform(click());
//    }
//
//    private static void createBottleInner(Activity activity, String name) {
//        createBottleInner(activity, name, defaultTestWineColor, defaultTestDomain, defaultTestMillesime, defaultTestPerson, defaultTestFood, defaultTestStock);
//    }
//
//    private static void createBottle(Activity activity, String name, WineColorEnum color, String domain, String millesime, String person, ImmutableList<FoodToEatWithEnum> foodList, int stock) {
//        onView(withId(R.id.fab_add_bottle)).perform(click());
//        createBottleInner(activity, name, color, domain, millesime, person, foodList, stock);
//        pressBack();
//        pressBack();
//    }
//
//    private static void createBottleInner(Activity activity, String name, WineColorEnum color, String domaine, String millesime, String person, List<FoodToEatWithEnum> foodList, int stock) {
//        onView(withId(R.id.bottle_edit_name)).perform(typeText(name));
//        if (!name.isEmpty())
//            hideKeyboardFromCreate();
//        onView(withId(R.id.bottle_edit_domain)).perform(typeText(domaine));
//        hideKeyboardFromCreate();
//        onView(withId(R.id.bottle_edit_stock)).perform(typeText(String.valueOf(stock)));
//        hideKeyboardFromCreate();
//        onView(withId(R.id.bottle_edit_wine_color)).perform(click());
//        onView(allOf(withId(R.id.wine_color_label), withText(activity.getString(color.stringResourceId)))).perform(click());
//
//        onView(withId(R.id.bottle_edit_person)).perform(typeText(person));
//        hideKeyboardFromCreate();
//        onView(withId(R.id.bottle_edit_comments)).perform(typeText(defaultTestComments));
//        hideKeyboardFromCreate();
//        onView(withId(R.id.bottle_edit_food)).perform(click());
//        for (FoodToEatWithEnum food : foodList) {
//            onView(withText(food.stringResourceId)).perform(click());
//        }
//        pressBack();
//
//        onView(withId(R.id.bottle_edit_millesime)).perform(click());
//        onView(allOf(withId(R.id.millesime_label), withText(millesime))).perform(click());
//    }
//
//    public static List<Integer> createSearchBottleSet() {
//        List<Integer> bottleCreatedIndexList = new ArrayList<>();
//        ImmutableList[] foods = new ImmutableList[]{
//                ImmutableList.of(),
//                ImmutableList.of(FoodToEatWithEnum.PorkProduct, FoodToEatWithEnum.Fish)
//        };
//
//        Calendar cal = Calendar.getInstance();
//        int currentYear = cal.get(Calendar.YEAR);
//
//        int count = 0;
//        int numberDifferentDomains = 2;
//        for (int i = 0; i < numberDifferentDomains; i++) {
//            String domain = "AA - Domaine " + (i + 1);
//            for (int c = 0; c < 2; c++) {
//                WineColorEnum color = WineColorEnum.getById(c);
//                for (int j = 0; j < 4; j++) {
//                    int millesime = currentYear - (3 * j);
//                    for (int k = 0; k < 2; k++) {
//                        String person = "Person " + (k + 1);
//                        for (int l = 0; l < 2; l++) {
//                            count++;
//                            String name = "AA - Suggest " + count;
//
//                            BottleModel bottle = new BottleModel();
//                            bottle.Domain = domain;
//                            bottle.WineColor = color;
//                            bottle.Millesime = millesime;
//                            bottle.PersonToShareWith = person;
//                            bottle.Name = name;
//                            bottle.FoodToEatWithList.addAll(foods[l]);
//                            bottle.Stock = 1;
//
//                            bottleCreatedIndexList.add(BottleManager.Instance.addBottle(bottle));
//                        }
//                    }
//                }
//            }
//        }
//        return bottleCreatedIndexList;
//    }
//
//    public static void deleteBottleSet(List<Integer> bottleCreatedIndexList) {
//        for (int bottleId : bottleCreatedIndexList) {
//            BottleManager.Instance.removeBottle(bottleId);
//        }
//    }
//}
