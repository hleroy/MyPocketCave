package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

import java.util.Collection;

public class CoordinatesManager {

    public static CoordinatesModelV2 getMaxRowCol(Collection<CoordinatesModelV2> coordinates) {
        int maxCol = -1;
        int maxRow = -1;
        for (CoordinatesModelV2 coordinate : coordinates) {
            if (coordinate.Col > maxCol) {
                maxCol = coordinate.Col;
            }
            if (coordinate.Row > maxRow) {
                maxRow = coordinate.Row;
            }
        }
        return new CoordinatesModelV2(maxRow, maxCol);
    }

    public static int getColFromPosition(int position) {
        return position;
    }

    public static int getRowFromPosition(int position, int itemCount) {
        return itemCount - 1 - position;
    }

    public static int getPositionFromCoordinates(int row, int col, int nbRows, int nbCols) {
        return (nbRows - 1 - row) * nbCols + col;
    }

    public static boolean containsRow(Collection<CoordinatesModelV2> coordinates, int row) {
        if (row < 0) return false;
        for (CoordinatesModelV2 coordinate : coordinates) {
            if (coordinate.Row >= row) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsCol(Collection<CoordinatesModelV2> coordinates, int col) {
        if (col < 0) return false;
        for (CoordinatesModelV2 coordinate : coordinates) {
            if (coordinate.Col >= col) {
                return true;
            }
        }
        return false;
    }

    public static int getMaxCol(Collection<CoordinatesModelV2> coordinates) {
        int maxCol = -1;
        for (CoordinatesModelV2 coordinate : coordinates) {
            if (coordinate.Col >= maxCol) {
                maxCol = coordinate.Col;
            }
        }
        return maxCol;
    }

    public static int getMaxRow(Collection<CoordinatesModelV2> coordinates) {
        int maxRow = -1;
        for (CoordinatesModelV2 coordinate : coordinates) {
            if (coordinate.Row > maxRow) {
                maxRow = coordinate.Row;
            }
        }
        return maxRow;
    }
}
