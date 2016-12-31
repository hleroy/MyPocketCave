package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.MillesimeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.storage.interfaces.IBottleStorageManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.SuggestBottleResultModel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BottleManagerTest {

    private static List<BottleModel> bottles;
    private static int currentYear;

    @BeforeClass
    public static void beforeClass() {
        createSearchBottleSet();
        DependencyManager.init();
        IBottleStorageManager mockBottleStorageManager = mock(IBottleStorageManager.class);
        when(mockBottleStorageManager.getBottles()).thenAnswer(new Answer<List<BottleModel>>() {
            @Override
            public List<BottleModel> answer(InvocationOnMock invocation) {
                return bottles;
            }
        });
        DependencyManager.registerSingleton(IBottleStorageManager.class, mockBottleStorageManager, true);
    }

    private static void createSearchBottleSet() {
        bottles = new ArrayList<>();
        List<List<FoodToEatWithEnum>> foods = new ArrayList<>();
        foods.add(new ArrayList<>());
        foods.add(new ArrayList<>());
        foods.get(1).add(FoodToEatWithEnum.PorkProduct);
        foods.get(1).add(FoodToEatWithEnum.Fish);

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);

        int count = 0;
        int numberDifferentDomains = 2;
        for (int i = 0; i < numberDifferentDomains; i++) {
            String domain = "AA - Domaine " + (i + 1);
            for (int c = 0; c < 2; c++) {
                WineColorEnum color = WineColorEnum.getById(c + 1);
                for (int j = 0; j < 4; j++) {
                    int millesime = currentYear - (3 * j);
                    for (int k = 0; k < 2; k++) {
                        String person = "Person " + (k + 1);
                        for (int l = 0; l < 2; l++) {
                            count++;
                            String name = "AA - Suggest " + count;

                            BottleModel bottle = new BottleModel();
                            bottle.Domain = domain;
                            bottle.WineColor = color;
                            bottle.Millesime = millesime;
                            bottle.PersonToShareWith = person;
                            bottle.Name = name;
                            bottle.FoodToEatWithList.addAll(foods.get(l));
                            bottle.Stock = 1;

                            bottles.add(bottle);
                        }
                    }
                }
            }
        }
    }

    @AfterClass
    public static void afterClass() {
        DependencyManager.cleanUp();
    }

    @Test
    public void getSuggestBottlesWhenNoCriteria() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size(), suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenDomainMandatory() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        searchCriteria.IsDomainRequired = true;
        searchCriteria.Domain = "AA - Domaine 1";

        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertEquals(searchCriteria.Domain, suggestBottle.Bottle.Domain);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenWineColorMandatory() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        searchCriteria.IsWineColorRequired = true;
        searchCriteria.WineColor = WineColorEnum.getById(1);

        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertEquals(searchCriteria.WineColor, suggestBottle.Bottle.WineColor);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenMillesimeMandatory() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        searchCriteria.IsMillesimeRequired = true;

        searchCriteria.Millesime = MillesimeEnum.LESS_THAN_TWO;
        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(bottles.size() / 4, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.Millesime <= currentYear);
            assertTrue(suggestBottle.Bottle.Millesime >= currentYear - 2);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }

        searchCriteria.Millesime = MillesimeEnum.THREE_TO_FIVE;
        suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(bottles.size() / 4, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.Millesime <= currentYear - 3);
            assertTrue(suggestBottle.Bottle.Millesime >= currentYear - 5);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }

        searchCriteria.Millesime = MillesimeEnum.SIX_TO_TEN;
        suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.Millesime <= currentYear - 6);
            assertTrue(suggestBottle.Bottle.Millesime >= currentYear - 10);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }

        searchCriteria.Millesime = MillesimeEnum.OVER_TEN;
        suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(0, suggestBottles.size());
    }

    @Test
    public void getSuggestBottlesWhenFoodMandatory() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        searchCriteria.IsFoodRequired = true;
        List<FoodToEatWithEnum> foodCriteria = new ArrayList<>();
        foodCriteria.add(FoodToEatWithEnum.PorkProduct);
        foodCriteria.add(FoodToEatWithEnum.Dessert);
        searchCriteria.FoodToEatWithList.clear();
        searchCriteria.FoodToEatWithList.addAll(foodCriteria);

        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.FoodToEatWithList.contains(FoodToEatWithEnum.PorkProduct)
                    || suggestBottle.Bottle.FoodToEatWithList.contains(FoodToEatWithEnum.Dessert));
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenPersonMandatory() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        searchCriteria.IsPersonRequired = true;
        searchCriteria.PersonToShareWith = "Person 1";

        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(5, suggestBottle.Score);
            assertEquals(searchCriteria.PersonToShareWith, suggestBottle.Bottle.PersonToShareWith);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenWineColorMandatoryAndOtherCriteriaNonMandatory() {
        SuggestBottleCriteria searchCriteria = new SuggestBottleCriteria();
        searchCriteria.IsWineColorRequired = true;
        searchCriteria.WineColor = WineColorEnum.getById(1);
        searchCriteria.Millesime = MillesimeEnum.THREE_TO_FIVE;

        List<SuggestBottleResultModel> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestBottles) {
            assertEquals(searchCriteria.WineColor, suggestBottle.Bottle.WineColor);
            assertTrue(bottles.contains(suggestBottle.Bottle));
            if (suggestBottle.Bottle.Millesime <= currentYear - 3 && suggestBottle.Bottle.Millesime >= currentYear - 5) {
                assertEquals(5, suggestBottle.Score);
            } else {
                assertEquals(4, suggestBottle.Score);
            }
        }
    }
}