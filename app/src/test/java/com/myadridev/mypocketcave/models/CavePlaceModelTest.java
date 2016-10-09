package com.myadridev.mypocketcave.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by adrie on 09/10/2016.
 */
public class CavePlaceModelTest {

    @Test
    public void createCavePlaceModel() {
        CavePlaceModel cavePlace = new CavePlaceModel();
        assertEquals(-1, cavePlace.BottleId);
        assertFalse(cavePlace.IsClickable);
        assertNull(cavePlace.PlaceType);
    }
}