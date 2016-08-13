package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;

@JsonSerialize(as = CavePlaceModel.class)
public class CavePlaceModel {

    public CavePlaceTypeEnum PlaceType;
    public int BottleId;

    public CavePlaceModel() {
        BottleId = -1;
    }
}
