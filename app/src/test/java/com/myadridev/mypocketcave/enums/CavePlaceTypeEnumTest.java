package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.PositionEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;

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

    @Test
    public void getByPositionAndColorExisting() {
        assertEquals(CavePlaceTypeEnumV2.brru, CavePlaceTypeEnumV2.getByPositionAndColor(PositionEnumV2.br, WineColorEnumV2.ru));
    }

    @Test
    public void getByPositionAndColorNonExisting() {
        assertEquals(null, CavePlaceTypeEnumV2.getByPositionAndColor(PositionEnumV2.n, WineColorEnumV2.ru));
    }

    @Test
    public void getEmptyByPosition() {
        assertEquals(CavePlaceTypeEnumV2.br, CavePlaceTypeEnumV2.getEmptyByPosition(PositionEnumV2.br));
    }
}