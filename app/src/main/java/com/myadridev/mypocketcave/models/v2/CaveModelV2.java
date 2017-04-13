package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.util.List;

public class CaveModelV2 implements IStorableModel, Comparable<CaveModelV2>, ICaveModel {

    @SerializedName("i")
    public int Id;
    @SerializedName("n")
    public String Name = "";
    @SerializedName("ct")
    public CaveTypeEnumV2 CaveType;
    @SerializedName("ca")
    public CaveArrangementModelV2 CaveArrangement;

    //TODO : photo

    public CaveModelV2() {
        CaveArrangement = new CaveArrangementModelV2();
    }

    public CaveModelV2(CaveModelV2 cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        CaveArrangement = new CaveArrangementModelV2(cave.CaveArrangement);
    }

    @Override
    public int compareTo(@NonNull CaveModelV2 otherCave) {
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
        return CaveType != null && Name != null && CaveArrangement != null;
    }

    public int getNumberBottles(int bottleId) {
        return CaveArrangement.IntNumberPlacedBottlesByIdMap.containsKey(bottleId) ? CaveArrangement.IntNumberPlacedBottlesByIdMap.get(bottleId) : 0;
    }

    public List<BottleModelV2> getBottles() {
        return BottleManager.getBottles(CaveArrangement.IntNumberPlacedBottlesByIdMap.keySet());
    }

    public void trimAll() {
        Name = Name.trim();
    }
}