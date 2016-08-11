package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.models.IStorableModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternManager {

    public static PatternManager Instance;
    private static int _maxPatternId;
    private static boolean _isInitialized;
    private final Map<Integer, PatternModel> patterns = new HashMap<>();
    private final int storeFileResourceId = R.string.filename_patterns;
    private final int storeSetResourceId = R.string.store_patterns;

    public static final int maxNumberOfPatterns = 14;
    public static final int numberOfColumnsForDisplay = 3;

    public static int MaxPatternId() {
        return _maxPatternId;
    }

    private PatternManager(boolean withStorage) {
        _maxPatternId = 0;
        if (withStorage)
            initializePatterns();
    }

    private PatternManager() {
        _maxPatternId = 0;
        initializePatterns();
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void InitWithoutStorage() {
        Instance = new PatternManager(false);
        _isInitialized = true;
    }

    public static void Init() {
        Instance = new PatternManager();
        _isInitialized = true;
    }

    private void initializePatterns() {
        final Map<Integer, IStorableModel> patternsAsIStorableModel = new HashMap<>();
        _maxPatternId = StorageManager.Instance.loadStoredData(storeFileResourceId, storeSetResourceId, PatternModel.class, patternsAsIStorableModel);

        patterns.clear();
        for (Map.Entry<Integer, IStorableModel> storableModelEntry : patternsAsIStorableModel.entrySet()) {
            IStorableModel storableModel = storableModelEntry.getValue();
            if (storableModel instanceof PatternModel) {
                patterns.put(storableModelEntry.getKey(), (PatternModel) storableModel);
            }
        }
    }

    public List<PatternModel> getSortedPatterns() {
        List<PatternModel> allPatterns = new ArrayList<>(patterns.values());
        Collections.sort(allPatterns);
        return allPatterns;
    }

    public PatternModel getPattern(int id) {
        return patterns.containsKey(id) ? patterns.get(id) : null;
    }

    public int addPatternNoSave(PatternModel pattern) {
        return addPatternInner(pattern);
    }

    public int addPattern(PatternModel pattern) {
        int patternId = addPatternInner(pattern);
        savePatterns();
        return patternId;
    }

    private int addPatternInner(PatternModel pattern) {
        int existingPatternId = getExistingPatternId(pattern);
        boolean isExistingPattern = existingPatternId != -1;
        if (patterns.size() >= maxNumberOfPatterns) {
            removeOldUsedPatterns(isExistingPattern);
        }
        if (isExistingPattern) {
            PatternModel existingPattern = getPattern(existingPatternId);
            int oldOrder = existingPattern.Order;
            updatePatternOrders(oldOrder);
            existingPattern.Order = 1;
            return existingPattern.Id;
        } else {
            updatePatternOrders();
            _maxPatternId++;
            pattern.Id = _maxPatternId;
            pattern.Order = 1;
            patterns.put(_maxPatternId, pattern);
            return _maxPatternId;
        }
    }

    private int getExistingPatternId(PatternModel pattern) {
        for (PatternModel existingPattern : patterns.values()) {
            if (pattern.Id != existingPattern.Id && pattern.hasSameValues(existingPattern)) {
                return existingPattern.Id;
            }
        }
        return -1;
    }

    private void updatePatternOrders() {
        updatePatternOrders(maxNumberOfPatterns);
    }

    private void updatePatternOrders(int minOrder) {
        for (PatternModel pattern : patterns.values()) {
            if (pattern.Order < minOrder) {
                pattern.Order = pattern.Order + 1;
            }
        }
    }

    private void removeOldUsedPatterns(boolean isExistingPattern) {
        List<PatternModel> allPatterns = getSortedPatterns();
        int numberOfPatternsKept = 0;
        int maxPatternsToKeep = isExistingPattern ? maxNumberOfPatterns : maxNumberOfPatterns - 1;
        for (PatternModel pattern : allPatterns) {
            if (numberOfPatternsKept < maxPatternsToKeep) {
                numberOfPatternsKept++;
            } else {
                patterns.remove(pattern.Id);
            }
        }
        setMaxPatternId();
    }

    private void setMaxPatternId() {
        int maxId = 0;
        for (int id : patterns.keySet()) {
            if (id > maxId)
                maxId = id;
        }
        _maxPatternId = maxId;
    }

    private void savePatterns() {
        StorageManager.Instance.storeData(storeFileResourceId, storeSetResourceId, patterns);
    }

    public void setLastUsedPattern(int patternId) {
        PatternModel pattern = getPattern(patternId);
        int oldOrder = pattern.Order;
        updatePatternOrders(oldOrder);
        pattern.Order = 1;
        savePatterns();
    }
}
