package com.myadridev.mypocketcave.models.v2;

public class PatternInfosV2 {

    public CavePlaceModelV2 CavePlace;
    public PatternModelWithBottlesV2 Pattern;
    public CoordinatesModelV2 PatternCoordinates;
    public CoordinatesModelV2 CavePlaceCoordinates;

    public PatternInfosV2(CavePlaceModelV2 cavePlace, PatternModelWithBottlesV2 pattern, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 cavePlaceCoordinates) {
        CavePlace = cavePlace;
        Pattern = pattern;
        PatternCoordinates = patternCoordinates;
        CavePlaceCoordinates = cavePlaceCoordinates;
    }
}