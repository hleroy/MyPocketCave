package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.v1.PatternModel;

import java.util.List;

public interface IPatternsStorageManager {

    List<PatternModel> getPatterns();

    PatternModel getPattern(int patternId);

    int insertPattern(Context context, PatternModel pattern);

    void updateAllPatterns(Context context, List<PatternModel> patterns);

    int getExistingPatternId(PatternModel pattern);

    void updatePattern(Context context, PatternModel pattern);

    void deletePattern(Context context, int patternId);

    void updateIndexes(Context context);
}