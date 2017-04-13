package com.myadridev.mypocketcave.managers.storage.sharedPreferences.v1;

import android.content.Context;
import android.content.SharedPreferences;

import com.myadridev.mypocketcave.managers.JsonManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v1.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPreferencesManager implements ISharedPreferencesManager {

    public static SharedPreferencesManager Instance;
    private static boolean isInitialized;
    private final int openMode = Context.MODE_PRIVATE;
    private String sharedPreferencesFolder;

    private SharedPreferencesManager(Context context) {
        sharedPreferencesFolder = context.getFilesDir().getParent() + File.separator + "shared_prefs" + File.separator;
    }

    public static void Init(Context context) {
        if (isInitialized) return;
        Instance = new SharedPreferencesManager(context);
        isInitialized = true;
    }

    public void delete(Context context, String filename) {
        SharedPreferences settings = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        File settingsFile = new File(sharedPreferencesFolder + filename);
        if (settingsFile.exists()) {
            settingsFile.delete();
        }
    }

    public void storeIntData(Context context, int storeFileResourceId, int keyResourceId, int dataToStore) {
        String key = context.getString(keyResourceId);
        String storeFilename = context.getString(storeFileResourceId);
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        SharedPreferences.Editor editor = storedData.edit();
        editor.putInt(key, dataToStore);
        editor.apply();
    }

    public void storeStringData(Context context, String storeFilename, int keyResourceId, String dataToStore) {
        String key = context.getString(keyResourceId);
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        SharedPreferences.Editor editor = storedData.edit();
        editor.putString(key, dataToStore);
        editor.apply();
    }

    public void storeStringData(Context context, String storeFilename, String key, Object dataToStore) {
        Map<String, Object> dataToStoreMap = new HashMap<>(1);
        dataToStoreMap.put(key, dataToStore);
        storeStringMapData(context, storeFilename, dataToStoreMap);
    }

    public void storeStringMapData(Context context, String storeFilename, Map<String, Object> dataToStoreMap) {
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        SharedPreferences.Editor editor = storedData.edit();

        for (Map.Entry<String, Object> dataToStore : dataToStoreMap.entrySet()) {
            String dataJson = JsonManager.writeValueAsString(dataToStore.getValue());
            if (dataJson != null) {
                editor.putString(dataToStore.getKey(), dataJson);
            }
        }
        editor.apply();
    }

    public void removeData(Context context, String storeFilename, String key) {
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        SharedPreferences.Editor editor = storedData.edit();
        editor.remove(key);
        editor.apply();
    }

    public Map<Integer, IStorableModel> loadStoredDataMap(Context context, String storeFilename, String keyIndex, int keyDetailResourceId, Class<? extends IStorableModel> dataType) {
        Map<Integer, IStorableModel> dataMap = new HashMap<>();
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);
        String indexListJson = storedData.getString(keyIndex, "");

        if (indexListJson.isEmpty()) return dataMap;

        List<Integer> indexList = new ArrayList<>();
        indexList = JsonManager.readValue(indexListJson, indexList.getClass());

        for (int id : indexList) {
            String keyDetail = context.getString(keyDetailResourceId, id);
            String dataJson = storedData.getString(keyDetail, null);
            if (dataJson == null) {
                continue;
            }
            IStorableModel data = JsonManager.readValue(dataJson, dataType);
            if (data == null || !data.isValid()) {
                continue;
            }
            data.trimAll();
            dataMap.put(data.getId(), data);
        }
        return dataMap;
    }

    public IStorableModel loadStoredStringData(Context context, String storeFilename, int keyResourceId, Class<? extends IStorableModel> dataType) {
        String key = context.getString(keyResourceId);
        return loadStoredStringData(context, storeFilename, key, dataType);
    }

    public IStorableModel loadStoredStringData(Context context, String storeFilename, String key, Class<? extends IStorableModel> dataType) {
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);

        String dataJson = storedData.getString(key, null);
        if (dataJson == null) {
            return null;
        }
        IStorableModel data = JsonManager.readValue(dataJson, dataType);
        if (data == null || !data.isValid())
            return null;

        data.trimAll();
        return data;
    }

    public String loadStoredStringData(Context context, String storeFilename, int keyResourceId) {
        String key = context.getString(keyResourceId);
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);

        return storedData.getString(key, null);
    }

    public int loadStoredIntData(Context context, int storeFileResourceId, int keyResourceId, int defaultValue) {
        String key = context.getString(keyResourceId);
        String storeFilename = context.getString(storeFileResourceId);
        SharedPreferences storedData = context.getSharedPreferences(storeFilename, openMode);

        return storedData.getInt(key, defaultValue);
    }
}
