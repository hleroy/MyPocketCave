package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ICaveArrangementModel;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@JsonSerialize(as = CaveArrangementModelV1.class)
public class CaveArrangementModelV1 implements ICaveArrangementModel {

    @JsonSerialize(keyUsing = CoordinatesModelSerializerV1.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializerV1.class)
    public final Map<CoordinatesModelV1, PatternModelWithBottlesV1> PatternMap;
    public final Map<Integer, Integer> IntNumberPlacedBottlesByIdMap;
    public int Id;
    public int TotalCapacity;
    public int TotalUsed;
    public int NumberBottlesBulk;
    public int NumberBoxes;
    public int BoxesNumberBottlesByColumn;
    public int BoxesNumberBottlesByRow;
    public Map<Integer, Float> floatNumberPlacedBottlesByIdMap;

    public CaveArrangementModelV1() {
        PatternMap = new HashMap<>();
        floatNumberPlacedBottlesByIdMap = new HashMap<>();
        IntNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public CaveArrangementModelV1(CaveArrangementModelV1 caveArrangement) {
        Id = caveArrangement.Id;
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModelV1, PatternModelWithBottlesV1> patternEntry : caveArrangement.PatternMap.entrySet()) {
            PatternMap.put(patternEntry.getKey(), new PatternModelWithBottlesV1(patternEntry.getValue()));
        }
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        BoxesNumberBottlesByColumn = caveArrangement.BoxesNumberBottlesByColumn;
        BoxesNumberBottlesByRow = caveArrangement.BoxesNumberBottlesByRow;
        floatNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.floatNumberPlacedBottlesByIdMap);
        IntNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.IntNumberPlacedBottlesByIdMap);
    }
}
