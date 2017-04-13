package com.myadridev.mypocketcave.models.v2;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.models.inferfaces.ICoordinatesModel;

public class CoordinatesModelV2 implements ICoordinatesModel {

    @SerializedName("r")
    public int Row;
    @SerializedName("c")
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
