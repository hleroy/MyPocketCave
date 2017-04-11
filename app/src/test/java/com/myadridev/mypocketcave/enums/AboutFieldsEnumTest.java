package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AboutFieldsEnumTest {

    @Test
    public void getStringResourceAboutFieldsEnum() {
        Assert.assertEquals(R.string.about_sources, AboutFieldsEnumV2.s.StringResourceId);
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