package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatternTypeEnumTest {

    @Test
    public void getByIdPatternTypeEnumExisting() {
        assertEquals(PatternTypeEnum.LINEAR, PatternTypeEnum.getById(PatternTypeEnum.LINEAR.Id));
    }

    @Test
    public void getByIdPatternTypeEnumNonExisting() {
        assertEquals(null, PatternTypeEnum.getById(-1));
    }
}