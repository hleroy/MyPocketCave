package com.myadridev.mypocketcave.enums;

import android.content.Context;

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

    @Test
    public void getByIdFoodToEatWithEnumExisting() {
        assertEquals(FoodToEatWithEnum.RedMeat, FoodToEatWithEnum.getById(FoodToEatWithEnum.RedMeat.Id));
    }

    @Test
    public void getByIdFoodToEatWithEnumNonExisting() {
        assertEquals(null, FoodToEatWithEnum.getById(-1));
    }

    @Mock
    Context mockContext;

    @Test
    public void getAllFoodLabels() {
        final String fakeOutput = "fake";

        when(mockContext.getString(anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return fakeOutput + (int) invocation.getArguments()[0];
            }
        });

        String[] allLabels = FoodToEatWithEnum.getAllFoodLabels(mockContext);
        assertEquals(FoodToEatWithEnum.values().length, allLabels.length);
        for (int i = 0; i < FoodToEatWithEnum.values().length; i++) {
            assertEquals(fakeOutput + FoodToEatWithEnum.getById(i).StringResourceId, allLabels[i]);
        }
    }
}