package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.models.v2.CavePlaceModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternModelTest {

    @Test
    public void createVoidPatternModel() {
        PatternModelV2 pattern = new PatternModelV2();

        assertEquals(0, pattern.Id);
        assertNull(pattern.Type);
        assertEquals(0, pattern.NumberBottlesByColumn);
        assertEquals(0, pattern.NumberBottlesByRow);
        assertFalse(pattern.IsHorizontallyExpendable);
        assertFalse(pattern.IsVerticallyExpendable);
        assertFalse(pattern.IsInverted);
        assertEquals(0, pattern.Order);
        assertTrue(pattern.PlaceMap.isEmpty());
    }

    @Test
    public void createPatternModelFromExisting() {
        PatternModelV2 expectedPattern = new PatternModelV2();
        expectedPattern.Id = 42;
        expectedPattern.Type = PatternTypeEnumV2.l;
        expectedPattern.NumberBottlesByColumn = 3;
        expectedPattern.NumberBottlesByRow = 2;
        expectedPattern.IsHorizontallyExpendable = false;
        expectedPattern.IsVerticallyExpendable = true;
        expectedPattern.IsInverted = false;
        expectedPattern.Order = 2;
        expectedPattern.PlaceMap.put(new CoordinatesModelV2(0, 1), CavePlaceTypeEnumV2.blc);
        expectedPattern.PlaceMap.put(new CoordinatesModelV2(1, 1), CavePlaceTypeEnumV2.brro);

        PatternModelV2 pattern = new PatternModelV2(expectedPattern);

        assertEquals(expectedPattern.Id, pattern.Id);
        assertEquals(expectedPattern.Type, pattern.Type);
        assertEquals(expectedPattern.NumberBottlesByColumn, pattern.NumberBottlesByColumn);
        assertEquals(expectedPattern.NumberBottlesByRow, pattern.NumberBottlesByRow);
        assertEquals(expectedPattern.IsHorizontallyExpendable, pattern.IsHorizontallyExpendable);
        assertEquals(expectedPattern.IsVerticallyExpendable, pattern.IsVerticallyExpendable);
        assertEquals(expectedPattern.IsInverted, pattern.IsInverted);
        assertEquals(expectedPattern.Order, pattern.Order);
        assertEquals(expectedPattern.PlaceMap.size(), pattern.PlaceMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> placeEntry : expectedPattern.PlaceMap.entrySet()) {
            CoordinatesModelV2 coordinate = placeEntry.getKey();
            assertTrue(pattern.PlaceMap.containsKey(coordinate));
            assertEquals(placeEntry.getValue(), pattern.PlaceMap.get(coordinate));
        }
    }

    @Test
    public void computePlacesMapWhenLinear() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.l;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = false;
        pattern.IsVerticallyExpendable = false;
        pattern.IsInverted = false;

        pattern.computePlacesMap();
        assertEquals(24, pattern.PlaceMap.size());
        for (int i = 0; i < pattern.NumberBottlesByColumn; i++) {
            for (int j = 0; j < pattern.NumberBottlesByRow; j++) {
                assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(2 * i, 2 * j)));
                assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(2 * i + 1, 2 * j)));
                assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(2 * i, 2 * j + 1)));
                assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(2 * i + 1, 2 * j + 1)));
            }
        }
    }

    @Test
    public void computePlacesMapWhenStaggeredHorizontallyExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 1;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = false;
        pattern.IsInverted = false;

        pattern.computePlacesMap();
        assertEquals(24, pattern.PlaceMap.size());
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 0)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(0, 1)));
        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(0, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 3)));

        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(1, 0)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(1, 1)));
        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(1, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(1, 3)));

        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(2, 0)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(2, 1)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(2, 2)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(2, 3)));

        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(3, 0)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 1)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 2)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(3, 3)));

        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(4, 0)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(4, 1)));
        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(4, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(4, 3)));

        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(5, 0)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(5, 1)));
        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(5, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(5, 3)));
    }

    @Test
    public void computePlacesMapWhenStaggeredVerticallyExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = false;
        pattern.IsVerticallyExpendable = true;
        pattern.IsInverted = false;

        pattern.computePlacesMap();
        assertEquals(24, pattern.PlaceMap.size());
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 0)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 1)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(0, 2)));
        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(0, 3)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 4)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 5)));

        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(1, 0)));
        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(1, 1)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(1, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(1, 3)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(1, 4)));
        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(1, 5)));

        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(2, 0)));
        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(2, 1)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(2, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(2, 3)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(2, 4)));
        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(2, 5)));

        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 0)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 1)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(3, 2)));
        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(3, 3)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 4)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 5)));
    }

    @Test
    public void computePlacesMapWhenStaggeredInvertedExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 1;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.IsInverted = true;

        pattern.computePlacesMap();
        assertEquals(16, pattern.PlaceMap.size());
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 0)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(0, 1)));
        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(0, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(0, 3)));

        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(1, 0)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(1, 1)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(1, 2)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(1, 3)));

        assertEquals(CavePlaceTypeEnumV2.bl, pattern.PlaceMap.get(new CoordinatesModelV2(2, 0)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(2, 1)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(2, 2)));
        assertEquals(CavePlaceTypeEnumV2.br, pattern.PlaceMap.get(new CoordinatesModelV2(2, 3)));

        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 0)));
        assertEquals(CavePlaceTypeEnumV2.tr, pattern.PlaceMap.get(new CoordinatesModelV2(3, 1)));
        assertEquals(CavePlaceTypeEnumV2.tl, pattern.PlaceMap.get(new CoordinatesModelV2(3, 2)));
        assertEquals(CavePlaceTypeEnumV2.n, pattern.PlaceMap.get(new CoordinatesModelV2(3, 3)));
    }

    @Test
    public void getNumberColumnsGridLayoutLinear() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.l;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;

        assertEquals(4, pattern.getNumberColumnsGridLayout());
    }

    @Test
    public void getNumberColumnsGridLayoutStaggeredHorizontallyExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;

        assertEquals(8, pattern.getNumberColumnsGridLayout());
    }

    @Test
    public void getNumberColumnsGridLayoutStaggeredNotHorizontallyExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = false;

        assertEquals(6, pattern.getNumberColumnsGridLayout());
    }

    @Test
    public void getNumberRowsGridLayoutLinear() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.l;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;

        assertEquals(6, pattern.getNumberRowsGridLayout());
    }

    @Test
    public void getNumberRowsGridLayoutStaggeredVerticallyExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 1;
        pattern.IsVerticallyExpendable = true;

        assertEquals(8, pattern.getNumberRowsGridLayout());
    }

    @Test
    public void getNumberRowsGridLayoutStaggeredNotVerticallyExpendable() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 1;
        pattern.IsVerticallyExpendable = false;

        assertEquals(6, pattern.getNumberRowsGridLayout());
    }

    @Test
    public void comparePatterns() {
        PatternModelV2 pattern1 = new PatternModelV2();
        pattern1.Order = 2;
        PatternModelV2 pattern2 = new PatternModelV2();
        pattern2.Order = 1;
        PatternModelV2 pattern3 = new PatternModelV2();
        pattern3.Order = 3;
        PatternModelV2 pattern4 = new PatternModelV2();
        pattern4.Order = 2;

        assertEquals(0, pattern1.compareTo(pattern4));
        assertEquals(1, pattern1.compareTo(pattern2));
        assertEquals(-1, pattern1.compareTo(pattern3));
    }

    @Test
    public void hasSameValues() {
        PatternModelV2 pattern1 = new PatternModelV2();
        assertFalse(pattern1.hasSameValues(null));

        pattern1.Type = PatternTypeEnumV2.l;
        PatternModelV2 pattern2 = new PatternModelV2();
        pattern2.Type = PatternTypeEnumV2.s;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.Type = PatternTypeEnumV2.l;
        pattern1.IsHorizontallyExpendable = true;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.IsHorizontallyExpendable = true;
        pattern1.IsVerticallyExpendable = true;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.IsVerticallyExpendable = true;
        pattern1.IsInverted = true;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.IsInverted = true;
        pattern1.NumberBottlesByRow = 3;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.NumberBottlesByRow = 3;
        pattern1.NumberBottlesByColumn = 2;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.NumberBottlesByColumn = 2;
        assertTrue(pattern1.hasSameValues(pattern2));
    }

    @Test
    public void getId() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Id = 42;
        assertEquals(42, pattern.getId());
    }

    @Test
    public void isValid() {
        PatternModelV2 pattern = new PatternModelV2();
        assertFalse(pattern.isValid());

        pattern.Type = PatternTypeEnumV2.l;
        assertFalse(pattern.isValid());

        pattern.NumberBottlesByColumn = 2;
        assertFalse(pattern.isValid());

        pattern.NumberBottlesByRow = 1;
        assertTrue(pattern.isValid());
    }

    @Test
    public void getPlaceMapForDisplay() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.PlaceMap.put(new CoordinatesModelV2(0, 0), CavePlaceTypeEnumV2.br);
        pattern.PlaceMap.put(new CoordinatesModelV2(1, 0), CavePlaceTypeEnumV2.n);
        pattern.PlaceMap.put(new CoordinatesModelV2(0, 1), CavePlaceTypeEnumV2.tl);

        Map<CoordinatesModelV2, CavePlaceModelV2> placeMap = pattern.getPlaceMapForDisplay();
        assertEquals(pattern.PlaceMap.size(), placeMap.size());
        for (Map.Entry<CoordinatesModelV2, CavePlaceTypeEnumV2> placeEntry : pattern.PlaceMap.entrySet()) {
            CoordinatesModelV2 coordinates = placeEntry.getKey();
            assertTrue(placeMap.containsKey(coordinates));
            CavePlaceModelV2 place = placeMap.get(coordinates);
            assertEquals(placeEntry.getValue(), place.PlaceType);
        }
    }

    @Test
    public void getCapacityAloneLinear() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.l;
        pattern.NumberBottlesByRow = 3;
        pattern.NumberBottlesByColumn = 4;

        assertEquals(12, pattern.getCapacityAlone());
    }

    @Test
    public void getCapacityAloneStaggered() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByRow = 3;
        pattern.NumberBottlesByColumn = 3;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;

        assertEquals(13, pattern.getCapacityAlone());

        PatternModelV2 pattern2 = new PatternModelV2();
        pattern2.Type = PatternTypeEnumV2.s;
        pattern2.NumberBottlesByRow = 3;
        pattern2.NumberBottlesByColumn = 3;
        pattern2.IsHorizontallyExpendable = true;

        assertEquals(13, pattern.getCapacityAlone());
    }

    @Test
    public void getCapacityAloneStaggeredInverted() {
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Type = PatternTypeEnumV2.s;
        pattern.NumberBottlesByRow = 3;
        pattern.NumberBottlesByColumn = 3;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.IsInverted = true;

        assertEquals(12, pattern.getCapacityAlone());

        PatternModelV2 pattern2 = new PatternModelV2();
        pattern2.Type = PatternTypeEnumV2.s;
        pattern2.NumberBottlesByRow = 3;
        pattern2.NumberBottlesByColumn = 3;
        pattern2.IsHorizontallyExpendable = true;
        pattern2.IsInverted = true;

        assertEquals(12, pattern.getCapacityAlone());
    }

    @Test
    public void isPatternHorizontallyCompatible() {
        PatternModelV2 pattern1 = new PatternModelV2();
        pattern1.Type = PatternTypeEnumV2.s;
        pattern1.NumberBottlesByRow = 3;
        pattern1.NumberBottlesByColumn = 3;
        pattern1.IsInverted = true;
        assertFalse(pattern1.isPatternHorizontallyCompatible(null));

        pattern1.IsHorizontallyExpendable = true;
        assertFalse(pattern1.isPatternHorizontallyCompatible(null));

        PatternModelV2 pattern2 = new PatternModelV2();
        assertFalse(pattern1.isPatternHorizontallyCompatible(pattern2));

        pattern2.IsHorizontallyExpendable = true;
        assertFalse(pattern1.isPatternHorizontallyCompatible(pattern2));

        pattern2.IsInverted = true;
        assertFalse(pattern1.isPatternHorizontallyCompatible(pattern2));

        pattern2.NumberBottlesByColumn = 3;
        assertTrue(pattern1.isPatternHorizontallyCompatible(pattern2));
    }

    @Test
    public void isPatternVerticallyCompatible() {
        PatternModelV2 pattern1 = new PatternModelV2();
        pattern1.Type = PatternTypeEnumV2.s;
        pattern1.NumberBottlesByRow = 3;
        pattern1.NumberBottlesByColumn = 3;
        pattern1.IsInverted = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(null));

        pattern1.IsVerticallyExpendable = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(null));

        PatternModelV2 pattern2 = new PatternModelV2();
        assertFalse(pattern1.isPatternVerticallyCompatible(pattern2));

        pattern2.IsVerticallyExpendable = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(pattern2));

        pattern2.IsInverted = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(pattern2));

        pattern2.NumberBottlesByRow = 3;
        assertTrue(pattern1.isPatternVerticallyCompatible(pattern2));
    }
}