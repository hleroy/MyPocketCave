package com.myadridev.mypocketcave.managers.storage.interfaces.v2;

import android.content.Context;

import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import java.util.List;

public interface IPatternsStorageManagerV2 {

    List<PatternModelV2> getPatterns();

    PatternModelV2 getPattern(int patternId);

    int insertPattern(Context context, PatternModelV2 pattern, boolean needsNewId);

    void updateAllPatterns(Context context, List<PatternModelV2> patterns);

    int getExistingPatternId(PatternModelV2 pattern);

    void updatePattern(Context context, PatternModelV2 pattern);

    void deletePattern(Context context, int patternId);

    void updateIndexes(Context context);
}
