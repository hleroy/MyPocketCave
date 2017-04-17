package com.myadridev.mypocketcave.models.v1;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.CaveTypeEnumV1;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

@Deprecated
@JsonSerialize(as = CaveLightModelV1.class)
public class CaveLightModelV1 implements IStorableModel, Comparable<CaveLightModelV1>, ICaveLightModel {

    public int Id;
    public String Name = "";
    public CaveTypeEnumV1 CaveType;
    public int TotalCapacity;
    public int TotalUsed;

    //TODO : photo

    public CaveLightModelV1() {
    }

    public CaveLightModelV1(CaveLightModelV1 cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        TotalCapacity = cave.TotalCapacity;
        TotalUsed = cave.TotalUsed;
    }

    public CaveLightModelV1(CaveModelV1 cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        TotalCapacity = cave.CaveArrangement.TotalCapacity;
        TotalUsed = cave.CaveArrangement.TotalUsed;
    }

    @Override
    public int compareTo(@NonNull CaveLightModelV1 otherCave) {
        int compareType = CaveType.Id - otherCave.CaveType.Id;
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

    @JsonIgnore
    public void trimAll() {
        Name = Name.trim();
    }
}
