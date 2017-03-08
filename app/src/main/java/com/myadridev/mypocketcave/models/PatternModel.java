package com.myadridev.mypocketcave.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = PatternModel.class)
public class PatternModel implements IStorableModel, Comparable<PatternModel> {

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, CavePlaceTypeEnum> PlaceMap;
    public int Id;
    public PatternTypeEnum Type;
    public int NumberBottlesByColumn;
    public int NumberBottlesByRow;
    public boolean IsHorizontallyExpendable;
    public boolean IsVerticallyExpendable;
    public boolean IsInverted;
    public int Order;

    public PatternModel() {
        PlaceMap = new HashMap<>();
    }

    public PatternModel(PatternModel pattern) {
        Id = pattern.Id;
        Type = pattern.Type;
        NumberBottlesByColumn = pattern.NumberBottlesByColumn;
        NumberBottlesByRow = pattern.NumberBottlesByRow;
        IsHorizontallyExpendable = pattern.IsHorizontallyExpendable;
        IsVerticallyExpendable = pattern.IsVerticallyExpendable;
        IsInverted = pattern.IsInverted;
        PlaceMap = new HashMap<>(pattern.PlaceMap);
        Order = pattern.Order;
    }

    public void computePlacesMap() {
        PlaceMap.clear();
        switch (Type) {
            case LINEAR:
                computeLinearPlacesMap();
                break;
            case STAGGERED_ROWS:
                computeStaggeredPlacesMap();
                break;
        }
    }

    private void computeStaggeredPlacesMap() {
        int numberRows = 4 * NumberBottlesByColumn - 2;
        int numberCols = 4 * NumberBottlesByRow - 2;

        if (IsVerticallyExpendable) {
            // add 1 row at beginning and end
            for (int colIndex = 0; colIndex < numberCols; colIndex += 2) {
                computePlaceWithBottom(PlaceMap, 0, colIndex + (IsHorizontallyExpendable ? 1 : 0), (colIndex % 4 == 0) == IsInverted);
                computePlaceWithTop(PlaceMap, numberRows + 1, colIndex + (IsHorizontallyExpendable ? 1 : 0), (colIndex % 4 == 0) == IsInverted);
            }
        }

        if (IsHorizontallyExpendable) {
            // add 1 column at beginning and end
            for (int rowIndex = 0; rowIndex < numberRows; rowIndex += 2) {
                computePlaceWithLeft(PlaceMap, rowIndex + (IsVerticallyExpendable ? 1 : 0), 0, (rowIndex % 4 == 0) == IsInverted);
                computePlaceWithRight(PlaceMap, rowIndex + (IsVerticallyExpendable ? 1 : 0), numberCols + 1, (rowIndex % 4 == 0) == IsInverted);
            }
        }

        for (int colIndex = 0; colIndex < numberCols; colIndex += 2) {
            for (int rowIndex = 0; rowIndex < numberRows; rowIndex += 2) {
                if ((rowIndex % 4 == colIndex % 4) == IsInverted) {
                    computeFullNoPlace(PlaceMap, rowIndex + (IsVerticallyExpendable ? 1 : 0), colIndex + (IsHorizontallyExpendable ? 1 : 0));
                } else {
                    computeFullPlace(PlaceMap, rowIndex + (IsVerticallyExpendable ? 1 : 0), colIndex + (IsHorizontallyExpendable ? 1 : 0));
                }
            }
        }

        if (IsVerticallyExpendable && IsHorizontallyExpendable) {
            PlaceMap.put(new CoordinatesModel(0, 0), CavePlaceTypeEnum.NO_PLACE);
            PlaceMap.put(new CoordinatesModel(0, numberCols + 1), CavePlaceTypeEnum.NO_PLACE);
            PlaceMap.put(new CoordinatesModel(numberRows + 1, 0), CavePlaceTypeEnum.NO_PLACE);
            PlaceMap.put(new CoordinatesModel(numberRows + 1, numberCols + 1), CavePlaceTypeEnum.NO_PLACE);
        }
    }

    private void computeLinearPlacesMap() {
        for (int row = 0; row < NumberBottlesByColumn; row++) {
            for (int col = 0; col < NumberBottlesByRow; col++) {
                computeFullPlace(PlaceMap, 2 * row, 2 * col);
            }
        }
    }

    private void computeFullPlace(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int row, int col) {
        placeMap.put(new CoordinatesModel(row, col), CavePlaceTypeEnum.PLACE_TOP_RIGHT);
        placeMap.put(new CoordinatesModel(row, col + 1), CavePlaceTypeEnum.PLACE_TOP_LEFT);
        placeMap.put(new CoordinatesModel(row + 1, col), CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT);
        placeMap.put(new CoordinatesModel(row + 1, col + 1), CavePlaceTypeEnum.PLACE_BOTTOM_LEFT);
    }

    private void computeFullNoPlace(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int row, int col) {
        placeMap.put(new CoordinatesModel(row, col), CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row, col + 1), CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row + 1, col), CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row + 1, col + 1), CavePlaceTypeEnum.NO_PLACE);
    }

    private void computePlaceWithRight(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModel(row, col), isInverted ? CavePlaceTypeEnum.PLACE_TOP_RIGHT : CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row + 1, col), isInverted ? CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT : CavePlaceTypeEnum.NO_PLACE);
    }

    private void computePlaceWithLeft(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModel(row, col), isInverted ? CavePlaceTypeEnum.PLACE_TOP_LEFT : CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row + 1, col), isInverted ? CavePlaceTypeEnum.PLACE_BOTTOM_LEFT : CavePlaceTypeEnum.NO_PLACE);
    }

    private void computePlaceWithTop(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModel(row, col), isInverted ? CavePlaceTypeEnum.PLACE_TOP_RIGHT : CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row, col + 1), isInverted ? CavePlaceTypeEnum.PLACE_TOP_LEFT : CavePlaceTypeEnum.NO_PLACE);
    }

    private void computePlaceWithBottom(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModel(row, col), isInverted ? CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT : CavePlaceTypeEnum.NO_PLACE);
        placeMap.put(new CoordinatesModel(row, col + 1), isInverted ? CavePlaceTypeEnum.PLACE_BOTTOM_LEFT : CavePlaceTypeEnum.NO_PLACE);
    }

    @JsonIgnore
    public int getNumberColumnsGridLayout() {
        switch (Type) {
            case LINEAR:
                return 2 * NumberBottlesByRow;
            case STAGGERED_ROWS:
                return 4 * NumberBottlesByRow - (IsHorizontallyExpendable ? 0 : 2);
            default:
                return 0;
        }
    }

    @JsonIgnore
    public int getNumberRowsGridLayout() {
        switch (Type) {
            case LINEAR:
                return 2 * NumberBottlesByColumn;
            case STAGGERED_ROWS:
                return 4 * NumberBottlesByColumn - (IsVerticallyExpendable ? 0 : 2);
            default:
                return 0;
        }
    }

    @Override
    public int compareTo(@NonNull PatternModel otherPattern) {
        int compareOrder = Order - otherPattern.Order;
        if (compareOrder > 0)
            return 1;
        else if (compareOrder < 0)
            return -1;
        else
            return 0;
    }

    @JsonIgnore
    public boolean hasSameValues(PatternModel otherPattern) {
        return otherPattern != null
                && Type == otherPattern.Type
                && IsHorizontallyExpendable == otherPattern.IsHorizontallyExpendable
                && IsVerticallyExpendable == otherPattern.IsVerticallyExpendable
                && IsInverted == otherPattern.IsInverted
                && NumberBottlesByRow == otherPattern.NumberBottlesByRow
                && NumberBottlesByColumn == otherPattern.NumberBottlesByColumn;
    }

    @JsonIgnore
    public boolean isValid() {
        return Type != null && NumberBottlesByColumn != 0 && NumberBottlesByRow != 0;
    }

    @JsonIgnore
    public int getId() {
        return Id;
    }

    @JsonIgnore
    public Map<CoordinatesModel, CavePlaceModel> getPlaceMapForDisplay() {
        Map<CoordinatesModel, CavePlaceModel> placeMapForDisplay = new HashMap<>(PlaceMap.size());
        for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> placeMapEntry : PlaceMap.entrySet()) {
            CoordinatesModel coordinates = placeMapEntry.getKey();
            CavePlaceModel cavePlace = new CavePlaceModel();
            cavePlace.PlaceType = placeMapEntry.getValue();
            placeMapForDisplay.put(coordinates, cavePlace);
        }
        return placeMapForDisplay;
    }

    @JsonIgnore
    public int getCapacityAlone() {
        switch (Type) {
            case LINEAR:
                return NumberBottlesByColumn * NumberBottlesByRow;
            case STAGGERED_ROWS:
                return getCapacityForEvenRows() + getCapacityForOddRows();
            default:
                return 0;
        }
    }

    @JsonIgnore
    private int getCapacityForEvenRows() {
        // on an even row : NumberBottlesByRow (-1 if inverted))
        // number of even rows : NumberBottlesByColumn
        return NumberBottlesByColumn * (NumberBottlesByRow - (IsInverted ? 1 : 0));
    }

    @JsonIgnore
    private int getCapacityForOddRows() {
        // on an odd row : NumberBottlesByRow (-1 if not inverted)
        // number of odd rows : NumberBottlesByColumn -1
        return (NumberBottlesByColumn - 1) * (NumberBottlesByRow - (IsInverted ? 0 : 1));
    }

    @JsonIgnore
    public boolean isPatternHorizontallyCompatible(PatternModel otherPattern) {
        // if both patterns not IsHorizontallyExpendable -> false
        // if otherPattern null -> false
        if (!IsHorizontallyExpendable || otherPattern == null || !otherPattern.IsHorizontallyExpendable)
            return false;

        // if same IsInverted and same NumberBottlesByColumn -> true
        return IsInverted == otherPattern.IsInverted && NumberBottlesByColumn == otherPattern.NumberBottlesByColumn;
    }

    @JsonIgnore
    public boolean isPatternVerticallyCompatible(PatternModel otherPattern) {
        // if both patterns not IsVerticallyExpendable -> false
        // if otherPattern null -> false
        if (!IsVerticallyExpendable || otherPattern == null || !otherPattern.IsVerticallyExpendable)
            return false;

        // if same IsInverted and same NumberBottlesByRow -> true
        return IsInverted == otherPattern.IsInverted && NumberBottlesByRow == otherPattern.NumberBottlesByRow;
    }

    @JsonIgnore
    public void trimAll() {
    }
}
