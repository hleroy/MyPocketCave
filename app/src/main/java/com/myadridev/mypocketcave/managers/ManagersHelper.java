package com.myadridev.mypocketcave.managers;

import android.content.Context;

public class ManagersHelper {

    public static void initializeAllManagers(Context context) {
        if (!StorageManager.IsInitialized()) {
            StorageManager.Init(context);
        }
        if (!CaveManager.IsInitialized()) {
            CaveManager.Init();
        }
        if (!BottleManager.IsInitialized()) {
            BottleManager.Init();
        }
        if (!CaveArrangementManager.IsInitialized()) {
            CaveArrangementManager.Init();
        }
        if (!CoordinatesModelManager.IsInitialized()) {
            CoordinatesModelManager.Init();
        }
    }
}
