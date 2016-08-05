package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum CaveTypeEnum {
    ANY(0, R.string.cave_type_none, -1),
    BULK(1, R.string.cave_type_bulk, R.drawable.cave_bulk),
    BOX(2, R.string.cave_type_box, R.drawable.cave_box),
    FRIDGE(3, R.string.cave_type_fridge, R.drawable.cave_fridge),
    RACK(4, R.string.cave_type_rack, R.drawable.cave_rack);

    public static final int number = values().length;
    public final int id;
    public final int stringResourceId;
    public final int drawableResourceId;

    CaveTypeEnum(int _id, int _stringResourceId, int _drawableResourceId) {
        id = _id;
        stringResourceId = _stringResourceId;
        drawableResourceId = _drawableResourceId;
    }

    public static CaveTypeEnum getById(int id) {
        for (CaveTypeEnum caveType : CaveTypeEnum.values()) {
            if (caveType.id == id) {
                return caveType;
            }
        }
        return null;
    }
}
