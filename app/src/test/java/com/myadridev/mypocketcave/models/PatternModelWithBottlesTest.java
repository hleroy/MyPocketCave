package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.models.v2.CavePlaceModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternModelWithBottlesTest {

    @Test
    public void createVoidPatternModelWithBottles() {
        PatternModelWithBottlesV2 pattern = new PatternModelWithBottlesV2();

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
        PatternModelV2 pattern = new PatternModelV2();
        pattern.PlaceMap.put(new CoordinatesModelV2(0, 0), CavePlaceTypeEnumV2.br);
        pattern.PlaceMap.put(new CoordinatesModelV2(1, 0), CavePlaceTypeEnumV2.n);
        pattern.PlaceMap.put(new CoordinatesModelV2(0, 1), CavePlaceTypeEnumV2.tl);

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);

        assertEquals(patternWithBottles.PlaceMap.size(), patternWithBottles.PlaceMapWithBottles.size());
    }

    @Test
    public void setClickablePlacesLinear() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.l;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;
        pattern.computePlacesMap();

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);
        patternWithBottles.setClickablePlaces();

        for (CavePlaceModelV2 placeWithBottle : patternWithBottles.PlaceMapWithBottles.values()) {
            assertTrue(placeWithBottle.IsClickable);
        }
    }

    @Test
    public void setClickablePlacesStaggered() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);
        patternWithBottles.setClickablePlaces();

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 coordinates = placeWithBottleEntry.getKey();
            CavePlaceModelV2 place = placeWithBottleEntry.getValue();

            if (coordinates.Row == 0 || coordinates.Row == 7 || coordinates.Col == 0 || coordinates.Col == 7
                    || place.PlaceType == CavePlaceTypeEnumV2.n) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setRightClickablePlaces() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);
        patternWithBottles.setRightClickablePlaces();

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 coordinates = placeWithBottleEntry.getKey();
            CavePlaceModelV2 place = placeWithBottleEntry.getValue();

            if (coordinates.Col != 7 || place.PlaceType == CavePlaceTypeEnumV2.n) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setLeftClickablePlaces() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);
        patternWithBottles.setLeftClickablePlaces();

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 coordinates = placeWithBottleEntry.getKey();
            CavePlaceModelV2 place = placeWithBottleEntry.getValue();

            if (coordinates.Col != 0 || place.PlaceType == CavePlaceTypeEnumV2.n) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setTopClickablePlaces() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);
        patternWithBottles.setTopClickablePlaces();

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 coordinates = placeWithBottleEntry.getKey();
            CavePlaceModelV2 place = placeWithBottleEntry.getValue();

            if (coordinates.Row != 7 || place.PlaceType == CavePlaceTypeEnumV2.n) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setBottomClickablePlaces() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.computePlacesMap();

        PatternModelWithBottlesV2 patternWithBottles = new PatternModelWithBottlesV2(pattern);
        patternWithBottles.setBottomClickablePlaces();

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> placeWithBottleEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 coordinates = placeWithBottleEntry.getKey();
            CavePlaceModelV2 place = placeWithBottleEntry.getValue();

            if (coordinates.Row != 0 || place.PlaceType == CavePlaceTypeEnumV2.n) {
                assertFalse(place.IsClickable);
            } else {
                assertTrue(place.IsClickable);
            }
        }
    }
}