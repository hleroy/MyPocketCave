package com.myadridev.mypocketcave.enums.v1;

import com.myadridev.mypocketcave.R;

@Deprecated
public enum MillesimeEnumV1 {
    ANY(0, R.string.millesime_none),
    LESS_THAN_TWO(1, R.string.millesime_less_than_two),
    THREE_TO_FIVE(2, R.string.millesime_three_to_five),
    SIX_TO_TEN(3, R.string.millesime_six_to_ten),
    OVER_TEN(4, R.string.millesime_over_ten);

    public final int Id;
    public final int StringResourceId;

    MillesimeEnumV1(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public static MillesimeEnumV1 getById(int id) {
        for (MillesimeEnumV1 millesime : MillesimeEnumV1.values()) {
            if (millesime.Id == id) {
                return millesime;
            }
        }
        return null;
    }
}
