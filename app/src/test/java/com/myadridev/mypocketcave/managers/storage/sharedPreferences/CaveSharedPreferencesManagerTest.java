package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.IStorableModel;

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
public class CaveSharedPreferencesManagerTest {

    private static Map<Integer, IStorableModel> caveMap;
    private static List<CaveModel> sortedCaves;
    private static List<Integer> caveIds;

    @BeforeClass
    public static void beforeClass() {
        initBottleMap();
        DependencyManager.init();
        ISharedPreferencesManager mockSharedPreferencesManager = mock(ISharedPreferencesManager.class);

        when(mockSharedPreferencesManager.getStringFromResource(eq(R.string.store_cave_key))).thenReturn("cave");
        when(mockSharedPreferencesManager.getStringFromResource(eq(R.string.store_cave), anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return "cave_" + (int) invocation.getArguments()[1];
            }
        });
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String fileName = (String) invocation.getArguments()[0];
                Map<String, Object> dataMap = (Map<String, Object>) invocation.getArguments()[1];
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String key = entry.getKey();
                    if (key.equalsIgnoreCase("cave") && fileName.startsWith("cave_")) {
                        CaveModel cave = (CaveModel) entry.getValue();
                        caveMap.put(cave.Id, cave);
                        sortedCaves.removeIf(existingCave -> existingCave.Id == cave.Id);
                        sortedCaves.add(cave);
                        caveIds.removeIf(id -> id == cave.Id);
                        caveIds.add(cave.Id);
                        Collections.sort(caveIds);
                        Collections.sort(sortedCaves);
                    }
                }
                return null;
            }
        }).when(mockSharedPreferencesManager).storeStringMapData(anyString(), anyMapOf(String.class, Object.class));

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
                String key = (String) invocation.getArguments()[0];
                if (key.startsWith("cave_")) {
                    int caveId = Integer.parseInt(key.substring(5, key.length()));
                    caveIds.removeIf(id -> id == caveId);
                    sortedCaves.removeIf(cave -> cave.Id == caveId);
                    caveMap.remove(caveId);
                }
                return null;
            }
        }).when(mockSharedPreferencesManager).delete(anyString());

        when(mockSharedPreferencesManager.loadStoredData(anyString(), eq(R.string.store_cave_key), eq(CaveModel.class)))
                .thenAnswer(new Answer<CaveModel>() {
                    @Override
                    public CaveModel answer(InvocationOnMock invocation) throws Throwable {
                        String fileName = (String) invocation.getArguments()[0];
                        if (fileName.startsWith("cave_")) {
                            int caveId = Integer.parseInt(fileName.substring(5, fileName.length()));
                            return caveMap.containsKey(caveId) ? (CaveModel) caveMap.get(caveId) : null;
                        }
                        return null;
                    }
                });
        DependencyManager.registerSingleton(ISharedPreferencesManager.class, mockSharedPreferencesManager, true);
    }

    private static void initBottleMap() {
        caveMap = new HashMap<>();
        caveIds = new ArrayList<>();
        sortedCaves = new ArrayList<>();

        CaveModel cave = new CaveModel();
        cave.Id = 1;
        cave.CaveType = CaveTypeEnum.BULK;
        cave.Name = "Name cave";
        CaveArrangementModel caveArrangement = new CaveArrangementModel();
        caveArrangement.Id = 42;
        caveArrangement.TotalUsed = 17;
        caveArrangement.TotalCapacity = 20;
        caveArrangement.NumberBottlesBulk = 20;
        cave.CaveArrangement = caveArrangement;

        caveMap.put(cave.Id, cave);
        sortedCaves.add(cave);
        caveIds.add(cave.Id);

        Collections.sort(caveIds);
        Collections.sort(sortedCaves);
    }

    @AfterClass
    public static void afterClass() {
        DependencyManager.cleanUp();
    }

    @Before
    public void before() {
        CaveSharedPreferencesManager.Init();
        assertTrue(CaveSharedPreferencesManager.IsInitialized());
    }

    @Test
    public void getCaveWhenNoCave() {
        CaveModel outputCave = CaveSharedPreferencesManager.Instance.getCave(-1);
        assertNull(outputCave);
    }

    @Test
    public void getCaveWhenCaveExists() {
        CaveModel outputCave = CaveSharedPreferencesManager.Instance.getCave(1);
        CaveModel expectedCave = (CaveModel) caveMap.get(1);
        assertNotNull(outputCave);
        assertEquals(expectedCave, outputCave);
    }

    @Test
    public void insertOrUpdateCave() {
        int newCaveId = 3;
        CaveModel newCave = new CaveModel();
        newCave.Id = newCaveId;
        newCave.CaveType = CaveTypeEnum.BULK;
        newCave.Name = "Name cave 3";
        CaveArrangementModel caveArrangement = new CaveArrangementModel();
        caveArrangement.Id = 14;
        caveArrangement.TotalUsed = 12;
        caveArrangement.TotalCapacity = 27;
        caveArrangement.NumberBottlesBulk = 27;
        newCave.CaveArrangement = caveArrangement;

        assertFalse(caveIds.contains(newCaveId));
        assertFalse(caveMap.containsKey(newCaveId));
        assertTrue(sortedCaves.parallelStream().noneMatch(cave -> cave.Id == newCaveId));

        CaveSharedPreferencesManager.Instance.insertOrUpdateCave(newCave);

        assertTrue(caveIds.contains(newCaveId));
        assertTrue(caveMap.containsKey(newCaveId));
        assertEquals(newCave, caveMap.get(newCaveId));

        sortedCaves.forEach(cave -> {
            if (cave.Id == newCaveId) {
                assertEquals(newCave, cave);
            }
        });

        // clean
        sortedCaves.removeIf(cave -> cave.Id == newCaveId);
        caveMap.remove(newCaveId);
        caveIds.removeIf(id -> id == newCaveId);
    }

    @Test
    public void deleteCave() {
        int idToRemove = 1;
        CaveModel oldCave = (CaveModel) caveMap.get(idToRemove);

        CaveSharedPreferencesManager.Instance.deleteCave(oldCave);

        assertFalse(caveIds.contains(idToRemove));
        assertTrue(sortedCaves.parallelStream().noneMatch(cave -> cave.Id == idToRemove));
        assertFalse(caveMap.containsKey(idToRemove));

        // clean
        caveIds.add(idToRemove);
        Collections.sort(caveIds);
        caveMap.put(idToRemove, oldCave);
        sortedCaves.add(oldCave);
        Collections.sort(sortedCaves);
    }
}