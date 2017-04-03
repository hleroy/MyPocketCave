package com.myadridev.mypocketcave.models.migration.from;

import android.content.Context;

import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.BottlesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.CaveSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.CavesSharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.PatternsSharedPreferencesManager;
import com.myadridev.mypocketcave.models.IBottleModel;
import com.myadridev.mypocketcave.models.ICaveLightModel;
import com.myadridev.mypocketcave.models.ICaveModel;
import com.myadridev.mypocketcave.models.IPatternModel;
import com.myadridev.mypocketcave.models.v1.BottleModel;
import com.myadridev.mypocketcave.models.v1.CaveLightModel;
import com.myadridev.mypocketcave.models.v1.CaveModel;
import com.myadridev.mypocketcave.models.v1.PatternModel;

import java.util.HashMap;
import java.util.Map;

public class MigrationFromV1Manager implements IMigrationFromManager {

    public Map<Integer, IBottleModel> loadBottles(Context context) {
        Map<Integer, BottleModel> allBottles = BottlesSharedPreferencesManager.getAllBottles(context);
        Map<Integer, IBottleModel> res = new HashMap<>(allBottles.size());
        for (Map.Entry<Integer, BottleModel> bottleEntry : allBottles.entrySet()) {
            res.put(bottleEntry.getKey(), bottleEntry.getValue());
        }
        return res;
    }

    public Map<Integer, ICaveLightModel> loadCaves(Context context) {
        Map<Integer, CaveLightModel> allCaves = CavesSharedPreferencesManager.getAllCaves(context);
        Map<Integer, ICaveLightModel> res = new HashMap<>(allCaves.size());
        for (Map.Entry<Integer, CaveLightModel> caveEntry : allCaves.entrySet()) {
            res.put(caveEntry.getKey(), caveEntry.getValue());
        }
        return res;
    }

    public Map<Integer, IPatternModel> loadPatterns(Context context) {
        Map<Integer, PatternModel> allPatterns = PatternsSharedPreferencesManager.getAllPatterns(context);
        Map<Integer, IPatternModel> res = new HashMap<>(allPatterns.size());
        for (Map.Entry<Integer, PatternModel> patternEntry : allPatterns.entrySet()) {
            res.put(patternEntry.getKey(), patternEntry.getValue());
        }
        return res;
    }

    public Map<Integer, ICaveModel> loadCave(Context context) {
        Map<Integer, CaveModel> allCaves = CaveSharedPreferencesManager.getAllCaves(context);
        Map<Integer, ICaveModel> res = new HashMap<>(allCaves.size());
        for (Map.Entry<Integer, CaveModel> caveEntry : allCaves.entrySet()) {
            res.put(caveEntry.getKey(), caveEntry.getValue());
        }
        return res;
    }
}
