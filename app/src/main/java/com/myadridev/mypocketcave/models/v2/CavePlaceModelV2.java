package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.ICavePlaceModel;

@JsonSerialize(as = CavePlaceModelV2.class)
public class CavePlaceModelV2 implements ICavePlaceModel {

    @JsonProperty("pt")
    public CavePlaceTypeEnumV2 PlaceType;
    @JsonProperty("bi")
    public int BottleId;
    @JsonProperty("ic")
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
