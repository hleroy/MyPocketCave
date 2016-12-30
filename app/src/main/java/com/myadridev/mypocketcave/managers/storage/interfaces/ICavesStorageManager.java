package com.myadridev.mypocketcave.managers.storage.interfaces;

import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.List;

public interface ICavesStorageManager {

    List<CaveLightModel> getCaves();

    int insertCave(CaveLightModel cave, boolean needsNewId);

    void updateCave(CaveLightModel cave);

    void deleteCave(int caveId);

    int getExistingCaveId(int id, String name, int caveTypeId);

    int getCavesCount();

    int getCavesCount(int caveTypeId);
}
