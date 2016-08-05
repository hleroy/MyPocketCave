package com.myadridev.mypocketcave.enums;

import android.content.Context;

import com.myadridev.mypocketcave.R;

public enum FoodToEatWithEnum {
    Aperitif(0, R.string.food_aperitif),
    PorkProduct(1, R.string.food_pork_product),
    Salad(2, R.string.food_salad),
    Soup(3, R.string.food_soup),
    Starter(4, R.string.food_starter),
    Fish(5, R.string.food_fish),
    WhiteMeat(6, R.string.food_white_meat),
    RedMeat(7, R.string.food_red_meat),
    Poultry(8, R.string.food_poultry),
    Game(9, R.string.food_game),
    Vegetable(10, R.string.food_vegetable),
    Exotic(11, R.string.food_exotic),
    SaltySweet(12, R.string.food_salty_sweet),
    Pasta(13, R.string.food_pasta),
    Giblet(14, R.string.food_giblet),
    Cheese(15, R.string.food_cheese),
    Dessert(16, R.string.food_dessert);

    public static final int number = values().length;
    public final int id;
    public final int stringResourceId;

    FoodToEatWithEnum(int _id, int _stringResourceId) {
        id = _id;
        stringResourceId = _stringResourceId;
    }

    public static FoodToEatWithEnum getById(int id) {
        for (FoodToEatWithEnum food : FoodToEatWithEnum.values()) {
            if (food.id == id) {
                return food;
            }
        }
        return null;
    }

    public static String[] getAllFoodLabels(Context context) {
        String[] allLabels = new String[number];
        for (FoodToEatWithEnum food : FoodToEatWithEnum.values()) {
            allLabels[food.id] = context.getString(food.stringResourceId);
        }
        return allLabels;
    }
}
