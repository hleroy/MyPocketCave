package com.myadridev.mypocketcave.models.v2;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.models.inferfaces.ISyncModel;

import java.util.ArrayList;
import java.util.List;

public class SyncModelV2 implements ISyncModel {

    @SerializedName("v")
    public String Version = "";
    @SerializedName("c")
    public final List<CaveModelV2> Caves;
    @SerializedName("b")
    public final List<BottleModelV2> Bottles;
    @SerializedName("p")
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
