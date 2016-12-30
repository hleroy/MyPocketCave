package com.myadridev.mypocketcave.helpers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StorageHelperTest {

    @Test
    public void getNewIdWhenContinuousList() {
        List<Integer> ids = new ArrayList<>(5);
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);
        ids.add(5);
        assertEquals(6,StorageHelper.getNewId(ids));
    }

    @Test
    public void getNewIdWhenNotContinuousList() {
        List<Integer> ids = new ArrayList<>(5);
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(5);
        ids.add(7);
        assertEquals(4,StorageHelper.getNewId(ids));
    }
}