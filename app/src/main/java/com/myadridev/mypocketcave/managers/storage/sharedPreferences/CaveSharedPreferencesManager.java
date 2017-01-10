package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.content.Context;

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
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

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

    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    @Override
    public CaveModel getCave(Context context, int caveId) {
        if (!allCavesMap.containsKey(caveId)) {
            IStorableModel caveAsStorableModel = getSharedPreferencesManager().loadStoredData(context, context.getString(filenameResourceId, caveId),
                    keyCaveResourceId, CaveModel.class);

            if (caveAsStorableModel != null && caveAsStorableModel instanceof CaveModel) {
                CaveModel cave = (CaveModel) caveAsStorableModel;
                allCavesMap.put(cave.Id, cave);
            }
        }
        return CollectionsHelper.getValueOrDefault(allCavesMap, caveId, null);
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
}
