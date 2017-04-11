package com.myadridev.mypocketcave.models.v1;

public class PatternInfos {
    public CavePlaceModel CavePlace;
    public PatternModelWithBottles Pattern;
    public CoordinatesModel PatternCoordinates;
    public CoordinatesModel CavePlaceCoordinates;

    public PatternInfos(CavePlaceModel cavePlace, PatternModelWithBottles pattern, CoordinatesModel patternCoordinates, CoordinatesModel cavePlaceCoordinates) {
        CavePlace = cavePlace;
        Pattern = pattern;
        PatternCoordinates = patternCoordinates;
        CavePlaceCoordinates = cavePlaceCoordinates;
    }
}