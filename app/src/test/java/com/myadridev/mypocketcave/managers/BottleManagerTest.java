package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IBottleStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleCriteriaV2;
import com.myadridev.mypocketcave.models.v2.SuggestBottleResultModelV2;

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

    private static List<BottleModelV2> bottles;
    private static int currentYear;

    @BeforeClass
    public static void beforeClass() {
        createSearchBottleSet();
        DependencyManager.init();
        IBottleStorageManagerV2 mockBottleStorageManager = mock(IBottleStorageManagerV2.class);
        when(mockBottleStorageManager.getBottles()).thenAnswer(new Answer<List<BottleModelV2>>() {
            @Override
            public List<BottleModelV2> answer(InvocationOnMock invocation) {
                return bottles;
            }
        });
        DependencyManager.registerSingleton(IBottleStorageManagerV2.class, mockBottleStorageManager, true);

        ICavesStorageManagerV2 mockCavesStorageManager = mock(ICavesStorageManagerV2.class);
        when(mockCavesStorageManager.getLightCaves())
                .thenAnswer(new Answer<List<CaveLightModelV2>>() {
                    @Override
                    public List<CaveLightModelV2> answer(InvocationOnMock invocation) throws Throwable {
                        return new ArrayList<>();
                    }
                });
        DependencyManager.registerSingleton(ICavesStorageManagerV2.class, mockCavesStorageManager, true);
    }

    private static void createSearchBottleSet() {
        bottles = new ArrayList<>();
        List<List<FoodToEatWithEnumV2>> foods = new ArrayList<>();
        foods.add(new ArrayList<>());
        foods.add(new ArrayList<>());
        foods.get(1).add(FoodToEatWithEnumV2.po);
        foods.get(1).add(FoodToEatWithEnumV2.fi);

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);

        int count = 0;
        int numberDifferentDomains = 2;
        for (int i = 0; i < numberDifferentDomains; i++) {
            String domain = "AA - Domaine " + (i + 1);
            for (int c = 0; c < 2; c++) {
                WineColorEnumV2 color = WineColorEnumV2.getById(c + 1);
                for (int j = 0; j < 4; j++) {
                    int millesime = currentYear - (3 * j);
                    for (int k = 0; k < 2; k++) {
                        String person = "Person " + (k + 1);
                        for (int l = 0; l < 2; l++) {
                            count++;
                            String name = "AA - Suggest " + count;

                            BottleModelV2 bottle = new BottleModelV2();
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
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size(), suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenDomainMandatory() {
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        searchCriteria.IsDomainRequired = true;
        searchCriteria.Domain = "AA - Domaine 1";

        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertEquals(searchCriteria.Domain, suggestBottle.Bottle.Domain);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenWineColorMandatory() {
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        searchCriteria.IsWineColorRequired = true;
        searchCriteria.WineColor = WineColorEnumV2.getById(1);

        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertEquals(searchCriteria.WineColor, suggestBottle.Bottle.WineColor);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenMillesimeMandatory() {
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        searchCriteria.IsMillesimeRequired = true;

        searchCriteria.Millesime = MillesimeEnumV2.ltt;
        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(bottles.size() / 4, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.Millesime <= currentYear);
            assertTrue(suggestBottle.Bottle.Millesime >= currentYear - 2);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }

        searchCriteria.Millesime = MillesimeEnumV2.ttf;
        suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(bottles.size() / 4, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.Millesime <= currentYear - 3);
            assertTrue(suggestBottle.Bottle.Millesime >= currentYear - 5);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }

        searchCriteria.Millesime = MillesimeEnumV2.stt;
        suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.Millesime <= currentYear - 6);
            assertTrue(suggestBottle.Bottle.Millesime >= currentYear - 10);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }

        searchCriteria.Millesime = MillesimeEnumV2.ot;
        suggestBottles = BottleManager.getSuggestBottles(searchCriteria);
        assertEquals(0, suggestBottles.size());
    }

    @Test
    public void getSuggestBottlesWhenFoodMandatory() {
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        searchCriteria.IsFoodRequired = true;
        List<FoodToEatWithEnumV2> foodCriteria = new ArrayList<>();
        foodCriteria.add(FoodToEatWithEnumV2.po);
        foodCriteria.add(FoodToEatWithEnumV2.de);
        searchCriteria.FoodToEatWithList.clear();
        searchCriteria.FoodToEatWithList.addAll(foodCriteria);

        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertTrue(suggestBottle.Bottle.FoodToEatWithList.contains(FoodToEatWithEnumV2.po)
                    || suggestBottle.Bottle.FoodToEatWithList.contains(FoodToEatWithEnumV2.de));
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenPersonMandatory() {
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        searchCriteria.IsPersonRequired = true;
        searchCriteria.PersonToShareWith = "Person 1";

        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            assertEquals(searchCriteria.PersonToShareWith, suggestBottle.Bottle.PersonToShareWith);
            assertTrue(bottles.contains(suggestBottle.Bottle));
        }
    }

    @Test
    public void getSuggestBottlesWhenWineColorMandatoryAndOtherCriteriaNonMandatory() {
        SuggestBottleCriteriaV2 searchCriteria = new SuggestBottleCriteriaV2();
        searchCriteria.IsWineColorRequired = true;
        searchCriteria.WineColor = WineColorEnumV2.getById(1);
        searchCriteria.Millesime = MillesimeEnumV2.ttf;

        List<SuggestBottleResultModelV2> suggestBottles = BottleManager.getSuggestBottles(searchCriteria);

        assertEquals(bottles.size() / 2, suggestBottles.size());
        for (SuggestBottleResultModelV2 suggestBottle : suggestBottles) {
            assertEquals(searchCriteria.WineColor, suggestBottle.Bottle.WineColor);
            assertTrue(bottles.contains(suggestBottle.Bottle));
            if (suggestBottle.Bottle.Millesime <= currentYear - 3 && suggestBottle.Bottle.Millesime >= currentYear - 5) {
                assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria, suggestBottle.Score);
            } else {
                assertEquals(SuggestBottleCriteriaV2.NumberOfCriteria - 1, suggestBottle.Score);
            }
        }
    }
}