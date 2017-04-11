package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MillesimeEnumTest {

    @Test
    public void getByIdMillesimeEnumExisting() {
        assertEquals(MillesimeEnumV2.ltt, MillesimeEnumV2.getById(MillesimeEnumV2.ltt.Id));
    }

    @Test
    public void getByIdMillesimeEnumNonExisting() {
        assertEquals(null, MillesimeEnumV2.getById(-1));
    }
}