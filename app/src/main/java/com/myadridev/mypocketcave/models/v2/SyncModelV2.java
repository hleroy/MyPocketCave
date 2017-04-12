package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ISyncModel;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = SyncModelV2.class)
public class SyncModelV2 implements ISyncModel {

    @JsonProperty("v")
    public String Version = "";
    @JsonProperty("c")
    public final List<CaveModelV2> Caves;
    @JsonProperty("b")
    public final List<BottleModelV2> Bottles;
    @JsonProperty("p")
    public final List<PatternModelV2> Patterns;

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
