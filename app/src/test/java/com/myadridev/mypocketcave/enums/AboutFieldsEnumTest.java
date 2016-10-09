package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AboutFieldsEnumTest {

    @Test
    public void getStringResourceAboutFieldsEnum() {
        Assert.assertEquals(R.string.about_sources, AboutFieldsEnum.SOURCES.StringResourceId);
    }

    @Test
    public void compareEqualAboutFieldsEnum() {
        assertEquals(0, AboutFieldsEnum.CONTACT.compare(AboutFieldsEnum.CONTACT));
    }

    @Test
    public void compareDifferentAboutFieldsEnum() {
        assertEquals(1, AboutFieldsEnum.CONTACT.compare(AboutFieldsEnum.VERSION));
        assertEquals(-1, AboutFieldsEnum.CONTACT.compare(AboutFieldsEnum.LICENSE));
    }
}