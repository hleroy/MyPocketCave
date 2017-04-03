package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.models.v1.CoordinatesModel;

import java.util.Collection;

public class CoordinatesManager {

    public static CoordinatesModel getMaxRowCol(Collection<CoordinatesModel> coordinates) {
        int maxCol = -1;
        int maxRow = -1;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col > maxCol) {
                maxCol = coordinate.Col;
            }
            if (coordinate.Row > maxRow) {
                maxRow = coordinate.Row;
            }
        }
        return new CoordinatesModel(maxRow, maxCol);
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

    public static boolean containsRow(Collection<CoordinatesModel> coordinates, int row) {
        if (row < 0) return false;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Row >= row) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsCol(Collection<CoordinatesModel> coordinates, int col) {
        if (col < 0) return false;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col >= col) {
                return true;
            }
        }
        return false;
    }

    public static int getMaxCol(Collection<CoordinatesModel> coordinates) {
        int maxCol = -1;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col >= maxCol) {
                maxCol = coordinate.Col;
            }
        }
        return maxCol;
    }

    public static int getMaxRow(Collection<CoordinatesModel> coordinates) {
        int maxRow = -1;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Row > maxRow) {
                maxRow = coordinate.Row;
            }
        }
        return maxRow;
    }
}
