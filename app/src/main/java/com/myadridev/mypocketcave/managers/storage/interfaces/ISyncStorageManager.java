package com.myadridev.mypocketcave.managers.storage.interfaces;

import android.content.Context;

public interface ISyncStorageManager {

    void resetDefaultLocation(boolean isWriteExternalStorage);

    String getImportLocation();

    String getExportLocation();

    void saveImportLocation(Context context, String importLocation);

    void saveExportLocation(Context context, String exportLocation);
}
