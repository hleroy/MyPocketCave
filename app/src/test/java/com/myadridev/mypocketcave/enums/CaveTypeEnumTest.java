package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaveTypeEnumTest {

    @Test
    public void getByIdCaveTypeEnumExisting() {
        assertEquals(CaveTypeEnum.BOX, CaveTypeEnum.getById(CaveTypeEnum.BOX.Id));
    }

    @Test
    public void getByIdCaveTypeEnumNonExisting() {
        assertEquals(null, CaveTypeEnum.getById(-1));
    }
}