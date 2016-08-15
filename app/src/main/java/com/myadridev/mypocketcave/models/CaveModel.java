package com.myadridev.mypocketcave.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;

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
        return CaveType != null && Name != null && CaveArrangement != null;
    }
}
