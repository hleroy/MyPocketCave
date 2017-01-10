package com.myadridev.mypocketcave.helpers;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CollectionsHelperTest {

    private Map<Integer, String> dataMap;

    @Before
    public void before() {
        dataMap = new HashMap<>(5);
        dataMap.put(1, "1");
        dataMap.put(2, "2");
        dataMap.put(3, "3");
        dataMap.put(4, "4");
        dataMap.put(5, "5");
    }

    @Test
    public void getValueOrDefaultWhenExisting() {
        assertEquals("3", CollectionsHelper.getValueOrDefault(dataMap, 3, null));
    }

    @Test
    public void getValueOrDefaultWhenNotExisting() {
        assertEquals(null, CollectionsHelper.getValueOrDefault(dataMap, 42, null));
    }
}