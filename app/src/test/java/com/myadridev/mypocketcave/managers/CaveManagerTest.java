package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.models.v2.CaveArrangementModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaveManagerTest {

    @Test
    public void getNumberBottles() {
        CaveModelV2 cave = new CaveModelV2();
        CaveArrangementModelV2 arrangement = new CaveArrangementModelV2();
        arrangement.IntNumberPlacedBottlesByIdMap.put(4, 6);
        cave.CaveArrangement = arrangement;

        assertEquals(0, CaveManager.getNumberBottles(cave, 3));
        assertEquals(6, CaveManager.getNumberBottles(cave, 4));
    }
}