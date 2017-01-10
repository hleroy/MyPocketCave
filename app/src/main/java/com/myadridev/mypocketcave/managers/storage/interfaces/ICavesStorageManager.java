package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.content.Context;

import com.myadridev.mypocketcave.models.CaveLightModel;

import java.util.List;

public interface ICavesStorageManager {

    List<CaveLightModel> getCaves();

    int insertCave(Context context, CaveLightModel cave, boolean needsNewId);

    void updateCave(Context context, CaveLightModel cave);

    void deleteCave(Context context, int caveId);

    int getExistingCaveId(int id, String name, int caveTypeId);

    int getCavesCount();

    int getCavesCount(int caveTypeId);
}
