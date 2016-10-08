package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

public class PatternManager {

    private static IPatternsStorageManager patternsStorageManager = null;

    private static IPatternsStorageManager getPatternsStorageManager() {
        if (patternsStorageManager == null) {
            patternsStorageManager = DependencyManager.getSingleton(IPatternsStorageManager.class);
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
        return getPatternsStorageManager().insertPattern(pattern);
    }

    public static void setLastUsedPattern(int patternId) {
        List<PatternModel> patterns = getPatterns();
        for (PatternModel pattern : patterns) {
            if (pattern.Id == patternId) {
                pattern.Order = 1;
            } else {
                pattern.Order = pattern.Order + 1;
            }
        }
        getPatternsStorageManager().updateAllPatterns(patterns);
    }
}
