package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatternTypeEnumTest {

    @Test
    public void getByIdPatternTypeEnumExisting() {
        assertEquals(PatternTypeEnumV2.l, PatternTypeEnumV2.getById(PatternTypeEnumV2.l.Id));
    }

    @Test
    public void getByIdPatternTypeEnumNonExisting() {
        assertEquals(null, PatternTypeEnumV2.getById(-1));
    }
}