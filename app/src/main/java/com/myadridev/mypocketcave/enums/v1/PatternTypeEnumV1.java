package com.myadridev.mypocketcave.enums.v1;

import com.myadridev.mypocketcave.R;

@Deprecated
public enum PatternTypeEnumV1 {
    LINEAR(0, R.string.pattern_type_linear, R.drawable.pattern_type_linear),
    STAGGERED_ROWS(1, R.string.pattern_type_staggered_rows, R.drawable.pattern_type_staggered_rows);
//    FREE(2, R.string.pattern_type_free, -1);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    PatternTypeEnumV1(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static PatternTypeEnumV1 getById(int id) {
        for (PatternTypeEnumV1 patternType : PatternTypeEnumV1.values()) {
            if (patternType.Id == id) {
                return patternType;
            }
        }
        return null;
    }
}
