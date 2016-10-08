package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.helpers.StorageHelper;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.IBottleStorageManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BottlesSharedPreferencesManager implements IBottleStorageManager {

    public static BottlesSharedPreferencesManager Instance;
    private static boolean _isInitialized;
    private Map<Integer, BottleModel> allBottlesMap;
    private String keyIndex;
    private String filename;
    private int keyBottleResourceId = R.string.store_bottle;

    private ISharedPreferencesManager sharedPreferencesManager = null;

    private BottlesSharedPreferencesManager() {
        keyIndex = getSharedPreferencesManager().getStringFromResource(R.string.store_indexes);
        filename = getSharedPreferencesManager().getStringFromResource(R.string.filename_bottles);

        loadAllBottles();
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init() {
        Instance = new BottlesSharedPreferencesManager();
        _isInitialized = true;
    }

    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class);
        }
        return sharedPreferencesManager;
    }

    private void loadAllBottles() {
        Map<Integer, IStorableModel> allBottlesAsStorableModel = getSharedPreferencesManager().loadStoredDataMap(filename, keyIndex, keyBottleResourceId, BottleModel.class);

        if (allBottlesAsStorableModel == null) {
            allBottlesMap = new HashMap<>();
            return;
        }
        allBottlesMap = new HashMap<>(allBottlesAsStorableModel.size());
        for (Map.Entry<Integer, IStorableModel> bottleAsStorableModelEntry : allBottlesAsStorableModel.entrySet()) {
            IStorableModel bottleAsStorableModel = bottleAsStorableModelEntry.getValue();
            if (bottleAsStorableModel instanceof BottleModel) {
                allBottlesMap.put(bottleAsStorableModelEntry.getKey(), (BottleModel) bottleAsStorableModel);
            }
        }
    }

    @Override
    public List<BottleModel> getBottles() {
        return new ArrayList<>(allBottlesMap.values());
    }

    @Override
    public BottleModel getBottle(int bottleId) {
        return CollectionsHelper.getValueOrDefault(allBottlesMap, bottleId, null);
    }

    @Override
    public int insertBottle(BottleModel bottle) {
        ArrayList<Integer> ids = new ArrayList<>(allBottlesMap.keySet());
        bottle.Id = StorageHelper.getNewId(ids);
        allBottlesMap.put(bottle.Id, bottle);
        ids.add(bottle.Id);

        Map<String, Object> dataToStoreMap = new HashMap<>(2);
        dataToStoreMap.put(keyIndex, ids);
        dataToStoreMap.put(getSharedPreferencesManager().getStringFromResource(keyBottleResourceId, bottle.Id), bottle);

        getSharedPreferencesManager().storeStringMapData(filename, dataToStoreMap);
        return bottle.Id;
    }

    @Override
    public void updateBottle(BottleModel bottle) {
        allBottlesMap.put(bottle.Id, bottle);
        getSharedPreferencesManager().storeStringData(filename, getSharedPreferencesManager().getStringFromResource(keyBottleResourceId, bottle.Id), bottle);
    }

    @Override
    public void deleteBottle(int bottleId) {
        allBottlesMap.remove(bottleId);
        ArrayList<Integer> ids = new ArrayList<>(allBottlesMap.keySet());
        getSharedPreferencesManager().storeStringData(filename, keyIndex, ids);
        getSharedPreferencesManager().removeData(filename, getSharedPreferencesManager().getStringFromResource(keyBottleResourceId, bottleId));
    }

    @Override
    public int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime) {
        for (BottleModel bottle : allBottlesMap.values()) {
            if (name.equals(bottle.Name)
                    && domain.equals(bottle.Domain)
                    && wineColorId == bottle.WineColor.id
                    && millesime == bottle.Millesime
                    && id != bottle.Id) {
                return bottle.Id;
            }
        }
        return 0;
    }

    @Override
    public int getBottlesCount() {
        return getBottlesCount(WineColorEnum.ANY.id);
    }

    @Override
    public int getBottlesCount(int wineColorId) {
        int bottlesCount = 0;
        boolean isAnyBottles = wineColorId == WineColorEnum.ANY.id;

        for (BottleModel bottle : allBottlesMap.values()) {
            if (isAnyBottles || bottle.WineColor.id == wineColorId) {
                bottlesCount += bottle.Stock;
            }
        }

        return bottlesCount;
    }

    @Override
    public String[] getDistinctPersons() {
        HashSet<String> distinctPersonsSet = new HashSet<>(allBottlesMap.size());
        int size = 0;

        for (BottleModel bottle : allBottlesMap.values()) {
            if (!bottle.PersonToShareWith.isEmpty() && distinctPersonsSet.add(bottle.PersonToShareWith)) {
                size++;
            }
        }

        List<String> distinctPersonsList = new ArrayList<>(distinctPersonsSet);
        Collections.sort(distinctPersonsList);
        String[] distinctPersonsArray = new String[size];
        distinctPersonsList.toArray(distinctPersonsArray);
        return distinctPersonsArray;
    }

    @Override
    public String[] getDistinctDomains() {
        HashSet<String> distinctDomainsSet = new HashSet<>(allBottlesMap.size());
        int size = 0;

        for (BottleModel bottle : allBottlesMap.values()) {
            if (!bottle.Domain.isEmpty() && distinctDomainsSet.add(bottle.Domain)) {
                size++;
            }
        }

        List<String> distinctDomainsList = new ArrayList<>(distinctDomainsSet);
        Collections.sort(distinctDomainsList);
        String[] distinctDomainsArray = new String[size];
        distinctDomainsList.toArray(distinctDomainsArray);
        return distinctDomainsArray;
    }

    @Override
    public List<BottleModel> getNonPlacedBottles() {
        List<BottleModel> nonPlacedBottles = new ArrayList<>(allBottlesMap.size());

        for (BottleModel bottle : allBottlesMap.values()) {
            if (bottle.NumberPlaced < bottle.Stock) {
                nonPlacedBottles.add(bottle);
            }
        }

        return nonPlacedBottles;
    }

    @Override
    public void updateNumberPlaced(int bottleId, int increment) {
        BottleModel bottle = getBottle(bottleId);
        bottle.NumberPlaced += increment;
        updateBottle(bottle);
    }
}
