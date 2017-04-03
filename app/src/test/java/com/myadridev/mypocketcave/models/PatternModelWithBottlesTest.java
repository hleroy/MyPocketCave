package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.models.v1.CavePlaceModel;
import com.myadridev.mypocketcave.models.v1.CoordinatesModel;
import com.myadridev.mypocketcave.models.v1.PatternModel;
import com.myadridev.mypocketcave.models.v1.PatternModelWithBottles;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternModelWithBottlesTest {

    @Test
    public void createVoidPatternModelWithBottles() {
        PatternModelWithBottles pattern = new PatternModelWithBottles();

        assertEquals(0, pattern.Id);
        assertNull(pattern.Type);
        assertEquals(0, pattern.NumberBottlesByColumn);
        assertEquals(0, pattern.NumberBottlesByRow);
        assertFalse(pattern.IsHorizontallyExpendable);
        assertFalse(pattern.IsVerticallyExpendable);
        assertFalse(pattern.IsInverted);
        assertEquals(0, pattern.Order);
        assertTrue(pattern.PlaceMap.isEmpty());
        assertTrue(pattern.PlaceMapWithBottles.isEmpty());
    }

    @Test
    public void createPatternModelWithBottlesFromPatternModel() {
        PatternModel pattern = new PatternModel();
        pattern.PlaceMap.put(new CoordinatesModel(0, 0), CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT);
        pattern.PlaceMap.put(new CoordinatesModel(1, 0), CavePlaceTypeEnum.NO_PLACE);
        pattern.PlaceMap.put(new CoordinatesModel(0, 1), CavePlaceTypeEnum.PLACE_TOP_LEFT);

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);

        assertEquals(patternWithBottles.PlaceMap.size(), patternWithBottles.PlaceMapWithBottles.size());
    }

    @Test
    public void setClickablePlacesLinear() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.LINEAR;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;
        pattern.computePlacesMap();

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);
        patternWithBottles.setClickablePlaces();

        for (CavePlaceModel placeWithBottle : patternWithBottles.PlaceMapWithBottles.values()) {
            assertTrue(placeWithBottle.IsClickable);
        }
    }

    @Test
    public void setClickablePlacesStaggered() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);
        patternWithBottles.setClickablePlaces();

        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel coordinates = placeWithBottleEntry.getKey();
            CavePlaceModel place = placeWithBottleEntry.getValue();

            if (coordinates.Row == 0 || coordinates.Row == 7 || coordinates.Col == 0 || coordinates.Col == 7
                    || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setRightClickablePlaces() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);
        patternWithBottles.setRightClickablePlaces();

        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel coordinates = placeWithBottleEntry.getKey();
            CavePlaceModel place = placeWithBottleEntry.getValue();

            if (coordinates.Col != 7 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setLeftClickablePlaces() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);
        patternWithBottles.setLeftClickablePlaces();

        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel coordinates = placeWithBottleEntry.getKey();
            CavePlaceModel place = placeWithBottleEntry.getValue();

            if (coordinates.Col != 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setTopClickablePlaces() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);
        patternWithBottles.setTopClickablePlaces();

        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel coordinates = placeWithBottleEntry.getKey();
            CavePlaceModel place = placeWithBottleEntry.getValue();

            if (coordinates.Row != 7 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setBottomClickablePlaces() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles(pattern);
        patternWithBottles.setBottomClickablePlaces();

        for (Map.Entry<CoordinatesModel, CavePlaceModel> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel coordinates = placeWithBottleEntry.getKey();
            CavePlaceModel place = placeWithBottleEntry.getValue();

            if (coordinates.Row != 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }
}