package com.myadridev.mypocketcave.enums.v1;

import com.myadridev.mypocketcave.R;

public enum PatternTypeEnum {
    LINEAR(0, R.string.pattern_type_linear, R.drawable.pattern_type_linear),
    STAGGERED_ROWS(1, R.string.pattern_type_staggered_rows, R.drawable.pattern_type_staggered_rows);
//    FREE(2, R.string.pattern_type_free, -1);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    PatternTypeEnum(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static PatternTypeEnum getById(int id) {
        for (PatternTypeEnum patternType : PatternTypeEnum.values()) {
            if (patternType.Id == id) {
                return patternType;
            }
        }
        return null;
    }
}
