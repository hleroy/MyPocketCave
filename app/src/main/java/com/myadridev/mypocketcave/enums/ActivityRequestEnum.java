package com.myadridev.mypocketcave.enums;

public enum ActivityRequestEnum {
    PATTERN_SELECTION(0),
    CREATE_PATTERN(1),
    EDIT_PATTERN(2);

    public final int Id;

    ActivityRequestEnum(int id) {
        Id = id;
    }

    public static ActivityRequestEnum getById(int id) {
        for (ActivityRequestEnum patternType : ActivityRequestEnum.values()) {
            if (patternType.Id == id) {
                return patternType;
            }
        }
        return null;
    }
}
