package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.support.annotation.StringRes;

import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.Map;

public interface ISharedPreferencesManager {

    int loadStoredDataMap(int storeFileResourceId, int storeSetResourceId, Class<? extends IStorableModel> dataType, Map<Integer, IStorableModel> dataMap);

    void storeData(int storeFileResourceId, int storeSetResourceId, Map<Integer, ? extends IStorableModel> dataMap);

    void delete(String filename);

    void storeStringData(String storeFilename, String key, Object dataToStore);

    void storeStringMapData(String storeFilename, Map<String, Object> dataToStoreMap);

    void removeData(String storeFilename, String key);

    Map<Integer, IStorableModel> loadStoredDataMap(String storeFilename, String keyIndex, int keyDetailResourceId, Class<? extends IStorableModel> dataType);

    IStorableModel loadStoredData(String storeFilename, int keyResourceId, Class<? extends IStorableModel> dataType);

    String getStringFromResource(@StringRes int resourceId, Object... formatArgs);
}
