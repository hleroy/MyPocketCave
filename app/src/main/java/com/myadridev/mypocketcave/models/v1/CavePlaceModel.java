package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;

@JsonSerialize(as = CavePlaceModel.class)
public class CavePlaceModel {

    public CavePlaceTypeEnum PlaceType;
    public int BottleId;
    public boolean IsClickable;

    public CavePlaceModel() {
        BottleId = -1;
        IsClickable = false;
    }

    public CavePlaceModel(CavePlaceModel cavePlace) {
        BottleId = cavePlace.BottleId;
        IsClickable = cavePlace.IsClickable;
        PlaceType = cavePlace.PlaceType;
    }
}
