package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = SuggestBottleResultModelV2.class)
public class SuggestBottleResultModelV2 implements Comparable<SuggestBottleResultModelV2> {

    @JsonProperty("s")
    public int Score;
    @JsonProperty("b")
    public BottleModelV2 Bottle;

    @Override
    public int compareTo(@NonNull SuggestBottleResultModelV2 otherResult) {
        int compareScore = otherResult.Score - Score;
        if (compareScore > 0)
            return 1;
        else if (compareScore < 0)
            return -1;
        else {
            return Bottle.compareTo(otherResult.Bottle);
        }
    }
}
