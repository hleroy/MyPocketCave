package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = SyncModelV2.class)
public class SyncModelV2 {

    public String Version;
    public List<CaveModelV2> Caves;
    public List<BottleModelV2> Bottles;
    public List<PatternModelV2> Patterns;

    public SyncModelV2() {
        Caves = new ArrayList<>();
        Bottles = new ArrayList<>();
        Patterns = new ArrayList<>();
    }

    public SyncModelV2(String version, List<CaveModelV2> caves, List<BottleModelV2> bottles, List<PatternModelV2> patterns) {
        Version = version;
        Caves = new ArrayList<>(caves);
        Bottles = new ArrayList<>(bottles);
        Patterns = new ArrayList<>(patterns);
    }
}
