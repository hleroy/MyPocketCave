package com.myadridev.mypocketcave.enums.v2;

import com.myadridev.mypocketcave.R;

public enum FarmingTypeEnumV2 {
    af(0, R.string.any_farming),
    of(1, R.string.organic_farming),
    nof(2, R.string.not_organic_farming);

    public final int Id;
    public final int StringResourceId;

    FarmingTypeEnumV2(int id, int stringResourceId){
        this.Id = id;
        this.StringResourceId = stringResourceId;
    }

    public static FarmingTypeEnumV2 getById(int id) {
        for (FarmingTypeEnumV2 farmingType : FarmingTypeEnumV2.values()) {
            if (farmingType.Id == id) {
                return farmingType;
            }
        }
        return null;
    }

}
