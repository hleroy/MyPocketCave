package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.models.v2.CavePlaceModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;

public class PatternInfos {

    public final CavePlaceModelV2 CavePlace;
    public final PatternModelWithBottlesV2 Pattern;
    public final CoordinatesModelV2 PatternCoordinates;
    public final CoordinatesModelV2 CavePlaceCoordinates;

    public PatternInfos(CavePlaceModelV2 cavePlace, PatternModelWithBottlesV2 pattern, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 cavePlaceCoordinates) {
        CavePlace = cavePlace;
        Pattern = pattern;
        PatternCoordinates = patternCoordinates;
        CavePlaceCoordinates = cavePlaceCoordinates;
    }
}