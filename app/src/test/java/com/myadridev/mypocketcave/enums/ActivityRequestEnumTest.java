package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityRequestEnumTest {

    @Test
    public void getByIdActivityRequestEnumExisting() {
        assertEquals(ActivityRequestEnum.CREATE_PATTERN, ActivityRequestEnum.getById(ActivityRequestEnum.CREATE_PATTERN.Id));
    }

    @Test
    public void getByIdActivityRequestEnumNonExisting() {
        assertEquals(null, ActivityRequestEnum.getById(-1));
    }
}