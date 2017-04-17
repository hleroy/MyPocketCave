package com.myadridev.mypocketcave.models.v1;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Deprecated
@JsonSerialize(as = SuggestBottleResultModelV1.class)
public class SuggestBottleResultModelV1 implements Comparable<SuggestBottleResultModelV1> {

    public int Score;
    public BottleModelV1 Bottle;

    @Override
    public int compareTo(@NonNull SuggestBottleResultModelV1 otherResult) {
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
