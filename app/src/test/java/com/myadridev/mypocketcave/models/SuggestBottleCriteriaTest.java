package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleCriteriaV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SuggestBottleCriteriaTest {

    @Test
    public void createSuggestBottleCriteria() {
        SuggestBottleCriteriaV2 criteria = new SuggestBottleCriteriaV2();
        assertEquals(WineColorEnumV2.a, criteria.WineColor);
        assertFalse(criteria.IsWineColorRequired);
        assertEquals("", criteria.Domain);
        assertFalse(criteria.IsDomainRequired);
        assertEquals(MillesimeEnumV2.a, criteria.Millesime);
        assertFalse(criteria.IsMillesimeRequired);
        assertTrue(criteria.FoodToEatWithList.isEmpty());
        assertFalse(criteria.IsFoodRequired);
        assertEquals("", criteria.PersonToShareWith);
        assertFalse(criteria.IsPersonRequired);
    }
}