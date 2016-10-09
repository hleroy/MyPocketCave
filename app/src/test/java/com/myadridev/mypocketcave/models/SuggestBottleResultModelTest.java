package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.WineColorEnum;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by adrie on 09/10/2016.
 */
public class SuggestBottleResultModelTest {

    @Test
    public void compareWithDifferentScores() {
        SuggestBottleResultModel result1 = new SuggestBottleResultModel();
        result1.Score = 1;
        SuggestBottleResultModel result2 = new SuggestBottleResultModel();
        result2.Score = 3;

        assertEquals(1, result1.compareTo(result2));
        assertEquals(-1, result2.compareTo(result1));
    }

    @Test
    public void compareWithIdenticalScores() {
        SuggestBottleResultModel result1 = new SuggestBottleResultModel();
        result1.Score = 1;
        result1.Bottle = new BottleModel();
        result1.Bottle.WineColor = WineColorEnum.CHAMPAGNE;
        SuggestBottleResultModel result2 = new SuggestBottleResultModel();
        result2.Score = 1;
        result2.Bottle = new BottleModel();
        result2.Bottle.WineColor = WineColorEnum.RED;

        assertEquals(result1.Bottle.compareTo(result2.Bottle), result1.compareTo(result2));
    }
}