package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = CoordinatesModel.class)
public class CoordinatesModel {

    public int Row;
    public int Col;

    public CoordinatesModel() {
    }

    public CoordinatesModel(int row, int col) {
        Row = row;
        Col = col;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other != null && other instanceof CoordinatesModel) {
            CoordinatesModel that = (CoordinatesModel) other;
            result = (this.Row == that.Row && this.Col == that.Col);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (41 * (41 + Row) + Col);
    }
}
