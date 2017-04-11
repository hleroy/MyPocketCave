package com.myadridev.mypocketcave.managers.migration.from;

import android.content.Context;

import com.myadridev.mypocketcave.models.inferfaces.IBottleModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;

import java.util.Collection;
import java.util.Map;

public interface IMigrationFromManager {

    Map<Integer, IBottleModel> loadBottles(Context context);

    Map<Integer, ICaveLightModel> loadCaveLights(Context context);

    Map<Integer, IPatternModel> loadPatterns(Context context);

    Map<Integer, ICaveModel> loadCaves(Context context, Collection<Integer> caveIds);
}
