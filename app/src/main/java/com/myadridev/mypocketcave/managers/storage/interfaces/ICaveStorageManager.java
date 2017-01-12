package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.content.Context;

import com.myadridev.mypocketcave.models.CaveLightModel;
import com.myadridev.mypocketcave.models.CaveModel;

import java.util.List;

public interface ICaveStorageManager {

    CaveModel getCave(Context context, int caveId);

    void insertOrUpdateCave(Context context, CaveModel cave);

    void deleteCave(Context context, CaveModel cave);

    List<CaveLightModel> getCavesWithBottle(int bottleId);

    boolean isBottleInTheCave(int bottleId, int caveId);
}
