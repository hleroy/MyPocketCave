package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICavesStorageManager;
import com.myadridev.mypocketcave.models.CaveLightModel;
import com.myadridev.mypocketcave.models.CaveModel;

import java.util.List;
import java.util.Map;

public class CaveManager {

    private static boolean listenerCavesRegistered = false;
    private static ICavesStorageManager cavesStorageManager = null;
    private static boolean listenerCaveRegistered = false;
    private static ICaveStorageManager caveStorageManager = null;

    private static ICavesStorageManager getCavesStorageManager() {
        if (cavesStorageManager == null) {
            cavesStorageManager = DependencyManager.getSingleton(ICavesStorageManager.class,
                    listenerCavesRegistered ? null : (OnDependencyChangeListener) () -> cavesStorageManager = null);
            listenerCavesRegistered = true;
        }
        return cavesStorageManager;
    }

    private static ICaveStorageManager getCaveStorageManager() {
        if (caveStorageManager == null) {
            caveStorageManager = DependencyManager.getSingleton(ICaveStorageManager.class,
                    listenerCaveRegistered ? null : (OnDependencyChangeListener) () -> caveStorageManager = null);
            listenerCaveRegistered = true;
        }
        return caveStorageManager;
    }

    public static List<CaveModel> getCaves() {
        return getCaveStorageManager().getCaves();
    }

    public static List<CaveLightModel> getLightCaves() {
        return getCavesStorageManager().getLightCaves();
    }

    public static CaveModel getCave(Context context, int caveId) {
        return getCaveStorageManager().getCave(context, caveId);
    }

    public static int addCave(Context context, CaveModel cave) {
        CaveLightModel caveLight = new CaveLightModel(cave);
        cave.Id = getCavesStorageManager().insertCave(context, caveLight, true);
        getCaveStorageManager().insertOrUpdateCave(context, cave);
        return cave.Id;
    }

    public static void editCave(Context context, CaveModel cave) {
        CaveLightModel caveLight = new CaveLightModel(cave);
        getCavesStorageManager().updateCave(context, caveLight);
        getCaveStorageManager().insertOrUpdateCave(context, cave);
    }

    public static void removeCave(Context context, CaveModel cave) {
        getCavesStorageManager().deleteCave(context, cave.Id);
        unplaceBottles(context, cave);
        getCaveStorageManager().deleteCave(context, cave);
    }

    private static void unplaceBottles(Context context, CaveModel cave) {
        for (Map.Entry<Integer, Float> numberPlacedBottleEntry : cave.CaveArrangement.getFloatNumberPlacedBottlesByIdMap().entrySet()) {
            BottleManager.updateNumberPlaced(context, numberPlacedBottleEntry.getKey(), -(int) Math.ceil(numberPlacedBottleEntry.getValue()));
        }
    }

    public static int getExistingCaveId(int id, String name, CaveTypeEnum caveType) {
        return getCavesStorageManager().getExistingCaveId(id, name, caveType.Id);
    }

    public static int getCavesCount() {
        return getCavesStorageManager().getCavesCount();
    }

    public static int getCavesCount(CaveTypeEnum caveType) {
        return getCavesStorageManager().getCavesCount(caveType.Id);
    }

    public static List<CaveLightModel> getLightCavesWithBottle(int bottleId) {
        return getCaveStorageManager().getLightCavesWithBottle(bottleId);
    }

    public static boolean isBottleInTheCave(int bottleId, int caveId) {
        return getCaveStorageManager().isBottleInTheCave(bottleId, caveId);
    }
}
