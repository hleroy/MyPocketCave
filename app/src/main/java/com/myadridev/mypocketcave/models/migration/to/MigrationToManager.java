package com.myadridev.mypocketcave.models.migration.to;

import android.content.Context;

import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IBottleStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICaveStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IPatternsStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.BottlesSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.CaveSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.CavesSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.PatternsSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.models.IBottleModel;
import com.myadridev.mypocketcave.models.ICaveLightModel;
import com.myadridev.mypocketcave.models.ICaveModel;
import com.myadridev.mypocketcave.models.IPatternModel;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import java.util.HashMap;
import java.util.Map;

public class MigrationToManager {

    // TODO : transform IModels to ModelV2

    public void migrateBottles(Context context, Map<Integer, IBottleModel> allBottlesMap) {
        Map<Integer, BottleModelV2> allBottlesMapV2 = new HashMap<>(allBottlesMap.size());
        for (Map.Entry<Integer, IBottleModel> bottleEntry : allBottlesMap.entrySet()) {
            IBottleModel bottle = bottleEntry.getValue();
            if (!(bottle instanceof BottleModelV2)) continue;
            allBottlesMapV2.put(bottleEntry.getKey(), (BottleModelV2) bottle);
        }
        BottlesSharedPreferencesManagerV2.init(context, allBottlesMapV2);
        DependencyManager.registerSingleton(IBottleStorageManagerV2.class, BottlesSharedPreferencesManagerV2.Instance);
    }

    public void migrateCaves(Context context, Map<Integer, ICaveLightModel> allCavesMap) {
        Map<Integer, CaveLightModelV2> allCavesMapV2 = new HashMap<>(allCavesMap.size());
        for (Map.Entry<Integer, ICaveLightModel> caveEntry : allCavesMap.entrySet()) {
            ICaveLightModel cave = caveEntry.getValue();
            if (!(cave instanceof CaveLightModelV2)) continue;
            allCavesMapV2.put(caveEntry.getKey(), (CaveLightModelV2) cave);
        }
        CavesSharedPreferencesManagerV2.init(context, allCavesMapV2);
        DependencyManager.registerSingleton(ICavesStorageManagerV2.class, CavesSharedPreferencesManagerV2.Instance);
    }

    public void migratePatterns(Context context, Map<Integer, IPatternModel> allPatternsMap) {
        Map<Integer, PatternModelV2> allPatternsMapV2 = new HashMap<>(allPatternsMap.size());
        for (Map.Entry<Integer, IPatternModel> patternEntry : allPatternsMap.entrySet()) {
            IPatternModel pattern = patternEntry.getValue();
            if (!(pattern instanceof PatternModelV2)) continue;
            allPatternsMapV2.put(patternEntry.getKey(), (PatternModelV2) pattern);
        }
        PatternsSharedPreferencesManagerV2.init(context, allPatternsMapV2);
        DependencyManager.registerSingleton(IPatternsStorageManagerV2.class, PatternsSharedPreferencesManagerV2.Instance);
    }

    public void migrateCave(Context context, Map<Integer, ICaveModel> allCavesMap) {
        Map<Integer, CaveModelV2> allCavesMapV2 = new HashMap<>(allCavesMap.size());
        for (Map.Entry<Integer, ICaveModel> caveEntry : allCavesMap.entrySet()) {
            ICaveModel cave = caveEntry.getValue();
            if (!(cave instanceof CaveModelV2)) continue;
            allCavesMapV2.put(caveEntry.getKey(), (CaveModelV2) cave);
        }
        CaveSharedPreferencesManagerV2.init(context, allCavesMapV2);
        DependencyManager.registerSingleton(ICaveStorageManagerV2.class, CaveSharedPreferencesManagerV2.Instance);
    }
}
