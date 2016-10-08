package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaveTypeEnumTest {

    @Test
    public void getByIdCaveTypeEnumExisting() throws Exception {
        assertEquals(CaveTypeEnum.BOX, CaveTypeEnum.getById(CaveTypeEnum.BOX.Id));
    }

    @Test
    public void getByIdCaveTypeEnumNonExisting() throws Exception {
        assertEquals(null, CaveTypeEnum.getById(-1));
    }
}