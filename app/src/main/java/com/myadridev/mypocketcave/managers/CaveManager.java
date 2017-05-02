package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICaveStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import java.util.List;
import java.util.Map;

public class CaveManager {

    private static boolean listenerCavesRegistered = false;
    private static ICavesStorageManagerV2 cavesStorageManager = null;
    private static boolean listenerCaveRegistered = false;
    private static ICaveStorageManagerV2 caveStorageManager = null;

    private static ICavesStorageManagerV2 getCavesStorageManager() {
        if (cavesStorageManager == null) {
            cavesStorageManager = DependencyManager.getSingleton(ICavesStorageManagerV2.class,
                    listenerCavesRegistered ? null : (OnDependencyChangeListener) () -> cavesStorageManager = null);
            listenerCavesRegistered = true;
        }
        return cavesStorageManager;
    }

    private static ICaveStorageManagerV2 getCaveStorageManager() {
        if (caveStorageManager == null) {
            caveStorageManager = DependencyManager.getSingleton(ICaveStorageManagerV2.class,
                    listenerCaveRegistered ? null : (OnDependencyChangeListener) () -> caveStorageManager = null);
            listenerCaveRegistered = true;
        }
        return caveStorageManager;
    }

    public static List<CaveModelV2> getCaves() {
        return getCaveStorageManager().getCaves();
    }

    public static List<CaveLightModelV2> getLightCaves() {
        return getCavesStorageManager().getLightCaves();
    }

    public static CaveModelV2 getCave(int caveId) {
        return getCaveStorageManager().getCave(caveId);
    }

    public static int addCave(Context context, CaveModelV2 cave) {
        CaveLightModelV2 caveLight = new CaveLightModelV2(cave);
        cave.Id = getCavesStorageManager().insertCave(context, caveLight, true);
        getCaveStorageManager().insertOrUpdateCave(context, cave);
        return cave.Id;
    }

    public static void addCaves(Context context, List<CaveModelV2> caves) {
        for (CaveModelV2 cave : caves) {
            // we want to keep the ids of the caves
            editCave(context, cave);
        }
        updateIndexes(context);
    }

    private static void updateIndexes(Context context) {
        getCavesStorageManager().updateIndexes(context);
    }

    public static void editCave(Context context, CaveModelV2 cave) {
        CaveLightModelV2 caveLight = new CaveLightModelV2(cave);
        getCavesStorageManager().updateCave(context, caveLight);
        getCaveStorageManager().insertOrUpdateCave(context, cave);
    }

    public static void removeCave(Context context, CaveModelV2 cave) {
        getCavesStorageManager().deleteCave(context, cave.Id);
        unplaceBottles(context, cave);
        getCaveStorageManager().deleteCave(context, cave);
    }

    public static void removeAllCaves(Context context) {
        for (CaveModelV2 cave : getCaves()) {
            removeCave(context, cave);
        }
    }

    private static void unplaceBottles(Context context, CaveModelV2 cave) {
        for (Map.Entry<Integer, Float> numberPlacedBottleEntry : CaveArrangementModelManager.getFloatNumberPlacedBottlesByIdMap(cave.CaveArrangement).entrySet()) {
            BottleManager.updateNumberPlaced(context, numberPlacedBottleEntry.getKey(), -(int) Math.ceil(numberPlacedBottleEntry.getValue()));
        }
    }

    public static int getExistingCaveId(int id, String name, CaveTypeEnumV2 caveType) {
        return getCavesStorageManager().getExistingCaveId(id, name, caveType.Id);
    }

    public static int getCavesCount() {
        return getCavesStorageManager().getCavesCount();
    }

    public static int getCavesCount(CaveTypeEnumV2 caveType) {
        return getCavesStorageManager().getCavesCount(caveType.Id);
    }

    public static List<CaveLightModelV2> getLightCavesWithBottle(int bottleId) {
        return getCaveStorageManager().getLightCavesWithBottle(bottleId);
    }

    public static boolean isBottleInTheCave(int bottleId, int caveId) {
        return getCaveStorageManager().isBottleInTheCave(bottleId, caveId);
    }

    public static int getNumberBottles(CaveModelV2 cave, int bottleId) {
        return cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.containsKey(bottleId)
                ? cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.get(bottleId)
                : 0;
    }

    public static List<BottleModelV2> getBottles(CaveModelV2 cave) {
        return BottleManager.getBottles(cave.CaveArrangement.IntNumberPlacedBottlesByIdMap.keySet());
    }
}
