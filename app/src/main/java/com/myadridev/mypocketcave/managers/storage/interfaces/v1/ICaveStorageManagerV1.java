package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.v1.CaveLightModelV1;
import com.myadridev.mypocketcave.models.v1.CaveModelV1;

import java.util.List;

@Deprecated
public interface ICaveStorageManagerV1 {

    CaveModelV1 getCave(Context context, int caveId);

    void insertOrUpdateCave(Context context, CaveModelV1 cave);

    void deleteCave(Context context, CaveModelV1 cave);

    List<CaveLightModelV1> getLightCavesWithBottle(int bottleId);

    boolean isBottleInTheCave(int bottleId, int caveId);

    List<CaveModelV1> getCaves();
}
