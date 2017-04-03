package com.myadridev.mypocketcave.models.migration.from;

import android.content.Context;

import com.myadridev.mypocketcave.models.IBottleModel;
import com.myadridev.mypocketcave.models.ICaveLightModel;
import com.myadridev.mypocketcave.models.ICaveModel;
import com.myadridev.mypocketcave.models.IPatternModel;

import java.util.Map;

public interface IMigrationFromManager {

    Map<Integer, IBottleModel> loadBottles(Context context);

    Map<Integer, ICaveLightModel> loadCaves(Context context);

    Map<Integer,IPatternModel> loadPatterns(Context context);

    Map<Integer,ICaveModel> loadCave(Context context);
}
