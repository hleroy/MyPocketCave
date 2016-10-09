package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICavesStorageManager;
import com.myadridev.mypocketcave.models.CaveLightModel;
import com.myadridev.mypocketcave.models.CaveModel;

import java.util.List;

public class CaveManager {

    private static boolean listenerCavesRegistered = false;
    private static ICavesStorageManager cavesStorageManager = null;
    private static ICavesStorageManager getCavesStorageManager() {
        if (cavesStorageManager == null) {
            cavesStorageManager = DependencyManager.getSingleton(ICavesStorageManager.class,
                    listenerCavesRegistered ? null : new OnDependencyChangeListener() {
                @Override
                public void onDependencyChange() {
                    cavesStorageManager = null;
                }
            });
            listenerCavesRegistered = true;
        }
        return cavesStorageManager;
    }

    private static boolean listenerCaveRegistered = false;
    private static ICaveStorageManager caveStorageManager = null;
    private static ICaveStorageManager getCaveStorageManager() {
        if (caveStorageManager == null) {
            caveStorageManager = DependencyManager.getSingleton(ICaveStorageManager.class,
                    listenerCaveRegistered ? null : new OnDependencyChangeListener() {
                @Override
                public void onDependencyChange() {
                    caveStorageManager = null;
                }
            });
            listenerCaveRegistered = true;
        }
        return caveStorageManager;
    }

    public static List<CaveLightModel> getCaves() {
        return getCavesStorageManager().getCaves();
    }

    public static CaveModel getCave(int caveId) {
        return getCaveStorageManager().getCave(caveId);
    }

    public static int addCave(CaveModel cave) {
        CaveLightModel caveLight = new CaveLightModel(cave);
        cave.Id = getCavesStorageManager().insertCave(caveLight, true);
        getCaveStorageManager().insertCave(cave);
        return cave.Id;
    }

    public static void editCave(CaveModel cave) {
        CaveLightModel caveLight = new CaveLightModel(cave);
        getCavesStorageManager().updateCave(caveLight);
        getCaveStorageManager().updateCave(cave);
    }

    public static void removeCave(CaveModel cave) {
        getCavesStorageManager().deleteCave(cave.Id);
        getCaveStorageManager().deleteCave(cave);
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
}
