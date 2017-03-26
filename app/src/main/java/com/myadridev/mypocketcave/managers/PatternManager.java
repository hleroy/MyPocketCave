package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

public class PatternManager {

    public static final int numberOfColumnsForDisplay = 3;
    private static boolean listenerPatternsRegistered = false;
    private static IPatternsStorageManager patternsStorageManager = null;

    private static IPatternsStorageManager getPatternsStorageManager() {
        if (patternsStorageManager == null) {
            patternsStorageManager = DependencyManager.getSingleton(IPatternsStorageManager.class,
                    listenerPatternsRegistered ? null : (OnDependencyChangeListener) () -> patternsStorageManager = null);
            listenerPatternsRegistered = true;
        }
        return patternsStorageManager;
    }

    public static List<PatternModel> getPatterns() {
        return getPatternsStorageManager().getPatterns();
    }

    public static PatternModel getPattern(int patternId) {
        return getPatternsStorageManager().getPattern(patternId);
    }

    public static int addPattern(Context context, PatternModel pattern) {
        int existingPatternId = getPatternsStorageManager().getExistingPatternId(pattern);
        if (existingPatternId == -1) {
            pattern.Order = 0;
            pattern.Id = getPatternsStorageManager().insertPattern(context, pattern);
            return pattern.Id;
        } else {
            return existingPatternId;
        }
    }

    public static void addPatterns(Context context, List<PatternModel> patterns) {
        for (PatternModel pattern : patterns) {
            // we want to keep the ids of the patterns
            editPattern(context, pattern);
        }
        updateIndexes(context);
    }

    private static void updateIndexes(Context context) {
        getPatternsStorageManager().updateIndexes(context);
    }

    private static void editPattern(Context context, PatternModel pattern) {
        getPatternsStorageManager().updatePattern(context, pattern);
    }

    public static void setLastUsedPattern(Context context, int patternId) {
        PatternModel existingPattern = getPattern(patternId);
        int order = existingPattern.Order;
        List<PatternModel> patterns = getPatterns();
        for (PatternModel pattern : patterns) {
            if (pattern.Id == patternId) {
                pattern.Order = 0;
            } else if (pattern.Order <= order) {
                pattern.Order = pattern.Order + 1;
            }
        }
        getPatternsStorageManager().updateAllPatterns(context, patterns);
    }

    private static void removePattern(Context context, PatternModel pattern) {
        getPatternsStorageManager().deletePattern(context, pattern.Id);
    }

    public static void removeAllPatterns(Context context) {
        for (PatternModel pattern : getPatterns()) {
            removePattern(context, pattern);
        }
    }
}
