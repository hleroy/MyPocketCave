package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(as = SyncModel.class)
public class SyncModel {

    public List<CaveModel> caves;
    public List<BottleModel> bottles;
    public List<PatternModel> patterns;

    public SyncModel(List<CaveModel> caves, List<BottleModel> bottles, List<PatternModel> patterns) {
        this.caves = caves;
        this.bottles = bottles;
        this.patterns = patterns;
    }
}
