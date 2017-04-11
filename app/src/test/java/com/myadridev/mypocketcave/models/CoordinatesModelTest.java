package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CoordinatesModelTest {

    @Test
    public void createVoidCoordinatesModel() {
        CoordinatesModelV2 coordinates = new CoordinatesModelV2();
        assertEquals(0, coordinates.Row);
        assertEquals(0, coordinates.Col);
    }

    @Test
    public void createCoordinatesModelFromExisting() {
        CoordinatesModelV2 coordinates = new CoordinatesModelV2(2, 3);
        assertEquals(2, coordinates.Row);
        assertEquals(3, coordinates.Col);
    }

    @Test
    public void equals() {
        CoordinatesModelV2 coordinates1 = new CoordinatesModelV2(2, 3);
        CoordinatesModelV2 coordinates2 = new CoordinatesModelV2(3, 3);
        CoordinatesModelV2 coordinates3 = new CoordinatesModelV2(2, 3);

        assertNotEquals(null, coordinates1);
        assertNotEquals("", coordinates1);
        assertNotEquals(coordinates1, coordinates2);
        assertEquals(coordinates1, coordinates3);
    }
}