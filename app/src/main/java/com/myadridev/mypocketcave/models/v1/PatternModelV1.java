package com.myadridev.mypocketcave.models.v1;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.CavePlaceTypeEnumV1;
import com.myadridev.mypocketcave.enums.v1.PatternTypeEnumV1;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@JsonSerialize(as = PatternModelV1.class)
public class PatternModelV1 implements IStorableModel, Comparable<PatternModelV1>, IPatternModel {

    @JsonSerialize(keyUsing = CoordinatesModelSerializerV1.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializerV1.class)
    public final Map<CoordinatesModelV1, CavePlaceTypeEnumV1> PlaceMap;
    public int Id;
    public PatternTypeEnumV1 Type;
    public int NumberBottlesByColumn;
    public int NumberBottlesByRow;
    public boolean IsHorizontallyExpendable;
    public boolean IsVerticallyExpendable;
    public boolean IsInverted;
    public int Order;

    public PatternModelV1() {
        PlaceMap = new HashMap<>();
    }

    public PatternModelV1(PatternModelV1 pattern) {
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
    public int compareTo(@NonNull PatternModelV1 otherPattern) {
        int compareOrder = Order - otherPattern.Order;
        if (compareOrder > 0)
            return 1;
        else if (compareOrder < 0)
            return -1;
        else
            return 0;
    }

    @JsonIgnore
    public boolean hasSameValues(PatternModelV1 otherPattern) {
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
    public Map<CoordinatesModelV1, CavePlaceModelV1> getPlaceMapForDisplay() {
        Map<CoordinatesModelV1, CavePlaceModelV1> placeMapForDisplay = new HashMap<>(PlaceMap.size());
        for (Map.Entry<CoordinatesModelV1, CavePlaceTypeEnumV1> placeMapEntry : PlaceMap.entrySet()) {
            CoordinatesModelV1 coordinates = placeMapEntry.getKey();
            CavePlaceModelV1 cavePlace = new CavePlaceModelV1();
            cavePlace.PlaceType = placeMapEntry.getValue();
            placeMapForDisplay.put(coordinates, cavePlace);
        }
        return placeMapForDisplay;
    }

    @JsonIgnore
    public void trimAll() {
    }
}
