package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WineColorEnumTest {

    @Test
    public void getByIdWineColorEnumExisting() {
        assertEquals(WineColorEnumV2.r, WineColorEnumV2.getById(WineColorEnumV2.r.Id));
    }

    @Test
    public void getByIdWineColorEnumNonExisting() {
        assertEquals(null, WineColorEnumV2.getById(-1));
    }
}