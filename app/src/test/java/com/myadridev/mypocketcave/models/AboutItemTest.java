package com.myadridev.mypocketcave.models;

import android.content.Context;

import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;
import com.myadridev.mypocketcave.models.v2.AboutItemV2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AboutItemTest {

    private final String fakeOutput = "item";
    @Mock
    Context mockContext;

    @Before
    public void before() {
        when(mockContext.getString(anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return fakeOutput + (int) invocation.getArguments()[0];
            }
        });
    }

    @Test
    public void compareEqualAboutItem() {
        AboutItemV2 item1 = new AboutItemV2(mockContext, AboutFieldsEnumV2.s, "value1");
        AboutItemV2 item2 = new AboutItemV2(mockContext, AboutFieldsEnumV2.c, "value2");
        AboutItemV2 item3 = new AboutItemV2(mockContext, AboutFieldsEnumV2.l, "value2");
        AboutItemV2 item4 = new AboutItemV2(mockContext, AboutFieldsEnumV2.s, "value2");

        assertEquals(0, item1.compareTo(item4));
        assertEquals(1, item1.compareTo(item2));
        assertEquals(-1, item1.compareTo(item3));
    }
}