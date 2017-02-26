package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISyncStorageManager;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.PatternModel;
import com.myadridev.mypocketcave.models.SyncModel;

import java.io.File;
import java.io.FileInputStream;
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

    public static boolean exportData(Context context, String exportLocation, String extension) {
        String exportData = getExportData(context);
        return exportData != null && performExport(exportLocation, extension, exportData);
    }

    private static String getExportData(Context context) {
        String version = VersionManager.getVersion(context);
        List<CaveModel> caves = CaveManager.getCaves();
        List<BottleModel> bottles = BottleManager.getBottles();
        List<PatternModel> patterns = PatternManager.getPatterns();
        SyncModel syncData = new SyncModel(version, caves, bottles, patterns);
        return JsonManager.writeValueAsString(syncData);
    }

    private static boolean performExport(String exportLocation, String extension, String exportData) {
        boolean isExportSuccessful = true;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String exportFileName = String.format("export_%s.%s", dateFormat.format(cal.getTime()), extension);
        File file = new File(exportLocation, exportFileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(exportData.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            isExportSuccessful = false;
        }
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            isExportSuccessful = false;
        }
        return isExportSuccessful;
    }

    public static int importData(Context context, String importLocation) {
        String version = VersionManager.getVersion(context);
        SyncModel importData = getImportData(importLocation);

        if (importData == null) {
            return R.string.error_import;
        }
        if (importData.Version.compareToIgnoreCase(version) > 0) {
            // import file was generated with an app with a version greater than current one
            return R.string.error_import_too_old_version;
        }

        performImport(context, importData);
        return -1;
    }

    private static SyncModel getImportData(String importLocation) {
        File file = new File(importLocation);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        String importDataJson = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            StringBuilder importDataJsonBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int sizeRead;
            while ((sizeRead = inputStream.read(buffer)) != -1) {
                importDataJsonBuilder.append(new String(buffer, 0, sizeRead));
            }
            importDataJson = importDataJsonBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SyncModel sync = JsonManager.readValue(importDataJson, SyncModel.class);

        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sync;
    }

    private static void performImport(Context context, SyncModel importData) {
        CaveManager.removeAllCaves(context);
        PatternManager.removeAllPatterns(context);
        BottleManager.removeAllBottles(context);

        BottleManager.addBottles(context, importData.Bottles);
        PatternManager.addPatterns(context, importData.Patterns);
        CaveManager.addCaves(context, importData.Caves);

        BottleManager.recomputeNumberPlaced(context);
    }
}
