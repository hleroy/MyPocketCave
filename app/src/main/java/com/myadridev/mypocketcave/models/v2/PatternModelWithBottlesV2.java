package com.myadridev.mypocketcave.models.v2;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.PositionEnumV2;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModelWithBottles;

import java.util.HashMap;
import java.util.Map;

public class PatternModelWithBottlesV2 extends PatternModelV2 implements IPatternModelWithBottles {

    @SerializedName("pmb")
    public final Map<CoordinatesModelV2, CavePlaceModelV2> PlaceMapWithBottles;

    // number is float because a bottle can be on 2 patterns at the same time
    @SerializedName("fnp")
    public final Map<Integer, Float> FloatNumberPlacedBottlesByIdMap;

    public PatternModelWithBottlesV2() {
        PlaceMapWithBottles = new HashMap<>();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public PatternModelWithBottlesV2(PatternModelWithBottlesV2 patternWithBottles) {
        super(patternWithBottles);
        PlaceMapWithBottles = new HashMap<>(patternWithBottles.PlaceMapWithBottles.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            PlaceMapWithBottles.put(placeEntry.getKey(), new CavePlaceModelV2(placeEntry.getValue()));
        }
        FloatNumberPlacedBottlesByIdMap = new HashMap<>(patternWithBottles.FloatNumberPlacedBottlesByIdMap);
    }

    public PatternModelWithBottlesV2(PatternModelV2 pattern) {
        super(pattern);
        PlaceMapWithBottles = getPlaceMapForDisplay();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public PatternModelWithBottlesV2(PatternModelV2 pattern, PatternModelWithBottlesV2 oldPattern) {
        this(pattern);
        if (pattern.Type != PatternTypeEnumV2.l || oldPattern.Type != PatternTypeEnumV2.l)
            return;

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeModelEntry : oldPattern.PlaceMapWithBottles.entrySet()) {
            CavePlaceModelV2 cavePlace = placeModelEntry.getValue();
            if (cavePlace.BottleId == -1) continue;
            CoordinatesModelV2 coordinates = placeModelEntry.getKey();
            if (!PlaceMapWithBottles.containsKey(coordinates)) continue;
            PlaceMapWithBottles.put(coordinates, cavePlace);
            FloatNumberPlacedBottlesByIdMap.put(cavePlace.BottleId, CollectionsHelper.getValueOrDefault(FloatNumberPlacedBottlesByIdMap, cavePlace.BottleId, 0f) + 0.25f);
        }
    }

    public void setClickablePlaces() {
        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeEntry : PlaceMapWithBottles.entrySet()) {
            CavePlaceModelV2 cavePlace = placeEntry.getValue();
            if (cavePlace.PlaceType.Position == PositionEnumV2.tr) {
                CoordinatesModelV2 coordinates = placeEntry.getKey();
                CoordinatesModelV2 coordinatesRight = new CoordinatesModelV2(coordinates.Row, coordinates.Col + 1);
                CavePlaceModelV2 placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                CoordinatesModelV2 coordinatesTop = new CoordinatesModelV2(coordinates.Row + 1, coordinates.Col);
                CavePlaceModelV2 placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                CoordinatesModelV2 coordinatesTopRight = new CoordinatesModelV2(coordinates.Row + 1, coordinates.Col + 1);
                CavePlaceModelV2 placeTopRight;
                if (PlaceMapWithBottles.containsKey(coordinatesTopRight)) {
                    placeTopRight = PlaceMapWithBottles.get(coordinatesTopRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnumV2.tl && placeTop.PlaceType.Position == PositionEnumV2.br && placeTopRight.PlaceType.Position == PositionEnumV2.bl) {
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
            CavePlaceModelV2 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV2(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV2.tr) {
                CoordinatesModelV2 coordinatesTop = new CoordinatesModelV2(row + 1, col);
                CavePlaceModelV2 placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.Position == PositionEnumV2.br) {
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
            CavePlaceModelV2 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV2(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV2.tl) {
                CoordinatesModelV2 coordinatesTop = new CoordinatesModelV2(row + 1, col);
                CavePlaceModelV2 placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.Position == PositionEnumV2.bl) {
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
            CavePlaceModelV2 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV2(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV2.tr) {
                CoordinatesModelV2 coordinatesRight = new CoordinatesModelV2(row, col + 1);
                CavePlaceModelV2 placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnumV2.tl) {
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
            CavePlaceModelV2 cavePlace = PlaceMapWithBottles.get(new CoordinatesModelV2(row, col));
            if (cavePlace.PlaceType.Position == PositionEnumV2.br) {
                CoordinatesModelV2 coordinatesRight = new CoordinatesModelV2(row, col + 1);
                CavePlaceModelV2 placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnumV2.bl) {
                    cavePlace.IsClickable = true;
                    placeRight.IsClickable = true;
                    col++;
                }
            }
        }
    }
}
