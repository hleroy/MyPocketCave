package com.myadridev.mypocketcave.enums.v2;


public enum PositionEnumV2 {
    n(0),
    bl(1),
    br(2),
    tl(3),
    tr(4);

    public final int Id;

    PositionEnumV2(int id) {
        Id = id;
    }

    public static PositionEnumV2 getById(int id) {
        for (PositionEnumV2 pos : values()) {
            if (id == pos.Id)
                return pos;
        }
        return null;
    }
}
