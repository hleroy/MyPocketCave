package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = PatternModel.class)
public class PatternModel implements IStorableModel {

    public int Id;
    public String Name;
    public PatternTypeEnum Type;
    public int NumberRaws;
    public int NumberCols;
    public boolean IsHorizontallyExpendable;
    public boolean IsVerticallyExpandable;

    @JsonSerialize(keyUsing = CoordinatesModelSerializer.class)
    @JsonDeserialize(keyUsing = CoordinatesModelDeserializer.class)
    public final Map<CoordinatesModel, CavePlaceTypeEnum> PlaceMap;

    public PatternModel() {
        PlaceMap = new HashMap<>();
    }

    public PatternModel(PatternModel pattern) {
        Id = pattern.Id;
        Name = pattern.Name;
        Type = pattern.Type;
        NumberRaws = pattern.NumberRaws;
        NumberCols = pattern.NumberCols;
        IsHorizontallyExpendable = pattern.IsHorizontallyExpendable;
        IsVerticallyExpandable = pattern.IsVerticallyExpandable;
        PlaceMap = new HashMap<>(pattern.PlaceMap);
    }

    @JsonIgnore
    public boolean isValid() {
        return Name != null && Type != null;
    }

    @JsonIgnore
    public int getId() {
        return Id;
    }
}
