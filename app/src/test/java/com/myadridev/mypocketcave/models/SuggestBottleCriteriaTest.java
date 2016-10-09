package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.MillesimeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by adrie on 09/10/2016.
 */
public class SuggestBottleCriteriaTest {

    @Test
    public void createSuggestBottleCriteria() {
        SuggestBottleCriteria criteria = new SuggestBottleCriteria();
        assertEquals(WineColorEnum.ANY, criteria.WineColor);
        assertFalse(criteria.IsWineColorRequired);
        assertEquals("", criteria.Domain);
        assertFalse(criteria.IsDomainRequired);
        assertEquals(MillesimeEnum.ANY, criteria.Millesime);
        assertFalse(criteria.IsMillesimeRequired);
        assertTrue(criteria.FoodToEatWithList.isEmpty());
        assertFalse(criteria.IsFoodRequired);
        assertEquals("", criteria.PersonToShareWith);
        assertFalse(criteria.IsPersonRequired);
    }
}