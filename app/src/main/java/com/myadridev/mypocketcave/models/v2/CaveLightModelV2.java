package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

@JsonSerialize(as = CaveLightModelV2.class)
public class CaveLightModelV2 implements IStorableModel, Comparable<CaveLightModelV2>, ICaveLightModel {

    @JsonProperty("i")
    public int Id;
    @JsonProperty("n")
    public String Name;
    @JsonProperty("ct")
    public CaveTypeEnumV2 CaveType;
    @JsonProperty("tc")
    public int TotalCapacity;
    @JsonProperty("tu")
    public int TotalUsed;

    //TODO : photo

    public CaveLightModelV2() {
    }

    public CaveLightModelV2(CaveLightModelV2 cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        TotalCapacity = cave.TotalCapacity;
        TotalUsed = cave.TotalUsed;
    }

    public CaveLightModelV2(CaveModelV2 cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        TotalCapacity = cave.CaveArrangement.TotalCapacity;
        TotalUsed = cave.CaveArrangement.TotalUsed;
    }

    @Override
    public int compareTo(@NonNull CaveLightModelV2 otherCave) {
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
