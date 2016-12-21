package com.myadridev.mypocketcave.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by adrie on 09/10/2016.
 */
public class CoordinatesModelTest {

    @Test
    public void createVoidCoordinatesModel() {
        CoordinatesModel coordinates = new CoordinatesModel();
        assertEquals(0, coordinates.Row);
        assertEquals(0, coordinates.Col);
    }

    @Test
    public void createCoordinatesModelFromExisting() {
        CoordinatesModel coordinates = new CoordinatesModel(2, 3);
        assertEquals(2, coordinates.Row);
        assertEquals(3, coordinates.Col);
    }

    @Test
    public void equals() {
        CoordinatesModel coordinates1 = new CoordinatesModel(2, 3);
        CoordinatesModel coordinates2 = new CoordinatesModel(3, 3);
        CoordinatesModel coordinates3 = new CoordinatesModel(2, 3);

        assertNotEquals(null, coordinates1);
        assertNotEquals("", coordinates1);
        assertNotEquals(coordinates1, coordinates2);
        assertEquals(coordinates1, coordinates3);
    }
}