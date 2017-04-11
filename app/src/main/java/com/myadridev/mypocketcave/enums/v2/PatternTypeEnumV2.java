package com.myadridev.mypocketcave.enums.v2;

import com.myadridev.mypocketcave.R;

public enum PatternTypeEnumV2 {
    l(0, R.string.pattern_type_linear, R.drawable.pattern_type_linear),
    s(1, R.string.pattern_type_staggered_rows, R.drawable.pattern_type_staggered_rows);
//    f(2, R.string.pattern_type_free, -1);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    PatternTypeEnumV2(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static PatternTypeEnumV2 getById(int id) {
        for (PatternTypeEnumV2 patternType : PatternTypeEnumV2.values()) {
            if (patternType.Id == id) {
                return patternType;
            }
        }
        return null;
    }
}
