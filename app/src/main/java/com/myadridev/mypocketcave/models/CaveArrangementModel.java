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
    public final Map<CoordinatesModel, PatternModelWithBottles> PatternMap;

    public int NumberBottlesBulk;
    public int NumberBoxes;
    public int NumberBottlesPerBox;

    public CaveArrangementModel() {
        PatternMap = new HashMap<>();
    }

    public CaveArrangementModel(CaveArrangementModel caveArrangement) {
        TotalCapacity = caveArrangement.TotalCapacity;
        TotalUsed = caveArrangement.TotalUsed;
        PatternMap = new HashMap<>(caveArrangement.PatternMap);
        NumberBottlesBulk = caveArrangement.NumberBottlesBulk;
        NumberBoxes = caveArrangement.NumberBoxes;
        NumberBottlesPerBox = caveArrangement.NumberBottlesPerBox;
    }

    public void movePatternMapToLeft() {
        Map<CoordinatesModel, PatternModelWithBottles> patternMapToLeft = new HashMap<>(PatternMap.size());

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternMapEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternMapEntry.getKey();
            patternMapToLeft.put(new CoordinatesModel(coordinates.Row, coordinates.Col - 1), patternMapEntry.getValue());
        }

        PatternMap.clear();
        PatternMap.putAll(patternMapToLeft);
    }

    public void movePatternMapToRight() {
        if (!PatternMap.containsKey(new CoordinatesModel(0, 0))) return;
        Map<CoordinatesModel, PatternModelWithBottles> patternMapToRight = new HashMap<>(PatternMap.size());

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternMapEntry : PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternMapEntry.getKey();
            patternMapToRight.put(new CoordinatesModel(coordinates.Row, coordinates.Col + 1), patternMapEntry.getValue());
        }

        PatternMap.clear();
        PatternMap.putAll(patternMapToRight);
    }

    public void setClickablePlaces() {
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternMapEntry : PatternMap.entrySet()) {
            PatternModelWithBottles pattern = patternMapEntry.getValue();
            pattern.setClickablePlaces();

            CoordinatesModel coordinates = patternMapEntry.getKey();
            if (pattern.IsHorizontallyExpendable) {
                CoordinatesModel coordinatesRight = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
                if (PatternMap.containsKey(coordinatesRight)) {
                    PatternModelWithBottles rightPattern = PatternMap.get(coordinatesRight);
                    if (pattern.isPatternHorizontallyCompatible(rightPattern)) {
                        pattern.setRightClickablePlaces();
                        rightPattern.setLeftClickablePlaces();
                    }
                }
            }

            if (pattern.IsVerticallyExpendable) {
                CoordinatesModel coordinatesTop = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
                if (PatternMap.containsKey(coordinatesTop)) {
                    PatternModelWithBottles topPattern = PatternMap.get(coordinatesTop);
                    if (pattern.isPatternVerticallyCompatible(topPattern)) {
                        pattern.setTopClickablePlaces();
                        topPattern.setBottomClickablePlaces();
                    }
                }
            }
        }
    }
}
