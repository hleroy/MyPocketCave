//package com.myadridev.mypocketcave.enums;
//
//import com.myadridev.mypocketcave.R;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class AboutFieldsEnumTest {
//
//    @Test
//    public void getStringResourceAboutFieldsEnum() throws Exception {
//        Assert.assertEquals(R.string.about_sources, AboutFieldsEnum.SOURCES.getStringResource());
//    }
//
//    @Test
//    public void compareEqualAboutFieldsEnum() throws Exception {
//        assertEquals(0, AboutFieldsEnum.CONTACT.compare(AboutFieldsEnum.CONTACT));
//    }
//
//    @Test
//    public void compareDifferentAboutFieldsEnum() throws Exception {
//        assertEquals(1, AboutFieldsEnum.CONTACT.compare(AboutFieldsEnum.VERSION));
//        assertEquals(-1, AboutFieldsEnum.CONTACT.compare(AboutFieldsEnum.LICENSE));
//    }
//}