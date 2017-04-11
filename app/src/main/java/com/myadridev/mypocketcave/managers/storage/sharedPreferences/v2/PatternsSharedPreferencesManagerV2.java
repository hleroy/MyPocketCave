package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IPatternsStorageManagerV2;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternsSharedPreferencesManagerV2 implements IPatternsStorageManagerV2 {

    public static PatternsSharedPreferencesManagerV2 Instance;
    private static boolean isInitialized;
    private final Map<Integer, PatternModelV2> allPatternsMap = new HashMap<>();;
    private String keyIndex;
    private String filename;
    private int keyPatternResourceId = R.string.store_pattern;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private PatternsSharedPreferencesManagerV2(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_patterns);

        loadAllPatterns(context);
    }

    private PatternsSharedPreferencesManagerV2(Context context, Map<Integer, PatternModelV2> allPatternsMap) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_patterns);

        for (PatternModelV2 pattern : allPatternsMap.values()) {
            insertPattern(context, pattern, false);
        }
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new PatternsSharedPreferencesManagerV2(context);
        isInitialized = true;
    }

    public static void init(Context context, Map<Integer, PatternModelV2> allPatternsMap) {
        if (isInitialized) return;
        Instance = new PatternsSharedPreferencesManagerV2(context, allPatternsMap);
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

    private void loadAllPatterns(Context context) {
        Map<Integer, IStorableModel> allPatternsAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyPatternResourceId, PatternModelV2.class);

        if (allPatternsAsStorableModel == null) {
            return;
        }
        for (Map.Entry<Integer, IStorableModel> patternAsStorableModelEntry : allPatternsAsStorableModel.entrySet()) {
            IStorableModel patternAsStorableModel = patternAsStorableModelEntry.getValue();
            if (patternAsStorableModel instanceof PatternModelV2) {
                allPatternsMap.put(patternAsStorableModelEntry.getKey(), (PatternModelV2) patternAsStorableModel);
            }
        }
    }

    public List<PatternModelV2> getPatterns() {
        List<PatternModelV2> patterns = new ArrayList<>(allPatternsMap.values());
        Collections.sort(patterns);
        return patterns;
    }

    public PatternModelV2 getPattern(int patternId) {
        return CollectionsHelper.getValueOrDefault(allPatternsMap, patternId, null);
    }

    public int insertPattern(Context context, PatternModelV2 pattern, boolean needsNewId) {
        List<Integer> ids = new ArrayList<>(allPatternsMap.keySet());
        if (needsNewId) {
            pattern.Id = StorageHelper.getNewId(ids);
        }
        allPatternsMap.put(pattern.Id, pattern);
        ids.add(pattern.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(context.getString(keyPatternResourceId, pattern.Id), pattern);

        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
        return pattern.Id;
    }

    public void updatePattern(Context context, PatternModelV2 pattern) {
        List<Integer> ids = new ArrayList<>(allPatternsMap.keySet());
        allPatternsMap.put(pattern.Id, pattern);
        ids.add(pattern.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(context.getString(keyPatternResourceId, pattern.Id), pattern);

        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
    }

    public void updateIndexes(Context context) {
        List<Integer> ids = new ArrayList<>(allPatternsMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
    }

    public void updateAllPatterns(Context context, List<PatternModelV2> patterns) {
        Map<String, Object> dataToStoreMap = new HashMap<>(patterns.size());
        for (PatternModelV2 pattern : patterns) {
            allPatternsMap.put(pattern.Id, pattern);
            dataToStoreMap.put(context.getString(keyPatternResourceId, pattern.Id), pattern);
        }
        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
    }

    public int getExistingPatternId(PatternModelV2 pattern) {
        for (PatternModelV2 existingPattern : allPatternsMap.values()) {
            if (pattern.Id != existingPattern.Id && pattern.hasSameValues(existingPattern)) {
                return existingPattern.Id;
            }
        }
        return -1;
    }

    public void deletePattern(Context context, int patternId) {
        allPatternsMap.remove(patternId);
        ArrayList<Integer> ids = new ArrayList<>(allPatternsMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
        getSharedPreferencesManager().removeData(context, filename, context.getString(keyPatternResourceId, patternId));
    }
}
