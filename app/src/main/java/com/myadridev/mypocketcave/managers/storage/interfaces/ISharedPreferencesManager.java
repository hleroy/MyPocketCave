package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.content.Context;

import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.Map;

public interface ISharedPreferencesManager {

    void delete(Context context, String filename);

    void storeStringData(Context context, String storeFilename, int keyResourceId, String dataToStore);

    void storeStringData(Context context, String storeFilename, String key, Object dataToStore);

    void storeStringMapData(Context context, String storeFilename, Map<String, Object> dataToStoreMap);

    void removeData(Context context, String storeFilename, String key);

    Map<Integer, IStorableModel> loadStoredDataMap(Context context, String storeFilename, String keyIndex, int keyDetailResourceId, Class<? extends IStorableModel> dataType);

    IStorableModel loadStoredData(Context context, String storeFilename, int keyResourceId, Class<? extends IStorableModel> dataType);

    IStorableModel loadStoredData(Context context, String storeFilename, String key, Class<? extends IStorableModel> dataType);

    String loadStoredData(Context context, String storeFilename, int keyResourceId);
}
