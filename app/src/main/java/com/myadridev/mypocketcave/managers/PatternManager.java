package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IPatternsStorageManagerV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import java.util.List;

public class PatternManager {

    private static boolean listenerPatternsRegistered = false;
    private static IPatternsStorageManagerV2 patternsStorageManager = null;

    private static IPatternsStorageManagerV2 getPatternsStorageManager() {
        if (patternsStorageManager == null) {
            patternsStorageManager = DependencyManager.getSingleton(IPatternsStorageManagerV2.class,
                    listenerPatternsRegistered ? null : (OnDependencyChangeListener) () -> patternsStorageManager = null);
            listenerPatternsRegistered = true;
        }
        return patternsStorageManager;
    }

    public static List<PatternModelV2> getPatterns() {
        return getPatternsStorageManager().getPatterns();
    }

    public static PatternModelV2 getPattern(int patternId) {
        return getPatternsStorageManager().getPattern(patternId);
    }

    public static int addPattern(Context context, PatternModelV2 pattern) {
        int existingPatternId = getPatternsStorageManager().getExistingPatternId(pattern);
        if (existingPatternId == -1) {
            pattern.Order = 0;
            pattern.Id = getPatternsStorageManager().insertPattern(context, pattern, true);
            return pattern.Id;
        } else {
            return existingPatternId;
        }
    }

    public static void addPatterns(Context context, List<PatternModelV2> patterns) {
        for (PatternModelV2 pattern : patterns) {
            // we want to keep the ids of the patterns
            editPattern(context, pattern);
        }
        updateIndexes(context);
    }

    private static void updateIndexes(Context context) {
        getPatternsStorageManager().updateIndexes(context);
    }

    private static void editPattern(Context context, PatternModelV2 pattern) {
        getPatternsStorageManager().updatePattern(context, pattern);
    }

    public static void setLastUsedPattern(Context context, int patternId) {
        PatternModelV2 existingPattern = getPattern(patternId);
        int order = existingPattern.Order;
        List<PatternModelV2> patterns = getPatterns();
        for (PatternModelV2 pattern : patterns) {
            if (pattern.Id == patternId) {
                pattern.Order = 0;
            } else if (pattern.Order <= order) {
                pattern.Order = pattern.Order + 1;
            }
        }
        getPatternsStorageManager().updateAllPatterns(context, patterns);
    }

    private static void removePattern(Context context, PatternModelV2 pattern) {
        getPatternsStorageManager().deletePattern(context, pattern.Id);
    }

    public static void removeAllPatterns(Context context) {
        for (PatternModelV2 pattern : getPatterns()) {
            removePattern(context, pattern);
        }
    }
}
