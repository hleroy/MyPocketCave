package com.myadridev.mypocketcave.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FoodToEatWithEnumTest {

    @Test
    public void getByIdFoodToEatWithEnumExisting() throws Exception {
        assertEquals(FoodToEatWithEnum.RedMeat, FoodToEatWithEnum.getById(FoodToEatWithEnum.RedMeat.id));
    }

    @Test
    public void getByIdFoodToEatWithEnumNonExisting() throws Exception {
        assertEquals(null, FoodToEatWithEnum.getById(-1));
    }
}