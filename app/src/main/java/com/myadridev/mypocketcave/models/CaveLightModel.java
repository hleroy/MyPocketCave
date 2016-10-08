package com.myadridev.mypocketcave.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;

@JsonSerialize(as = CaveLightModel.class)
public class CaveLightModel implements IStorableModel, Comparable<CaveLightModel> {

    public int Id;
    public String Name;
    public CaveTypeEnum CaveType;
    public int TotalCapacity;
    public int TotalUsed;

    //TODO : photo

    public CaveLightModel() {
    }

    public CaveLightModel(CaveLightModel cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        TotalCapacity = cave.TotalCapacity;
        TotalUsed = cave.TotalUsed;
    }

    public CaveLightModel(CaveModel cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        TotalCapacity = cave.CaveArrangement.TotalCapacity;
        TotalUsed = cave.CaveArrangement.TotalUsed;
    }

    @Override
    public int compareTo(@NonNull CaveLightModel otherCave) {
        int compareType = CaveType.id - otherCave.CaveType.id;
        if (compareType > 0)
            return 1;
        else if (compareType < 0)
            return -1;
        else {
            int compareName = Name.compareToIgnoreCase(otherCave.Name);
            if (compareName < 0) {
                return -1;
            } else if (compareName > 0) {
                return 1;
            } else {

                return 0;
            }
        }
    }

    @JsonIgnore
    public int getId() {
        return Id;
    }

    @JsonIgnore
    public boolean isValid() {
        return CaveType != null && Name != null;
    }
}
