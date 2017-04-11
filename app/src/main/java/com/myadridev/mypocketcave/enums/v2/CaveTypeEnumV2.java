package com.myadridev.mypocketcave.enums.v2;

import com.myadridev.mypocketcave.R;

public enum CaveTypeEnumV2 {
    a(0, R.string.cave_type_none, -1),
    bu(1, R.string.cave_type_bulk, R.drawable.cave_bulk),
    bo(2, R.string.cave_type_box, R.drawable.cave_box),
    f(3, R.string.cave_type_fridge, R.drawable.cave_fridge),
    r(4, R.string.cave_type_rack, R.drawable.cave_rack);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    CaveTypeEnumV2(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static CaveTypeEnumV2 getById(int id) {
        for (CaveTypeEnumV2 caveType : CaveTypeEnumV2.values()) {
            if (caveType.Id == id) {
                return caveType;
            }
        }
        return null;
    }
}
