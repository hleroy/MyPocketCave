package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CavesSharedPreferencesManagerV2 implements ICavesStorageManagerV2 {

    public static CavesSharedPreferencesManagerV2 Instance;
    private static boolean isInitialized;
    private final Map<Integer, CaveLightModelV2> allCavesMap = new HashMap<>();
    private String keyIndex;
    private String filename;
    private int keyCaveResourceId = R.string.store_cave;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private CavesSharedPreferencesManagerV2(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_caves);

        loadAllCaves(context);
    }

    private CavesSharedPreferencesManagerV2(Context context, Map<Integer, CaveLightModelV2> allCavesMap) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_caves);

        for (CaveLightModelV2 cave : allCavesMap.values()) {
            insertCave(context, cave, false);
        }
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new CavesSharedPreferencesManagerV2(context);
        isInitialized = true;
    }

    public static void init(Context context, Map<Integer, CaveLightModelV2> allCavesMap) {
        if (isInitialized) return;
        Instance = new CavesSharedPreferencesManagerV2(context, allCavesMap);
        isInitialized = true;
    }

    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllCaves(Context context) {
        Map<Integer, IStorableModel> allCavesAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyCaveResourceId, CaveLightModelV2.class);

        if (allCavesAsStorableModel == null) {
            return;
        }
        for (Map.Entry<Integer, IStorableModel> caveAsStorableModelEntry : allCavesAsStorableModel.entrySet()) {
            IStorableModel caveAsStorableModel = caveAsStorableModelEntry.getValue();
            if (caveAsStorableModel instanceof CaveLightModelV2) {
                allCavesMap.put(caveAsStorableModelEntry.getKey(), (CaveLightModelV2) caveAsStorableModel);
            }
        }
    }

    public List<CaveLightModelV2> getLightCaves() {
        List<CaveLightModelV2> caves = new ArrayList<>(allCavesMap.values());
        Collections.sort(caves);
        return caves;
    }

    public int insertCave(Context context, CaveLightModelV2 cave, boolean needsNewId) {
        List<Integer> ids = new ArrayList<>(allCavesMap.keySet());
        if (needsNewId) {
            cave.Id = StorageHelper.getNewId(ids);
        }
        allCavesMap.put(cave.Id, cave);
        ids.add(cave.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(context.getString(keyCaveResourceId, cave.Id), cave);

        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
        return cave.Id;
    }

    public void updateCave(Context context, CaveLightModelV2 cave) {
        allCavesMap.put(cave.Id, cave);
        getSharedPreferencesManager().storeStringData(context, filename, context.getString(keyCaveResourceId, cave.Id), cave);
    }

    public void updateIndexes(Context context) {
        List<Integer> ids = new ArrayList<>(allCavesMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
    }

    public void deleteCave(Context context, int caveId) {
        allCavesMap.remove(caveId);
        List<Integer> ids = new ArrayList<>(allCavesMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
        getSharedPreferencesManager().removeData(context, filename, context.getString(keyCaveResourceId, caveId));
    }

    public int getExistingCaveId(int id, String name, int caveTypeId) {
        for (CaveLightModelV2 cave : allCavesMap.values()) {
            if (name.equals(cave.Name)
                    && caveTypeId == cave.CaveType.Id
                    && id != cave.Id) {
                return cave.Id;
            }
        }
        return 0;
    }

    public int getCavesCount() {
        return allCavesMap.size();
    }

    public int getCavesCount(int caveTypeId) {
        int cavesCount = 0;

        for (CaveLightModelV2 cave : allCavesMap.values()) {
            if (cave.CaveType.Id == caveTypeId) {
                cavesCount++;
            }
        }

        return cavesCount;
    }
}
