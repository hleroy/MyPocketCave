package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v2;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IBottleStorageManagerV2;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BottlesSharedPreferencesManagerV2 implements IBottleStorageManagerV2 {

    public static BottlesSharedPreferencesManagerV2 Instance;
    private static boolean isInitialized;
    private final Map<Integer, BottleModelV2> allBottlesMap = new HashMap<>();
    private String keyIndex;
    private String filename;
    private int keyBottleResourceId = R.string.store_bottle;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private BottlesSharedPreferencesManagerV2(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_bottles);

        loadAllBottles(context);
    }

    private BottlesSharedPreferencesManagerV2(Context context, Map<Integer, BottleModelV2> allBottlesMap) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_bottles);

        for (BottleModelV2 bottle : allBottlesMap.values()) {
            insertBottle(context, bottle, false);
        }
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new BottlesSharedPreferencesManagerV2(context);
        isInitialized = true;
    }

    public static void init(Context context, Map<Integer, BottleModelV2> allBottlesMap) {
        if (isInitialized) return;
        Instance = new BottlesSharedPreferencesManagerV2(context, allBottlesMap);
        isInitialized = true;
    }

    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllBottles(Context context) {
        Map<Integer, IStorableModel> allBottlesAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyBottleResourceId, BottleModelV2.class);

        if (allBottlesAsStorableModel == null) {
            return;
        }
        for (Map.Entry<Integer, IStorableModel> bottleAsStorableModelEntry : allBottlesAsStorableModel.entrySet()) {
            IStorableModel bottleAsStorableModel = bottleAsStorableModelEntry.getValue();
            if (bottleAsStorableModel instanceof BottleModelV2) {
                allBottlesMap.put(bottleAsStorableModelEntry.getKey(), (BottleModelV2) bottleAsStorableModel);
            }
        }
    }

    public List<BottleModelV2> getBottles() {
        List<BottleModelV2> bottles = new ArrayList<>(allBottlesMap.values());
        Collections.sort(bottles);
        return bottles;
    }

    public BottleModelV2 getBottle(int bottleId) {
        return CollectionsHelper.getValueOrDefault(allBottlesMap, bottleId, null);
    }

    public int insertBottle(Context context, BottleModelV2 bottle, boolean needsNewId) {
        List<Integer> ids = new ArrayList<>(allBottlesMap.keySet());
        if (needsNewId) {
            bottle.Id = StorageHelper.getNewId(ids);
        }
        allBottlesMap.put(bottle.Id, bottle);
        ids.add(bottle.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(context.getString(keyBottleResourceId, bottle.Id), bottle);

        getSharedPreferencesManager().storeStringMapData(context, filename, dataToStoreMap);
        return bottle.Id;
    }

    public void updateIndexes(Context context) {
        List<Integer> ids = new ArrayList<>(allBottlesMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
    }

    public void updateBottle(Context context, BottleModelV2 bottle) {
        allBottlesMap.put(bottle.Id, bottle);
        getSharedPreferencesManager().storeStringData(context, filename, context.getString(keyBottleResourceId, bottle.Id), bottle);
    }

    public void deleteBottle(Context context, int bottleId) {
        allBottlesMap.remove(bottleId);
        List<Integer> ids = new ArrayList<>(allBottlesMap.keySet());
        getSharedPreferencesManager().storeStringData(context, filename, keyIndex, ids);
        getSharedPreferencesManager().removeData(context, filename, context.getString(keyBottleResourceId, bottleId));
    }

    public int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime) {
        for (BottleModelV2 bottle : allBottlesMap.values()) {
            if (name.equals(bottle.Name)
                    && domain.equals(bottle.Domain)
                    && wineColorId == bottle.WineColor.Id
                    && millesime == bottle.Millesime
                    && id != bottle.Id) {
                return bottle.Id;
            }
        }
        return 0;
    }

    public int getBottlesPlacedCount() {
        return getBottlesPlacedCount(allBottlesMap.values(), WineColorEnumV2.a.Id);
    }

    public int getBottlesPlacedCount(Collection<BottleModelV2> bottles, int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnumV2.a.Id;

        for (BottleModelV2 bottle : bottles) {
            if (isAnyBottles || bottle.WineColor.Id == wineColorId) {
                bottlesCount += bottle.NumberPlaced;
            }
        }
        return bottlesCount;
    }

    public int getBottlesCount() {
        return getBottlesCount(allBottlesMap.values(), WineColorEnumV2.a.Id);
    }

    public int getBottlesCount(Collection<BottleModelV2> bottles) {
        return getBottlesCount(bottles, WineColorEnumV2.a.Id);
    }

    public int getBottlesCount(int wineColorId) {
        return getBottlesCount(allBottlesMap.values(), wineColorId);
    }

    public int getBottlesCount(Collection<BottleModelV2> bottles, int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnumV2.a.Id;

        for (BottleModelV2 bottle : bottles) {
            if (isAnyBottles || bottle.WineColor.Id == wineColorId) {
                bottlesCount += bottle.Stock;
            }
        }
        return bottlesCount;
    }

    public List<String> getDistinctPersons() {
        HashSet<String> distinctPersonsSet = new HashSet<>(allBottlesMap.size());

        for (BottleModelV2 bottle : allBottlesMap.values()) {
            if (!bottle.PersonToShareWith.isEmpty()) {
                distinctPersonsSet.add(bottle.PersonToShareWith);
            }
        }

        List<String> distinctPersonsList = new ArrayList<>(distinctPersonsSet);
        Collections.sort(distinctPersonsList);
        return distinctPersonsList;
    }

    public List<String> getDistinctDomains() {
        HashSet<String> distinctDomainsSet = new HashSet<>(allBottlesMap.size());

        for (BottleModelV2 bottle : allBottlesMap.values()) {
            if (!bottle.Domain.isEmpty()) {
                distinctDomainsSet.add(bottle.Domain);
            }
        }

        List<String> distinctDomainsList = new ArrayList<>(distinctDomainsSet);
        Collections.sort(distinctDomainsList);
        return distinctDomainsList;
    }

    public List<BottleModelV2> getNonPlacedBottles() {
        List<BottleModelV2> nonPlacedBottles = new ArrayList<>(allBottlesMap.size());

        for (BottleModelV2 bottle : allBottlesMap.values()) {
            if (bottle.NumberPlaced < bottle.Stock) {
                nonPlacedBottles.add(bottle);
            }
        }

        Collections.sort(nonPlacedBottles);
        return nonPlacedBottles;
    }

    public void drinkBottle(Context context, int bottleId, int quantity) {
        BottleModelV2 bottle = getBottle(bottleId);
        bottle.Stock = Math.max(0, bottle.Stock - quantity);
        updateBottle(context, bottle);
    }

    public void updateNumberPlaced(Context context, int bottleId, int increment) {
        BottleModelV2 bottle = getBottle(bottleId);
        bottle.NumberPlaced = Math.max(0, bottle.NumberPlaced + increment);
        updateBottle(context, bottle);
    }
}
