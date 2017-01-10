package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.content.Context;

import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

public interface IPatternsStorageManager {

    List<PatternModel> getPatterns();

    PatternModel getPattern(int patternId);

    int insertPattern(Context context, PatternModel pattern);

    void updateAllPatterns(Context context, List<PatternModel> patterns);

    int getExistingPatternId(PatternModel pattern);
}
