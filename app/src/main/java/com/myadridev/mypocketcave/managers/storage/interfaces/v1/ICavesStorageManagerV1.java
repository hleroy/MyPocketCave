package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.v1.CaveLightModelV1;

import java.util.List;

@Deprecated
public interface ICavesStorageManagerV1 {

    List<CaveLightModelV1> getLightCaves();

    int insertCave(Context context, CaveLightModelV1 cave, boolean needsNewId);

    void updateCave(Context context, CaveLightModelV1 cave);

    void deleteCave(Context context, int caveId);

    int getExistingCaveId(int id, String name, int caveTypeId);

    int getCavesCount();

    int getCavesCount(int caveTypeId);

    void updateIndexes(Context context);
}
