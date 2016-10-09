package com.myadridev.mypocketcave.models;

import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IBottleStorageManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaveArrangementModelTest {

    @BeforeClass
    public static void beforeClass() {
        DependencyManager.init();
    }

    @AfterClass
    public static void afterClass() {
        DependencyManager.cleanUp();
    }

    @Test
    public void createVoidCaveArrangementModel() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        assertEquals(0, arrangement.Id);
        assertEquals(0, arrangement.TotalCapacity);
        assertEquals(0, arrangement.TotalUsed);
        assertTrue(arrangement.PatternMap.isEmpty());
        assertEquals(0, arrangement.NumberBottlesBulk);
        assertEquals(0, arrangement.NumberBoxes);
        assertEquals(0, arrangement.NumberBottlesPerBox);
    }

    @Test
    public void createCaveArrangementModelFromExisting() {
        CaveArrangementModel expectedArrangement = new CaveArrangementModel();
        expectedArrangement.Id = 3;
        expectedArrangement.TotalCapacity = 3;
        expectedArrangement.TotalUsed = 1;
        expectedArrangement.PatternMap.put(new CoordinatesModel(0, 0), new PatternModelWithBottles());
        expectedArrangement.PatternMap.put(new CoordinatesModel(1, 0), new PatternModelWithBottles());
        expectedArrangement.NumberBottlesBulk = 43;
        expectedArrangement.NumberBoxes = 17;
        expectedArrangement.NumberBottlesPerBox = 37;

        CaveArrangementModel arrangement = new CaveArrangementModel(expectedArrangement);

        assertEquals(expectedArrangement.Id, arrangement.Id);
        assertEquals(expectedArrangement.TotalCapacity, arrangement.TotalCapacity);
        assertEquals(expectedArrangement.TotalUsed, arrangement.TotalUsed);
        assertEquals(expectedArrangement.PatternMap.size(), arrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : expectedArrangement.PatternMap.entrySet()) {
            assertTrue(arrangement.PatternMap.containsKey(patternEntry.getKey()));
            assertEquals(patternEntry.getValue(), arrangement.PatternMap.get(patternEntry.getKey()));
        }
        assertEquals(expectedArrangement.NumberBottlesBulk, arrangement.NumberBottlesBulk);
        assertEquals(expectedArrangement.NumberBoxes, arrangement.NumberBoxes);
        assertEquals(expectedArrangement.NumberBottlesPerBox, arrangement.NumberBottlesPerBox);
    }

    @Test
    public void computeTotalCapacityWithBulk() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        arrangement.NumberBottlesBulk = 42;

        arrangement.computeTotalCapacityWithBulk();
        assertEquals(arrangement.NumberBottlesBulk, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithBoxes() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        arrangement.NumberBottlesPerBox = 6;
        arrangement.NumberBoxes = 4;

        arrangement.computeTotalCapacityWithBoxes();
        assertEquals(arrangement.NumberBottlesPerBox * arrangement.NumberBoxes, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithOneLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(6, arrangement.TotalCapacity);
    }

    @NonNull
    private PatternModel getLinearPatternModel() {
        PatternModel linPattern = new PatternModel();
        linPattern.IsHorizontallyExpendable = false;
        linPattern.IsVerticallyExpendable = false;
        linPattern.IsInverted = false;
        linPattern.NumberBottlesByColumn = 3;
        linPattern.NumberBottlesByRow = 2;
        linPattern.Type = PatternTypeEnum.LINEAR;
        return linPattern;
    }

    @Test
    public void computeTotalCapacityWithPatternWithHorizontalLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        PatternModelWithBottles linPatternWithBottles1 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles2 = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles2);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(12, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithVerticalLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        PatternModelWithBottles linPatternWithBottles1 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles2 = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), linPatternWithBottles2);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(12, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithHorizontalAndVerticalLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        PatternModelWithBottles linPatternWithBottles1 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles2 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles3 = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), linPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles3);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(18, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithExpendablesPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles3);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(47, arrangement.TotalCapacity);
    }

    @NonNull
    private PatternModel getStaggeredPatternModel() {
        PatternModel staggeredPattern = new PatternModel();
        staggeredPattern.IsHorizontallyExpendable = true;
        staggeredPattern.IsVerticallyExpendable = true;
        staggeredPattern.IsInverted = false;
        staggeredPattern.NumberBottlesByColumn = 3;
        staggeredPattern.NumberBottlesByRow = 3;
        staggeredPattern.Type = PatternTypeEnum.STAGGERED_ROWS;
        return staggeredPattern;
    }

    @Test
    public void computeTotalCapacityWithPatternWithExpendablesAndNotExpendablesPatternTopRight() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), linPatternWithBottles);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(51, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithExpendablesAndNotExpendablesPatternTopLeft() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), linPatternWithBottles);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(51, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithExpendablesAndNotExpendablesPatternBottomRight() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(53, arrangement.TotalCapacity);
    }

    @Test
    public void computeTotalCapacityWithPatternWithExpendablesAndNotExpendablesPatternBottomLeft() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles);

        arrangement.computeTotalCapacityWithPattern();
        assertEquals(53, arrangement.TotalCapacity);
    }

    @Test
    public void movePatternMapToLeft() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 2), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 2), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles);

        arrangement.movePatternMapToLeft();
        assertEquals(4, arrangement.PatternMap.size());
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(0, 1)), staggeredPatternWithBottles1);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(1, 0)), staggeredPatternWithBottles2);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(1, 1)), staggeredPatternWithBottles3);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(0, 0)), linPatternWithBottles);
    }

    @Test
    public void movePatternMapToRightWhenNoOrigin() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 2), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 2), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles);

        arrangement.movePatternMapToRight();
        assertEquals(4, arrangement.PatternMap.size());
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(0, 2)), staggeredPatternWithBottles1);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(1, 1)), staggeredPatternWithBottles2);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(1, 2)), staggeredPatternWithBottles3);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(0, 1)), linPatternWithBottles);
    }

    @Test
    public void movePatternMapToRightWhenOrigin() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        PatternModel linPattern = getLinearPatternModel();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles);

        arrangement.movePatternMapToRight();
        assertEquals(4, arrangement.PatternMap.size());
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(0, 2)), staggeredPatternWithBottles1);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(1, 1)), staggeredPatternWithBottles2);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(1, 2)), staggeredPatternWithBottles3);
        assertEquals(arrangement.PatternMap.get(new CoordinatesModel(0, 1)), linPatternWithBottles);
    }

    @Test
    public void setClickablePlacesWithOneLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles);

        arrangement.setClickablePlaces();
        for (PatternModelWithBottles pattern : arrangement.PatternMap.values()) {
            for (CavePlaceModel place : pattern.PlaceMapWithBottles.values()) {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setClickablePlacesWithHorizontalLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();
        PatternModelWithBottles linPatternWithBottles1 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles2 = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles2);

        arrangement.setClickablePlaces();
        for (PatternModelWithBottles pattern : arrangement.PatternMap.values()) {
            for (CavePlaceModel place : pattern.PlaceMapWithBottles.values()) {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setClickablePlacesWithVerticalLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();
        PatternModelWithBottles linPatternWithBottles1 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles2 = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), linPatternWithBottles2);

        arrangement.setClickablePlaces();
        for (PatternModelWithBottles pattern : arrangement.PatternMap.values()) {
            for (CavePlaceModel place : pattern.PlaceMapWithBottles.values()) {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setClickablePlacesWithHorizontalAndVerticalLinearPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();
        PatternModelWithBottles linPatternWithBottles1 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles2 = new PatternModelWithBottles(linPattern);
        PatternModelWithBottles linPatternWithBottles3 = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), linPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles3);

        arrangement.setClickablePlaces();
        for (PatternModelWithBottles pattern : arrangement.PatternMap.values()) {
            for (CavePlaceModel place : pattern.PlaceMapWithBottles.values()) {
                assertTrue(place.IsClickable);
            }
        }
    }

    @Test
    public void setClickablePlacesWithExpendablesPattern() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        staggeredPattern.computePlacesMap();
        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles3);

        arrangement.setClickablePlaces();
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : arrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();
            for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : pattern.PlaceMapWithBottles.entrySet()) {
                CoordinatesModel coordinates = placeEntry.getKey();
                CavePlaceModel place = placeEntry.getValue();

                if (patternCoordinates.Row + patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 0) {
                    if (coordinates.Col == 11 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Col == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                }
            }
        }
    }

    @Test
    public void setClickablePlacesWithExpendablesAndNotExpendablesPatternTopRight() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        staggeredPattern.computePlacesMap();

        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), linPatternWithBottles);

        arrangement.setClickablePlaces();
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : arrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();
            for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : pattern.PlaceMapWithBottles.entrySet()) {
                CoordinatesModel coordinates = placeEntry.getKey();
                CavePlaceModel place = placeEntry.getValue();

                if (patternCoordinates.Row + patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 1 && patternCoordinates.Col == 1) {
                    assertTrue(place.IsClickable);
                } else if (patternCoordinates.Row == 0) {
                    if (coordinates.Col == 11 || coordinates.Row == 0 || coordinates.Row == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Col == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                }
            }
        }
    }

    @Test
    public void setClickablePlacesWithExpendablesAndNotExpendablesPatternTopLeft() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        staggeredPattern.computePlacesMap();

        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), linPatternWithBottles);

        arrangement.setClickablePlaces();
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : arrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();
            for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : pattern.PlaceMapWithBottles.entrySet()) {
                CoordinatesModel coordinates = placeEntry.getKey();
                CavePlaceModel place = placeEntry.getValue();

                if (patternCoordinates.Row + patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Row == 0 || coordinates.Row == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 1 && patternCoordinates.Col == 1) {
                    if (coordinates.Col == 0 || coordinates.Col == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 0) {
                    if (coordinates.Col == 11 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Col == 0) {
                    assertTrue(place.IsClickable);
                }
            }
        }
    }

    @Test
    public void setClickablePlacesWithExpendablesAndNotExpendablesPatternBottomRight() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        staggeredPattern.computePlacesMap();

        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), linPatternWithBottles);

        arrangement.setClickablePlaces();
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : arrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();
            for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : pattern.PlaceMapWithBottles.entrySet()) {
                CoordinatesModel coordinates = placeEntry.getKey();
                CavePlaceModel place = placeEntry.getValue();

                if (patternCoordinates.Row + patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Col == 11 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 1 && patternCoordinates.Col == 1) {
                    if (coordinates.Row == 0 || coordinates.Col == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 0) {
                    assertTrue(place.IsClickable);
                } else if (patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                }
            }
        }
    }

    @Test
    public void setClickablePlacesWithExpendablesAndNotExpendablesPatternBottomLeft() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        staggeredPattern.computePlacesMap();

        PatternModel linPattern = getLinearPatternModel();
        linPattern.computePlacesMap();

        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles linPatternWithBottles = new PatternModelWithBottles(linPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), linPatternWithBottles);

        arrangement.setClickablePlaces();
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : arrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles pattern = patternEntry.getValue();
            for (Map.Entry<CoordinatesModel, CavePlaceModel> placeEntry : pattern.PlaceMapWithBottles.entrySet()) {
                CoordinatesModel coordinates = placeEntry.getKey();
                CavePlaceModel place = placeEntry.getValue();

                if (patternCoordinates.Row + patternCoordinates.Col == 0) {
                    assertTrue(place.IsClickable);
                } else if (patternCoordinates.Row == 1 && patternCoordinates.Col == 1) {
                    if (coordinates.Col == 11 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Row == 0) {
                    if (coordinates.Col == 0 || coordinates.Col == 11 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                } else if (patternCoordinates.Col == 0) {
                    if (coordinates.Col == 0 || coordinates.Row == 0 || place.PlaceType == CavePlaceTypeEnum.NO_PLACE) {
                        assertFalse(place.IsClickable);
                    } else {
                        assertTrue(place.IsClickable);
                    }
                }
            }
        }
    }

    @Test
    public void placeBottleRedSamePatternPlaceTopRight() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        arrangement.placeBottle(origin, new CoordinatesModel(1, 1), bottle.Id);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 1));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 2));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 1));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 2));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    private CaveArrangementModel getArrangementStaggered() {
        CaveArrangementModel arrangement = new CaveArrangementModel();
        PatternModel staggeredPattern = getStaggeredPatternModel();
        staggeredPattern.computePlacesMap();
        PatternModelWithBottles staggeredPatternWithBottles1 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles2 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles3 = new PatternModelWithBottles(staggeredPattern);
        PatternModelWithBottles staggeredPatternWithBottles4 = new PatternModelWithBottles(staggeredPattern);
        arrangement.PatternMap.put(new CoordinatesModel(0, 1), staggeredPatternWithBottles1);
        arrangement.PatternMap.put(new CoordinatesModel(1, 0), staggeredPatternWithBottles2);
        arrangement.PatternMap.put(new CoordinatesModel(1, 1), staggeredPatternWithBottles3);
        arrangement.PatternMap.put(new CoordinatesModel(0, 0), staggeredPatternWithBottles4);

        arrangement.computeTotalCapacityWithPattern();
        arrangement.setClickablePlaces();
        arrangement.TotalUsed = 0;
        return arrangement;
    }

    @Mock
    IBottleStorageManager mockBottleStorageManager;

    @Test
    public void placeBottleWhiteSamePatternPlaceTopLeft() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.WHITE;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        arrangement.placeBottle(origin, new CoordinatesModel(1, 2), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 1));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_WHITE, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 2));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_WHITE, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 1));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_WHITE, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 2));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_WHITE, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRoseSamePatternPlaceBottomRight() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.ROSE;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        arrangement.placeBottle(origin, new CoordinatesModel(2, 1), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 1));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_ROSE, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 2));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_ROSE, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 1));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_ROSE, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 2));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_ROSE, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleChampagneSamePatternPlaceBottomLeft() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.CHAMPAGNE;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        arrangement.placeBottle(origin, new CoordinatesModel(2, 2), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 1));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(1, 2));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 1));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(2, 2));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedBottomPatternPlaceTopRight() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel topCoord = new CoordinatesModel(1, 0);
        PatternModelWithBottles topPattern = arrangement.PatternMap.get(topCoord);
        arrangement.placeBottle(origin, new CoordinatesModel(11, 3), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 3));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 4));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 3));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 4));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedBottomPatternPlaceTopLeft() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel topCoord = new CoordinatesModel(1, 0);
        PatternModelWithBottles topPattern = arrangement.PatternMap.get(topCoord);
        arrangement.placeBottle(origin, new CoordinatesModel(11, 4), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 3));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 4));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 3));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 4));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedLeftPatternPlaceTopRight() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel rightCoord = new CoordinatesModel(0, 1);
        PatternModelWithBottles rightPattern = arrangement.PatternMap.get(rightCoord);
        arrangement.placeBottle(origin, new CoordinatesModel(3, 11), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 11));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 0));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 11));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 0));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedLeftPatternPlaceBottomRight() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel rightCoord = new CoordinatesModel(0, 1);
        PatternModelWithBottles rightPattern = arrangement.PatternMap.get(rightCoord);
        arrangement.placeBottle(origin, new CoordinatesModel(4, 11), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 11));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 0));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 11));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 0));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedRightPatternPlaceTopLeft() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel rightCoord = new CoordinatesModel(0, 1);
        PatternModelWithBottles rightPattern = arrangement.PatternMap.get(rightCoord);
        arrangement.placeBottle(rightCoord, new CoordinatesModel(3, 0), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 11));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 0));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 11));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 0));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedRightPatternPlaceBottomLeft() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel rightCoord = new CoordinatesModel(0, 1);
        PatternModelWithBottles rightPattern = arrangement.PatternMap.get(rightCoord);
        arrangement.placeBottle(rightCoord, new CoordinatesModel(4, 0), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 11));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(3, 0));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 11));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = rightPattern.PlaceMapWithBottles.get(new CoordinatesModel(4, 0));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedTopPatternPlaceBottomRight() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel topCoord = new CoordinatesModel(1, 0);
        PatternModelWithBottles topPattern = arrangement.PatternMap.get(topCoord);
        arrangement.placeBottle(topCoord, new CoordinatesModel(0, 3), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 3));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 4));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 3));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 4));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }

    @Test
    public void placeBottleRedTopPatternPlaceBottomLeft() {
        BottleModel bottle = new BottleModel();
        bottle.Id = 42;
        bottle.WineColor = WineColorEnum.RED;

        CaveArrangementModel arrangement = getArrangementStaggered();
        when(mockBottleStorageManager.getBottle(anyInt())).thenReturn(bottle);
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);

        CoordinatesModel origin = new CoordinatesModel(0, 0);
        PatternModelWithBottles pattern = arrangement.PatternMap.get(origin);
        CoordinatesModel topCoord = new CoordinatesModel(1, 0);
        PatternModelWithBottles topPattern = arrangement.PatternMap.get(topCoord);
        arrangement.placeBottle(topCoord, new CoordinatesModel(0, 4), bottle.Id);

        CavePlaceModel topRight = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 3));
        assertEquals(bottle.Id, topRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_RIGHT_RED, topRight.PlaceType);

        CavePlaceModel topLeft = pattern.PlaceMapWithBottles.get(new CoordinatesModel(11, 4));
        assertEquals(bottle.Id, topLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_TOP_LEFT_RED, topLeft.PlaceType);

        CavePlaceModel bottomRight = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 3));
        assertEquals(bottle.Id, bottomRight.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_RED, bottomRight.PlaceType);

        CavePlaceModel bottomLeft = topPattern.PlaceMapWithBottles.get(new CoordinatesModel(0, 4));
        assertEquals(bottle.Id, bottomLeft.BottleId);
        assertEquals(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_RED, bottomLeft.PlaceType);

        assertEquals(1, arrangement.TotalUsed);
    }
}