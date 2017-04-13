package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

public class CaveLightModelV2 implements IStorableModel, Comparable<CaveLightModelV2>, ICaveLightModel {

    @SerializedName("i")
    public int Id;
    @SerializedName("n")
    public String Name = "";
    @SerializedName("ct")
    public CaveTypeEnumV2 CaveType;
    @SerializedName("tc")
    public int TotalCapacity;
    @SerializedName("tu")
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

    public int getId() {
        return Id;
    }

    public boolean isValid() {
        return CaveType != null && Name != null;
    }

    public void trimAll() {
        Name = Name.trim();
    }
}
