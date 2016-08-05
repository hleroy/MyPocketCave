package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.Collection;

public class CoordinatesModelManager {

    public static CoordinatesModelManager Instance;
    private static boolean _isInitialized;

    private CoordinatesModelManager() {
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init() {
        Instance = new CoordinatesModelManager();
        _isInitialized = true;
    }

    public CoordinatesModel getMaxRawCol(Collection<CoordinatesModel> coordinates) {
        int maxCol = 0;
        int maxRaw = 0;
        for (CoordinatesModel coordinate : coordinates) {
            if (coordinate.Col > maxCol) {
                maxCol = coordinate.Col;
            }
            if (coordinate.Raw > maxRaw) {
                maxRaw = coordinate.Raw;
            }
        }
        return new CoordinatesModel(maxRaw, maxCol);
    }
}
