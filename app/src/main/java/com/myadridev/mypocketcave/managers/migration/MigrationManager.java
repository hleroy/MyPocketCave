package com.myadridev.mypocketcave.managers.migration;

import android.content.Context;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.enums.VersionEnum;
import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;
import com.myadridev.mypocketcave.managers.DependencyManager;
import com.myadridev.mypocketcave.managers.migration.from.IMigrationFromManager;
import com.myadridev.mypocketcave.managers.migration.from.MigrationFromV1Manager;
import com.myadridev.mypocketcave.managers.migration.to.MigrationToManager;
import com.myadridev.mypocketcave.managers.storage.interfaces.v2.ISharedPreferencesManagerV2;
import com.myadridev.mypocketcave.models.inferfaces.IBottleModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveLightModel;
import com.myadridev.mypocketcave.models.inferfaces.ICaveModel;
import com.myadridev.mypocketcave.models.inferfaces.IPatternModel;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import java.util.Collection;
import java.util.Map;

public class MigrationManager {

    private static final VersionEnum CURRENT_VERSION = VersionEnum.V2;

    private static IMigrationFromManager migrationFromManager;
    private static MigrationToManager migrationToManager = new MigrationToManager();
    ;

    private static int fileResourceId = R.string.filename_migration;
    private static int keyResourceId = R.string.store_version_key;

    private static boolean listenerSharedPreferencesRegistered = false;
    private static ISharedPreferencesManagerV2 sharedPreferencesManager = null;

    private static ISharedPreferencesManagerV2 getSharedPreferencesManager() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = DependencyManager.getSingleton(ISharedPreferencesManagerV2.class,
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

    public static Map<Integer, BottleModelV2> migrateBottles(Context context) {
        Map<Integer, IBottleModel> allBottles = migrationFromManager.loadBottles(context);
        return migrationToManager.migrateBottles(allBottles);
    }

    public static Map<Integer, CaveLightModelV2> migrateCaves(Context context) {
        Map<Integer, ICaveLightModel> allCaves = migrationFromManager.loadCaveLights(context);
        return migrationToManager.migrateCaves(allCaves);
    }

    public static Map<Integer, PatternModelV2> migratePatterns(Context context) {
        Map<Integer, IPatternModel> allPatterns = migrationFromManager.loadPatterns(context);
        return migrationToManager.migratePatterns(allPatterns);
    }

    public static Map<Integer, CaveModelV2> migrateCave(Context context, Collection<Integer> caveIds) {
        Map<Integer, ICaveModel> allCaves = migrationFromManager.loadCaves(context, caveIds);
        return migrationToManager.migrateCave(allCaves);
    }

    public static void finalizeMigration(Context context) {
        getSharedPreferencesManager().storeIntData(context, fileResourceId, keyResourceId, CURRENT_VERSION.Id);
    }
}
