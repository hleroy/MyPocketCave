package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = PatternModel.class)
public class PatternModel implements IStorableModel, Comparable<PatternModel> {

    public int Id;
    public PatternTypeEnum Type;
    public int NumberBottlesByColumn;
    public int NumberBottlesByRow;
    public boolean IsHorizontallyExpendable;
    public boolean IsVerticallyExpendable;
    public boolean IsInverted;
    public int Order;

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, CavePlaceTypeEnum> PlaceMap;

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
        int numberRows = 2 * NumberBottlesByColumn - 1;
        int numberCols = 2 * NumberBottlesByRow - 1;

        if (IsVerticallyExpendable) {
            // add 1 row at beginning and end
            for (int colIndex = 0; colIndex < numberCols; colIndex++) {
                if (colIndex % 2 == 0) {
                    PlaceMap.put(new CoordinatesModel(0, colIndex + (IsHorizontallyExpendable ? 1 : 0)),
                            IsInverted ? CavePlaceTypeEnum.PLACE_WITH_BOTTOM : CavePlaceTypeEnum.NO_PLACE);
                    PlaceMap.put(new CoordinatesModel(numberRows + 1, colIndex + (IsHorizontallyExpendable ? 1 : 0)),
                            IsInverted ? CavePlaceTypeEnum.PLACE_WITH_TOP : CavePlaceTypeEnum.NO_PLACE);
                } else {
                    PlaceMap.put(new CoordinatesModel(0, colIndex + (IsHorizontallyExpendable ? 1 : 0)),
                            IsInverted ? CavePlaceTypeEnum.NO_PLACE : CavePlaceTypeEnum.PLACE_WITH_BOTTOM);
                    PlaceMap.put(new CoordinatesModel(numberRows + 1, colIndex + (IsHorizontallyExpendable ? 1 : 0)),
                            IsInverted ? CavePlaceTypeEnum.NO_PLACE : CavePlaceTypeEnum.PLACE_WITH_TOP);
                }
            }
        }

        if (IsHorizontallyExpendable) {
            // add 1 column at beginning and end
            for (int rowIndex = 0; rowIndex < numberRows; rowIndex++) {
                if (rowIndex % 2 == 0) {
                    PlaceMap.put(new CoordinatesModel(rowIndex + (IsVerticallyExpendable ? 1 : 0), 0),
                            IsInverted ? CavePlaceTypeEnum.PLACE_WITH_LEFT : CavePlaceTypeEnum.NO_PLACE);
                    PlaceMap.put(new CoordinatesModel(rowIndex + (IsVerticallyExpendable ? 1 : 0), numberCols + 1),
                            IsInverted ? CavePlaceTypeEnum.PLACE_WITH_RIGHT : CavePlaceTypeEnum.NO_PLACE);
                } else {
                    PlaceMap.put(new CoordinatesModel(rowIndex + (IsVerticallyExpendable ? 1 : 0), 0),
                            IsInverted ? CavePlaceTypeEnum.NO_PLACE : CavePlaceTypeEnum.PLACE_WITH_LEFT);
                    PlaceMap.put(new CoordinatesModel(rowIndex + (IsVerticallyExpendable ? 1 : 0), numberCols + 1),
                            IsInverted ? CavePlaceTypeEnum.NO_PLACE : CavePlaceTypeEnum.PLACE_WITH_RIGHT);
                }
            }
        }

        for (int colIndex = 0; colIndex < numberCols; colIndex++) {
            for (int rowIndex = 0; rowIndex < numberRows; rowIndex++) {
                if (rowIndex % 2 == colIndex % 2) {
                    PlaceMap.put(new CoordinatesModel(rowIndex + (IsVerticallyExpendable ? 1 : 0), colIndex + (IsHorizontallyExpendable ? 1 : 0)),
                            IsInverted ? CavePlaceTypeEnum.NO_PLACE : CavePlaceTypeEnum.PLACE);
                } else {
                    PlaceMap.put(new CoordinatesModel(rowIndex + (IsVerticallyExpendable ? 1 : 0), colIndex + (IsHorizontallyExpendable ? 1 : 0)),
                            IsInverted ? CavePlaceTypeEnum.PLACE : CavePlaceTypeEnum.NO_PLACE);
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
                PlaceMap.put(new CoordinatesModel(row, col), CavePlaceTypeEnum.PLACE);
            }
        }
    }

    @JsonIgnore
    public int getNumberColumnsGridLayout() {
        switch (Type) {
            case LINEAR:
                return NumberBottlesByRow;
            case STAGGERED_ROWS:
                return 2 * NumberBottlesByRow - 1 + (IsHorizontallyExpendable ? 2 : 0);
        }
        return 0;
    }

    @JsonIgnore
    public int getNumberRowsGridLayout() {
        switch (Type) {
            case LINEAR:
                return NumberBottlesByColumn;
            case STAGGERED_ROWS:
                return 2 * NumberBottlesByColumn - 1 + (IsVerticallyExpendable ? 2 : 0);
        }
        return 0;
    }

    @Override
    public int compareTo(PatternModel otherPattern) {
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
        return Type == otherPattern.Type
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
}
