package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.models.inferfaces.ISyncModel;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@JsonSerialize(as = SyncModelV1.class)
public class SyncModelV1 implements ISyncModel {

    public final List<CaveModelV1> Caves;
    public final List<BottleModelV1> Bottles;
    public final List<PatternModelV1> Patterns;
    public String Version = "";

    public SyncModelV1() {
        Caves = new ArrayList<>();
        Bottles = new ArrayList<>();
        Patterns = new ArrayList<>();
    }

    public SyncModelV1(String version, List<CaveModelV1> caves, List<BottleModelV1> bottles, List<PatternModelV1> patterns) {
        Version = version;
        Caves = new ArrayList<>(caves);
        Bottles = new ArrayList<>(bottles);
        Patterns = new ArrayList<>(patterns);
    }
}
