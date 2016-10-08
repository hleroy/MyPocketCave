package com.myadridev.mypocketcave.helpers;

import android.content.Context;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FoodToEatHelperTest {

    @Mock
    Context mockContext;

    private final String fakeOutput = "fake";
    private final String foodSeparator = ", ";

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
    public void computeFoodViewTextWithBooleans() {
        boolean[] foodToEatWithList = new boolean[FoodToEatWithEnum.values().length];

        StringBuilder expectedSb = new StringBuilder();
        boolean isAtLeastOneFood = false;
        for (int i = 0; i < FoodToEatWithEnum.values().length; i++) {
            foodToEatWithList[i] = i % 3 == 0;
            if (foodToEatWithList[i]) {
                if (isAtLeastOneFood) {
                    expectedSb.append(foodSeparator);
                }
                expectedSb.append(fakeOutput + FoodToEatWithEnum.getById(i).StringResourceId);
                isAtLeastOneFood = true;
            }
        }

        String computedTest = FoodToEatHelper.computeFoodViewText(mockContext, foodToEatWithList);
        assertEquals(expectedSb.toString(), computedTest);
    }

    @Test
    public void computeFoodViewTextWithLists() {
        List<FoodToEatWithEnum> foodToEatWithList = new ArrayList<>();

        StringBuilder expectedSb = new StringBuilder();
        boolean isAtLeastOneFood = false;
        for (int i = 0; i < FoodToEatWithEnum.values().length; i++) {
            if (i % 3 == 0) {
                FoodToEatWithEnum food = FoodToEatWithEnum.getById(i);
                foodToEatWithList.add(food);
                if (isAtLeastOneFood) {
                    expectedSb.append(foodSeparator);
                }
                expectedSb.append(fakeOutput + food.StringResourceId);
                isAtLeastOneFood = true;
            }
        }

        String computedTest = FoodToEatHelper.computeFoodViewText(mockContext, foodToEatWithList);
        assertEquals(expectedSb.toString(), computedTest);
    }
}