package com.myadridev.mypocketcave.enums;

public enum ActivityRequestEnum {
    PATTERN_SELECTION(0),
    CREATE_PATTERN(1);

    public static final int number = values().length;
    public final int id;

    ActivityRequestEnum(int _id) {
        id = _id;
    }

    public static ActivityRequestEnum getById(int id) {
        for (ActivityRequestEnum patternType : ActivityRequestEnum.values()) {
            if (patternType.id == id) {
                return patternType;
            }
        }
        return null;
    }
}
