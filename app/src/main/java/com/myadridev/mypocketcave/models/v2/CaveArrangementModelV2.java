package com.myadridev.mypocketcave.models.v2;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.models.inferfaces.ICaveArrangementModel;

import java.util.HashMap;
import java.util.Map;

public class CaveArrangementModelV2 implements ICaveArrangementModel {

    @SerializedName("pm")
    public final Map<CoordinatesModelV2, PatternModelWithBottlesV2> PatternMap;
    @SerializedName("inpm")
    public final Map<Integer, Integer> IntNumberPlacedBottlesByIdMap;
    @SerializedName("fnpm")
    public final Map<Integer, Float> FloatNumberPlacedBottlesByIdMap;
    @SerializedName("i")
    public int Id;
    @SerializedName("tc")
    public int TotalCapacity;
    @SerializedName("tu")
    public int TotalUsed;
    @SerializedName("nbb")
    public int NumberBottlesBulk;
    @SerializedName("nb")
    public int NumberBoxes;
    @SerializedName("bnc")
    public int BoxesNumberBottlesByColumn;
    @SerializedName("bnr")
    public int BoxesNumberBottlesByRow;

    public CaveArrangementModelV2() {
        PatternMap = new HashMap<>();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
        IntNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public CaveArrangementModelV2(CaveArrangementModelV2 caveArrangement) {
        Id = caveArrangement.Id;
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModelV2, PatternModelWithBottlesV2> patternEntry : caveArrangement.PatternMap.entrySet()) {
            PatternMap.put(patternEntry.getKey(), new PatternModelWithBottlesV2(patternEntry.getValue()));
        }
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        BoxesNumberBottlesByColumn = caveArrangement.BoxesNumberBottlesByColumn;
        BoxesNumberBottlesByRow = caveArrangement.BoxesNumberBottlesByRow;
        FloatNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.FloatNumberPlacedBottlesByIdMap);
        IntNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.IntNumberPlacedBottlesByIdMap);
    }
}
