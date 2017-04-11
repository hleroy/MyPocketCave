package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaveTypeEnumTest {

    @Test
    public void getByIdCaveTypeEnumExisting() {
        assertEquals(CaveTypeEnumV2.bo, CaveTypeEnumV2.getById(CaveTypeEnumV2.bo.Id));
    }

    @Test
    public void getByIdCaveTypeEnumNonExisting() {
        assertEquals(null, CaveTypeEnumV2.getById(-1));
    }
}