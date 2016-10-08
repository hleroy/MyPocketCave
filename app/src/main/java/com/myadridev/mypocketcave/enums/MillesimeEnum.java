package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum MillesimeEnum {
    ANY(0, R.string.millesime_none),
    LESS_THAN_TWO(1, R.string.millesime_less_than_two),
    THREE_TO_FIVE(2, R.string.millesime_three_to_five),
    SIX_TO_TEN(3, R.string.millesime_six_to_ten),
    OVER_TEN(4, R.string.millesime_over_ten);

    public final int Id;
    public final int StringResourceId;

    MillesimeEnum(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public static MillesimeEnum getById(int id) {
        for (MillesimeEnum millesime : MillesimeEnum.values()) {
            if (millesime.Id == id) {
                return millesime;
            }
        }
        return null;
    }
}
