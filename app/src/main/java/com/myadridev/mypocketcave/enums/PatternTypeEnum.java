package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum PatternTypeEnum {
    LINEAR(0, R.string.pattern_type_linear, R.drawable.pattern_type_linear),
    STAGGERED_ROWS(1, R.string.pattern_type_staggered_rows, R.drawable.pattern_type_staggered_rows),
    FREE(2, R.string.pattern_type_free, -1);

    public static final int number = values().length;
    public final int id;
    public final int stringResourceId;
    public final int drawableResourceId;

    PatternTypeEnum(int _id, int _stringResourceId, int _drawableResourceId) {
        id = _id;
        stringResourceId = _stringResourceId;
        drawableResourceId = _drawableResourceId;
    }

    public static PatternTypeEnum getById(int id) {
        for (PatternTypeEnum patternType : PatternTypeEnum.values()) {
            if (patternType.id == id) {
                return patternType;
            }
        }
        return null;
    }
}
