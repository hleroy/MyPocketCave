package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.Collection;

public class CoordinatesManager {

    public static CoordinatesManager Instance;
    private static boolean _isInitialized;

    private CoordinatesManager() {
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init() {
        Instance = new CoordinatesManager();
        _isInitialized = true;
    }

    public CoordinatesModel getMaxRowCol(Collection<CoordinatesModel> coordinates) {
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

    public int getColFromPosition(int position) {
        return position;
    }

    public int getRowFromPosition(int position, int itemCount) {
        return itemCount - 1 - position;
    }

    public boolean containsRow(Collection<CoordinatesModel> coordinates, int row) {
        if (row < 0) return false;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Row >= row) {
                return true;
            }
        }
        return false;
    }

    public boolean containsCol(Collection<CoordinatesModel> coordinates, int col) {
        if (col < 0) return false;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col >= col) {
                return true;
            }
        }
        return false;
    }

    public int getMaxCol(Collection<CoordinatesModel> coordinates) {
        int maxCol = -1;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col >= maxCol) {
                maxCol = coordinate.Col;
            }
        }
        return maxCol;
    }

    public int getMaxRow(Collection<CoordinatesModel> coordinates) {
        int maxRow = -1;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Row > maxRow) {
                maxRow = coordinate.Row;
            }
        }
        return maxRow;
    }
}
