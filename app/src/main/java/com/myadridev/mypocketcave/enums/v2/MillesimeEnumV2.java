package com.myadridev.mypocketcave.enums.v2;

import com.myadridev.mypocketcave.R;

public enum MillesimeEnumV2 {
    a(0, R.string.millesime_none),
    ltt(1, R.string.millesime_less_than_two),
    ttf(2, R.string.millesime_three_to_five),
    stt(3, R.string.millesime_six_to_ten),
    ot(4, R.string.millesime_over_ten);

    public final int Id;
    public final int StringResourceId;

    MillesimeEnumV2(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public static MillesimeEnumV2 getById(int id) {
        for (MillesimeEnumV2 millesime : MillesimeEnumV2.values()) {
            if (millesime.Id == id) {
                return millesime;
            }
        }
        return null;
    }
}
