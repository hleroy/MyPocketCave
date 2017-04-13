package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.v1.WineColorEnum;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.IBottleStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;
import com.myadridev.mypocketcave.models.v1.BottleModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BottlesSharedPreferencesManager implements IBottleStorageManager {

    public static BottlesSharedPreferencesManager Instance;
    private static boolean isInitialized;
    private static Map<Integer, BottleModel> allBottlesMap = new HashMap<>();
    private String keyIndex;
    private String filename;
    private int keyBottleResourceId = R.string.store_bottle;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private BottlesSharedPreferencesManager(Context context) {
        keyIndex = context.getString(R.string.store_indexes);
        filename = context.getString(R.string.filename_bottles);

        loadAllBottles(context);
    }

    public static Map<Integer, BottleModel> getAllBottles(Context context) {
        init(context);
        return allBottlesMap;
    }

    public static void init(Context context) {
        if (isInitialized) return;
        Instance = new BottlesSharedPreferencesManager(context);
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
        Map<Integer, IStorableModel> allBottlesAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(context, filename, keyIndex, keyBottleResourceId, BottleModel.class);

        if (allBottlesAsStorableModel == null) {
            return;
        }
        for (Map.Entry<Integer, IStorableModel> bottleAsStorableModelEntry : allBottlesAsStorableModel.entrySet()) {
            IStorableModel bottleAsStorableModel = bottleAsStorableModelEntry.getValue();
            if (bottleAsStorableModel instanceof BottleModel) {
                allBottlesMap.put(bottleAsStorableModelEntry.getKey(), (BottleModel) bottleAsStorableModel);
            }
        }
    }

    public List<BottleModel> getBottles() {
        List<BottleModel> bottles = new ArrayList<>(allBottlesMap.values());
        Collections.sort(bottles);
        return bottles;
    }

    public BottleModel getBottle(int bottleId) {
        return CollectionsHelper.getValueOrDefault(allBottlesMap, bottleId, null);
    }

    public int insertBottle(Context context, BottleModel bottle) {
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

    public void updateBottle(Context context, BottleModel bottle) {
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
        for (BottleModel bottle : allBottlesMap.values()) {
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
        return getBottlesPlacedCount(allBottlesMap.values(), WineColorEnum.ANY.Id);
    }

    public int getBottlesPlacedCount(Collection<BottleModel> bottles, int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnum.ANY.Id;

        for (BottleModel bottle : bottles) {
            if (isAnyBottles || bottle.WineColor.Id == wineColorId) {
                bottlesCount += bottle.NumberPlaced;
            }
        }
        return bottlesCount;
    }

    public int getBottlesCount() {
        return getBottlesCount(allBottlesMap.values(), WineColorEnum.ANY.Id);
    }

    public int getBottlesCount(Collection<BottleModel> bottles) {
        return getBottlesCount(bottles, WineColorEnum.ANY.Id);
    }

    public int getBottlesCount(int wineColorId) {
        return getBottlesCount(allBottlesMap.values(), wineColorId);
    }

    public int getBottlesCount(Collection<BottleModel> bottles, int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnum.ANY.Id;

        for (BottleModel bottle : bottles) {
            if (isAnyBottles || bottle.WineColor.Id == wineColorId) {
                bottlesCount += bottle.Stock;
            }
        }
        return bottlesCount;
    }

    public List<String> getDistinctPersons() {
        HashSet<String> distinctPersonsSet = new HashSet<>(allBottlesMap.size());

        for (BottleModel bottle : allBottlesMap.values()) {
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

        for (BottleModel bottle : allBottlesMap.values()) {
            if (!bottle.Domain.isEmpty()) {
                distinctDomainsSet.add(bottle.Domain);
            }
        }

        List<String> distinctDomainsList = new ArrayList<>(distinctDomainsSet);
        Collections.sort(distinctDomainsList);
        return distinctDomainsList;
    }

    public List<BottleModel> getNonPlacedBottles() {
        List<BottleModel> nonPlacedBottles = new ArrayList<>(allBottlesMap.size());

        for (BottleModel bottle : allBottlesMap.values()) {
            if (bottle.NumberPlaced < bottle.Stock) {
                nonPlacedBottles.add(bottle);
            }
        }

        Collections.sort(nonPlacedBottles);
        return nonPlacedBottles;
    }

    public void drinkBottle(Context context, int bottleId, int quantity) {
        BottleModel bottle = getBottle(bottleId);
        bottle.Stock = Math.max(0, bottle.Stock - quantity);
        updateBottle(context, bottle);
    }

    public void updateNumberPlaced(Context context, int bottleId, int increment) {
        BottleModel bottle = getBottle(bottleId);
        bottle.NumberPlaced = Math.max(0, bottle.NumberPlaced + increment);
        updateBottle(context, bottle);
    }
}
