package com.myadridev.mypocketcave.managers.migration.from;

import android.content.Context;

import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.BottlesSharedPreferencesManagerV1;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.CaveSharedPreferencesManagerV1;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.CavesSharedPreferencesManagerV1;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1.PatternsSharedPreferencesManagerV1;
import com.myadridev.mypocketcave.models.inferfaces.IBottleModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;
import com.myadridev.mypocketcave.models.v1.BottleModelV1;
import com.myadridev.mypocketcave.models.v1.CaveLightModelV1;
import com.myadridev.mypocketcave.models.v1.CaveModelV1;
import com.myadridev.mypocketcave.models.v1.PatternModelV1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MigrationFromV1Manager implements IMigrationFromManager {

    public Map<Integer, IBottleModel> loadBottles(Context context) {
        Map<Integer, BottleModelV1> allBottles = BottlesSharedPreferencesManagerV1.getAllBottles(context);
        Map<Integer, IBottleModel> res = new HashMap<>(allBottles.size());
        for (Map.Entry<Integer, BottleModelV1> bottleEntry : allBottles.entrySet()) {
            res.put(bottleEntry.getKey(), bottleEntry.getValue());
        }
        return res;
    }

    public Map<Integer, ICaveLightModel> loadCaveLights(Context context) {
        Map<Integer, CaveLightModelV1> allCaves = CavesSharedPreferencesManagerV1.getAllCaves(context);
        Map<Integer, ICaveLightModel> res = new HashMap<>(allCaves.size());
        for (Map.Entry<Integer, CaveLightModelV1> caveEntry : allCaves.entrySet()) {
            res.put(caveEntry.getKey(), caveEntry.getValue());
        }
        return res;
    }

    public Map<Integer, IPatternModel> loadPatterns(Context context) {
        Map<Integer, PatternModelV1> allPatterns = PatternsSharedPreferencesManagerV1.getAllPatterns(context);
        Map<Integer, IPatternModel> res = new HashMap<>(allPatterns.size());
        for (Map.Entry<Integer, PatternModelV1> patternEntry : allPatterns.entrySet()) {
            res.put(patternEntry.getKey(), patternEntry.getValue());
        }
        return res;
    }

    public Map<Integer, ICaveModel> loadCaves(Context context, Collection<Integer> caveIds) {
        Map<Integer, CaveModelV1> allCaves = CaveSharedPreferencesManagerV1.getAllCaves(context, caveIds);
        Map<Integer, ICaveModel> res = new HashMap<>(allCaves.size());
        for (Map.Entry<Integer, CaveModelV1> caveEntry : allCaves.entrySet()) {
            res.put(caveEntry.getKey(), caveEntry.getValue());
        }
        return res;
    }
}
