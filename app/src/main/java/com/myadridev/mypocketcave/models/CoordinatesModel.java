package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = CoordinatesModel.class)
public class CoordinatesModel {

    public int Raw;
    public int Col;

    public CoordinatesModel() {
    }

    public CoordinatesModel(int raw, int col) {
        Raw = raw;
        Col = col;
    }
}
