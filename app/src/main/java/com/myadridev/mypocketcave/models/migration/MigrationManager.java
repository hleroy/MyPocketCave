package com.myadridev.mypocketcave.models.migration;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.VersionEnum;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.ISharedPreferencesManager;
import com.myadridev.mypocketcave.models.IBottleModel;
import com.myadridev.mypocketcave.models.ICaveLightModel;
import com.myadridev.mypocketcave.models.ICaveModel;
import com.myadridev.mypocketcave.models.IPatternModel;
import com.myadridev.mypocketcave.models.migration.from.IMigrationFromManager;
import com.myadridev.mypocketcave.models.migration.from.MigrationFromV1Manager;
import com.myadridev.mypocketcave.models.migration.to.MigrationToManager;

import java.util.Map;

public class MigrationManager {

    private static final VersionEnum CURRENT_VERSION = VersionEnum.V2;

    private static IMigrationFromManager migrationFromManager;
    private static MigrationToManager migrationToManager = new MigrationToManager();
    ;

    private static int fileResourceId = R.string.filename_migration;
    private static int keyResourceId = R.string.store_version_key;

    private static boolean listenerSharedPreferencesRegistered = false;
    private static ISharedPreferencesManager sharedPreferencesManager = null;

    private static ISharedPreferencesManager getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManager.class,
                    listenerSharedPreferencesRegistered ? null : (OnDependencyChangeListener) () -> sharedPreferencesManager = null);
            listenerSharedPreferencesRegistered = true;
        }
        return sharedPreferencesManager;
    }

    public static boolean needsMigration(Context context) {
        int fromAsInt = getSharedPreferencesManager().loadStoredIntData(context, fileResourceId, keyResourceId, 0);
        VersionEnum from = VersionEnum.getById(fromAsInt);

        if (from == null || from == CURRENT_VERSION) return false;

        switch (from) {
            case V1:
                migrationFromManager = new MigrationFromV1Manager();
                break;
            default:
                return false;
        }
        return true;
    }

    public static void migrateBottles(Context context) {
        Map<Integer, IBottleModel> allBottles = migrationFromManager.loadBottles(context);
        migrationToManager.migrateBottles(context, allBottles);
    }

    public static void migrateCaves(Context context) {
        Map<Integer, ICaveLightModel> allCaves = migrationFromManager.loadCaves(context);
        migrationToManager.migrateCaves(context, allCaves);
    }

    public static void migratePatterns(Context context) {
        Map<Integer, IPatternModel> allPatterns = migrationFromManager.loadPatterns(context);
        migrationToManager.migratePatterns(context, allPatterns);
    }

    public static void migrateCave(Context context) {
        Map<Integer, ICaveModel> allCaves = migrationFromManager.loadCave(context);
        migrationToManager.migrateCave(context, allCaves);
    }

    public static void finalizeMigration(Context context) {
        getSharedPreferencesManager().storeIntData(context, fileResourceId, keyResourceId, CURRENT_VERSION.Id);
    }
}
