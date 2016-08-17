package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.managers.SQLite.CaveSQLiteManager;
import com.myadridev.mypocketcave.models.CaveModel;

import java.util.List;

public class CaveManager {

    public static List<CaveModel> getCaves() {
        return CaveSQLiteManager.getCaves();
    }

    public static CaveModel getCave(int caveId) {
        return CaveSQLiteManager.getCave(caveId);
    }

    public static int addCave(CaveModel cave) {
        return CaveSQLiteManager.insertCave(cave);
    }

    public static void editCave(CaveModel cave) {
        CaveSQLiteManager.updateCave(cave);
    }

    public static void removeCave(CaveModel cave) {
        CaveSQLiteManager.deleteCave(cave);
    }

    public static int getExistingCaveId(int id, String name, CaveTypeEnum caveType) {
        return CaveSQLiteManager.getExistingCaveId(id, name, caveType.id);
    }

    public static int getCavesCount() {
        return CaveSQLiteManager.getCavesCount();
    }

    public static int getCavesCount(CaveTypeEnum caveType) {
        return CaveSQLiteManager.getCavesCount(caveType.id);
    }
}
