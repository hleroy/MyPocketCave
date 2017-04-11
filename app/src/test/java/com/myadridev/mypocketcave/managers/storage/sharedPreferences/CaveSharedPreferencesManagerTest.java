package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ICavesStorageManagerV2;
import com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2.CaveSharedPreferencesManagerV2;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v2.CaveArrangementModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
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
import static org.mockito.Matchers.any;
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
    private static List<CaveModelV2> sortedCaves;
    private static List<Integer> caveIds;

    @Mock
    Context context;

    @BeforeClass
    public static void beforeClass() {
        initBottleMap();
        DependencyManager.init();

        ICavesStorageManagerV2 mockCavesStorageManager = mock(ICavesStorageManagerV2.class);
        when(mockCavesStorageManager.getLightCaves())
                .thenAnswer(new Answer<List<CaveLightModelV2>>() {
                    @Override
                    public List<CaveLightModelV2> answer(InvocationOnMock invocation) throws Throwable {
                        ArrayList<CaveLightModelV2> caveLightModels = new ArrayList<>();
                        for (CaveModelV2 cave : sortedCaves) {
                            caveLightModels.add(new CaveLightModelV2(cave));
                        }
                        return caveLightModels;
                    }
                });
        DependencyManager.registerSingleton(ICavesStorageManagerV2.class, mockCavesStorageManager, true);

        ISharedPreferencesManager mockSharedPreferencesManager = mock(ISharedPreferencesManager.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String fileName = (String) invocation.getArguments()[1];
                Map<String, Object> dataMap = (Map<String, Object>) invocation.getArguments()[2];
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String key = entry.getKey();
                    if (key.equalsIgnoreCase("cave") && fileName.startsWith("cave_")) {
                        CaveModelV2 cave = (CaveModelV2) entry.getValue();
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
        }).when(mockSharedPreferencesManager).storeStringMapData(any(Context.class), anyString(), anyMapOf(String.class, Object.class));

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                Context context = (Context) arguments[0];
                String storeFilename = (String) arguments[1];
                String key = (String) arguments[2];
                Object dataToStore = arguments[3];
                Map<String, Object> dataToStoreMap = new HashMap<>(1);
                dataToStoreMap.put(key, dataToStore);
                mockSharedPreferencesManager.storeStringMapData(context, storeFilename, dataToStoreMap);
                return null;
            }
        }).when(mockSharedPreferencesManager).storeStringData(any(Context.class), anyString(), anyString(), Matchers.anyObject());

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[1];
                if (key.startsWith("cave_")) {
                    int caveId = Integer.parseInt(key.substring(5, key.length()));
                    caveIds.removeIf(id -> id == caveId);
                    sortedCaves.removeIf(cave -> cave.Id == caveId);
                    caveMap.remove(caveId);
                }
                return null;
            }
        }).when(mockSharedPreferencesManager).delete(any(Context.class), anyString());

        when(mockSharedPreferencesManager.loadStoredStringData(any(Context.class), anyString(), eq(R.string.store_cave_key), eq(CaveModelV2.class)))
                .thenAnswer(new Answer<CaveModelV2>() {
                    @Override
                    public CaveModelV2 answer(InvocationOnMock invocation) throws Throwable {
                        String fileName = (String) invocation.getArguments()[1];
                        if (fileName.startsWith("cave_")) {
                            int caveId = Integer.parseInt(fileName.substring(5, fileName.length()));
                            return caveMap.containsKey(caveId) ? (CaveModelV2) caveMap.get(caveId) : null;
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

        CaveModelV2 cave = new CaveModelV2();
        cave.Id = 1;
        cave.CaveType = CaveTypeEnumV2.bu;
        cave.Name = "Name cave";
        CaveArrangementModelV2 caveArrangement = new CaveArrangementModelV2();
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
        when(context.getString(eq(R.string.store_cave_key))).thenReturn("cave");
        when(context.getString(eq(R.string.store_cave), anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return "cave_" + (int) invocation.getArguments()[1];
            }
        });
        CaveSharedPreferencesManagerV2.init(context);
    }

    @Test
    public void getCaveWhenNoCave() {
        CaveModelV2 outputCave = CaveSharedPreferencesManagerV2.Instance.getCave(context, -1);
        assertNull(outputCave);
    }

    @Test
    public void getCaveWhenCaveExists() {
        CaveModelV2 outputCave = CaveSharedPreferencesManagerV2.Instance.getCave(context, 1);
        CaveModelV2 expectedCave = (CaveModelV2) caveMap.get(1);
        assertNotNull(outputCave);
        assertEquals(expectedCave, outputCave);
    }

    @Test
    public void insertOrUpdateCave() {
        int newCaveId = 3;
        CaveModelV2 newCave = new CaveModelV2();
        newCave.Id = newCaveId;
        newCave.CaveType = CaveTypeEnumV2.bu;
        newCave.Name = "Name cave 3";
        CaveArrangementModelV2 caveArrangement = new CaveArrangementModelV2();
        caveArrangement.Id = 14;
        caveArrangement.TotalUsed = 12;
        caveArrangement.TotalCapacity = 27;
        caveArrangement.NumberBottlesBulk = 27;
        newCave.CaveArrangement = caveArrangement;

        assertFalse(caveIds.contains(newCaveId));
        assertFalse(caveMap.containsKey(newCaveId));
        assertTrue(sortedCaves.parallelStream().noneMatch(cave -> cave.Id == newCaveId));

        CaveSharedPreferencesManagerV2.Instance.insertOrUpdateCave(context, newCave);

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
        CaveModelV2 oldCave = (CaveModelV2) caveMap.get(idToRemove);

        CaveSharedPreferencesManagerV2.Instance.deleteCave(context, oldCave);

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