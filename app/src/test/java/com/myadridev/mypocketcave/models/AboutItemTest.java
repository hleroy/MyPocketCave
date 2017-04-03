package com.myadridev.mypocketcave.models;

import android.content.Context;

import com.myadridev.mypocketcave.enums.AboutFieldsEnum;
import com.myadridev.mypocketcave.models.v1.AboutItem;

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
        AboutItem item1 = new AboutItem(mockContext, AboutFieldsEnum.SOURCES, "value1");
        AboutItem item2 = new AboutItem(mockContext, AboutFieldsEnum.CONTACT, "value2");
        AboutItem item3 = new AboutItem(mockContext, AboutFieldsEnum.LICENSE, "value2");
        AboutItem item4 = new AboutItem(mockContext, AboutFieldsEnum.SOURCES, "value2");

        assertEquals(0, item1.compareTo(item4));
        assertEquals(1, item1.compareTo(item2));
        assertEquals(-1, item1.compareTo(item3));
    }
}