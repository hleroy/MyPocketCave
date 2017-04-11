package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.managers.BottleManager;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.util.List;

@JsonSerialize(as = CaveModelV2.class)
public class CaveModelV2 implements IStorableModel, Comparable<CaveModelV2>, ICaveModel {

    @JsonProperty("i")
    public int Id;
    @JsonProperty("n")
    public String Name;
    @JsonProperty("ct")
    public CaveTypeEnumV2 CaveType;
    @JsonProperty("ca")
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

    @JsonIgnore
    public int getId() {
        return Id;
    }

    @JsonIgnore
    public boolean isValid() {
        return CaveType != null && Name != null && CaveArrangement != null;
    }

    @JsonIgnore
    public int getNumberBottles(int bottleId) {
        return CaveArrangement.IntNumberPlacedBottlesByIdMap.containsKey(bottleId) ? CaveArrangement.IntNumberPlacedBottlesByIdMap.get(bottleId) : 0;
    }

    @JsonIgnore
    public List<BottleModelV2> getBottles() {
        return BottleManager.getBottles(CaveArrangement.IntNumberPlacedBottlesByIdMap.keySet());
    }

    @JsonIgnore
    public void trimAll() {
        Name = Name.trim();
    }
}
