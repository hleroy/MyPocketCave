package com.myadridev.mypocketcave.enums.v1;

import com.myadridev.mypocketcave.R;

@Deprecated
public enum CaveTypeEnumV1 {
    ANY(0, R.string.cave_type_none, -1),
    BULK(1, R.string.cave_type_bulk, R.drawable.cave_bulk),
    BOX(2, R.string.cave_type_box, R.drawable.cave_box),
    FRIDGE(3, R.string.cave_type_fridge, R.drawable.cave_fridge),
    RACK(4, R.string.cave_type_rack, R.drawable.cave_rack);

    public final int Id;
    public final int StringResourceId;
    public final int DrawableResourceId;

    CaveTypeEnumV1(int id, int stringResourceId, int drawableResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
        DrawableResourceId = drawableResourceId;
    }

    public static CaveTypeEnumV1 getById(int id) {
        for (CaveTypeEnumV1 caveType : CaveTypeEnumV1.values()) {
            if (caveType.Id == id) {
                return caveType;
            }
        }
        return null;
    }
}
