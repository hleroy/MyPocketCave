package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

public class PatternManager {

    private static boolean listenerPatternsRegistered = false;
    private static IPatternsStorageManager patternsStorageManager = null;
    private static IPatternsStorageManager getPatternsStorageManager() {
        if (patternsStorageManager == null) {
            patternsStorageManager = DependencyManager.getSingleton(IPatternsStorageManager.class,
                    listenerPatternsRegistered ? null : new OnDependencyChangeListener() {
                @Override
                public void onDependencyChange() {
                    patternsStorageManager = null;
                }
            });
            listenerPatternsRegistered = true;
        }
        return patternsStorageManager;
    }

    public static final int numberOfColumnsForDisplay = 3;

    public static List<PatternModel> getPatterns() {
        return getPatternsStorageManager().getPatterns();
    }

    public static PatternModel getPattern(int patternId) {
        return getPatternsStorageManager().getPattern(patternId);
    }

    public static int addPattern(PatternModel pattern) {
        int existingPatternId = getPatternsStorageManager().getExistingPatternId(pattern);
        if (existingPatternId == -1) {
            pattern.Id = getPatternsStorageManager().insertPattern(pattern);
            return pattern.Id;
        } else {
            return existingPatternId;
        }
    }

    public static void setLastUsedPattern(int patternId) {
        PatternModel existingPattern = getPattern(patternId);
        int order = existingPattern.Order;
        List<PatternModel> patterns = getPatterns();
        for (PatternModel pattern : patterns) {
            if (pattern.Id == patternId) {
                pattern.Order = 1;
            } else if (pattern.Order < order) {
                pattern.Order = pattern.Order + 1;
            }
        }
        getPatternsStorageManager().updateAllPatterns(patterns);
    }
}
