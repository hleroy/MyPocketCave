package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CavePlaceTypeEnumTest {

    @Test
    public void getByIdCavePlaceTypeEnumExisting() {
        assertEquals(CavePlaceTypeEnumV2.n, CavePlaceTypeEnumV2.getById(CavePlaceTypeEnumV2.n.Id));
    }

    @Test
    public void getByIdCavePlaceTypeEnumNonExisting() {
        assertEquals(null, CavePlaceTypeEnumV2.getById(-1));
    }
}