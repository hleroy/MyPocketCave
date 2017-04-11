package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ICoordinatesModel;

@JsonSerialize(as = CoordinatesModelV2.class)
public class CoordinatesModelV2 implements ICoordinatesModel {

    @JsonProperty("r")
    public int Row;
    @JsonProperty("c")
    public int Col;

    public CoordinatesModelV2() {
    }

    public CoordinatesModelV2(int row, int col) {
        Row = row;
        Col = col;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other != null && other instanceof CoordinatesModelV2) {
            CoordinatesModelV2 that = (CoordinatesModelV2) other;
            result = (this.Row == that.Row && this.Col == that.Col);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (41 * (41 + Row) + Col);
    }
}
