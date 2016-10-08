package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MillesimeEnumTest {

    @Test
    public void getByIdMillesimeEnumExisting() throws Exception {
        assertEquals(MillesimeEnum.LESS_THAN_TWO, MillesimeEnum.getById(MillesimeEnum.LESS_THAN_TWO.Id));
    }

    @Test
    public void getByIdMillesimeEnumNonExisting() throws Exception {
        assertEquals(null, MillesimeEnum.getById(-1));
    }
}