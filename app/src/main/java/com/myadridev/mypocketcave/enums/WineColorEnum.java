package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum WineColorEnum {
    ANY(0, R.string.wine_color_none, -1),
    RED(1, R.string.wine_color_red, R.drawable.wine_red),
    WHITE(2, R.string.wine_color_white, R.drawable.wine_white),
    ROSE(3, R.string.wine_color_rose, R.drawable.wine_rose),
    CHAMPAGNE(4, R.string.wine_color_champagne, R.drawable.champagne);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    WineColorEnum(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static WineColorEnum getById(int id) {
        for (WineColorEnum wineColor : WineColorEnum.values()) {
            if (wineColor.Id == id) {
                return wineColor;
            }
        }
        return null;
    }
}
