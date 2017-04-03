package com.myadridev.mypocketcave.managers.storage.interfaces.v2;

import android.content.Context;

import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import java.util.List;

public interface ICaveStorageManagerV2 {

    CaveModelV2 getCave(Context context, int caveId);

    void insertOrUpdateCave(Context context, CaveModelV2 cave);

    void deleteCave(Context context, CaveModelV2 cave);

    List<CaveLightModelV2> getLightCavesWithBottle(int bottleId);

    boolean isBottleInTheCave(int bottleId, int caveId);

    List<CaveModelV2> getCaves();
}
