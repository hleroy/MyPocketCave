package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IBottleStorageManagerV2;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveArrangementModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaveArrangementModelTest {

    private static final int bottleRedId = 42;
    private static final int bottleWhiteId = 27;
    private static final int bottleRoseId = 13;
    private static final int bottleChampagneId = 92;
    @Mock
    static IBottleStorageManagerV2 mockBottleStorageManager;

    @BeforeClass
    public static void beforeClass() {
        DependencyManager.init();

        BottleModelV2 bottleRed = new BottleModelV2();
        bottleRed.Id = bottleRedId;
        bottleRed.WineColor = WineColorEnumV2.r;
        BottleModelV2 bottleWhite = new BottleModelV2();
        bottleWhite.Id = bottleWhiteId;
        bottleWhite.WineColor = WineColorEnumV2.w;
        BottleModelV2 bottleRose = new BottleModelV2();
        bottleRose.Id = bottleRoseId;
        bottleRose.WineColor = WineColorEnumV2.ro;
        BottleModelV2 bottleChampagne = new BottleModelV2();
        bottleChampagne.Id = bottleChampagneId;
        bottleChampagne.WineColor = WineColorEnumV2.c;

        mockBottleStorageManager = mock(IBottleStorageManagerV2.class);
        when(mockBottleStorageManager.getBottle(bottleRedId)).thenReturn(bottleRed);
        when(mockBottleStorageManager.getBottle(bottleWhiteId)).thenReturn(bottleWhite);
        when(mockBottleStorageManager.getBottle(bottleRoseId)).thenReturn(bottleRose);
        when(mockBottleStorageManager.getBottle(bottleChampagneId)).thenReturn(bottleChampagne);
        DependencyManager.registerSingleton(IBottleStorageManagerV2.class, mockBottleStorageManager, true);
    }

    @AfterClass
    public static void afterClass() {
        DependencyManager.cleanUp();
    }

    @Test
    public void createVoidCaveArrangementModel() {
        CaveArrangementModelV2 arrangement = new CaveArrangementModelV2();
        assertEquals(0, arrangement.Id);
        assertEquals(0, arrangement.TotalCapacity);
        assertEquals(0, arrangement.TotalUsed);
        assertTrue(arrangement.PatternMap.isEmpty());
        assertEquals(0, arrangement.NumberBottlesBulk);
        assertEquals(0, arrangement.NumberBoxes);
        assertEquals(0, arrangement.BoxesNumberBottlesByColumn);
        assertEquals(0, arrangement.BoxesNumberBottlesByRow);
    }

    @Test
    public void createCaveArrangementModelFromExisting() {
        CaveArrangementModelV2 expectedArrangement = new CaveArrangementModelV2();
        expectedArrangement.Id = 3;
        expectedArrangement.TotalCapacity = 3;
        expectedArrangement.TotalUsed = 1;
        PatternModelWithBottlesV2 pattern1 = new PatternModelWithBottlesV2();
        pattern1.Id = 5;
        expectedArrangement.PatternMap.put(new CoordinatesModelV2(0, 0), pattern1);
        PatternModelWithBottlesV2 pattern2 = new PatternModelWithBottlesV2();
        pattern2.Id = 9;
        expectedArrangement.PatternMap.put(new CoordinatesModelV2(1, 0), pattern2);
        expectedArrangement.NumberBottlesBulk = 43;
        expectedArrangement.NumberBoxes = 17;
        expectedArrangement.BoxesNumberBottlesByColumn = 2;
        expectedArrangement.BoxesNumberBottlesByRow = 3;

        CaveArrangementModelV2 arrangement = new CaveArrangementModelV2(expectedArrangement);

        assertEquals(expectedArrangement.Id, arrangement.Id);
        assertEquals(expectedArrangement.TotalCapacity, arrangement.TotalCapacity);
        assertEquals(expectedArrangement.TotalUsed, arrangement.TotalUsed);
        assertEquals(expectedArrangement.PatternMap.size(), arrangement.PatternMap.size());
        for (Map.Entry<CoordinatesModelV2, PatternModelWithBottlesV2> patternEntry : expectedArrangement.PatternMap.entrySet()) {
            assertTrue(arrangement.PatternMap.containsKey(patternEntry.getKey()));
            PatternModelWithBottlesV2 patternModelWithBottlesV2 = arrangement.PatternMap.get(patternEntry.getKey());
            assertEquals(patternEntry.getValue().Id, patternModelWithBottlesV2.Id);
        }
        assertEquals(expectedArrangement.NumberBottlesBulk, arrangement.NumberBottlesBulk);
        assertEquals(expectedArrangement.NumberBoxes, arrangement.NumberBoxes);
        assertEquals(expectedArrangement.BoxesNumberBottlesByColumn, arrangement.BoxesNumberBottlesByColumn);
        assertEquals(expectedArrangement.BoxesNumberBottlesByRow, arrangement.BoxesNumberBottlesByRow);
    }
}