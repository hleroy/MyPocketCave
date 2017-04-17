package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v1.WineColorEnumV1;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.IBottleStorageManagerV1;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ISharedPreferencesManagerV1;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v1.BottleModelV1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Deprecated
public class BottlesSharedPreferencesManagerV1 implements IBottleStorageManagerV1 {

    public static BottlesSharedPreferencesManagerV1 Instance;
    private static boolean isInitialized;
    private static Map<Integer, BottleModelV1> allBottlesMap = new HashMap<>();
    private String keyIndex;
    private String filename;
    private int keyBottleResourceId = R.string.store_bottle;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManagerV1 sharedPreferencesManager = null;

    private BottlesSharedPreferencesManagerV1(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_bottles);

        loadAllBottles(context);
    }

    public static Map<Integer, BottleModelV1> getAllBottles(Context context) {
        init(context);
        return allBottlesMap;
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new BottlesSharedPreferencesManagerV1(context);
        isInitialized = true;
    }

    private ISharedPreferencesManagerV1 getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManagerV1.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    private void loadAllBottles(Context context) {
        Map<Integer, IStorableModel> allBottlesAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyBottleResourceId, BottleModelV1.class);

        if (allBottlesAsStorableModel == null) {
            return;
        }
        for (Map.Entry<Integer, IStorableModel> bottleAsStorableModelEntry : allBottlesAsStorableModel.entrySet()) {
            IStorableModel bottleAsStorableModel = bottleAsStorableModelEntry.getValue();
            if (bottleAsStorableModel instanceof BottleModelV1) {
                allBottlesMap.put(bottleAsStorableModelEntry.getKey(), (BottleModelV1) bottleAsStorableModel);
            }
        }
    }

    public List<BottleModelV1> getBottles() {
        List<BottleModelV1> bottles = new ArrayList<>(allBottlesMap.values());
        Collections.sort(bottles);
        return bottles;
    }

    public BottleModelV1 getBottle(int bottleId) {
        return CollectionsHelper.getValueOrDefault(allBottlesMap, bottleId, null);
    }

    public int insertBottle(Context context, BottleModelV1 bottle) {
        List<Integer> ids = new ArrayList<>(allBottlesMap.keySet());
        bottle.Id = StorageHelper.getNewId(ids);
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

    public void updateBottle(Context context, BottleModelV1 bottle) {
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
        for (BottleModelV1 bottle : allBottlesMap.values()) {
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
        return getBottlesPlacedCount(allBottlesMap.values(), WineColorEnumV1.ANY.Id);
    }

    public int getBottlesPlacedCount(Collection<BottleModelV1> bottles, int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnumV1.ANY.Id;

        for (BottleModelV1 bottle : bottles) {
            if (isAnyBottles || bottle.WineColor.Id == wineColorId) {
                bottlesCount += bottle.NumberPlaced;
            }
        }
        return bottlesCount;
    }

    public int getBottlesCount() {
        return getBottlesCount(allBottlesMap.values(), WineColorEnumV1.ANY.Id);
    }

    public int getBottlesCount(Collection<BottleModelV1> bottles) {
        return getBottlesCount(bottles, WineColorEnumV1.ANY.Id);
    }

    public int getBottlesCount(int wineColorId) {
        return getBottlesCount(allBottlesMap.values(), wineColorId);
    }

    public int getBottlesCount(Collection<BottleModelV1> bottles, int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnumV1.ANY.Id;

        for (BottleModelV1 bottle : bottles) {
            if (isAnyBottles || bottle.WineColor.Id == wineColorId) {
                bottlesCount += bottle.Stock;
            }
        }
        return bottlesCount;
    }

    public List<String> getDistinctPersons() {
        HashSet<String> distinctPersonsSet = new HashSet<>(allBottlesMap.size());

        for (BottleModelV1 bottle : allBottlesMap.values()) {
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

        for (BottleModelV1 bottle : allBottlesMap.values()) {
            if (!bottle.Domain.isEmpty()) {
                distinctDomainsSet.add(bottle.Domain);
            }
        }

        List<String> distinctDomainsList = new ArrayList<>(distinctDomainsSet);
        Collections.sort(distinctDomainsList);
        return distinctDomainsList;
    }

    public List<BottleModelV1> getNonPlacedBottles() {
        List<BottleModelV1> nonPlacedBottles = new ArrayList<>(allBottlesMap.size());

        for (BottleModelV1 bottle : allBottlesMap.values()) {
            if (bottle.NumberPlaced < bottle.Stock) {
                nonPlacedBottles.add(bottle);
            }
        }

        Collections.sort(nonPlacedBottles);
        return nonPlacedBottles;
    }

    public void drinkBottle(Context context, int bottleId, int quantity) {
        BottleModelV1 bottle = getBottle(bottleId);
        bottle.Stock = Math.max(0, bottle.Stock - quantity);
        updateBottle(context, bottle);
    }

    public void updateNumberPlaced(Context context, int bottleId, int increment) {
        BottleModelV1 bottle = getBottle(bottleId);
        bottle.NumberPlaced = Math.max(0, bottle.NumberPlaced + increment);
        updateBottle(context, bottle);
    }
}
