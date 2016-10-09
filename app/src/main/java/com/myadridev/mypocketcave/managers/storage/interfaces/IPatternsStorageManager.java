package com.myadridev.mypocketcave.managers.storage.interfaces;

import com.myadridev.mypocketcave.models.PatternModel;

import java.util.List;

/**
 * Created by adrien on 01/10/2016.
 */

public interface IPatternsStorageManager {

    List<PatternModel> getPatterns();

    PatternModel getPattern(int patternId);

    int insertPattern(PatternModel pattern);

    void updateAllPatterns(List<PatternModel> patterns);

    int getExistingPatternId(PatternModel pattern);
}
