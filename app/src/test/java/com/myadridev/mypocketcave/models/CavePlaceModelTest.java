package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
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

    @Test
    public void createCavePlaceModelFromExisting() {
        CavePlaceModelV2 expectedCavePlace = new CavePlaceModelV2();
        expectedCavePlace.BottleId = 7;
        expectedCavePlace.IsClickable = false;
        expectedCavePlace.PlaceType = CavePlaceTypeEnumV2.bla;

        CavePlaceModelV2 cavePlace = new CavePlaceModelV2(expectedCavePlace);

        assertEquals(expectedCavePlace.BottleId, cavePlace.BottleId);
        assertEquals(expectedCavePlace.IsClickable, cavePlace.IsClickable);
        assertEquals(expectedCavePlace.PlaceType, cavePlace.PlaceType);
    }
}