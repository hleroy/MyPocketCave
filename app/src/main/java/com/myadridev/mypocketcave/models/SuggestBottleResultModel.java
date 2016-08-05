package com.myadridev.mypocketcave.models;

public class SuggestBottleResultModel implements Comparable<SuggestBottleResultModel> {

    public int Score;
    public BottleModel Bottle;

    @Override
    public int compareTo(SuggestBottleResultModel otherResult) {
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
