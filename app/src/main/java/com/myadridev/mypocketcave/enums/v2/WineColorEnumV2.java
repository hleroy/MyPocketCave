package com.myadridev.mypocketcave.enums.v2;

import com.myadridev.mypocketcave.R;

public enum WineColorEnumV2 {
    a(0, R.string.wine_color_none, -1),
    r(1, R.string.wine_color_red, R.drawable.wine_red),
    w(2, R.string.wine_color_white, R.drawable.wine_white),
    ro(3, R.string.wine_color_rose, R.drawable.wine_rose),
    c(4, R.string.wine_color_champagne, R.drawable.champagne),
    wh(5, R.string.wine_color_whisky, R.drawable.whisky),
    ru(6, R.string.wine_color_rum, R.drawable.rum),
    vo(7, R.string.wine_color_vodka, R.drawable.vodka),
    ap(8, R.string.wine_color_aperitif, R.drawable.aperitif),
    l(9, R.string.wine_color_liqueur, R.drawable.liqueur),
    b(10, R.string.wine_color_beer, R.drawable.beer),
    ci(11, R.string.wine_color_cider, R.drawable.cider),
    s(12, R.string.wine_color_soft, R.drawable.soft),
    o(13, R.string.wine_color_other, R.drawable.other);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    WineColorEnumV2(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static WineColorEnumV2 getById(int id) {
        for (WineColorEnumV2 wineColor : WineColorEnumV2.values()) {
            if (wineColor.Id == id) {
                return wineColor;
            }
        }
        return null;
    }
}
