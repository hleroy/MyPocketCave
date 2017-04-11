package com.myadridev.mypocketcave.enums.v2;

import android.content.Context;

import com.myadridev.mypocketcave.R;

public enum FoodToEatWithEnumV2 {
    a(0, R.string.food_aperitif),
    po(1, R.string.food_pork_product),
    sa(2, R.string.food_salad),
    so(3, R.string.food_soup),
    st(4, R.string.food_starter),
    fg(5, R.string.food_foie_gras),
    fi(6, R.string.food_fish),
    wm(7, R.string.food_white_meat),
    rm(8, R.string.food_red_meat),
    gr(9, R.string.food_grilled_meat),
    pou(10, R.string.food_poultry),
    ga(11, R.string.food_game),
    gi(12, R.string.food_giblet),
    v(13, R.string.food_vegetable),
    ex(14, R.string.food_exotic),
    sp(15, R.string.food_spicy),
    ss(16, R.string.food_salty_sweet),
    pa(17, R.string.food_pasta),
    ch(18, R.string.food_cheese),
    de(19, R.string.food_dessert),
    cho(20, R.string.food_chocolate);

    public final int Id;
    public final int StringResourceId;

    FoodToEatWithEnumV2(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public static FoodToEatWithEnumV2 getById(int id) {
        for (FoodToEatWithEnumV2 food : FoodToEatWithEnumV2.values()) {
            if (food.Id == id) {
                return food;
            }
        }
        return null;
    }

    public static String[] getAllFoodLabels(Context context) {
        String[] allLabels = new String[values().length];
        for (FoodToEatWithEnumV2 food : FoodToEatWithEnumV2.values()) {
            allLabels[food.Id] = context.getString(food.StringResourceId);
        }
        return allLabels;
    }
}
