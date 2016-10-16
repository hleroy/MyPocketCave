package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ICaveStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.HashMap;
import java.util.Map;

public class CaveSharedPreferencesManager implements ICaveStorageManager {

    public static CaveSharedPreferencesManager Instance;
    private static boolean _isInitialized;
    private final Map<Integer, CaveModel> allCavesMap;
    private int filenameResourceId = R.string.store_cave;
    private int keyCaveResourceId = R.string.store_cave_key;

    private CaveSharedPreferencesManager() {
        allCavesMap = new HashMap<>();
    }

    public static void Init() {
        Instance = new CaveSharedPreferencesManager();
        _isInitialized = true;
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;
    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : new OnDependencyChangeListener() {
                @Override
                public void onDependencyChange() {
                    sharedPreferencesManager = null;
                }
            });
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    @Override
    public CaveModel getCave(int caveId) {
        if (!allCavesMap.containsKey(caveId)) {
            IStorableModel caveAsStorableModel = getSharedPreferencesManager().loadStoredData(getSharedPreferencesManager().getStringFromResource(filenameResourceId, caveId),
                    keyCaveResourceId, CaveModel.class);

            if (caveAsStorableModel != null && caveAsStorableModel instanceof CaveModel) {
                CaveModel cave = (CaveModel) caveAsStorableModel;
                allCavesMap.put(cave.Id, cave);
            }
        }
        return CollectionsHelper.getValueOrDefault(allCavesMap, caveId, null);
    }

    @Override
    public void insertCave(CaveModel cave) {
        insertOrUpdateCave(cave);
    }

    private void insertOrUpdateCave(CaveModel cave) {
        allCavesMap.put(cave.Id, cave);
        getSharedPreferencesManager().storeStringData(getSharedPreferencesManager().getStringFromResource(filenameResourceId, cave.Id),
                getSharedPreferencesManager().getStringFromResource(keyCaveResourceId), cave);
    }

    @Override
    public void updateCave(CaveModel cave) {
        insertOrUpdateCave(cave);
    }

    @Override
    public void deleteCave(CaveModel cave) {
        getSharedPreferencesManager().delete(getSharedPreferencesManager().getStringFromResource(filenameResourceId, cave.Id));
    }
}
