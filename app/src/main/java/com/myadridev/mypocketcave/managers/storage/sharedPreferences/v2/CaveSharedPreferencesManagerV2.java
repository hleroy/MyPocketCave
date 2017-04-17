package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2;

import android.content.Context;
import android.support.annotation.Nullable;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ISharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICaveStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaveSharedPreferencesManagerV2 implements ICaveStorageManagerV2 {

    public static CaveSharedPreferencesManagerV2 Instance;
    private static boolean isInitialized;
    private final Map<Integer, CaveModelV2> allCavesMap = new HashMap<>();
    private final int filenameResourceId = R.string.store_cave;
    private final int keyCaveResourceId = R.string.store_cave_key;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManagerV2 sharedPreferencesManager = null;
    private boolean listenerCavesStorageRegistered = false;
    private ICavesStorageManagerV2 cavesStorageManager = null;

    private CaveSharedPreferencesManagerV2(Context context) {
        loadAllCaves(context);
    }

    private CaveSharedPreferencesManagerV2(Context context, Map<Integer, CaveModelV2> allCavesMap) {
        for (CaveModelV2 cave : allCavesMap.values()) {
            insertOrUpdateCave(context, cave);
        }
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new CaveSharedPreferencesManagerV2(context);
        isInitialized = true;
    }

    public static void init(Context context, Map<Integer, CaveModelV2> allCavesMap) {
        if (isInitialized) return;
        Instance = new CaveSharedPreferencesManagerV2(context, allCavesMap);
        isInitialized = true;
    }

    private ICavesStorageManagerV2 getCavesStorageManager() {
        if (cavesStorageManager == null) {
            cavesStorageManager = DependencyManager.getSingleton(ICavesStorageManagerV2.class,
                    listenerCavesStorageRegistered ? null : (OnDependencyChangeListener) () -> cavesStorageManager = null);
            listenerCavesStorageRegistered = true;
        }
        return cavesStorageManager;
    }

    private ISharedPreferencesManagerV2 getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManagerV2.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllCaves(Context context) {
        List<CaveLightModelV2> cavesLight = getCavesStorageManager().getLightCaves();
        for (CaveLightModelV2 caveLight : cavesLight) {
            CaveModelV2 cave = loadCave(context, caveLight.Id);
            if (cave != null) {
                allCavesMap.put(caveLight.Id, cave);
            }
        }
    }

    public List<CaveModelV2> getCaves() {
        List<CaveModelV2> caves = new ArrayList<>(allCavesMap.values());
        Collections.sort(caves);
        return caves;
    }

    public CaveModelV2 getCave(int caveId) {
        return CollectionsHelper.getValueOrDefault(allCavesMap, caveId, null);
    }

    @Nullable
    private CaveModelV2 loadCave(Context context, int caveId) {
        IStorableModel caveAsStorableModel = getSharedPreferencesManager().loadStoredStringData(context, context.getString(filenameResourceId, caveId), keyCaveResourceId, CaveModelV2.class);

        if (caveAsStorableModel != null && caveAsStorableModel instanceof CaveModelV2) {
            return (CaveModelV2) caveAsStorableModel;
        }
        return null;
    }

    public void insertOrUpdateCave(Context context, CaveModelV2 cave) {
        allCavesMap.put(cave.Id, cave);
        getSharedPreferencesManager().storeStringData(context, context.getString(filenameResourceId, cave.Id), context.getString(keyCaveResourceId), cave);
    }

    public void deleteCave(Context context, CaveModelV2 cave) {
        getSharedPreferencesManager().delete(context, context.getString(filenameResourceId, cave.Id));
    }

    public List<CaveLightModelV2> getLightCavesWithBottle(int bottleId) {
        List<CaveLightModelV2> cavesWithBottle = new ArrayList<>(allCavesMap.size());

        for (CaveModelV2 cave : allCavesMap.values()) {
            int numberBottlesInTheCave = cave.getNumberBottles(bottleId);
            if (numberBottlesInTheCave > 0) {
                CaveLightModelV2 caveLight = new CaveLightModelV2();
                caveLight.Id = cave.Id;
                caveLight.Name = cave.Name;
                caveLight.CaveType = cave.CaveType;
                caveLight.TotalUsed = numberBottlesInTheCave;
                cavesWithBottle.add(caveLight);
            }
        }

        Collections.sort(cavesWithBottle);
        return cavesWithBottle;
    }

    public boolean isBottleInTheCave(int bottleId, int caveId) {
        if (allCavesMap.containsKey(caveId)) {
            CaveModelV2 cave = allCavesMap.get(caveId);
            return cave.getNumberBottles(bottleId) > 0;
        }
        return false;
    }
}
