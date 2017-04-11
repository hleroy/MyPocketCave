package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleResultModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SuggestBottleResultModelTest {

    @Test
    public void compareWithDifferentScores() {
        SuggestBottleResultModelV2 result1 = new SuggestBottleResultModelV2();
        result1.Score = 1;
        SuggestBottleResultModelV2 result2 = new SuggestBottleResultModelV2();
        result2.Score = 3;

        assertEquals(1, result1.compareTo(result2));
        assertEquals(-1, result2.compareTo(result1));
    }

    @Test
    public void compareWithIdenticalScores() {
        SuggestBottleResultModelV2 result1 = new SuggestBottleResultModelV2();
        result1.Score = 1;
        result1.Bottle = new BottleModelV2();
        result1.Bottle.WineColor = WineColorEnumV2.c;
        SuggestBottleResultModelV2 result2 = new SuggestBottleResultModelV2();
        result2.Score = 1;
        result2.Bottle = new BottleModelV2();
        result2.Bottle.WineColor = WineColorEnumV2.r;

        assertEquals(result1.Bottle.compareTo(result2.Bottle), result1.compareTo(result2));
    }
}