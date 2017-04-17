package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.CavePlaceTypeEnumV1;
import com.myadridev.mypocketcave.models.inferfaces.ICavePlaceModel;

@Deprecated
@JsonSerialize(as = CavePlaceModelV1.class)
public class CavePlaceModelV1 implements ICavePlaceModel {

    public CavePlaceTypeEnumV1 PlaceType;
    public int BottleId;
    public boolean IsClickable;

    public CavePlaceModelV1() {
        BottleId = -1;
        IsClickable = false;
    }

    public CavePlaceModelV1(CavePlaceModelV1 cavePlace) {
        BottleId = cavePlace.BottleId;
        IsClickable = cavePlace.IsClickable;
        PlaceType = cavePlace.PlaceType;
    }
}
