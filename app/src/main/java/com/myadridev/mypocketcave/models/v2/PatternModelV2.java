package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.util.HashMap;
import java.util.Map;

public class PatternModelV2 implements IStorableModel, Comparable<PatternModelV2>, IPatternModel {

    @SerializedName("pm")
    public final Map<CoordinatesModelV2, CavePlaceTypeEnumV2> PlaceMap;
    @SerializedName("i")
    public int Id;
    @SerializedName("t")
    public PatternTypeEnumV2 Type;
    @SerializedName("nbc")
    public int NumberBottlesByColumn;
    @SerializedName("nbr")
    public int NumberBottlesByRow;
    @SerializedName("ihe")
    public boolean IsHorizontallyExpendable;
    @SerializedName("ive")
    public boolean IsVerticallyExpendable;
    @SerializedName("ii")
    public boolean IsInverted;
    @SerializedName("o")
    public int Order;

    public PatternModelV2() {
        PlaceMap = new HashMap<>();
    }

    public PatternModelV2(PatternModelV2 pattern) {
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
            case l:
                computeLinearPlacesMap();
                break;
            case s:
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
            PlaceMap.put(new CoordinatesModelV2(0, 0), CavePlaceTypeEnumV2.n);
            PlaceMap.put(new CoordinatesModelV2(0, numberCols + 1), CavePlaceTypeEnumV2.n);
            PlaceMap.put(new CoordinatesModelV2(numberRows + 1, 0), CavePlaceTypeEnumV2.n);
            PlaceMap.put(new CoordinatesModelV2(numberRows + 1, numberCols + 1), CavePlaceTypeEnumV2.n);
        }
    }

    private void computeLinearPlacesMap() {
        for (int row = 0; row < NumberBottlesByColumn; row++) {
            for (int col = 0; col < NumberBottlesByRow; col++) {
                computeFullPlace(PlaceMap, 2 * row, 2 * col);
            }
        }
    }

    private void computeFullPlace(Map<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMap, int row, int col) {
        placeMap.put(new CoordinatesModelV2(row, col), CavePlaceTypeEnumV2.tr);
        placeMap.put(new CoordinatesModelV2(row, col + 1), CavePlaceTypeEnumV2.tl);
        placeMap.put(new CoordinatesModelV2(row + 1, col), CavePlaceTypeEnumV2.br);
        placeMap.put(new CoordinatesModelV2(row + 1, col + 1), CavePlaceTypeEnumV2.bl);
    }

    private void computeFullNoPlace(Map<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMap, int row, int col) {
        placeMap.put(new CoordinatesModelV2(row, col), CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row, col + 1), CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row + 1, col), CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row + 1, col + 1), CavePlaceTypeEnumV2.n);
    }

    private void computePlaceWithRight(Map<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModelV2(row, col), isInverted ? CavePlaceTypeEnumV2.tr : CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row + 1, col), isInverted ? CavePlaceTypeEnumV2.br : CavePlaceTypeEnumV2.n);
    }

    private void computePlaceWithLeft(Map<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModelV2(row, col), isInverted ? CavePlaceTypeEnumV2.tl : CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row + 1, col), isInverted ? CavePlaceTypeEnumV2.bl : CavePlaceTypeEnumV2.n);
    }

    private void computePlaceWithTop(Map<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModelV2(row, col), isInverted ? CavePlaceTypeEnumV2.tr : CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row, col + 1), isInverted ? CavePlaceTypeEnumV2.tl : CavePlaceTypeEnumV2.n);
    }

    private void computePlaceWithBottom(Map<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMap, int row, int col, boolean isInverted) {
        placeMap.put(new CoordinatesModelV2(row, col), isInverted ? CavePlaceTypeEnumV2.br : CavePlaceTypeEnumV2.n);
        placeMap.put(new CoordinatesModelV2(row, col + 1), isInverted ? CavePlaceTypeEnumV2.bl : CavePlaceTypeEnumV2.n);
    }

    public int getNumberColumnsGridLayout() {
        switch (Type) {
            case l:
                return 2 * NumberBottlesByRow;
            case s:
                return 4 * NumberBottlesByRow - (IsHorizontallyExpendable ? 0 : 2);
            default:
                return 0;
        }
    }

    public int getNumberRowsGridLayout() {
        switch (Type) {
            case l:
                return 2 * NumberBottlesByColumn;
            case s:
                return 4 * NumberBottlesByColumn - (IsVerticallyExpendable ? 0 : 2);
            default:
                return 0;
        }
    }

    @Override
    public int compareTo(@NonNull PatternModelV2 otherPattern) {
        int compareOrder = Order - otherPattern.Order;
        if (compareOrder > 0)
            return 1;
        else if (compareOrder < 0)
            return -1;
        else
            return 0;
    }

    public boolean hasSameValues(PatternModelV2 otherPattern) {
        return otherPattern != null
                && Type == otherPattern.Type
                && IsHorizontallyExpendable == otherPattern.IsHorizontallyExpendable
                && IsVerticallyExpendable == otherPattern.IsVerticallyExpendable
                && IsInverted == otherPattern.IsInverted
                && NumberBottlesByRow == otherPattern.NumberBottlesByRow
                && NumberBottlesByColumn == otherPattern.NumberBottlesByColumn;
    }

    public boolean isValid() {
        return Type != null && NumberBottlesByColumn != 0 && NumberBottlesByRow != 0;
    }

    public int getId() {
        return Id;
    }

    public Map<CoordinatesModelV2, CavePlaceModelV2> getPlaceMapForDisplay() {
        Map<CoordinatesModelV2, CavePlaceModelV2> placeMapForDisplay = new HashMap<>(PlaceMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> placeMapEntry : PlaceMap.entrySet()) {
            CoordinatesModelV2 coordinates = placeMapEntry.getKey();
            CavePlaceModelV2 cavePlace = new CavePlaceModelV2();
            cavePlace.PlaceType = placeMapEntry.getValue();
            placeMapForDisplay.put(coordinates, cavePlace);
        }
        return placeMapForDisplay;
    }

    public int getCapacityAlone() {
        switch (Type) {
            case l:
                return NumberBottlesByColumn * NumberBottlesByRow;
            case s:
                return getCapacityForEvenRows() + getCapacityForOddRows();
            default:
                return 0;
        }
    }

    private int getCapacityForEvenRows() {
        // on an even row : NumberBottlesByRow (-1 if inverted))
        // number of even rows : NumberBottlesByColumn
        return NumberBottlesByColumn * (NumberBottlesByRow - (IsInverted ? 1 : 0));
    }

    private int getCapacityForOddRows() {
        // on an odd row : NumberBottlesByRow (-1 if not inverted)
        // number of odd rows : NumberBottlesByColumn -1
        return (NumberBottlesByColumn - 1) * (NumberBottlesByRow - (IsInverted ? 0 : 1));
    }

    public boolean isPatternHorizontallyCompatible(PatternModelV2 otherPattern) {
        // if both patterns not IsHorizontallyExpendable -> false
        // if otherPattern null -> false
        if (!IsHorizontallyExpendable || otherPattern == null || !otherPattern.IsHorizontallyExpendable)
            return false;

        // if same IsInverted and same NumberBottlesByColumn -> true
        return IsInverted == otherPattern.IsInverted && NumberBottlesByColumn == otherPattern.NumberBottlesByColumn;
    }

    public boolean isPatternVerticallyCompatible(PatternModelV2 otherPattern) {
        // if both patterns not IsVerticallyExpendable -> false
        // if otherPattern null -> false
        if (!IsVerticallyExpendable || otherPattern == null || !otherPattern.IsVerticallyExpendable)
            return false;

        // if same IsInverted and same NumberBottlesByRow -> true
        return IsInverted == otherPattern.IsInverted && NumberBottlesByRow == otherPattern.NumberBottlesByRow;
    }

    public void trimAll() {
    }
}
