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
        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> entry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceModelV2 value = entry.getValue();
            assertTrue(patternWithBottles.PlaceMap.containsKey(key));
            assertEquals(patternWithBottles.PlaceMap.get(key), value.PlaceType);
        }
    }

    @Test
    public void createPatternModelWithBottlesFromExisting() {
        PatternModelWithBottlesV2 expectedPattern = new PatternModelWithBottlesV2();

        expectedPattern.Id = 4;
        expectedPattern.Type = PatternTypeEnumV2.s;
        expectedPattern.NumberBottlesByColumn = 7;
        expectedPattern.NumberBottlesByRow = 5;
        expectedPattern.IsHorizontallyExpendable = true;
        expectedPattern.IsVerticallyExpendable = false;
        expectedPattern.IsInverted = true;
        expectedPattern.Order = 8;
        expectedPattern.PlaceMap.put(new CoordinatesModelV2(0, 0), CavePlaceTypeEnumV2.br);
        expectedPattern.PlaceMap.put(new CoordinatesModelV2(1, 0), CavePlaceTypeEnumV2.n);
        expectedPattern.PlaceMap.put(new CoordinatesModelV2(0, 1), CavePlaceTypeEnumV2.tl);
        CavePlaceModelV2 place1 = new CavePlaceModelV2();
        place1.BottleId = 5;
        expectedPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(0, 0), place1);
        CavePlaceModelV2 place2 = new CavePlaceModelV2();
        place2.BottleId = 4;
        expectedPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(1, 0), place2);
        CavePlaceModelV2 place3 = new CavePlaceModelV2();
        place3.BottleId = 1;
        expectedPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(0, 1), place3);

        PatternModelWithBottlesV2 pattern = new PatternModelWithBottlesV2(expectedPattern);

        assertEquals(expectedPattern.Id, pattern.Id);
        assertEquals(expectedPattern.Type, pattern.Type);
        assertEquals(expectedPattern.NumberBottlesByColumn, pattern.NumberBottlesByColumn);
        assertEquals(expectedPattern.NumberBottlesByRow, pattern.NumberBottlesByRow);
        assertEquals(expectedPattern.IsHorizontallyExpendable, pattern.IsHorizontallyExpendable);
        assertEquals(expectedPattern.IsVerticallyExpendable, pattern.IsVerticallyExpendable);
        assertEquals(expectedPattern.IsInverted, pattern.IsInverted);
        assertEquals(expectedPattern.Order, pattern.Order);
        assertEquals(expectedPattern.PlaceMap.size(), pattern.PlaceMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> entry : pattern.PlaceMap.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceTypeEnumV2 value = entry.getValue();
            assertTrue(expectedPattern.PlaceMap.containsKey(key));
            assertEquals(expectedPattern.PlaceMap.get(key), value);
        }
        assertEquals(expectedPattern.PlaceMapWithBottles.size(), pattern.PlaceMapWithBottles.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> entry : pattern.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceModelV2 value = entry.getValue();
            assertTrue(expectedPattern.PlaceMapWithBottles.containsKey(key));
            assertEquals(expectedPattern.PlaceMapWithBottles.get(key).BottleId, value.BottleId);
        }
    }

    @Test
    public void createPatternModelWithBottlesFromPatternModelAndExisting() {
        PatternModelWithBottlesV2 oldPattern = new PatternModelWithBottlesV2();
        oldPattern.Id = 4;
        oldPattern.Type = PatternTypeEnumV2.l;
        oldPattern.NumberBottlesByColumn = 7;
        oldPattern.NumberBottlesByRow = 5;
        oldPattern.IsHorizontallyExpendable = true;
        oldPattern.IsVerticallyExpendable = false;
        oldPattern.IsInverted = true;
        oldPattern.Order = 8;
        oldPattern.PlaceMap.put(new CoordinatesModelV2(0, 0), CavePlaceTypeEnumV2.br);
        oldPattern.PlaceMap.put(new CoordinatesModelV2(1, 0), CavePlaceTypeEnumV2.n);
        oldPattern.PlaceMap.put(new CoordinatesModelV2(0, 1), CavePlaceTypeEnumV2.tl);
        oldPattern.PlaceMap.put(new CoordinatesModelV2(1, 1), CavePlaceTypeEnumV2.tl);
        CavePlaceModelV2 place1 = new CavePlaceModelV2();
        place1.BottleId = 5;
        oldPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(0, 0), place1);
        CavePlaceModelV2 place2 = new CavePlaceModelV2();
        place2.BottleId = 4;
        oldPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(1, 0), place2);
        CavePlaceModelV2 place3 = new CavePlaceModelV2();
        place3.BottleId = 1;
        oldPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(0, 1), place3);
        CavePlaceModelV2 place4 = new CavePlaceModelV2();
        place4.BottleId = 1;
        oldPattern.PlaceMapWithBottles.put(new CoordinatesModelV2(1, 1), place4);

        PatternModelV2 patternModel = new PatternModelV2();
        patternModel.Type = PatternTypeEnumV2.s;
        patternModel.Id = 4;
        patternModel.PlaceMap.put(new CoordinatesModelV2(0, 0), CavePlaceTypeEnumV2.br);
        patternModel.PlaceMap.put(new CoordinatesModelV2(1, 0), CavePlaceTypeEnumV2.n);

        PatternModelWithBottlesV2 pattern = new PatternModelWithBottlesV2(patternModel, oldPattern);

        assertEquals(patternModel.Id, pattern.Id);
        assertEquals(patternModel.Type, pattern.Type);
        assertEquals(patternModel.NumberBottlesByColumn, pattern.NumberBottlesByColumn);
        assertEquals(patternModel.NumberBottlesByRow, pattern.NumberBottlesByRow);
        assertEquals(patternModel.IsHorizontallyExpendable, pattern.IsHorizontallyExpendable);
        assertEquals(patternModel.IsVerticallyExpendable, pattern.IsVerticallyExpendable);
        assertEquals(patternModel.IsInverted, pattern.IsInverted);
        assertEquals(patternModel.Order, pattern.Order);
        assertEquals(patternModel.PlaceMap.size(), pattern.PlaceMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> entry : pattern.PlaceMap.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceTypeEnumV2 value = entry.getValue();
            assertTrue(oldPattern.PlaceMap.containsKey(key));
            assertEquals(oldPattern.PlaceMap.get(key), value);
        }
        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> entry : pattern.PlaceMapWithBottles.entrySet()) {
            assertEquals(-1, entry.getValue().BottleId);
        }

        patternModel.Type = PatternTypeEnumV2.l;
        oldPattern.Type = PatternTypeEnumV2.s;
        pattern = new PatternModelWithBottlesV2(patternModel, oldPattern);

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> entry : pattern.PlaceMapWithBottles.entrySet()) {
            assertEquals(-1, entry.getValue().BottleId);
        }

        patternModel.Type = PatternTypeEnumV2.l;
        oldPattern.Type = PatternTypeEnumV2.l;
        pattern = new PatternModelWithBottlesV2(patternModel, oldPattern);

        assertEquals(2, pattern.PlaceMapWithBottles.size());
        assertTrue(pattern.PlaceMapWithBottles.containsKey(new CoordinatesModelV2(0, 0)));
        assertTrue(pattern.PlaceMapWithBottles.containsKey(new CoordinatesModelV2(1, 0)));

        for (Map.Entry<CoordinatesModelV2, CavePlaceModelV2> entry : pattern.PlaceMapWithBottles.entrySet()) {
            CoordinatesModelV2 key = entry.getKey();
            CavePlaceModelV2 value = entry.getValue();
            assertTrue(oldPattern.PlaceMapWithBottles.containsKey(key));
            assertEquals(oldPattern.PlaceMapWithBottles.get(key).BottleId, value.BottleId);
        }
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