package com.myadridev.mypocketcave.managers;

import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoordinatesManagerTest {

    @Test
    public void getMaxRowCol() {
        List<CoordinatesModelV2> coordinatesList = getCoordinatesCollection();
        CoordinatesModelV2 expectedMaxRowCol = new CoordinatesModelV2(5, 4);
        CoordinatesModelV2 maxRowCol = CoordinatesManager.getMaxRowCol(coordinatesList);
        assertEquals(expectedMaxRowCol, maxRowCol);
    }

    @NonNull
    private List<CoordinatesModelV2> getCoordinatesCollection() {
        List<CoordinatesModelV2> coordinatesList = new ArrayList<>(5);
        coordinatesList.add(new CoordinatesModelV2(0, 0));
        coordinatesList.add(new CoordinatesModelV2(2, 4));
        coordinatesList.add(new CoordinatesModelV2(3, 2));
        coordinatesList.add(new CoordinatesModelV2(1, 1));
        coordinatesList.add(new CoordinatesModelV2(5, 2));
        return coordinatesList;
    }

    @Test
    public void getColFromPosition() {
        assertEquals(42, CoordinatesManager.getColFromPosition(42));
    }

    @Test
    public void getRowFromPosition() {
        assertEquals(117, CoordinatesManager.getRowFromPosition(42, 160));
    }

    @Test
    public void getPositionFromCoordinates() {
        assertEquals(5, CoordinatesManager.getPositionFromCoordinates(4, 2, 6, 3));
    }

    @Test
    public void containsRow() {
        List<CoordinatesModelV2> coordinatesList = getCoordinatesCollection();
        assertTrue(CoordinatesManager.containsRow(coordinatesList, 2));
        assertTrue(CoordinatesManager.containsRow(coordinatesList, 4));
        assertFalse(CoordinatesManager.containsRow(coordinatesList, 6));
    }

    @Test
    public void containsCol() {
        List<CoordinatesModelV2> coordinatesList = getCoordinatesCollection();
        assertTrue(CoordinatesManager.containsCol(coordinatesList, 2));
        assertTrue(CoordinatesManager.containsCol(coordinatesList, 4));
        assertFalse(CoordinatesManager.containsCol(coordinatesList, 5));
    }

    @Test
    public void getMaxCol() {
        List<CoordinatesModelV2> coordinatesList = getCoordinatesCollection();
        assertEquals(4, CoordinatesManager.getMaxCol(coordinatesList));
    }

    @Test
    public void getMaxRow() {
        List<CoordinatesModelV2> coordinatesList = getCoordinatesCollection();
        assertEquals(5, CoordinatesManager.getMaxRow(coordinatesList));
    }
}