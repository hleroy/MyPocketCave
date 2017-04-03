package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.models.v1.CavePlaceModel;
import com.myadridev.mypocketcave.models.v1.CoordinatesModel;
import com.myadridev.mypocketcave.models.v1.PatternModel;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternModelTest {

    @Test
    public void createVoidPatternModel() {
        PatternModel pattern = new PatternModel();

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
        PatternModel expectedPattern = new PatternModel();
        expectedPattern.Id = 42;
        expectedPattern.Type = PatternTypeEnum.LINEAR;
        expectedPattern.NumberBottlesByColumn = 3;
        expectedPattern.NumberBottlesByRow = 2;
        expectedPattern.IsHorizontallyExpendable = false;
        expectedPattern.IsVerticallyExpendable = true;
        expectedPattern.IsInverted = false;
        expectedPattern.Order = 2;
        expectedPattern.PlaceMap.put(new CoordinatesModel(0, 1), CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE);
        expectedPattern.PlaceMap.put(new CoordinatesModel(1, 1), CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_ROSE);

        PatternModel pattern = new PatternModel(expectedPattern);

        assertEquals(expectedPattern.Id, pattern.Id);
        assertEquals(expectedPattern.Type, pattern.Type);
        assertEquals(expectedPattern.NumberBottlesByColumn, pattern.NumberBottlesByColumn);
        assertEquals(expectedPattern.NumberBottlesByRow, pattern.NumberBottlesByRow);
        assertEquals(expectedPattern.IsHorizontallyExpendable, pattern.IsHorizontallyExpendable);
        assertEquals(expectedPattern.IsVerticallyExpendable, pattern.IsVerticallyExpendable);
        assertEquals(expectedPattern.IsInverted, pattern.IsInverted);
        assertEquals(expectedPattern.Order, pattern.Order);
        assertEquals(expectedPattern.PlaceMap.size(), pattern.PlaceMap.size());
        for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> placeEntry : expectedPattern.PlaceMap.entrySet()) {
            CoordinatesModel coordinate = placeEntry.getKey();
            assertTrue(pattern.PlaceMap.containsKey(coordinate));
            assertEquals(placeEntry.getValue(), pattern.PlaceMap.get(coordinate));
        }
    }

    @Test
    public void computePlacesMapWhenLinear() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.LINEAR;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = false;
        pattern.IsVerticallyExpendable = false;
        pattern.IsInverted = false;

        pattern.computePlacesMap();
        assertEquals(24, pattern.PlaceMap.size());
        for (int i = 0; i < pattern.NumberBottlesByColumn; i++) {
            for (int j = 0; j < pattern.NumberBottlesByRow; j++) {
                assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(2 * i, 2 * j)));
                assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(2 * i + 1, 2 * j)));
                assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(2 * i, 2 * j + 1)));
                assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(2 * i + 1, 2 * j + 1)));
            }
        }
    }

    @Test
    public void computePlacesMapWhenStaggeredHorizontallyExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 1;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = false;
        pattern.IsInverted = false;

        pattern.computePlacesMap();
        assertEquals(24, pattern.PlaceMap.size());
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(0, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(0, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 3)));

        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(1, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(1, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(1, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(1, 3)));

        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(2, 0)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(2, 1)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(2, 2)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(2, 3)));

        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(3, 0)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 1)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 2)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(3, 3)));

        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(4, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(4, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(4, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(4, 3)));

        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(5, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(5, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(5, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(5, 3)));
    }

    @Test
    public void computePlacesMapWhenStaggeredVerticallyExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = false;
        pattern.IsVerticallyExpendable = true;
        pattern.IsInverted = false;

        pattern.computePlacesMap();
        assertEquals(24, pattern.PlaceMap.size());
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 0)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(0, 2)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(0, 3)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 4)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 5)));

        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(1, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(1, 1)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(1, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(1, 3)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(1, 4)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(1, 5)));

        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(2, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(2, 1)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(2, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(2, 3)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(2, 4)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(2, 5)));

        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 0)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(3, 2)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(3, 3)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 4)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 5)));
    }

    @Test
    public void computePlacesMapWhenStaggeredInvertedExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 1;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.IsInverted = true;

        pattern.computePlacesMap();
        assertEquals(16, pattern.PlaceMap.size());
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(0, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(0, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(0, 3)));

        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(1, 0)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(1, 1)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(1, 2)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(1, 3)));

        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT, pattern.PlaceMap.get(new CoordinatesModel(2, 0)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(2, 1)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(2, 2)));
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(2, 3)));

        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 0)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT, pattern.PlaceMap.get(new CoordinatesModel(3, 1)));
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT, pattern.PlaceMap.get(new CoordinatesModel(3, 2)));
        assertEquals(CavePlaceTypeEnum.NO_PLACE, pattern.PlaceMap.get(new CoordinatesModel(3, 3)));
    }

    @Test
    public void getNumberColumnsGridLayoutLinear() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.LINEAR;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;

        assertEquals(4, pattern.getNumberColumnsGridLayout());
    }

    @Test
    public void getNumberColumnsGridLayoutStaggeredHorizontallyExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = true;

        assertEquals(8, pattern.getNumberColumnsGridLayout());
    }

    @Test
    public void getNumberColumnsGridLayoutStaggeredNotHorizontallyExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 1;
        pattern.NumberBottlesByRow = 2;
        pattern.IsHorizontallyExpendable = false;

        assertEquals(6, pattern.getNumberColumnsGridLayout());
    }

    @Test
    public void getNumberRowsGridLayoutLinear() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.LINEAR;
        pattern.NumberBottlesByColumn = 3;
        pattern.NumberBottlesByRow = 2;

        assertEquals(6, pattern.getNumberRowsGridLayout());
    }

    @Test
    public void getNumberRowsGridLayoutStaggeredVerticallyExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 1;
        pattern.IsVerticallyExpendable = true;

        assertEquals(8, pattern.getNumberRowsGridLayout());
    }

    @Test
    public void getNumberRowsGridLayoutStaggeredNotVerticallyExpendable() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByColumn = 2;
        pattern.NumberBottlesByRow = 1;
        pattern.IsVerticallyExpendable = false;

        assertEquals(6, pattern.getNumberRowsGridLayout());
    }

    @Test
    public void comparePatterns() {
        PatternModel pattern1 = new PatternModel();
        pattern1.Order = 2;
        PatternModel pattern2 = new PatternModel();
        pattern2.Order = 1;
        PatternModel pattern3 = new PatternModel();
        pattern3.Order = 3;
        PatternModel pattern4 = new PatternModel();
        pattern4.Order = 2;

        assertEquals(0, pattern1.compareTo(pattern4));
        assertEquals(1, pattern1.compareTo(pattern2));
        assertEquals(-1, pattern1.compareTo(pattern3));
    }

    @Test
    public void hasSameValues() {
        PatternModel pattern1 = new PatternModel();
        assertFalse(pattern1.hasSameValues(null));

        pattern1.Type = PatternTypeEnum.LINEAR;
        PatternModel pattern2 = new PatternModel();
        pattern2.Type = PatternTypeEnum.STAGGERED_ROWS;
        assertFalse(pattern1.hasSameValues(pattern2));

        pattern2.Type = PatternTypeEnum.LINEAR;
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
        PatternModel pattern = new PatternModel();
        pattern.Id = 42;
        assertEquals(42, pattern.getId());
    }

    @Test
    public void isValid() {
        PatternModel pattern = new PatternModel();
        assertFalse(pattern.isValid());

        pattern.Type = PatternTypeEnum.LINEAR;
        assertFalse(pattern.isValid());

        pattern.NumberBottlesByColumn = 2;
        assertFalse(pattern.isValid());

        pattern.NumberBottlesByRow = 1;
        assertTrue(pattern.isValid());
    }

    @Test
    public void getPlaceMapForDisplay() {
        PatternModel pattern = new PatternModel();
        pattern.PlaceMap.put(new CoordinatesModel(0, 0), CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT);
        pattern.PlaceMap.put(new CoordinatesModel(1, 0), CavePlaceTypeEnum.NO_PLACE);
        pattern.PlaceMap.put(new CoordinatesModel(0, 1), CavePlaceTypeEnum.PLACE_TOP_LEFT);

        Map<CoordinatesModel, CavePlaceModel> placeMap = pattern.getPlaceMapForDisplay();
        assertEquals(pattern.PlaceMap.size(), placeMap.size());
        for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> placeEntry : pattern.PlaceMap.entrySet()) {
            CoordinatesModel coordinates = placeEntry.getKey();
            assertTrue(placeMap.containsKey(coordinates));
            CavePlaceModel place = placeMap.get(coordinates);
            assertEquals(placeEntry.getValue(), place.PlaceType);
        }
    }

    @Test
    public void getCapacityAloneLinear() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.LINEAR;
        pattern.NumberBottlesByRow = 3;
        pattern.NumberBottlesByColumn = 4;

        assertEquals(12, pattern.getCapacityAlone());
    }

    @Test
    public void getCapacityAloneStaggered() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByRow = 3;
        pattern.NumberBottlesByColumn = 3;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;

        assertEquals(13, pattern.getCapacityAlone());

        PatternModel pattern2 = new PatternModel();
        pattern2.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern2.NumberBottlesByRow = 3;
        pattern2.NumberBottlesByColumn = 3;
        pattern2.IsHorizontallyExpendable = true;

        assertEquals(13, pattern.getCapacityAlone());
    }

    @Test
    public void getCapacityAloneStaggeredInverted() {
        PatternModel pattern = new PatternModel();
        pattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern.NumberBottlesByRow = 3;
        pattern.NumberBottlesByColumn = 3;
        pattern.IsHorizontallyExpendable = true;
        pattern.IsVerticallyExpendable = true;
        pattern.IsInverted = true;

        assertEquals(12, pattern.getCapacityAlone());

        PatternModel pattern2 = new PatternModel();
        pattern2.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern2.NumberBottlesByRow = 3;
        pattern2.NumberBottlesByColumn = 3;
        pattern2.IsHorizontallyExpendable = true;
        pattern2.IsInverted = true;

        assertEquals(12, pattern.getCapacityAlone());
    }

    @Test
    public void isPatternHorizontallyCompatible() {
        PatternModel pattern1 = new PatternModel();
        pattern1.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern1.NumberBottlesByRow = 3;
        pattern1.NumberBottlesByColumn = 3;
        pattern1.IsInverted = true;
        assertFalse(pattern1.isPatternHorizontallyCompatible(null));

        pattern1.IsHorizontallyExpendable = true;
        assertFalse(pattern1.isPatternHorizontallyCompatible(null));

        PatternModel pattern2 = new PatternModel();
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
        PatternModel pattern1 = new PatternModel();
        pattern1.Type = PatternTypeEnum.STAGGERED_ROWS;
        pattern1.NumberBottlesByRow = 3;
        pattern1.NumberBottlesByColumn = 3;
        pattern1.IsInverted = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(null));

        pattern1.IsVerticallyExpendable = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(null));

        PatternModel pattern2 = new PatternModel();
        assertFalse(pattern1.isPatternVerticallyCompatible(pattern2));

        pattern2.IsVerticallyExpendable = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(pattern2));

        pattern2.IsInverted = true;
        assertFalse(pattern1.isPatternVerticallyCompatible(pattern2));

        pattern2.NumberBottlesByRow = 3;
        assertTrue(pattern1.isPatternVerticallyCompatible(pattern2));
    }
}