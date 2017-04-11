package com.myadridev.mypocketcave.managers.migration.to;

import com.myadridev.mypocketcave.helpers.ModelMigrationHelper;
import com.myadridev.mypocketcave.models.inferfaces.IBottleModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import java.util.HashMap;
import java.util.Map;

public class MigrationToManager {

    public Map<Integer, BottleModelV2> migrateBottles(Map<Integer, IBottleModel> allBottlesMap) {
        Map<Integer, BottleModelV2> allBottlesMapV2 = new HashMap<>(allBottlesMap.size());
        for (Map.Entry<Integer, IBottleModel> bottleEntry : allBottlesMap.entrySet()) {
            IBottleModel bottle = bottleEntry.getValue();
            BottleModelV2 newBottle = ModelMigrationHelper.getBottle(bottle);
            if (newBottle == null) continue;
            allBottlesMapV2.put(bottleEntry.getKey(), newBottle);
        }

        return allBottlesMapV2;
    }

    public Map<Integer, CaveLightModelV2> migrateCaves(Map<Integer, ICaveLightModel> allCavesMap) {
        Map<Integer, CaveLightModelV2> allCavesMapV2 = new HashMap<>(allCavesMap.size());
        for (Map.Entry<Integer, ICaveLightModel> caveEntry : allCavesMap.entrySet()) {
            ICaveLightModel cave = caveEntry.getValue();
            CaveLightModelV2 newCave = ModelMigrationHelper.getCaveLight(cave);
            if (newCave == null) continue;
            allCavesMapV2.put(caveEntry.getKey(), newCave);
        }
        return allCavesMapV2;
    }

    public Map<Integer, PatternModelV2> migratePatterns(Map<Integer, IPatternModel> allPatternsMap) {
        Map<Integer, PatternModelV2> allPatternsMapV2 = new HashMap<>(allPatternsMap.size());
        for (Map.Entry<Integer, IPatternModel> patternEntry : allPatternsMap.entrySet()) {
            IPatternModel pattern = patternEntry.getValue();
            PatternModelV2 newPattern = ModelMigrationHelper.getPattern(pattern);
            if (newPattern == null) continue;
            allPatternsMapV2.put(patternEntry.getKey(), newPattern);
        }
        return allPatternsMapV2;
    }

    public Map<Integer, CaveModelV2> migrateCave(Map<Integer, ICaveModel> allCavesMap) {
        Map<Integer, CaveModelV2> allCavesMapV2 = new HashMap<>(allCavesMap.size());
        for (Map.Entry<Integer, ICaveModel> caveEntry : allCavesMap.entrySet()) {
            ICaveModel cave = caveEntry.getValue();
            CaveModelV2 newCave = ModelMigrationHelper.getCave(cave);
            if (newCave == null) continue;
            allCavesMapV2.put(caveEntry.getKey(), newCave);
        }
        return allCavesMapV2;
    }
}
