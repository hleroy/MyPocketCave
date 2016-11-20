package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.IStorableModel;
import com.myadridev.mypocketcave.models.PatternModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiresApi(api = Build.VERSION_CODES.N)
@RunWith(MockitoJUnitRunner.class)
public class BottlesSharedPreferencesManagerTest {

    private static Map<Integer, IStorableModel> bottleMap;
    private static List<BottleModel> sortedBottles;
    private static List<Integer> bottleIds;

    @BeforeClass
    public static void beforeClass() {
        initBottleMap();
        DependencyManager.init();
        ISharedPreferencesManager mockSharedPreferencesManager = mock(ISharedPreferencesManager.class);
        when(mockSharedPreferencesManager.loadStoredDataMap(anyString(), anyString(), anyInt(), eq(BottleModel.class))).thenAnswer(new Answer<Map<Integer, IStorableModel>>() {
            @Override
            public Map<Integer, IStorableModel> answer(InvocationOnMock invocation) {
                return bottleMap;
            }
        });
        when(mockSharedPreferencesManager.getStringFromResource(eq(R.string.store_indexes))).thenReturn("indexes");
        when(mockSharedPreferencesManager.getStringFromResource(eq(R.string.filename_bottles))).thenReturn("bottles");
        when(mockSharedPreferencesManager.getStringFromResource(eq(R.string.store_bottle), anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return "bottle_" + (int) invocation.getArguments()[1];
            }
        });
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Object> dataMap = (Map<String, Object>) invocation.getArguments()[1];
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith("bottle_")) {
                        BottleModel bottle = (BottleModel) entry.getValue();
                        bottleMap.put(bottle.Id, bottle);
                        sortedBottles.removeIf(existingBottle -> existingBottle.Id == bottle.Id);
                        sortedBottles.add(bottle);
                        Collections.sort(sortedBottles);
                    } else if (key.equalsIgnoreCase("indexes")) {
                        List<Integer> ids = (List<Integer>) entry.getValue();
                        bottleIds.clear();
                        bottleIds.addAll(ids);
                        Collections.sort(bottleIds);
                    }
                }
                return null;
            }
        }).when(mockSharedPreferencesManager).storeStringMapData(eq("bottles"), anyMapOf(String.class, Object.class));

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                String storeFilename = (String) arguments[0];
                String key = (String) arguments[1];
                Object dataToStore = arguments[2];
                Map<String, Object> dataToStoreMap = new HashMap<>(1);
                dataToStoreMap.put(key, dataToStore);
                mockSharedPreferencesManager.storeStringMapData(storeFilename, dataToStoreMap);
                return null;
            }
        }).when(mockSharedPreferencesManager).storeStringData(anyString(), anyString(), Matchers.anyObject());

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[1];
                if (key.startsWith("bottle_")) {
                    int bottleId = Integer.parseInt(key.substring(7, key.length()));
                    sortedBottles.removeIf(bottle -> bottle.Id == bottleId);
                    bottleMap.remove(bottleId);
                }
                return null;
            }
        }).when(mockSharedPreferencesManager).removeData(eq("bottles"), anyString());
        DependencyManager.registerSingleton(ISharedPreferencesManager.class, mockSharedPreferencesManager, true);
    }

    private static void initBottleMap() {
        bottleMap = new HashMap<>();
        bottleIds = new ArrayList<>();
        sortedBottles = new ArrayList<>();
        List<List<FoodToEatWithEnum>> foods = new ArrayList<>();
        foods.add(new ArrayList<>());
        foods.add(new ArrayList<>());
        foods.get(1).add(FoodToEatWithEnum.PorkProduct);
        foods.get(1).add(FoodToEatWithEnum.Fish);

        int count = 0;
        int numberDifferentDomains = 2;
        for (int i = 0; i < numberDifferentDomains; i++) {
            String domain = "AA - Domaine " + (i + 1);
            for (int c = 0; c < 2; c++) {
                WineColorEnum color = WineColorEnum.getById(c + 1);
                int millesime = 42;
                for (int k = 0; k < 2; k++) {
                    String person = "Person " + (k + 1);
                    for (int l = 0; l < 2; l++) {
                        count++;
                        String name = "AA - Suggest " + count;

                        BottleModel bottle = new BottleModel();
                        bottle.Id = count;
                        bottle.Domain = domain;
                        bottle.WineColor = color;
                        bottle.Millesime = millesime;
                        bottle.PersonToShareWith = person;
                        bottle.Name = name;
                        bottle.FoodToEatWithList.addAll(foods.get(l));
                        bottle.Stock = 2;
                        bottle.NumberPlaced = count % 3;

                        bottleMap.put(bottle.Id, bottle);
                        sortedBottles.add(bottle);
                        bottleIds.add(bottle.Id);
                    }
                }
            }
        }

        Collections.sort(bottleIds);
        Collections.sort(sortedBottles);
    }

    @AfterClass
    public static void afterClass() {
        DependencyManager.cleanUp();
    }

    @Before
    public void before() {
        BottlesSharedPreferencesManager.Init();
        assertTrue(BottlesSharedPreferencesManager.IsInitialized());
    }

    @Test
    public void getBottles() {
        List<BottleModel> outputBottles = BottlesSharedPreferencesManager.Instance.getBottles();
        assertEquals(sortedBottles.size(), outputBottles.size());
        for (int i = 0; i < sortedBottles.size(); i++) {
            assertEquals(sortedBottles.get(i), outputBottles.get(i));
        }
    }

    @Test
    public void getBottleWhenNoBottle() {
        BottleModel outputBottle = BottlesSharedPreferencesManager.Instance.getBottle(-1);
        assertNull(outputBottle);
    }

    @Test
    public void getBottleWhenBottleExists() {
        BottleModel outputBottle = BottlesSharedPreferencesManager.Instance.getBottle(1);
        BottleModel expectedBottle = (BottleModel) bottleMap.get(1);
        assertNotNull(outputBottle);
        assertEquals(expectedBottle, outputBottle);
    }

    @Test
    public void insertBottle() {
        BottleModel newBottle = new BottleModel();
        newBottle.WineColor = WineColorEnum.CHAMPAGNE;
        newBottle.Domain = "New domain";
        newBottle.PersonToShareWith = "new Person";
        newBottle.Stock = 42;
        newBottle.Millesime = 2012;
        newBottle.Name = "new name";
        newBottle.FoodToEatWithList.add(FoodToEatWithEnum.Exotic);
        newBottle.FoodToEatWithList.add(FoodToEatWithEnum.Cheese);

        int expectedBottleId = 17;
        assertFalse(bottleIds.contains(expectedBottleId));
        assertFalse(bottleMap.containsKey(expectedBottleId));
        assertTrue(sortedBottles.parallelStream().noneMatch(bottle -> bottle.Id == expectedBottleId));

        int outputBottleId = BottlesSharedPreferencesManager.Instance.insertBottle(newBottle);

        assertEquals(expectedBottleId, outputBottleId);
        assertEquals(expectedBottleId, newBottle.Id);
        assertTrue(bottleIds.contains(expectedBottleId));
        assertTrue(bottleMap.containsKey(expectedBottleId));
        assertEquals(newBottle, bottleMap.get(expectedBottleId));

        sortedBottles.forEach(bottle -> {
            if (bottle.Id == expectedBottleId) {
                assertEquals(newBottle, bottle);
            }
        });

        // clean
        sortedBottles.removeIf(bottle -> bottle.Id == expectedBottleId);
        bottleMap.remove(expectedBottleId);
        bottleIds.removeIf(id -> id == expectedBottleId);
    }

    @Test
    public void updateBottle() {
        BottleModel updatedBottle = new BottleModel();
        updatedBottle.Id = 1;
        updatedBottle.WineColor = WineColorEnum.CHAMPAGNE;
        updatedBottle.Domain = "New domain";
        updatedBottle.PersonToShareWith = "new Person";
        updatedBottle.Stock = 42;
        updatedBottle.Millesime = 2012;
        updatedBottle.Name = "new name";
        updatedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Exotic);
        updatedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Cheese);

        IStorableModel oldBottle = bottleMap.get(updatedBottle.Id);

        BottlesSharedPreferencesManager.Instance.updateBottle(updatedBottle);

        assertTrue(bottleIds.contains(updatedBottle.Id));
        assertTrue(bottleMap.containsKey(updatedBottle.Id));
        assertEquals(updatedBottle, bottleMap.get(updatedBottle.Id));

        sortedBottles.forEach(bottle -> {
            if (bottle.Id == updatedBottle.Id) {
                assertEquals(updatedBottle, bottle);
            }
        });

        // clean
        bottleMap.put(updatedBottle.Id, oldBottle);
        sortedBottles.replaceAll(bottle -> bottle.Id == updatedBottle.Id ? (BottleModel) oldBottle : bottle);
    }

    @Test
    public void deleteBottle() {
        int idToRemove = 1;
        IStorableModel oldBottle = bottleMap.get(idToRemove);

        BottlesSharedPreferencesManager.Instance.deleteBottle(idToRemove);

        assertFalse(bottleIds.contains(idToRemove));
        assertTrue(sortedBottles.parallelStream().noneMatch(bottle -> bottle.Id == idToRemove));
        assertFalse(bottleMap.containsKey(idToRemove));

        // clean
        bottleIds.add(idToRemove);
        Collections.sort(bottleIds);
        bottleMap.put(idToRemove, oldBottle);
        sortedBottles.add((BottleModel) oldBottle);
        Collections.sort(sortedBottles);
    }

    @Test
    public void getExistingBottleId() {
        int expectedId = 1;
        BottleModel existingBottle = (BottleModel) bottleMap.get(expectedId);

        int existingId = BottlesSharedPreferencesManager.Instance.getExistingBottleId(42, existingBottle.Name, existingBottle.Domain, existingBottle.WineColor.Id, existingBottle.Millesime);
        assertEquals(expectedId, existingId);

        int nonExistingId = BottlesSharedPreferencesManager.Instance.getExistingBottleId(existingBottle.Id, existingBottle.Name, existingBottle.Domain, existingBottle.WineColor.Id, existingBottle.Millesime);
        assertEquals(0, nonExistingId);
    }

    @Test
    public void getBottlesCount() {
        assertEquals(bottleMap.values().parallelStream().mapToInt(bottle -> ((BottleModel) bottle).Stock).sum(),
                BottlesSharedPreferencesManager.Instance.getBottlesCount());

        int numberRed = sortedBottles.parallelStream()
                .filter(bottle -> bottle.WineColor == WineColorEnum.RED)
                .mapToInt(bottle -> bottle.Stock).sum();
        assertEquals(numberRed, BottlesSharedPreferencesManager.Instance.getBottlesCount(WineColorEnum.RED.Id));

        List<BottleModel> filteredBottles = sortedBottles.parallelStream()
                .filter(bottle -> bottle.PersonToShareWith.equalsIgnoreCase("Person 1"))
                .collect(Collectors.toList());
        assertEquals(filteredBottles.parallelStream().mapToInt(bottle -> bottle.Stock).sum(),
                BottlesSharedPreferencesManager.Instance.getBottlesCount(filteredBottles));

        int numberFilteredRed = filteredBottles.parallelStream()
                .filter(bottle -> bottle.WineColor == WineColorEnum.RED)
                .mapToInt(bottle -> bottle.Stock).sum();
        assertEquals(numberFilteredRed,
                BottlesSharedPreferencesManager.Instance.getBottlesCount(filteredBottles, WineColorEnum.RED.Id));
    }

    @Test
    public void getDistinctPersons() {
        List<String> expectedDistinctPersonsList = sortedBottles.parallelStream()
                .filter(bottle -> bottle.PersonToShareWith != null && !bottle.PersonToShareWith.isEmpty())
                .map(bottle -> bottle.PersonToShareWith).distinct()
                .collect(Collectors.toList());
        Collections.sort(expectedDistinctPersonsList);
        int size = expectedDistinctPersonsList.size();
        String[] expectedDistinctPersons = new String[size];
        expectedDistinctPersonsList.toArray(expectedDistinctPersons);
        String[] distinctPersons = BottlesSharedPreferencesManager.Instance.getDistinctPersons();

        assertEquals(size, distinctPersons.length);
        for (int i = 0; i < size; i++) {
            assertEquals(expectedDistinctPersons[i], distinctPersons[i]);
        }
    }

    @Test
    public void getDistinctDomains() {
        List<String> expectedDistinctDomainsList = sortedBottles.parallelStream()
                .filter(bottle -> bottle.Domain != null && !bottle.Domain.isEmpty())
                .map(bottle -> bottle.Domain).distinct()
                .collect(Collectors.toList());
        Collections.sort(expectedDistinctDomainsList);
        int size = expectedDistinctDomainsList.size();
        String[] expectedDistinctDomains = new String[size];
        expectedDistinctDomainsList.toArray(expectedDistinctDomains);
        String[] distinctDomains = BottlesSharedPreferencesManager.Instance.getDistinctDomains();

        assertEquals(size, distinctDomains.length);
        for (int i = 0; i < size; i++) {
            assertEquals(expectedDistinctDomains[i], distinctDomains[i]);
        }
    }

    @Test
    public void getNonPlacedBottles() {
        List<BottleModel> expectedNonPlaced = sortedBottles.parallelStream()
                .filter(bottle -> bottle.NumberPlaced != bottle.Stock).collect(Collectors.toList());
        List<BottleModel> nonPlacedBottles = BottlesSharedPreferencesManager.Instance.getNonPlacedBottles();

        int size = expectedNonPlaced.size();
        assertEquals(size, nonPlacedBottles.size());
        for (int i = 0 ; i < size; i++) {
            assertEquals(expectedNonPlaced.get(i), nonPlacedBottles.get(i));
        }
    }

    @Test
    public void updateNumberPlaced() {
        int bottleId = 5;
        BottleModel bottleToUpdate = (BottleModel) bottleMap.get(bottleId);
        int numberPlacedBefore = bottleToUpdate.NumberPlaced;
        int increment = -1;
        BottlesSharedPreferencesManager.Instance.updateNumberPlaced(bottleId, increment);
        int numberPlacedAfter = bottleToUpdate.NumberPlaced;
        assertEquals(numberPlacedBefore + increment, numberPlacedAfter);
    }
}