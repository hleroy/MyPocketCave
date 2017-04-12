package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ISyncModel;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = SyncModel.class)
public class SyncModel implements ISyncModel {

    public String Version = "";
    public List<CaveModel> Caves;
    public List<BottleModel> Bottles;
    public List<PatternModel> Patterns;

    public SyncModel() {
        Caves = new ArrayList<>();
        Bottles = new ArrayList<>();
        Patterns = new ArrayList<>();
    }

    public SyncModel(String version, List<CaveModel> caves, List<BottleModel> bottles, List<PatternModel> patterns) {
        Version = version;
        Caves = new ArrayList<>(caves);
        Bottles = new ArrayList<>(bottles);
        Patterns = new ArrayList<>(patterns);
    }
}
