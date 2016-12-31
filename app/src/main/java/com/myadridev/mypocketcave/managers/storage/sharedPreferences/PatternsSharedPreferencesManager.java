package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.IStorableModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternsSharedPreferencesManager implements IPatternsStorageManager {

    public static PatternsSharedPreferencesManager Instance;
    private static boolean _isInitialized;
    private Map<Integer, PatternModel> allPatternsMap;
    private String keyIndex;
    private String filename;
    private int keyPatternResourceId = R.string.store_pattern;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private PatternsSharedPreferencesManager() {
        keyIndex = getSharedPreferencesManager().getStringFromResource(R.string.store_indexes);
        filename = getSharedPreferencesManager().getStringFromResource(R.string.filename_patterns);

        loadAllPatterns();
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init() {
        Instance = new PatternsSharedPreferencesManager();
        _isInitialized = true;
    }

    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllPatterns() {
        Map<Integer, IStorableModel> allPatternsAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(filename, keyIndex, keyPatternResourceId, PatternModel.class);

        if (allPatternsAsStorableModel == null) {
            allPatternsMap = new HashMap<>();
            return;
        }
        allPatternsMap = new HashMap<>(allPatternsAsStorableModel.size());
        for (Map.Entry<Integer, IStorableModel> patternAsStorableModelEntry : allPatternsAsStorableModel.entrySet()) {
            IStorableModel patternAsStorableModel = patternAsStorableModelEntry.getValue();
            if (patternAsStorableModel instanceof PatternModel) {
                allPatternsMap.put(patternAsStorableModelEntry.getKey(), (PatternModel) patternAsStorableModel);
            }
        }
    }

    @Override
    public List<PatternModel> getPatterns() {
        List<PatternModel> patterns = new ArrayList<>(allPatternsMap.values());
        Collections.sort(patterns);
        return patterns;
    }

    @Override
    public PatternModel getPattern(int patternId) {
        return CollectionsHelper.getValueOrDefault(allPatternsMap, patternId, null);
    }

    @Override
    public int insertPattern(PatternModel pattern) {
        ArrayList<Integer> ids = new ArrayList<>(allPatternsMap.keySet());
        pattern.Id = StorageHelper.getNewId(ids);
        allPatternsMap.put(pattern.Id, pattern);
        ids.add(pattern.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(getSharedPreferencesManager().getStringFromResource(keyPatternResourceId, pattern.Id), pattern);

        getSharedPreferencesManager().storeStringMapData(filename, dataToStoreMap);
        return pattern.Id;
    }

    @Override
    public void updateAllPatterns(List<PatternModel> patterns) {
        Map<String, Object> dataToStoreMap = new HashMap<>(patterns.size());
        for (PatternModel pattern : patterns) {
            allPatternsMap.put(pattern.Id, pattern);
            dataToStoreMap.put(getSharedPreferencesManager().getStringFromResource(keyPatternResourceId, pattern.Id), pattern);
        }
        getSharedPreferencesManager().storeStringMapData(filename, dataToStoreMap);
    }

    @Override
    public int getExistingPatternId(PatternModel pattern) {
        for (PatternModel existingPattern : allPatternsMap.values()) {
            if (pattern.Id != existingPattern.Id && pattern.hasSameValues(existingPattern)) {
                return existingPattern.Id;
            }
        }
        return -1;
    }
}
