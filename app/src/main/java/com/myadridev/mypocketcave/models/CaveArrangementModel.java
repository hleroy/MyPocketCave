package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = CaveArrangementModel.class)
public class CaveArrangementModel {

    public int TotalCapacity;
    public int TotalUsed;

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, Integer> PatternMap;

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, CavePlaceModel> PlaceMap;

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, Integer> CoordinatesBottlesMap;

    public int NumberBottlesBulk;
    public int NumberBoxes;
    public int NumberBottlesPerBox;

    public CaveArrangementModel() {
        PatternMap = new HashMap<>();
        PlaceMap = new HashMap<>();
        CoordinatesBottlesMap = new HashMap<>();
    }

    public CaveArrangementModel(CaveArrangementModel caveArrangement) {
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap);
        PlaceMap = new HashMap<>(caveArrangement.PlaceMap);
        CoordinatesBottlesMap = new HashMap<>(caveArrangement.CoordinatesBottlesMap);
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        NumberBottlesPerBox = caveArrangement.NumberBottlesPerBox;
    }

    public void movePatternMapToLeft() {
        Map<CoordinatesModel, Integer> patternMapToLeft = new HashMap<>(PatternMap.size());

        for (Map.Entry<CoordinatesModel, Integer> patternMapEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternMapEntry.getKey();
            patternMapToLeft.put(new CoordinatesModel(coordinates.Row, coordinates.Col - 1), patternMapEntry.getValue());
        }

        PatternMap.clear();
        PatternMap.putAll(patternMapToLeft);
    }

    public void movePatternMapToRight(int numberRows) {
        if (!PatternMap.containsKey(new CoordinatesModel(0, 0))) return;
        Map<CoordinatesModel, Integer> patternMapToRight = new HashMap<>(PatternMap.size());

        for (Map.Entry<CoordinatesModel, Integer> patternMapEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternMapEntry.getKey();
            patternMapToRight.put(new CoordinatesModel(coordinates.Row, coordinates.Col + 1), patternMapEntry.getValue());
        }

        PatternMap.clear();
        PatternMap.putAll(patternMapToRight);

        Map<CoordinatesModel, Integer> coordinatesBottlesMapToRight = new HashMap<>(CoordinatesBottlesMap.size());
        for (Map.Entry<CoordinatesModel, Integer> patternBottleMapEntry : CoordinatesBottlesMap.entrySet()) {
            CoordinatesModel coordinates = patternBottleMapEntry.getKey();
            coordinatesBottlesMapToRight.put(new CoordinatesModel(coordinates.Row, coordinates.Col + numberRows), patternBottleMapEntry.getValue());
        }

        CoordinatesBottlesMap.clear();
        CoordinatesBottlesMap.putAll(coordinatesBottlesMapToRight);
    }

    public void movePatternMapToRight() {
        movePatternMapToRight(0);
    }
}
