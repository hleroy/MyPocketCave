package com.myadridev.mypocketcave.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.managers.BottleManager;

import java.util.List;

@JsonSerialize(as = CaveModel.class)
public class CaveModel implements IStorableModel, Comparable<CaveModel> {

    public int Id;
    public String Name;
    public CaveTypeEnum CaveType;
    public CaveArrangementModel CaveArrangement;

    //TODO : photo

    public CaveModel() {
        CaveArrangement = new CaveArrangementModel();
    }

    public CaveModel(CaveModel cave) {
        Id = cave.Id;
        Name = cave.Name;
        CaveType = cave.CaveType;
        CaveArrangement = new CaveArrangementModel(cave.CaveArrangement);
    }

    @Override
    public int compareTo(@NonNull CaveModel otherCave) {
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
    public List<BottleModel> getBottles() {
        return BottleManager.getBottles(CaveArrangement.IntNumberPlacedBottlesByIdMap.keySet());
    }

    @JsonIgnore
    public void trimAll() {
        Name = Name.trim();
    }
}
