package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.content.Context;
import android.os.Environment;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;

import java.io.File;

public class SyncSharedPreferencesManager implements ISyncStorageManager {

    public static SyncSharedPreferencesManager Instance;
    private static boolean isInitialized;
    private final String filename;
    private final int keyImportLocationResourceId = R.string.store_sync_import;
    private final int keyExportLocationResourceId = R.string.store_sync_export;

    private String importLocation;
    private String exportLocation;
    private String defaultLocation = null;
    private boolean listenerSharedPreferencesRegistered = false;
    private ISharedPreferencesManager sharedPreferencesManager = null;

    private SyncSharedPreferencesManager(Context context, boolean isWriteExternalStorage) {
        filename = context.getString(R.string.filename_sync);

        resetDefaultLocation(isWriteExternalStorage);
        importLocation = getSharedPreferencesManager().loadStoredData(context, filename, keyImportLocationResourceId);
        exportLocation = getSharedPreferencesManager().loadStoredData(context, filename, keyExportLocationResourceId);
    }

    public static void Init(Context context, boolean isWriteExternalStorage) {
        Instance = new SyncSharedPreferencesManager(context, isWriteExternalStorage);
        isInitialized = true;
    }

    public static boolean IsInitialized() {
        return isInitialized;
    }

    private ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    public void resetDefaultLocation(boolean isWriteExternalStorage) {
        if (isWriteExternalStorage) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory != null) {
                defaultLocation = externalStorageDirectory.getAbsolutePath();
            }
        }
        if (defaultLocation == null) {
            defaultLocation = Environment.getDownloadCacheDirectory().getAbsolutePath();
        }
    }

    public String getImportLocation() {
        if (importLocation == null) {
            return defaultLocation;
        }
        return importLocation;
    }

    public String getExportLocation() {
        if (exportLocation == null) {
            return defaultLocation;
        }
        return exportLocation;
    }

    public void saveImportLocation(Context context, String importLocation) {
        getSharedPreferencesManager().storeStringData(context, filename, keyImportLocationResourceId, importLocation);
    }

    public void saveExportLocation(Context context, String exportLocation) {
        getSharedPreferencesManager().storeStringData(context, filename, keyExportLocationResourceId, exportLocation);
    }
}
