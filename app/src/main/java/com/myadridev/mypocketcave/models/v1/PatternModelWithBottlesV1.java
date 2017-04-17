package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.PatternTypeEnumV1;
import com.myadridev.mypocketcave.enums.v1.PositionEnumV1;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModelWithBottles;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@JsonSerialize(as = PatternModelWithBottlesV1.class)
public class PatternModelWithBottlesV1 extends PatternModelV1 implements IPatternModelWithBottles {

    @JsonSerialize(keyUsing = CoordinatesModelSerializerV1.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializerV1.class)
    public final Map<CoordinatesModelV1, CavePlaceModelV1> PlaceMapWithBottles;
    public int PatternWithBottlesId;

    // number is float because a bottle can be on 2 patterns at the same time
    public Map<Integer, Float> FloatNumberPlacedBottlesByIdMap;

    public PatternModelWithBottlesV1() {
        PlaceMapWithBottles = new HashMap<>();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public PatternModelWithBottlesV1(PatternModelWithBottlesV1 patternWithBottles) {
        super(patternWithBottles);
        PlaceMapWithBottles = new HashMap<>(patternWithBottles.PlaceMapWithBottles.size());
        for (Map.Entry<CoordinatesModelV1, CavePlaceModelV1> placeEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            PlaceMapWithBottles.put(placeEntry.getKey(), new CavePlaceModelV1(placeEntry.getValue()));
        }
        FloatNumberPlacedBottlesByIdMap = new HashMap<>(patternWithBottles.FloatNumberPlacedBottlesByIdMap);
    }

    public PatternModelWithBottlesV1(PatternModelV1 pattern) {
        super(pattern);
        PlaceMapWithBottles = getPlaceMapForDisplay();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public PatternModelWithBottlesV1(PatternModelV1 pattern, PatternModelWithBottlesV1 oldPattern) {
        this(pattern);
        if (pattern.Type != PatternTypeEnumV1.LINEAR || oldPattern.Type != PatternTypeEnumV1.LINEAR)
            return;

        for (Map.Entry<CoordinatesModelV1, CavePlaceModelV1> placeModelEntry : oldPattern.PlaceMapWithBottles.entrySet()) {
            CavePlaceModelV1 cavePlace = placeModelEntry.getValue();
            if (cavePlace.BottleId == -1) continue;
            CoordinatesModelV1 coordinates = placeModelEntry.getKey();
            if (!PlaceMapWithBottles.containsKey(coordinates)) continue;
            PlaceMapWithBottles.put(coordinates, cavePlace);
            FloatNumberPlacedBottlesByIdMap.put(cavePlace.BottleId, CollectionsHelper.getValueOrDefault(FloatNumberPlacedBottlesByIdMap, cavePlace.BottleId, 0f) + 0.25f);
        }
    }

    public void setClickablePlaces() {
        for (Map.Entry<CoordinatesModelV1, CavePlaceModelV1> placeEntry : PlaceMapWithBottles.entrySet()) {
            CavePlaceModelV1 cavePlace = placeEntry.getValue();
            if (cavePlace.PlaceType.Position == PositionEnumV1.TOP_RIGHT) {
                CoordinatesModelV1 coordinates = placeEntry.getKey();
                CoordinatesModelV1 coordinatesRight = new CoordinatesModelV1(coordinates.Row, coordinates.Col + 1);
                CavePlaceModelV1 placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                CoordinatesModelV1 coordinatesTop = new CoordinatesModelV1(coordinates.Row + 1, coordinates.Col);
                CavePlaceModelV1 placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                CoordinatesModelV1 coordinatesTopRight = new CoordinatesModelV1(coordinates.Row + 1, coordinates.Col + 1);
                CavePlaceModelV1 placeTopRight;
                if (PlaceMapWithBottles.containsKey(coordinatesTopRight)) {
                    placeTopRight = PlaceMapWithBottles.get(coordinatesTopRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnumV1.TOP_LEFT && placeTop.PlaceType.Position == PositionEnumV1.BOTTOM_RIGHT && placeTopRight.PlaceType.Position == PositionEnumV1.BOTTOM_LEFT) {
                    cavePlace.IsClickable = true;
                    placeRight.IsClickable = true;
                    placeTop.IsClickable = true;
                    placeTopRight.IsClickable = true;
                }
            }
        }
    }

    public void setRightClickablePlaces() {
        int col = getNumberColumnsGridLayout() - 1;
        for (int row = 0; row < getNumberRowsGridLayout(); row++) {
            CavePlaceModelV1 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV1(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV1.TOP_RIGHT) {
                CoordinatesModelV1 coordinatesTop = new CoordinatesModelV1(row + 1, col);
                CavePlaceModelV1 placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.Position == PositionEnumV1.BOTTOM_RIGHT) {
                    cavePlace.IsClickable = true;
                    placeTop.IsClickable = true;
                    row++;
                }
            }
        }
    }

    public void setLeftClickablePlaces() {
        int col = 0;
        for (int row = 0; row < getNumberRowsGridLayout(); row++) {
            CavePlaceModelV1 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV1(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV1.TOP_LEFT) {
                CoordinatesModelV1 coordinatesTop = new CoordinatesModelV1(row + 1, col);
                CavePlaceModelV1 placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.Position == PositionEnumV1.BOTTOM_LEFT) {
                    cavePlace.IsClickable = true;
                    placeTop.IsClickable = true;
                    row++;
                }
            }
        }
    }

    public void setTopClickablePlaces() {
        int row = getNumberRowsGridLayout() - 1;
        for (int col = 0; col < getNumberColumnsGridLayout(); col++) {
            CavePlaceModelV1 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV1(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV1.TOP_RIGHT) {
                CoordinatesModelV1 coordinatesRight = new CoordinatesModelV1(row, col + 1);
                CavePlaceModelV1 placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnumV1.TOP_LEFT) {
                    cavePlace.IsClickable = true;
                    placeRight.IsClickable = true;
                    col++;
                }
            }
        }
    }

    public void setBottomClickablePlaces() {
        int row = 0;
        for (int col = 0; col < getNumberColumnsGridLayout(); col++) {
            CavePlaceModelV1 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV1(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV1.BOTTOM_RIGHT) {
                CoordinatesModelV1 coordinatesRight = new CoordinatesModelV1(row, col + 1);
                CavePlaceModelV1 placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnumV1.BOTTOM_LEFT) {
                    cavePlace.IsClickable = true;
                    placeRight.IsClickable = true;
                    col++;
                }
            }
        }
    }
}
