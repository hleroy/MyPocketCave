package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;

@JsonSerialize(as = CavePlaceModelV2.class)
public class CavePlaceModelV2 {

    public CavePlaceTypeEnum PlaceType;
    public int BottleId;
    public boolean IsClickable;

    public CavePlaceModelV2() {
        BottleId = -1;
        IsClickable = false;
    }

    public CavePlaceModelV2(CavePlaceModelV2 cavePlace) {
        BottleId = cavePlace.BottleId;
        IsClickable = cavePlace.IsClickable;
        PlaceType = cavePlace.PlaceType;
    }
}
