package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum WineColorEnum {
    ANY(0, R.string.wine_color_none, -1),
    RED(1, R.string.wine_color_red, R.drawable.wine_red),
    WHITE(2, R.string.wine_color_white, R.drawable.wine_white),
    ROSE(3, R.string.wine_color_rose, R.drawable.wine_rose),
    CHAMPAGNE(4, R.string.wine_color_champagne, R.drawable.champagne),
    WHISKY(5, R.string.wine_color_whisky, R.drawable.whisky),
    RUM(6, R.string.wine_color_rum, R.drawable.rum),
    VODKA(7, R.string.wine_color_vodka, R.drawable.vodka),
    APERITIF(8, R.string.wine_color_aperitif, R.drawable.aperitif),
    LIQUEUR(9, R.string.wine_color_liqueur, R.drawable.liqueur),
    BEER(10, R.string.wine_color_beer, R.drawable.beer),
    CIDER(11, R.string.wine_color_cider, R.drawable.cider),
    SOFT(12, R.string.wine_color_soft, R.drawable.soft),
    OTHER(13, R.string.wine_color_other, R.drawable.other);

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
