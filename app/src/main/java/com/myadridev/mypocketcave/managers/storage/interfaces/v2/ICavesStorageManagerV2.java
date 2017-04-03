package com.myadridev.mypocketcave.managers.storage.interfaces.v2;

import android.content.Context;

import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;

import java.util.List;

public interface ICavesStorageManagerV2 {

    List<CaveLightModelV2> getLightCaves();

    int insertCave(Context context, CaveLightModelV2 cave, boolean needsNewId);

    void updateCave(Context context, CaveLightModelV2 cave);

    void deleteCave(Context context, int caveId);

    int getExistingCaveId(int id, String name, int caveTypeId);

    int getCavesCount();

    int getCavesCount(int caveTypeId);

    void updateIndexes(Context context);
}
