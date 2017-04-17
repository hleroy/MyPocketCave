package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.v1.PatternModelV1;

import java.util.List;

@Deprecated
public interface IPatternsStorageManagerV1 {

    List<PatternModelV1> getPatterns();

    PatternModelV1 getPattern(int patternId);

    int insertPattern(Context context, PatternModelV1 pattern);

    void updateAllPatterns(Context context, List<PatternModelV1> patterns);

    int getExistingPatternId(PatternModelV1 pattern);

    void updatePattern(Context context, PatternModelV1 pattern);

    void deletePattern(Context context, int patternId);

    void updateIndexes(Context context);
}
