package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaveManager {

    public static CaveManager Instance;
    private static int _maxCaveId;
    private static boolean _isInitialized;
    private final Map<Integer, CaveModel> caves = new HashMap<>();
    private final int storeFileResourceId = R.string.filename_caves;
    private final int storeSetResourceId = R.string.store_caves;

    public static int MaxCaveId() {
        return _maxCaveId;
    }

    private CaveManager(boolean withStorage) {
        _maxCaveId = 0;
        if (withStorage)
            initializeCaves();
    }

    private CaveManager() {
        _maxCaveId = 0;
        initializeCaves();
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void InitWithoutStorage() {
        Instance = new CaveManager(false);
        _isInitialized = true;
    }

    public static void Init() {
        Instance = new CaveManager();
        _isInitialized = true;
    }

    private void initializeCaves() {
        final Map<Integer, IStorableModel> cavesAsIStorableModel = new HashMap<>();
        _maxCaveId = StorageManager.Instance.loadStoredData(storeFileResourceId, storeSetResourceId, CaveModel.class, cavesAsIStorableModel);

        caves.clear();
        for (Map.Entry<Integer, IStorableModel> storableModelEntry : cavesAsIStorableModel.entrySet()) {
            IStorableModel storableModel = storableModelEntry.getValue();
            if (storableModel instanceof CaveModel) {
                caves.put(storableModelEntry.getKey(), (CaveModel) storableModel);
            }
        }
    }

    public List<CaveModel> getCaves() {
        return new ArrayList<>(caves.values());
    }

    public CaveModel getCave(int id) {
        return caves.containsKey(id) ? caves.get(id) : null;
    }

    public int addCaveNoSave(CaveModel cave) {
        _maxCaveId++;
        cave.Id = _maxCaveId;
        caves.put(_maxCaveId, cave);
        return _maxCaveId;
    }

    public int addCave(CaveModel cave) {
        _maxCaveId++;
        cave.Id = _maxCaveId;
        caves.put(_maxCaveId, cave);
        saveCaves();
        return _maxCaveId;
    }

    public void editCaveNoSave(CaveModel cave) {
        caves.put(cave.Id, cave);
    }

    public void editCave(CaveModel cave) {
        caves.put(cave.Id, cave);
        saveCaves();
    }

    public void removeCaveNoSave(int id) {
        if (caves.containsKey(id)) {
            caves.remove(id);
            setMaxCaveId();
        }
    }

    public void removeCave(int id) {
        if (caves.containsKey(id)) {
            caves.remove(id);
            setMaxCaveId();
            saveCaves();
        }
    }

    public int getExistingCaveId(int id, String name, CaveTypeEnum caveType) {
        for (CaveModel existingCave : caves.values()) {
            if (id != existingCave.Id
                    && name.equalsIgnoreCase(existingCave.Name)
                    && caveType == existingCave.CaveType) {
                return existingCave.Id;
            }
        }

        return -1;
    }

    private void setMaxCaveId() {
        int maxId = 0;
        for (int id : caves.keySet()) {
            if (id > maxId)
                maxId = id;
        }
        _maxCaveId = maxId;
    }

    public void saveCaves() {
        StorageManager.Instance.storeData(storeFileResourceId, storeSetResourceId, caves);
    }

    public int getCavesCount() {
        return getCavesCount(caves.values());
    }

    public int getCavesCount(Collection<CaveModel> caves) {
        return caves.size();
    }

    public int getCavesCount(CaveTypeEnum caveType) {
        return getCavesCount(caves.values(), caveType);
    }

    public int getCavesCount(Collection<CaveModel> caves, CaveTypeEnum caveType) {
        int count = 0;
        for (CaveModel cave : caves) {
            if (cave.CaveType == caveType)
                count++;
        }
        return count;
    }
}
