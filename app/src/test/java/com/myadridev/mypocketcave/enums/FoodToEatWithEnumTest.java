package com.myadridev.mypocketcave.enums;

import android.content.Context;

import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;

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
public class FoodToEatWithEnumTest {

    @Mock
    Context mockContext;

    @Test
    public void getByIdFoodToEatWithEnumExisting() {
        assertEquals(FoodToEatWithEnumV2.rm, FoodToEatWithEnumV2.getById(FoodToEatWithEnumV2.rm.Id));
    }

    @Test
    public void getByIdFoodToEatWithEnumNonExisting() {
        assertEquals(null, FoodToEatWithEnumV2.getById(-1));
    }

    @Test
    public void getAllFoodLabels() {
        final String fakeOutput = "fake";

        when(mockContext.getString(anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return fakeOutput + (int) invocation.getArguments()[0];
            }
        });

        String[] allLabels = FoodToEatWithEnumV2.getAllFoodLabels(mockContext);
        assertEquals(FoodToEatWithEnumV2.values().length, allLabels.length);
        for (int i = 0; i < FoodToEatWithEnumV2.values().length; i++) {
            assertEquals(fakeOutput + FoodToEatWithEnumV2.getById(i).StringResourceId, allLabels[i]);
        }
    }
}