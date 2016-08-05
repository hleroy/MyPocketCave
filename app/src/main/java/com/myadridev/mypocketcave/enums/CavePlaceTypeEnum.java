package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum CavePlaceTypeEnum {
    NO_PLACE(0, -1),
    PLACE(1, R.drawable.place_type_place),
    PLACE_WITH_RIGHT(2, R.drawable.place_type_place_with_right),
    PLACE_WITH_LEFT(3, R.drawable.place_type_place_with_left),
    PLACE_WITH_BOTTOM(5, R.drawable.place_type_place_with_bottom),
    PLACE_WITH_TOP(4, R.drawable.place_type_place_with_top);

    public static final int number = values().length;
    public final int id;
    public final int drawableResourceId;

    CavePlaceTypeEnum(int _id, int _drawableResourceId) {
        id = _id;
        drawableResourceId = _drawableResourceId;
    }

    public static CavePlaceTypeEnum getById(int id) {
        for (CavePlaceTypeEnum cavePlaceType : CavePlaceTypeEnum.values()) {
            if (cavePlaceType.id == id) {
                return cavePlaceType;
            }
        }
        return null;
    }
}
