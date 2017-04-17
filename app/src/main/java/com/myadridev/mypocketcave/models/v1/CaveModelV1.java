package com.myadridev.mypocketcave.models.v1;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.CaveTypeEnumV1;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

@Deprecated
@JsonSerialize(as = CaveModelV1.class)
public class CaveModelV1 implements IStorableModel, Comparable<CaveModelV1>, ICaveModel {

    public int Id;
    public String Name = "";
    public CaveTypeEnumV1 CaveType;
    public CaveArrangementModelV1 CaveArrangement;

    //TODO : photo

    public CaveModelV1() {
        CaveArrangement = new CaveArrangementModelV1();
    }

    public CaveModelV1(CaveModelV1 cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        CaveArrangement = new CaveArrangementModelV1(cave.CaveArrangement);
    }

    @Override
    public int compareTo(@NonNull CaveModelV1 otherCave) {
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
    public void trimAll() {
        Name = Name.trim();
    }

    @JsonIgnore
    public int getNumberBottles(int bottleId) {
        return CaveArrangement.IntNumberPlacedBottlesByIdMap.containsKey(bottleId) ? CaveArrangement.IntNumberPlacedBottlesByIdMap.get(bottleId) : 0;
    }
}
