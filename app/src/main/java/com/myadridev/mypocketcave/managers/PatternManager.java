package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.managers.storage.SQLite.PatternSQLiteManager;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

public class PatternManager {

    public static final int numberOfColumnsForDisplay = 3;

    public static List<PatternModel> getPatterns() {
        return PatternSQLiteManager.getPatterns();
    }

    public static PatternModel getPattern(int patternId) {
        return PatternSQLiteManager.getPattern(patternId);
    }

    public static int addPattern(PatternModel pattern) {
        return PatternSQLiteManager.insertPattern(pattern);
    }

    public static void setLastUsedPattern(int patternId) {
        List<PatternModel> patterns = getPatterns();
        for (PatternModel pattern : patterns) {
            if (pattern.Id == patternId) {
                pattern.Order = 1;
            } else {
                pattern.Order = pattern.Order + 1;
            }
            PatternSQLiteManager.updatePatternOrder(pattern.Id, pattern.Order);
        }
    }
}
