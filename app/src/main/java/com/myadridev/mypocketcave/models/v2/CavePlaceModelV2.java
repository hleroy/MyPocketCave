package com.myadridev.mypocketcave.models.v2;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.ICavePlaceModel;

public class CavePlaceModelV2 implements ICavePlaceModel {

    @SerializedName("pt")
    public CavePlaceTypeEnumV2 PlaceType;
    @SerializedName("bi")
    public int BottleId;
    @SerializedName("ic")
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
