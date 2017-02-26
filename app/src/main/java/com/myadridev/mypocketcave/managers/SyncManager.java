package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;

public class SyncManager {

    private static boolean listenerSyncRegistered = false;
    private static ISyncStorageManager syncStorageManager = null;

    private static ISyncStorageManager getSyncStorageManager() {
        if (syncStorageManager == null) {
            syncStorageManager = DependencyManager.getSingleton(ISyncStorageManager.class,
                    listenerSyncRegistered ? null : (OnDependencyChangeListener) () -> syncStorageManager = null);
            listenerSyncRegistered = true;
        }
        return syncStorageManager;
    }

    public static void resetDefaultLocation(boolean isWriteExternalStorage) {
        getSyncStorageManager().resetDefaultLocation(isWriteExternalStorage);
    }

    public static String getImportLocation() {
        return getSyncStorageManager().getImportLocation();
    }

    public static String getExportLocation() {
        return getSyncStorageManager().getExportLocation();
    }

    public static void saveImportLocation(Context context, String importLocation) {
        getSyncStorageManager().saveImportLocation(context, importLocation);
    }

    public static void saveExportLocation(Context context, String exportLocation) {
        getSyncStorageManager().saveExportLocation(context, exportLocation);
    }
}
