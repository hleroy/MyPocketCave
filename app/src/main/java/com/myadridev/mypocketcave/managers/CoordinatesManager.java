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
        int maxRaw = -1;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col > maxCol) {
                maxCol = coordinate.Col;
            }
            if (coordinate.Row > maxRaw) {
                maxRaw = coordinate.Row;
            }
        }
        return new CoordinatesModel(maxRaw, maxCol);
    }

    public int getColFromPosition(int position) {
        return position;
    }

    public int getRowFromPosition(int position, int itemCount) {
        return itemCount - 1 - position;
    }
}
