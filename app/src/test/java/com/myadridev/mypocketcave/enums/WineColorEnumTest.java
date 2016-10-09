package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WineColorEnumTest {

    @Test
    public void getByIdWineColorEnumExisting() {
        assertEquals(WineColorEnum.RED, WineColorEnum.getById(WineColorEnum.RED.Id));
    }

    @Test
    public void getByIdWineColorEnumNonExisting() {
        assertEquals(null, WineColorEnum.getById(-1));
    }
}