package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionEnumTest {

    @Test
    public void getByIdExisting() {
        assertEquals(VersionEnum.V2, VersionEnum.getById(1));
    }

    @Test
    public void getByIdNonExisting() {
        assertEquals(null, VersionEnum.getById(-1));
    }
}