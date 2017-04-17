package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.IPatternsStorageManagerV1;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ISharedPreferencesManagerV1;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v1.PatternModelV1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class PatternsSharedPreferencesManagerV1 implements IPatternsStorageManagerV1 {

    public static PatternsSharedPreferencesManagerV1 Instance;
    private static boolean isInitialized;
    private static Map<Integer, PatternModelV1> allPatternsMap = new HashMap<>();
    private String keyIndex;
    private String filename;
    private int keyPatternResourceId = R.string.store_pattern;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManagerV1 sharedPreferencesManager = null;

    private PatternsSharedPreferencesManagerV1(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_patterns);

        loadAllPatterns(context);
    }

    public static Map<Integer, PatternModelV1> getAllPatterns(Context context) {
        init(context);
        return allPatternsMap;
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new PatternsSharedPreferencesManagerV1(context);
        isInitialized = true;
    }

    private ISharedPreferencesManagerV1 getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManagerV1.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllPatterns(Context context) {
        Map<Integer, IStorableModel> allPatternsAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyPatternResourceId, PatternModelV1.class);

        if (allPatternsAsStorableModel == null) {
            return;
        }
        for (Map.Entry<Integer, IStorableModel> patternAsStorableModelEntry : allPatternsAsStorableModel.entrySet()) {
            IStorableModel patternAsStorableModel = patternAsStorableModelEntry.getValue();
            if (patternAsStorableModel instanceof PatternModelV1) {
                allPatternsMap.put(patternAsStorableModelEntry.getKey(), (PatternModelV1) patternAsStorableModel);
            }
        }
    }

    public List<PatternModelV1> getPatterns() {
        List<PatternModelV1> patterns = new ArrayList<>(allPatternsMap.values());
        Collections.sort(patterns);
        return patterns;
    }

    public PatternModelV1 getPattern(int patternId) {
        return CollectionsHelper.getValueOrDefault(allPatternsMap, patternId, null);
    }

    public int insertPattern(Context context, PatternModelV1 pattern) {
        List<Integer> ids = new ArrayList<>(allPatternsMap.keySet());
        pattern.Id = StorageHelper.getNewId(ids);
        allPatternsMap.put(pattern.Id, pattern);
        ids.add(pattern.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(context.getString(keyPatternResourceId, pattern.Id), pattern);

        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
        return pattern.Id;
    }

    public void updatePattern(Context context, PatternModelV1 pattern) {
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

    public void updateAllPatterns(Context context, List<PatternModelV1> patterns) {
        Map<String, Object> dataToStoreMap = new HashMap<>(patterns.size());
        for (PatternModelV1 pattern : patterns) {
            allPatternsMap.put(pattern.Id, pattern);
            dataToStoreMap.put(context.getString(keyPatternResourceId, pattern.Id), pattern);
        }
        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
    }

    public int getExistingPatternId(PatternModelV1 pattern) {
        for (PatternModelV1 existingPattern : allPatternsMap.values()) {
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
