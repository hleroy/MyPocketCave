package com.myadridev.mypocketcave.enums;

public enum VersionEnum {
    V1(0),
    V2(1);

    public final int Id;

    VersionEnum(int id) {
        Id = id;
    }

    public static VersionEnum getById(int id) {
        for (VersionEnum version : VersionEnum.values()) {
            if (version.Id == id) {
                return version;
            }
        }
        return null;
    }
}
