package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;
import com.myadridev.mypocketcave.models.v2.SyncModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyncModelTest {

    @Test
    public void createSyncModel() {
        SyncModelV2 sync = new SyncModelV2();
        assertEquals("", sync.Version);
        assertTrue(sync.Bottles.isEmpty());
        assertTrue(sync.Caves.isEmpty());
        assertTrue(sync.Patterns.isEmpty());
    }

    @Test
    public void createSyncModelFromExisting() {
        SyncModelV2 expectedSync = new SyncModelV2();
        expectedSync.Version = "jn";
        BottleModelV2 bottle = new BottleModelV2();
        bottle.Id = 7;
        expectedSync.Bottles.add(bottle);
        CaveModelV2 cave = new CaveModelV2();
        cave.Id = 5;
        expectedSync.Caves.add(cave);
        PatternModelV2 pattern = new PatternModelV2();
        pattern.Id = 6;
        expectedSync.Patterns.add(pattern);

        SyncModelV2 sync = new SyncModelV2(expectedSync.Version, expectedSync.Caves, expectedSync.Bottles, expectedSync.Patterns);

        assertEquals(expectedSync.Version, sync.Version);
        assertEquals(expectedSync.Bottles.size(), sync.Bottles.size());
        for (int i = 0; i < sync.Bottles.size(); i++) {
            assertEquals(expectedSync.Bottles.get(i).Id, sync.Bottles.get(i).Id);
        }
        assertEquals(expectedSync.Caves.size(), sync.Caves.size());
        for (int i = 0; i < sync.Caves.size(); i++) {
            assertEquals(expectedSync.Caves.get(i).Id, sync.Caves.get(i).Id);
        }
        assertEquals(expectedSync.Patterns.size(), sync.Patterns.size());
        for (int i = 0; i < sync.Patterns.size(); i++) {
            assertEquals(expectedSync.Patterns.get(i).Id, sync.Patterns.get(i).Id);
        }
    }

}