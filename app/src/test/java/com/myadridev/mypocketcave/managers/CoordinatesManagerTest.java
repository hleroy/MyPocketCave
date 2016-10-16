package com.myadridev.mypocketcave.managers;

import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.models.CoordinatesModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by adrie on 09/10/2016.
 */
public class CoordinatesManagerTest {

    @Test
    public void getMaxRowCol() {
        List<CoordinatesModel> coordinatesList = getCoordinatesCollection();
        CoordinatesModel expectedMaxRowCol = new CoordinatesModel(5, 4);
        CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(coordinatesList);
        assertEquals(expectedMaxRowCol, maxRowCol);
    }

    @NonNull
    private List<CoordinatesModel> getCoordinatesCollection() {
        List<CoordinatesModel> coordinatesList = new ArrayList<>(5);
        coordinatesList.add(new CoordinatesModel(0, 0));
        coordinatesList.add(new CoordinatesModel(2, 4));
        coordinatesList.add(new CoordinatesModel(3, 2));
        coordinatesList.add(new CoordinatesModel(1, 1));
        coordinatesList.add(new CoordinatesModel(5, 2));
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
    public void containsRow() {
        List<CoordinatesModel> coordinatesList = getCoordinatesCollection();
        assertTrue(CoordinatesManager.containsRow(coordinatesList, 2));
        assertTrue(CoordinatesManager.containsRow(coordinatesList, 4));
        assertFalse(CoordinatesManager.containsRow(coordinatesList, 6));
    }

    @Test
    public void containsCol() {
        List<CoordinatesModel> coordinatesList = getCoordinatesCollection();
        assertTrue(CoordinatesManager.containsCol(coordinatesList, 2));
        assertTrue(CoordinatesManager.containsCol(coordinatesList, 4));
        assertFalse(CoordinatesManager.containsCol(coordinatesList, 5));
    }

    @Test
    public void getMaxCol() {
        List<CoordinatesModel> coordinatesList = getCoordinatesCollection();
        assertEquals(4, CoordinatesManager.getMaxCol(coordinatesList));
    }

    @Test
    public void getMaxRow() {
        List<CoordinatesModel> coordinatesList = getCoordinatesCollection();
        assertEquals(5, CoordinatesManager.getMaxRow(coordinatesList));
    }
}