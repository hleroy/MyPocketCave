package com.myadridev.mypocketcave.managers.storage.sharedPreferences;

import android.os.Environment;

import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;

import java.io.File;

public class SyncSharedPreferencesManager implements ISyncStorageManager {

    public static SyncSharedPreferencesManager Instance;
    private static boolean isInitialized;

    private String defaultLocation = null;

    private SyncSharedPreferencesManager() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            defaultLocation = externalStorageDirectory.getAbsolutePath();
        } else {
            defaultLocation = Environment.getDownloadCacheDirectory().getAbsolutePath();
        }
    }

    public static void Init() {
        Instance = new SyncSharedPreferencesManager();
        isInitialized = true;
    }

    public static boolean IsInitialized() {
        return isInitialized;
    }

    public String getImportLocation() {
        return defaultLocation;
    }

    public String getExportLocation() {
        return defaultLocation;
    }
}
