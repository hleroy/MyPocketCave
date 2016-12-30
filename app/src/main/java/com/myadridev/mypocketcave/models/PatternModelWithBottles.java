package com.myadridev.mypocketcave.models;

import android.util.SparseArray;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = PatternModelWithBottles.class)
public class PatternModelWithBottles extends PatternModel {

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, CavePlaceModel> PlaceMapWithBottles;
    public int PatternWithBottlesId;

    // number is float because a bottle can be on 2 patterns at the same time
    public SparseArray<Float> NumberPlacedBottlesByIdMap;

    public PatternModelWithBottles() {
        PlaceMapWithBottles = new HashMap<>();
        NumberPlacedBottlesByIdMap = new SparseArray<>();
    }

    public PatternModelWithBottles(PatternModel pattern) {
        super(pattern);
        PlaceMapWithBottles = getPlaceMapForDisplay();
        NumberPlacedBottlesByIdMap = new SparseArray<>();
    }

    public void setClickablePlaces() {
        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : PlaceMapWithBottles.entrySet()) {
            CavePlaceModel cavePlace = placeEntry.getValue();
            if (cavePlace.PlaceType.isTopRight()) {
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

                if (placeRight.PlaceType.isTopLeft() && placeTop.PlaceType.isBottomRight() && placeTopRight.PlaceType.isBottomLeft()) {
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
            if (cavePlace.PlaceType.isTopRight()) {
                CoordinatesModel coordinatesTop = new CoordinatesModel(row + 1, col);
                CavePlaceModel placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.isBottomRight()) {
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
            if (cavePlace.PlaceType.isTopLeft()) {
                CoordinatesModel coordinatesTop = new CoordinatesModel(row + 1, col);
                CavePlaceModel placeTop;
                if (PlaceMapWithBottles.containsKey(coordinatesTop)) {
                    placeTop = PlaceMapWithBottles.get(coordinatesTop);
                } else {
                    continue;
                }

                if (placeTop.PlaceType.isBottomLeft()) {
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
            if (cavePlace.PlaceType.isTopRight()) {
                CoordinatesModel coordinatesRight = new CoordinatesModel(row, col + 1);
                CavePlaceModel placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.isTopLeft()) {
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
            if (cavePlace.PlaceType.isBottomRight()) {
                CoordinatesModel coordinatesRight = new CoordinatesModel(row, col + 1);
                CavePlaceModel placeRight;
                if (PlaceMapWithBottles.containsKey(coordinatesRight)) {
                    placeRight = PlaceMapWithBottles.get(coordinatesRight);
                } else {
                    continue;
                }

                if (placeRight.PlaceType.isBottomLeft()) {
                    cavePlace.IsClickable = true;
                    placeRight.IsClickable = true;
                    col++;
                }
            }
        }
    }
}
