package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum MillesimeEnum {
    ANY(0, R.string.millesime_none),
    LESS_THAN_TWO(1, R.string.millesime_less_than_two),
    THREE_TO_FIVE(2, R.string.millesime_three_to_five),
    SIX_TO_TEN(3, R.string.millesime_six_to_ten),
    OVER_TEN(4, R.string.millesime_over_ten);

    public static final int number = values().length;
    public final int id;
    public final int stringResourceId;

    MillesimeEnum(int _id, int _stringResourceId) {
        id = _id;
        stringResourceId = _stringResourceId;
    }

    public static MillesimeEnum getById(int id) {
        for (MillesimeEnum millesime : MillesimeEnum.values()) {
            if (millesime.id == id) {
                return millesime;
            }
        }
        return null;
    }
}
