package com.myadridev.mypocketcave.enums.v1;

import android.content.Context;

import com.myadridev.mypocketcave.R;

@Deprecated
public enum FoodToEatWithEnumV1 {
    Aperitif(0, R.string.food_aperitif),
    PorkProduct(1, R.string.food_pork_product),
    Salad(2, R.string.food_salad),
    Soup(3, R.string.food_soup),
    Starter(4, R.string.food_starter),
    FoieGras(5, R.string.food_foie_gras),
    Fish(6, R.string.food_fish),
    WhiteMeat(7, R.string.food_white_meat),
    RedMeat(8, R.string.food_red_meat),
    GrilledMeat(9, R.string.food_grilled_meat),
    Poultry(10, R.string.food_poultry),
    Game(11, R.string.food_game),
    Giblet(12, R.string.food_giblet),
    Vegetable(13, R.string.food_vegetable),
    Exotic(14, R.string.food_exotic),
    Spicy(15, R.string.food_spicy),
    SaltySweet(16, R.string.food_salty_sweet),
    Pasta(17, R.string.food_pasta),
    Cheese(18, R.string.food_cheese),
    Dessert(19, R.string.food_dessert),
    Chocolate(20, R.string.food_chocolate);

    public final int Id;
    public final int StringResourceId;

    FoodToEatWithEnumV1(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public static FoodToEatWithEnumV1 getById(int id) {
        for (FoodToEatWithEnumV1 food : FoodToEatWithEnumV1.values()) {
            if (food.Id == id) {
                return food;
            }
        }
        return null;
    }

    public static String[] getAllFoodLabels(Context context) {
        String[] allLabels = new String[values().length];
        for (FoodToEatWithEnumV1 food : FoodToEatWithEnumV1.values()) {
            allLabels[food.Id] = context.getString(food.StringResourceId);
        }
        return allLabels;
    }
}
