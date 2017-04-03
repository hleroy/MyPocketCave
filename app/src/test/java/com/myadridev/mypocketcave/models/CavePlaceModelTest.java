package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.models.v1.CavePlaceModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class CavePlaceModelTest {

    @Test
    public void createCavePlaceModel() {
        CavePlaceModel cavePlace = new CavePlaceModel();
        assertEquals(-1, cavePlace.BottleId);
        assertFalse(cavePlace.IsClickable);
        assertNull(cavePlace.PlaceType);
    }
}