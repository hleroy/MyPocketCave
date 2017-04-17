package com.myadridev.mypocketcave.managers.storage.interfaces.v1;

import android.content.Context;

import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.util.Map;

@Deprecated
public interface ISharedPreferencesManagerV1 {

    void delete(Context context, String filename);

    void storeIntData(Context context, int storeFileResourceId, int keyResourceId, int dataToStore);

    void storeStringData(Context context, String storeFilename, int keyResourceId, String dataToStore);

    void storeStringData(Context context, String storeFilename, String key, Object dataToStore);

    void storeStringMapData(Context context, String storeFilename, Map<String, Object> dataToStoreMap);

    void removeData(Context context, String storeFilename, String key);

    Map<Integer, IStorableModel> loadStoredDataMap(Context context, String storeFilename, String keyIndex, int keyDetailResourceId, Class<? extends IStorableModel> dataType);

    IStorableModel loadStoredStringData(Context context, String storeFilename, int keyResourceId, Class<? extends IStorableModel> dataType);

    IStorableModel loadStoredStringData(Context context, String storeFilename, String key, Class<? extends IStorableModel> dataType);

    String loadStoredStringData(Context context, String storeFilename, int keyResourceId);

    int loadStoredIntData(Context context, int storeFileResourceId, int keyResourceId, int defaultValue);
}
