package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.models.v2.CavePlaceModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class CavePlaceModelTest {

    @Test
    public void createCavePlaceModel() {
        CavePlaceModelV2 cavePlace = new CavePlaceModelV2();
        assertEquals(-1, cavePlace.BottleId);
        assertFalse(cavePlace.IsClickable);
        assertNull(cavePlace.PlaceType);
    }
}