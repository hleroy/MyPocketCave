package com.myadridev.mypocketcave.models.v1;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = SuggestBottleResultModel.class)
public class SuggestBottleResultModel implements Comparable<SuggestBottleResultModel> {

    public int Score;
    public BottleModel Bottle;

    @Override
    public int compareTo(@NonNull SuggestBottleResultModel otherResult) {
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
