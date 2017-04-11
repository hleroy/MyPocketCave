package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ICaveArrangementModel;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = CaveArrangementModel.class)
public class CaveArrangementModel implements ICaveArrangementModel {

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, PatternModelWithBottles> PatternMap;
    public final Map<Integer, Integer> IntNumberPlacedBottlesByIdMap;
    public int Id;
    public int TotalCapacity;
    public int TotalUsed;
    public int NumberBottlesBulk;
    public int NumberBoxes;
    public int BoxesNumberBottlesByColumn;
    public int BoxesNumberBottlesByRow;
    public Map<Integer, Float> floatNumberPlacedBottlesByIdMap;

    public CaveArrangementModel() {
        PatternMap = new HashMap<>();
        floatNumberPlacedBottlesByIdMap = new HashMap<>();
        IntNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public CaveArrangementModel(CaveArrangementModel caveArrangement) {
        Id = caveArrangement.Id;
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : caveArrangement.PatternMap.entrySet()) {
            PatternMap.put(patternEntry.getKey(), new PatternModelWithBottles(patternEntry.getValue()));
        }
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        BoxesNumberBottlesByColumn = caveArrangement.BoxesNumberBottlesByColumn;
        BoxesNumberBottlesByRow = caveArrangement.BoxesNumberBottlesByRow;
        floatNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.floatNumberPlacedBottlesByIdMap);
        IntNumberPlacedBottlesByIdMap = new HashMap<>(caveArrangement.IntNumberPlacedBottlesByIdMap);
    }
}
