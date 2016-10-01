package com.myadridev.mypocketcave.managers.storage.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myadridev.mypocketcave.listeners.OnDatabaseReadyListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLiteManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PocketCaveDb";

    private boolean isDatabaseReady = false;
    private List<OnDatabaseReadyListener> onDatabaseReadyListeners;

    public static SQLiteManager Instance;
    private Context context;

    private static boolean isInitialized = false;

    public static boolean IsInitialized() {
        return isInitialized;
    }

    public static void Init(Context context) {
        Instance = new SQLiteManager(context);
        isInitialized = true;
    }

    private SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addOnDatabaseReadyListener(new OnDatabaseReadyListener() {
            @Override
            public void onDatabaseReady() {
                isDatabaseReady = true;
                clearOnDatabaseReadyListener();
            }
        });
        // References
        createReferences(db);

        // Bottles
        for (String query : BottleSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }

        // Patterns
        for (String query : PatternSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }
        for (String query : PatternWithBottlesSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }

        // Caves
        for (String query : CaveArrangementSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }
        for (String query : CaveSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }

        fireOnDatabaseReady();
    }

    private void createReferences(SQLiteDatabase db) {
        // create tables
        for (String query : CavePlaceTypeSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }
        for (String query : CaveTypeSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }
        for (String query : FoodToEatWithSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }
        for (String query : PatternTypeSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }
        for (String query : WineColorSQLiteManager.getCreateTableQuery()) {
            execSQLIfNeeded(db, query);
        }

        // populate
        CavePlaceTypeSQLiteManager.insertAll(db);
        CaveTypeSQLiteManager.insertAll(db);
        FoodToEatWithSQLiteManager.insertAll(db);
        PatternTypeSQLiteManager.insertAll(db);
        WineColorSQLiteManager.insertAll(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Bottles
        execSQLIfNeeded(db, BottleSQLiteManager.getUpgradeQuery(oldVersion, newVersion));

        // Patterns
        execSQLIfNeeded(db, PatternSQLiteManager.getUpgradeQuery(oldVersion, newVersion));
        execSQLIfNeeded(db, PatternWithBottlesSQLiteManager.getUpgradeQuery(oldVersion, newVersion));

        // Caves
        execSQLIfNeeded(db, CaveArrangementSQLiteManager.getUpgradeQuery(oldVersion, newVersion));
        execSQLIfNeeded(db, CaveSQLiteManager.getUpgradeQuery(oldVersion, newVersion));
    }

    private void execSQLIfNeeded(SQLiteDatabase db, String query) {
        if (query != null && !query.isEmpty())
            db.execSQL(query);
    }

    public SQLiteDatabase getSQLiteWritableDatabase() {
        SQLiteDatabase writableDb = getWritableDatabase();
        ensureDatabaseReady();
        return writableDb;
    }

    public SQLiteDatabase getSQLiteReadableDatabase() {
        SQLiteDatabase readableDb = getReadableDatabase();
        ensureDatabaseReady();
        return readableDb;
    }

    private void addOnDatabaseReadyListener(OnDatabaseReadyListener onDatabaseReadyListener) {
        if (onDatabaseReadyListeners == null) {
            onDatabaseReadyListeners = new ArrayList<>();
        }
        onDatabaseReadyListeners.add(onDatabaseReadyListener);
    }

    private void clearOnDatabaseReadyListener() {
        onDatabaseReadyListeners.clear();
    }

    private void fireOnDatabaseReady() {
        if (onDatabaseReadyListeners != null) {
            for (OnDatabaseReadyListener onDatabaseReadyListener : onDatabaseReadyListeners) {
                onDatabaseReadyListener.onDatabaseReady();
            }
        }
    }

    private void ensureDatabaseReady() {
        File database = context.getDatabasePath(DATABASE_NAME);

        if (!database.exists()) {
            // Database does not exist so create it
            while (!isDatabaseReady) {
                // wait
            }
        } else {
            isDatabaseReady = true;
        }
    }

    public void deleteAllCaves() {
        SQLiteDatabase db = getSQLiteWritableDatabase();
        db.delete(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME, null, null);
        db.delete(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_TABLE_NAME, null, null);
        db.delete(CaveSQLiteManager.CAVES_TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllPatterns() {
        SQLiteDatabase db = getSQLiteWritableDatabase();
        db.delete(PatternSQLiteManager.PATTERN_PLACES_TABLE_NAME, null, null);
        db.delete(PatternSQLiteManager.PATTERNS_TABLE_NAME, null, null);
        db.delete(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, null, null);
        db.delete(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_TABLE_NAME, null, null);
        db.close();
    }

    public void resetNumberPlacedBottles() {
        SQLiteDatabase db = getSQLiteWritableDatabase();
        ContentValues updatedFields = new ContentValues(1);
        updatedFields.put(BottleSQLiteManager.NUMBER_PLACED, 0);
        db.update(BottleSQLiteManager.BOTTLES_TABLE_NAME, updatedFields, null, null);
        db.close();
    }
}
