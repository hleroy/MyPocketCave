package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CavePlaceTypeEnumTest {

    @Test
    public void getByIdCavePlaceTypeEnumExisting() {
        assertEquals(CavePlaceTypeEnum.NO_PLACE, CavePlaceTypeEnum.getById(CavePlaceTypeEnum.NO_PLACE.Id));
    }

    @Test
    public void getByIdCavePlaceTypeEnumNonExisting() {
        assertEquals(null, CavePlaceTypeEnum.getById(-1));
    }

    @Test
    public void isBottomRight() {
        assertTrue(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE.isBottomRight());
        assertFalse(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE.isBottomRight());
        assertFalse(CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE.isBottomRight());
        assertFalse(CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE.isBottomRight());
    }

    @Test
    public void isBottomLeft() {
        assertFalse(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE.isBottomLeft());
        assertTrue(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE.isBottomLeft());
        assertFalse(CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE.isBottomLeft());
        assertFalse(CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE.isBottomLeft());
    }

    @Test
    public void isTopRight() {
        assertFalse(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE.isTopRight());
        assertFalse(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE.isTopRight());
        assertTrue(CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE.isTopRight());
        assertFalse(CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE.isTopRight());
    }

    @Test
    public void isTopLeft() {
        assertFalse(CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT_CHAMPAGNE.isTopLeft());
        assertFalse(CavePlaceTypeEnum.PLACE_BOTTOM_LEFT_CHAMPAGNE.isTopLeft());
        assertFalse(CavePlaceTypeEnum.PLACE_TOP_RIGHT_CHAMPAGNE.isTopLeft());
        assertTrue(CavePlaceTypeEnum.PLACE_TOP_LEFT_CHAMPAGNE.isTopLeft());
    }
}