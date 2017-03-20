package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CavePlaceTypeEnumTest {

    @Test
    public void getByIdCavePlaceTypeEnumExisting() {
        assertEquals(CavePlaceTypeEnum.NO_PLACE, CavePlaceTypeEnum.getById(CavePlaceTypeEnum.NO_PLACE.Id));
    }

    @Test
    public void getByIdCavePlaceTypeEnumNonExisting() {
        assertEquals(null, CavePlaceTypeEnum.getById(-1));
    }
}