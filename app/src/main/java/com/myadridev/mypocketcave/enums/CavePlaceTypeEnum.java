package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum CavePlaceTypeEnum {
    NO_PLACE(0, -1),
    PLACE_BOTTOM_RIGHT(1, R.drawable.place_type_bottom_right),
    PLACE_BOTTOM_LEFT(2, R.drawable.place_type_bottom_left),
    PLACE_TOP_RIGHT(3, R.drawable.place_type_top_right),
    PLACE_TOP_LEFT(4, R.drawable.place_type_top_left),
    PLACE_BOTTOM_RIGHT_RED(11, R.drawable.place_type_bottom_right_red),
    PLACE_BOTTOM_LEFT_RED(12, R.drawable.place_type_bottom_left_red),
    PLACE_TOP_RIGHT_RED(13, R.drawable.place_type_top_right_red),
    PLACE_TOP_LEFT_RED(14, R.drawable.place_type_top_left_red),
    PLACE_BOTTOM_RIGHT_WHITE(21, R.drawable.place_type_bottom_right_white),
    PLACE_BOTTOM_LEFT_WHITE(22, R.drawable.place_type_bottom_left_white),
    PLACE_TOP_RIGHT_WHITE(23, R.drawable.place_type_top_right_white),
    PLACE_TOP_LEFT_WHITE(24, R.drawable.place_type_top_left_white),
    PLACE_BOTTOM_RIGHT_ROSE(31, R.drawable.place_type_bottom_right_rose),
    PLACE_BOTTOM_LEFT_ROSE(32, R.drawable.place_type_bottom_left_rose),
    PLACE_TOP_RIGHT_ROSE(33, R.drawable.place_type_top_right_rose),
    PLACE_TOP_LEFT_ROSE(34, R.drawable.place_type_top_left_rose),
    PLACE_BOTTOM_RIGHT_CHAMPAGNE(41, R.drawable.place_type_bottom_right_champagne),
    PLACE_BOTTOM_LEFT_CHAMPAGNE(42, R.drawable.place_type_bottom_left_champagne),
    PLACE_TOP_RIGHT_CHAMPAGNE(43, R.drawable.place_type_top_right_champagne),
    PLACE_TOP_LEFT_CHAMPAGNE(44, R.drawable.place_type_top_left_champagne);

    public final int Id;
    public final int DrawableResourceId;

    CavePlaceTypeEnum(int id, int drawableResourceId) {
        Id = id;
        DrawableResourceId = drawableResourceId;
    }

    public static CavePlaceTypeEnum getById(int id) {
        for (CavePlaceTypeEnum cavePlaceType : CavePlaceTypeEnum.values()) {
            if (cavePlaceType.Id == id) {
                return cavePlaceType;
            }
        }
        return null;
    }

    public boolean isBottomRight() {
        return Id % 10 == 1;
    }

    public boolean isBottomLeft() {
        return Id % 10 == 2;
    }

    public boolean isTopRight() {
        return Id % 10 == 3;
    }

    public boolean isTopLeft() {
        return Id % 10 == 4;
    }
}
