package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesManager implements ISharedPreferencesManager {

    public static SharedPreferencesManager Instance;
    private static boolean _isInitialized;
    private final Context context;
    private final int openMode = Context.MODE_PRIVATE;
    private ObjectMapper jsonMapper = new ObjectMapper();
    private String sharedPreferencesFolder;

    private SharedPreferencesManager(Context _context) {
        context = _context;
        sharedPreferencesFolder = context.getFilesDir().getParent() + File.separator + "shared_prefs" + File.separator;
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init(Context context) {
        Instance = new SharedPreferencesManager(context);
        _isInitialized = true;
    }

    public String getStringFromResource(@StringRes int resourceId, Object... formatArgs) {
        return context.getString(resourceId, formatArgs);
    }

    public int loadStoredDataMap(int storeFileResourceId, int storeSetResourceId, Class<? extends IStorableModel> dataType, Map<Integer, IStorableModel> dataMap) {
        String storeFile = context.getString(storeFileResourceId);
        String storeSet = context.getString(storeSetResourceId);

        SharedPreferences storedData = context.getSharedPreferences(storeFile, openMode);
        Set<String> allDataJsonSet = storedData.getStringSet(storeSet, new HashSet<>());
        int maxDataId = 0;
        for (String dataJson : allDataJsonSet) {
            IStorableModel data;
            try {
                data = jsonMapper.readValue(dataJson, dataType);
                if (!data.isValid())
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            if (data.getId() > maxDataId)
                maxDataId = data.getId();
            dataMap.put(data.getId(), data);
        }
        return maxDataId;
    }

    public void storeData(int storeFileResourceId, int storeSetResourceId, Map<Integer, ? extends IStorableModel> dataMap) {
        String storeFile = context.getString(storeFileResourceId);
        String storeSet = context.getString(storeSetResourceId);
        Map<Integer, String> dataJsonMap = serializeData(dataMap);

        SharedPreferences storedData = context.getSharedPreferences(storeFile, openMode);
        SharedPreferences.Editor editor = storedData.edit();
        Set<String> dataJsonSet = new HashSet<>();
        for (String dataJson : dataJsonMap.values()) {
            dataJsonSet.add(dataJson);
        }
        editor.putStringSet(storeSet, dataJsonSet);
        editor.apply();
    }

    public void delete(String filename) {
        SharedPreferences settings = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        // here we want the file to be deleted immediately
        settings.edit().clear().commit();
        File settingsFile = new File(sharedPreferencesFolder + filename);
        if (settingsFile.exists()) {
            settingsFile.delete();
        }
    }

    @NonNull
    private Map<Integer, String> serializeData(Map<Integer, ? extends IStorableModel> dataMap) {
        Map<Integer, String> serializedData = new HashMap<>();

        for (Map.Entry<Integer, ? extends IStorableModel> dataEntry : dataMap.entrySet()) {
            try {
                String dataJson = jsonMapper.writeValueAsString(dataEntry.getValue());
                serializedData.put(dataEntry.getKey(), dataJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return serializedData;
    }

    public void storeStringData(String storeFilename, String key, Object dataToStore) {
        Map<String, Object> dataToStoreMap = new HashMap<>(1);
        dataToStoreMap.put(key, dataToStore);
        storeStringMapData(storeFilename, dataToStoreMap);
    }

    public void storeStringMapData(String storeFilename, Map<String, Object> dataToStoreMap) {
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        SharedPreferences.Editor editor = storedData.edit();

        for (Map.Entry<String, Object> dataToStore : dataToStoreMap.entrySet()) {
            try {
                String dataJson = jsonMapper.writeValueAsString(dataToStore.getValue());
                editor.putString(dataToStore.getKey(), dataJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        editor.apply();
    }

    public void removeData(String storeFilename, String key) {
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        SharedPreferences.Editor editor = storedData.edit();
        editor.remove(key);
        editor.apply();
    }

    public Map<Integer, IStorableModel> loadStoredDataMap(String storeFilename, String keyIndex, int keyDetailResourceId, Class<? extends IStorableModel> dataType) {
        Map<Integer, IStorableModel> dataMap = new HashMap<>();
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        String indexListJson = storedData.getString(keyIndex, "");

        if (indexListJson.isEmpty()) return dataMap;

        List<Integer> indexList = new ArrayList<>();
        try {
            indexList = jsonMapper.readValue(indexListJson, indexList.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int id : indexList) {
            String keyDetail = context.getString(keyDetailResourceId, id);
            String dataJson = storedData.getString(keyDetail, null);
            if (dataJson == null) {
                continue;
            }
            IStorableModel data;
            try {
                data = jsonMapper.readValue(dataJson, dataType);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (data.isValid()) {
                dataMap.put(data.getId(), data);
            }
        }
        return dataMap;
    }

    public IStorableModel loadStoredData(String storeFilename, int keyResourceId, Class<? extends IStorableModel> dataType) {
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);

        String keyDetail = context.getString(keyResourceId);
        String dataJson = storedData.getString(keyDetail, null);
        if (dataJson == null) {
            return null;
        }
        IStorableModel data;
        try {
            data = jsonMapper.readValue(dataJson, dataType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data.isValid() ? data : null;
    }
}
