package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AboutFieldsEnumTest {

    @Test
    public void getStringResourceAboutFieldsEnum() {
        assertEquals(R.string.about_sources, AboutFieldsEnumV2.s.StringResourceId);
    }

    @Test
    public void getByIdExisting() {
        assertEquals(AboutFieldsEnumV2.s, AboutFieldsEnumV2.getById(2));
    }

    @Test
    public void getByIdNonExisting() {
        assertEquals(null, AboutFieldsEnumV2.getById(-1));
    }

    @Test
    public void compareEqualAboutFieldsEnum() {
        assertEquals(0, AboutFieldsEnumV2.c.compare(AboutFieldsEnumV2.c));
    }

    @Test
    public void compareDifferentAboutFieldsEnum() {
        assertEquals(1, AboutFieldsEnumV2.c.compare(AboutFieldsEnumV2.v));
        assertEquals(-1, AboutFieldsEnumV2.c.compare(AboutFieldsEnumV2.l));
    }
}