package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.content.Context;
import android.support.annotation.Nullable;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICavesStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.CaveLightModel;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaveSharedPreferencesManager implements ICaveStorageManager {

    public static CaveSharedPreferencesManager Instance;
    private static boolean _isInitialized;
    private final Map<Integer, CaveModel> allCavesMap;
    private int filenameResourceId = R.string.store_cave;
    private int keyCaveResourceId = R.string.store_cave_key;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;
    private boolean listenerCavesStorageRegistered = false;
    private ICavesStorageManager cavesStorageManager = null;

    private CaveSharedPreferencesManager(Context context) {
        allCavesMap = new HashMap<>();

        loadAllCaves(context);
    }

    public static void Init(Context context) {
        Instance = new CaveSharedPreferencesManager(context);
        _isInitialized = true;
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    private ICavesStorageManager getCavesStorageManager() {
        if (cavesStorageManager == null) {
            cavesStorageManager = DependencyManager.getSingleton(ICavesStorageManager.class,
                    listenerCavesStorageRegistered ? null : (OnDependencyChangeListener) () -> cavesStorageManager = null);
            listenerCavesStorageRegistered = true;
        }
        return cavesStorageManager;
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
        List<CaveLightModel> cavesLight = getCavesStorageManager().getCaves();
        for (CaveLightModel caveLight : cavesLight) {
            CaveModel cave = loadCave(context, caveLight.Id);
            if (cave != null) {
                allCavesMap.put(caveLight.Id, cave);
            }
        }
    }

    @Override
    public CaveModel getCave(Context context, int caveId) {
        return CollectionsHelper.getValueOrDefault(allCavesMap, caveId, null);
    }

    @Nullable
    private CaveModel loadCave(Context context, int caveId) {
        IStorableModel caveAsStorableModel = getSharedPreferencesManager().loadStoredData(context, context.getString(filenameResourceId, caveId),
                keyCaveResourceId, CaveModel.class);

        if (caveAsStorableModel != null && caveAsStorableModel instanceof CaveModel) {
            return (CaveModel) caveAsStorableModel;
        }
        return null;
    }

    @Override
    public void insertOrUpdateCave(Context context, CaveModel cave) {
        allCavesMap.put(cave.Id, cave);
        getSharedPreferencesManager().storeStringData(context, context.getString(filenameResourceId, cave.Id),
                context.getString(keyCaveResourceId), cave);
    }

    @Override
    public void deleteCave(Context context, CaveModel cave) {
        getSharedPreferencesManager().delete(context, context.getString(filenameResourceId, cave.Id));
    }

    @Override
    public List<CaveLightModel> getCavesWithBottle(int bottleId) {
        List<CaveLightModel> cavesWithBottle = new ArrayList<>(allCavesMap.size());

        for (CaveModel cave : allCavesMap.values()) {
            int numberBottlesInTheCave = cave.getNumberBottles(bottleId);
            if (numberBottlesInTheCave > 0) {
                CaveLightModel caveLight = new CaveLightModel();
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

    @Override
    public boolean isBottleInTheCave(int bottleId, int caveId) {
        if (allCavesMap.containsKey(caveId)) {
            CaveModel cave = allCavesMap.get(caveId);
            return cave.getNumberBottles(bottleId) > 0;
        }
        return false;
    }
}
