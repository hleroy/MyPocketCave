package com.myadridev.mypocketcave.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesStorageManager {

    public static SharedPreferencesStorageManager Instance;
    private static boolean _isInitialized;
    private final Context context;
    private final int openMode = Context.MODE_PRIVATE;
    private ObjectMapper jsonMapper = new ObjectMapper();

    private SharedPreferencesStorageManager(Context _context) {
        context = _context;
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init(Context context) {
        Instance = new SharedPreferencesStorageManager(context);
        _isInitialized = true;
    }

    public int loadStoredData(int storeFileResourceId, int storeSetResourceId, Class<? extends IStorableModel> dataType, Map<Integer, IStorableModel> dataMap) {
        String storeFile = context.getString(storeFileResourceId);
        String storeSet = context.getString(storeSetResourceId);

        SharedPreferences storedData = context.getSharedPreferences(storeFile, openMode);
        Set<String> allDataJsonSet = storedData.getStringSet(storeSet, new HashSet<String>());
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

    public void delete(int storeFileResourceId) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(storeFileResourceId), Context.MODE_PRIVATE);
        settings.edit().clear().apply();
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
}
