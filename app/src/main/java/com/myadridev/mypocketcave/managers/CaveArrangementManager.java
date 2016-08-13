package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

import java.util.Map;

public class CaveArrangementManager {

    public static CaveArrangementManager Instance;
    private static boolean _isInitialized;

    private CaveArrangementManager() {
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init() {
        Instance = new CaveArrangementManager();
        _isInitialized = true;
    }

    public int computeTotalCapacityWithBulk(CaveArrangementModel caveArrangement) {
        return caveArrangement.NumberBottlesBulk;
    }

    public int computeTotalCapacityWithBoxes(CaveArrangementModel caveArrangement) {
        return caveArrangement.NumberBottlesPerBox * caveArrangement.NumberBoxes;
    }

    public int computeTotalCapacityWithPattern(CaveArrangementModel caveArrangement) {
        int capacity = 0;
        int capacityDoubled = 0; // for the half-places, there will be added twice

        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : caveArrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();

            capacity += pattern.getCapacityAlone();

            // if pattern.IsHorizontallyExpendable look at left and right pattern
            if (pattern.IsHorizontallyExpendable) {
                // number of half-places : pattern.NumberBottlesByColumn (-1 if not inverted)
                CoordinatesModel leftCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
                if (caveArrangement.PatternMap.containsKey(leftCoordinates)) {
                    PatternModelWithBottles leftPattern = caveArrangement.PatternMap.get(leftCoordinates);
                    if (pattern.isPatternHorizontallyCompatible(leftPattern)) {
                        capacityDoubled += pattern.NumberBottlesByColumn - (pattern.IsInverted ? 0 : 1);
                    }
                }
                CoordinatesModel rightCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
                if (caveArrangement.PatternMap.containsKey(rightCoordinates)) {
                    PatternModelWithBottles rightPattern = caveArrangement.PatternMap.get(rightCoordinates);
                    if (pattern.isPatternHorizontallyCompatible(rightPattern)) {
                        capacityDoubled += pattern.NumberBottlesByColumn - (pattern.IsInverted ? 0 : 1);
                    }
                }
            }

            // if pattern.IsVerticallyExpendable look at top and bottom pattern
            if (pattern.IsVerticallyExpendable) {
                // number of half-places : pattern.NumberBottlesByRow (-1 if not inverted)
                CoordinatesModel bottomCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
                if (caveArrangement.PatternMap.containsKey(bottomCoordinates)) {
                    PatternModelWithBottles bottomPattern = caveArrangement.PatternMap.get(bottomCoordinates);
                    if (pattern.isPatternVerticallyCompatible(bottomPattern)) {
                        capacityDoubled += pattern.NumberBottlesByRow - (pattern.IsInverted ? 0 : 1);
                    }
                }
                CoordinatesModel topCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);
                if (caveArrangement.PatternMap.containsKey(topCoordinates)) {
                    PatternModelWithBottles topPattern = caveArrangement.PatternMap.get(topCoordinates);
                    if (pattern.isPatternVerticallyCompatible(topPattern)) {
                        capacityDoubled += pattern.NumberBottlesByRow - (pattern.IsInverted ? 0 : 1);
                    }
                }
            }
        }
        capacity += capacityDoubled / 2;
        return capacity;
    }
}
