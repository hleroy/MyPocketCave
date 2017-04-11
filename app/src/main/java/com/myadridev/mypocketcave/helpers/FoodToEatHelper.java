package com.myadridev.mypocketcave.helpers;

import android.content.Context;

import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;

import java.util.List;

public class FoodToEatHelper {
    private static final String foodSeparator = ", ";

    public static String computeFoodViewText(Context context, boolean[] foodToEatWithList) {
        boolean isAtLeastOneFood = false;
        StringBuilder computedText = new StringBuilder();
        for (FoodToEatWithEnumV2 food : FoodToEatWithEnumV2.values()) {
            if (foodToEatWithList[food.Id]) {
                if (isAtLeastOneFood) {
                    computedText.append(foodSeparator);
                }
                computedText.append(context.getString(food.StringResourceId));
                isAtLeastOneFood = true;
            }
        }
        return computedText.toString();
    }

    public static String computeFoodViewText(Context context, List<FoodToEatWithEnumV2> foodToEatWithList) {
        boolean isAtLeastOneFood = false;
        StringBuilder computedText = new StringBuilder();
        for (FoodToEatWithEnumV2 food : foodToEatWithList) {
            if (isAtLeastOneFood) {
                computedText.append(foodSeparator);
            }
            computedText.append(context.getString(food.StringResourceId));
            isAtLeastOneFood = true;
        }
        return computedText.toString();
    }
}
