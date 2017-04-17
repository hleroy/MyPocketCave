package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ICoordinatesModel;

@Deprecated
@JsonSerialize(as = CoordinatesModelV1.class)
public class CoordinatesModelV1 implements ICoordinatesModel {

    public int Row;
    public int Col;

    public CoordinatesModelV1() {
    }

    public CoordinatesModelV1(int row, int col) {
        Row = row;
        Col = col;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other != null && other instanceof CoordinatesModelV1) {
            CoordinatesModelV1 that = (CoordinatesModelV1) other;
            result = (this.Row == that.Row && this.Col == that.Col);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (41 * (41 + Row) + Col);
    }
}
