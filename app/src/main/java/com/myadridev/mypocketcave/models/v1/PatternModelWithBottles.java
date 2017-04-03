package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.enums.PositionEnum;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.models.CoordinatesModelDeserializer;
import com.myadridev.mypocketcave.models.CoordinatesModelSerializer;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = PatternModelWithBottles.class)
public class PatternModelWithBottles extends PatternModel {

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, CavePlaceModel> PlaceMapWithBottles;
    public int PatternWithBottlesId;

    // number is float because a bottle can be on 2 patterns at the same time
    public Map<Integer, Float> FloatNumberPlacedBottlesByIdMap;

    public PatternModelWithBottles() {
        PlaceMapWithBottles = new HashMap<>();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public PatternModelWithBottles(PatternModelWithBottles patternWithBottles) {
        super(patternWithBottles);
        PlaceMapWithBottles = new HashMap<>(patternWithBottles.PlaceMapWithBottles.size());
        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            PlaceMapWithBottles.put(placeEntry.getKey(), new CavePlaceModel(placeEntry.getValue()));
        }
        FloatNumberPlacedBottlesByIdMap = new HashMap<>(patternWithBottles.FloatNumberPlacedBottlesByIdMap);
    }

    public PatternModelWithBottles(PatternModel pattern) {
        super(pattern);
        PlaceMapWithBottles = getPlaceMapForDisplay();
        FloatNumberPlacedBottlesByIdMap = new HashMap<>();
    }

    public PatternModelWithBottles(PatternModel pattern, PatternModelWithBottles oldPattern) {
        this(pattern);
        if (pattern.Type != PatternTypeEnum.LINEAR || oldPattern.Type != PatternTypeEnum.LINEAR)
            return;

        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeModelEntry : oldPattern.PlaceMapWithBottles.entrySet()) {
            CavePlaceModel cavePlace = placeModelEntry.getValue();
            if (cavePlace.BottleId == -1) continue;
            CoordinatesModel coordinates = placeModelEntry.getKey();
            if (!PlaceMapWithBottles.containsKey(coordinates)) continue;
            PlaceMapWithBottles.put(coordinates, cavePlace);
            FloatNumberPlacedBottlesByIdMap.put(cavePlace.BottleId, CollectionsHelper.getValueOrDefault(FloatNumberPlacedBottlesByIdMap, cavePlace.BottleId, 0f) + 0.25f);
        }
    }

    public void setClickablePlaces() {
        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : PlaceMapWithBottles.entrySet()) {
            CavePlaceModel cavePlace = placeEntry.getValue();
            if (cavePlace.PlaceType.Position == PositionEnum.TOP_RIGHT) {
                CoordinatesModel coordinates = placeEntry.getKey();
                CoordinatesModel coordinatesRight = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
                CavePlaceModel placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                CoordinatesModel coordinatesTop = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
                CavePlaceModel placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                CoordinatesModel coordinatesTopRight = new CoordinatesModel(coordinates.Row + 1, coordinates.Col + 1);
                CavePlaceModel placeTopRight;
                if (PlaceMapWithBottles.containsKey(coordinatesTopRight)) {
                    placeTopRight = PlaceMapWithBottles.get(coordinatesTopRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnum.TOP_LEFT && placeTop.PlaceType.Position == PositionEnum.BOTTOM_RIGHT && placeTopRight.PlaceType.Position == PositionEnum.BOTTOM_LEFT) {
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
            CavePlaceModel cavePlace = PlaceMapWithBottles.get(new CoordinatesModel(row, col));
            if (cavePlace.PlaceType.Position == PositionEnum.TOP_RIGHT) {
                CoordinatesModel coordinatesTop = new CoordinatesModel(row + 1, col);
                CavePlaceModel placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.Position == PositionEnum.BOTTOM_RIGHT) {
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
            CavePlaceModel cavePlace = PlaceMapWithBottles.get(new CoordinatesModel(row, col));
            if (cavePlace.PlaceType.Position == PositionEnum.TOP_LEFT) {
                CoordinatesModel coordinatesTop = new CoordinatesModel(row + 1, col);
                CavePlaceModel placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.Position == PositionEnum.BOTTOM_LEFT) {
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
            CavePlaceModel cavePlace = PlaceMapWithBottles.get(new CoordinatesModel(row, col));
            if (cavePlace.PlaceType.Position == PositionEnum.TOP_RIGHT) {
                CoordinatesModel coordinatesRight = new CoordinatesModel(row, col + 1);
                CavePlaceModel placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnum.TOP_LEFT) {
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
            CavePlaceModel cavePlace = PlaceMapWithBottles.get(new CoordinatesModel(row, col));
            if (cavePlace.PlaceType.Position == PositionEnum.BOTTOM_RIGHT) {
                CoordinatesModel coordinatesRight = new CoordinatesModel(row, col + 1);
                CavePlaceModel placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.Position == PositionEnum.BOTTOM_LEFT) {
                    cavePlace.IsClickable = true;
                    placeRight.IsClickable = true;
                    col++;
                }
            }
        }
    }
}
