package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum WineColorEnum {
    ANY(0, R.string.wine_color_none, -1),
    RED(1, R.string.wine_color_red, R.drawable.wine_red),
    WHITE(2, R.string.wine_color_white, R.drawable.wine_white),
    ROSE(3, R.string.wine_color_rose, R.drawable.wine_rose),
    CHAMPAGNE(4, R.string.wine_color_champagne, R.drawable.champagne);

    public static final int number = values().length;
    public final int id;
    public final int stringResourceId;
    public final int drawableResourceId;

    WineColorEnum(int _id, int _stringResourceId, int _drawableResourceId) {
        id = _id;
        stringResourceId = _stringResourceId;
        drawableResourceId = _drawableResourceId;
    }

    public static WineColorEnum getById(int id) {
        for (WineColorEnum wineColor : WineColorEnum.values()) {
            if (wineColor.id == id) {
                return wineColor;
            }
        }
        return null;
    }
}
