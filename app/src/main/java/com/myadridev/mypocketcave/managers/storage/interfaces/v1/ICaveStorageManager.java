package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.v1.CaveLightModel;
import com.myadridev.mypocketcave.models.v1.CaveModel;

import java.util.List;

public interface ICaveStorageManager {

    CaveModel getCave(Context context, int caveId);

    void insertOrUpdateCave(Context context, CaveModel cave);

    void deleteCave(Context context, CaveModel cave);

    List<CaveLightModel> getLightCavesWithBottle(int bottleId);

    boolean isBottleInTheCave(int bottleId, int caveId);

    List<CaveModel> getCaves();
}
