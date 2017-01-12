package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICavesStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.CaveLightModel;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CavesSharedPreferencesManager implements ICavesStorageManager {

    public static CavesSharedPreferencesManager Instance;
    private static boolean isInitialized;
    private Map<Integer, CaveLightModel> allCavesMap;
    private String keyIndex;
    private String filename;
    private int keyCaveResourceId = R.string.store_cave;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private CavesSharedPreferencesManager(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_caves);

        loadAllCaves(context);
    }

    public static boolean IsInitialized() {
        return isInitialized;
    }

    public static void Init(Context context) {
        Instance = new CavesSharedPreferencesManager(context);
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
        Map<Integer, IStorableModel> allCavesAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyCaveResourceId, CaveLightModel.class);

        if (allCavesAsStorableModel == null) {
            allCavesMap = new HashMap<>();
            return;
        }
        allCavesMap = new HashMap<>(allCavesAsStorableModel.size());
        for (Map.Entry<Integer, IStorableModel> caveAsStorableModelEntry : allCavesAsStorableModel.entrySet()) {
            IStorableModel caveAsStorableModel = caveAsStorableModelEntry.getValue();
            if (caveAsStorableModel instanceof CaveLightModel) {
                allCavesMap.put(caveAsStorableModelEntry.getKey(), (CaveLightModel) caveAsStorableModel);
            }
        }
    }

    @Override
    public List<CaveLightModel> getCaves() {
        ArrayList<CaveLightModel> caves = new ArrayList<>(allCavesMap.values());
        Collections.sort(caves);
        return caves;
    }

    @Override
    public int insertCave(Context context, CaveLightModel cave, boolean needsNewId) {
        ArrayList<Integer> ids = new ArrayList<>(allCavesMap.keySet());
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

    @Override
    public void updateCave(Context context, CaveLightModel cave) {
        allCavesMap.put(cave.Id, cave);
        getSharedPreferencesManager().storeStringData(context, filename, context.getString(keyCaveResourceId, cave.Id), cave);
    }

    @Override
    public void deleteCave(Context context, int caveId) {
        allCavesMap.remove(caveId);
        ArrayList<Integer> ids = new ArrayList<>(allCavesMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
        getSharedPreferencesManager().removeData(context, filename, context.getString(keyCaveResourceId, caveId));
    }

    @Override
    public int getExistingCaveId(int id, String name, int caveTypeId) {
        for (CaveLightModel cave : allCavesMap.values()) {
            if (name.equals(cave.Name)
                    && caveTypeId == cave.CaveType.Id
                    && id != cave.Id) {
                return cave.Id;
            }
        }
        return 0;
    }

    @Override
    public int getCavesCount() {
        return allCavesMap.size();
    }

    @Override
    public int getCavesCount(int caveTypeId) {
        int cavesCount = 0;

        for (CaveLightModel cave : allCavesMap.values()) {
            if (cave.CaveType.Id == caveTypeId) {
                cavesCount++;
            }
        }

        return cavesCount;
    }
}
