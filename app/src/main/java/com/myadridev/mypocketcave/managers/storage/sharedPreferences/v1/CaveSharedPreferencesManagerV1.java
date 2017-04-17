package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1;

import android.content.Context;
import android.support.annotation.Nullable;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ICaveStorageManagerV1;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ICavesStorageManagerV1;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ISharedPreferencesManagerV1;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v1.CaveLightModelV1;
import com.myadridev.mypocketcave.models.v1.CaveModelV1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class CaveSharedPreferencesManagerV1 implements ICaveStorageManagerV1 {

    private static final Map<Integer, CaveModelV1> allCavesMap = new HashMap<>();
    public static CaveSharedPreferencesManagerV1 Instance;
    private static boolean isInitialized;
    private int filenameResourceId = R.string.store_cave;
    private int keyCaveResourceId = R.string.store_cave_key;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManagerV1 sharedPreferencesManager = null;
    private boolean listenerCavesStorageRegistered = false;
    private ICavesStorageManagerV1 cavesStorageManager = null;

    private CaveSharedPreferencesManagerV1(Context context) {
        loadAllCaves(context);
    }

    private CaveSharedPreferencesManagerV1(Context context, Collection<Integer> caveIds) {
        loadAllCaves(context, caveIds);
    }

    public static Map<Integer, CaveModelV1> getAllCaves(Context context, Collection<Integer> caveIds) {
        init(context, caveIds);
        return allCavesMap;
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new CaveSharedPreferencesManagerV1(context);
        isInitialized = true;
    }

    public static void init(Context context, Collection<Integer> caveIds) {
        if (isInitialized) return;
        Instance = new CaveSharedPreferencesManagerV1(context, caveIds);
        isInitialized = true;
    }

    private ICavesStorageManagerV1 getCavesStorageManager() {
        if (cavesStorageManager == null) {
            cavesStorageManager = DependencyManager.getSingleton(ICavesStorageManagerV1.class,
                    listenerCavesStorageRegistered ? null : (OnDependencyChangeListener) () -> cavesStorageManager = null);
            listenerCavesStorageRegistered = true;
        }
        return cavesStorageManager;
    }

    private ISharedPreferencesManagerV1 getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManagerV1.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllCaves(Context context) {
        List<CaveLightModelV1> cavesLight = getCavesStorageManager().getLightCaves();
        List<Integer> caveIds = new ArrayList<>(cavesLight.size());
        for (CaveLightModelV1 cave : cavesLight) {
            caveIds.add(cave.Id);
        }
        loadAllCaves(context, caveIds);
    }

    private void loadAllCaves(Context context, Collection<Integer> caveIds) {
        for (int caveId : caveIds) {
            CaveModelV1 cave = loadCave(context, caveId);
            if (cave != null) {
                allCavesMap.put(caveId, cave);
            }
        }
    }

    public List<CaveModelV1> getCaves() {
        List<CaveModelV1> caves = new ArrayList<>(allCavesMap.values());
        Collections.sort(caves);
        return caves;
    }

    public CaveModelV1 getCave(Context context, int caveId) {
        return CollectionsHelper.getValueOrDefault(allCavesMap, caveId, null);
    }

    @Nullable
    private CaveModelV1 loadCave(Context context, int caveId) {
        IStorableModel caveAsStorableModel = getSharedPreferencesManager().loadStoredStringData(context, context.getString(filenameResourceId, caveId), keyCaveResourceId, CaveModelV1.class);

        if (caveAsStorableModel != null && caveAsStorableModel instanceof CaveModelV1) {
            return (CaveModelV1) caveAsStorableModel;
        }
        return null;
    }

    public void insertOrUpdateCave(Context context, CaveModelV1 cave) {
        allCavesMap.put(cave.Id, cave);
        getSharedPreferencesManager().storeStringData(context, context.getString(filenameResourceId, cave.Id), context.getString(keyCaveResourceId), cave);
    }

    public void deleteCave(Context context, CaveModelV1 cave) {
        getSharedPreferencesManager().delete(context, context.getString(filenameResourceId, cave.Id));
    }

    public List<CaveLightModelV1> getLightCavesWithBottle(int bottleId) {
        List<CaveLightModelV1> cavesWithBottle = new ArrayList<>(allCavesMap.size());

        for (CaveModelV1 cave : allCavesMap.values()) {
            int numberBottlesInTheCave = cave.getNumberBottles(bottleId);
            if (numberBottlesInTheCave > 0) {
                CaveLightModelV1 caveLight = new CaveLightModelV1();
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
            CaveModelV1 cave = allCavesMap.get(caveId);
            return cave.getNumberBottles(bottleId) > 0;
        }
        return false;
    }
}
