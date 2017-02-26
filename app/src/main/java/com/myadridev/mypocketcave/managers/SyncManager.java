package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.models.SyncModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

    public static String getExportData() {
        List<CaveModel> caves = CaveManager.getCaves();
        List<BottleModel> bottles = BottleManager.getBottles();
        List<PatternModel> patterns = PatternManager.getPatterns();
        SyncModel syncData = new SyncModel(caves, bottles, patterns);
        return JsonManager.writeValueAsString(syncData);
    }

    public static boolean createExportFile(String exportData, String exportLocation, String extension) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String exportFileName = String.format("export_%s.%s", dateFormat.format(cal.getTime()), extension);
        File file = new File(exportLocation, exportFileName);
        FileOutputStream os = null;
        boolean isError = false;
        try {
            os = new FileOutputStream(file);
            os.write(exportData.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            isError = true;
        }
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            isError = true;
        }
        return isError;
    }
}
