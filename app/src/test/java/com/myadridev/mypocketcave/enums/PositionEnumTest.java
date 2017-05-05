package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.PositionEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionEnumTest {

    @Test
    public void getByIdExisting() {
        assertEquals(PositionEnumV2.bl, PositionEnumV2.getById(1));
    }

    @Test
    public void getByIdNonExisting() {
        assertEquals(null, PositionEnumV2.getById(-1));
    }
}