package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CavePlaceModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaveSQLiteManager {

    public static final String CAVES_TABLE_NAME = "Caves";

    public static final String CAVE_ID = "CaveId";
    public static final String NAME = "Name";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(1);
        queries.add("CREATE TABLE " + CAVES_TABLE_NAME + " (" + CAVE_ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, "
                + CaveTypeSQLiteManager.CAVE_TYPE_ID + " INTEGER, " + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + " INTEGER);");
        return queries;
    }

    public static String getUpgradeQuery(int oldVersion, int newVersion) {
        return null;
    }

    // Insert
    public static int insertCave(CaveModel cave) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            db.beginTransaction();
            cave.CaveArrangement.Id = insertCaveArrangement(db, cave.CaveArrangement);
            cave.Id = insertCave(db, cave, cave.CaveArrangement.Id);

            for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : cave.CaveArrangement.PatternMap.entrySet()) {
                CoordinatesModel patternCoordinates = patternEntry.getKey();
                PatternModelWithBottles patternWithBottles = patternEntry.getValue();
                insertPatternWithBottleInfos(db, cave, patternCoordinates, patternWithBottles);
            }

            db.setTransactionSuccessful();
            return cave.Id;
        } catch (Exception e) {
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Update
    public static void updateCave(CaveModel cave) {
        SQLiteDatabase writableDb = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            writableDb.beginTransaction();
            updateCaveArrangement(writableDb, cave.CaveArrangement);
            updateCave(writableDb, cave);

            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
            writableDb.close();
        }

        SQLiteDatabase readableDb = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor caveArrangementsPatternsWthBottlesFromDb = readableDb.query(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME,
                getCaveArrangementPatternsWithBottlesColumn(), CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + "=?", new String[]{String.valueOf(cave.CaveArrangement.Id)},
                null, null, null, null);

        List<Integer> patternIdToDeleteList = new ArrayList<>();
        Map<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteAndAddMap = new HashMap<>();
        List<CoordinatesModel> patternCoordinatesToUpdateList = new ArrayList<>();
        if (caveArrangementsPatternsWthBottlesFromDb != null && caveArrangementsPatternsWthBottlesFromDb.moveToFirst()) {
            do {
                CoordinatesModel patternCoordinates = new CoordinatesModel(caveArrangementsPatternsWthBottlesFromDb.getInt(0), caveArrangementsPatternsWthBottlesFromDb.getInt(1));
                int patternWithBottlesId = caveArrangementsPatternsWthBottlesFromDb.getInt(2);
                if (cave.CaveArrangement.PatternMap.containsKey(patternCoordinates)) {
                    PatternModelWithBottles patternWithBottles = cave.CaveArrangement.PatternMap.get(patternCoordinates);
                    if (patternWithBottles.PatternWithBottlesId == patternWithBottlesId) {
                        patternCoordinatesToUpdateList.add(patternCoordinates);
                    } else {
                        patternCoordinatesAndIdToDeleteAndAddMap.put(patternCoordinates, patternWithBottlesId);
                    }
                } else {
                    patternIdToDeleteList.add(patternWithBottlesId);
                }
            } while (caveArrangementsPatternsWthBottlesFromDb.moveToNext());
            caveArrangementsPatternsWthBottlesFromDb.close();
        }

        readableDb.close();

        writableDb = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            writableDb.beginTransaction();
            for (int patternId : patternIdToDeleteList) {
                // there is no more pattern in this place -> delete the old one
                deletePatternWithBottleInfos(writableDb, cave, patternId);
            }
            for (int patternId : patternCoordinatesAndIdToDeleteAndAddMap.values()) {
                // there is no more pattern in this place -> delete the old one
                deletePatternWithBottleInfos(writableDb, cave, patternId);
            }

            for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : cave.CaveArrangement.PatternMap.entrySet()) {
                CoordinatesModel patternCoordinates = patternEntry.getKey();
                PatternModelWithBottles patternWithBottles = patternEntry.getValue();
                if (patternCoordinatesAndIdToDeleteAndAddMap.containsKey(patternCoordinates)) {
                    // there is a different pattern in this place -> delete the old one and add the new one
                    insertPatternWithBottleInfos(writableDb, cave, patternCoordinates, patternWithBottles);
                } else if (patternCoordinatesToUpdateList.contains(patternCoordinates)) {
                    updatePatternWithBottleInfos(writableDb, cave, patternCoordinates, patternWithBottles);
                } else {
                    // there is a new pattern in a different place
                    insertPatternWithBottleInfos(writableDb, cave, patternCoordinates, patternWithBottles);
                }
            }

            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
            writableDb.close();
        }
    }

    // Get
    public static List<CaveModel> getCaves() {
        List<CaveModel> caves = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor cavesFromDb = db.rawQuery(getCavesWithPlacesRawQuery()
                        + " order by c." + CAVE_ID + ","
                        + "pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID,
                null);
        if (cavesFromDb != null && cavesFromDb.moveToFirst()) {
            caves = getCavesWithPlacesFromDbResults(cavesFromDb);
            cavesFromDb.close();
        }

        Cursor cavesWithBottlesFromDb = db.rawQuery(getCavesWithPlacesWithBottlesRawQuery()
                        + " order by c." + CAVE_ID + ","
                        + "pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID,
                null);
        if (cavesWithBottlesFromDb != null && cavesWithBottlesFromDb.moveToFirst()) {
            getCavesWithPlacesWithBottlesFromDbResults(cavesWithBottlesFromDb, caves);
            cavesWithBottlesFromDb.close();
        }

        Collections.sort(caves);
        return caves;
    }

    public static CaveModel getCave(int caveId) {
        List<CaveModel> caves = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor cavesFromDb = db.rawQuery(getCavesWithPlacesRawQuery() + " where c." + CAVE_ID + " =?",
                new String[]{String.valueOf(caveId)});
        if (cavesFromDb != null && cavesFromDb.moveToFirst()) {
            caves = getCavesWithPlacesFromDbResults(cavesFromDb);
            cavesFromDb.close();
        }

        Cursor cavesWithBottlesFromDb = db.rawQuery(getCavesWithPlacesWithBottlesRawQuery() + " where c." + CAVE_ID + " =?",
                new String[]{String.valueOf(caveId)});
        if (cavesWithBottlesFromDb != null && cavesWithBottlesFromDb.moveToFirst()) {
            getCavesWithPlacesWithBottlesFromDbResults(cavesWithBottlesFromDb, caves);
            cavesWithBottlesFromDb.close();
        }

        return !caves.isEmpty() ? caves.get(0) : null;
    }

    public static int getExistingCaveId(int id, String name, int caveTypeId) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor caveIdFromDb = db.query(CAVES_TABLE_NAME, new String[]{CAVE_ID},
                NAME + "=? and " + CaveTypeSQLiteManager.CAVE_TYPE_ID + "=?",
                new String[]{name, String.valueOf(caveTypeId)}, null, null, null, null);

        int existingId = -1;
        if (caveIdFromDb != null && caveIdFromDb.moveToFirst()) {
            existingId = caveIdFromDb.getInt(0);
            caveIdFromDb.close();
        }

        return existingId != id ? existingId : -1;
    }

    public static int getCavesCount() {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        int count = 0;
        Cursor caveCountFromDb = db.rawQuery("select count(1) from " + CAVES_TABLE_NAME + ";", null);
        if (caveCountFromDb != null && caveCountFromDb.moveToFirst()) {
            count = caveCountFromDb.getInt(0);
            caveCountFromDb.close();
        }

        return count;
    }

    public static int getCavesCount(int caveTypeId) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        int count = 0;
        Cursor caveCountFromDb = db.rawQuery("select count(1) from " + CAVES_TABLE_NAME + " where " + CaveTypeSQLiteManager.CAVE_TYPE_ID + " =?;",
                new String[]{String.valueOf(caveTypeId)});
        if (caveCountFromDb != null) {
            caveCountFromDb.moveToFirst();
            count = caveCountFromDb.getInt(0);
            caveCountFromDb.close();
        }

        return count;
    }

    // Delete
    public static void deleteCave(CaveModel cave) {
        // TODO : update bottle.NumberPlaced
        SQLiteDatabase writableDb = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            writableDb.beginTransaction();

            for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : cave.CaveArrangement.PatternMap.entrySet()) {
                PatternModelWithBottles patternWithBottles = patternEntry.getValue();
                deletePatternWithBottleInfos(writableDb, cave, patternWithBottles.Id);
            }
            deleteCaveArrangement(writableDb, cave.CaveArrangement.Id);
            deleteCave(writableDb, cave.Id);

            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
            writableDb.close();
        }
    }

    // Private
    private static String[] getCaveArrangementPatternsWithBottlesColumn() {
        return new String[]{CaveArrangementSQLiteManager.ROW, CaveArrangementSQLiteManager.COLUMN, PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID};
    }

    private static String[] getPatternsWithBottlesCavePlacesColumn() {
        return new String[]{PatternWithBottlesSQLiteManager.ROW, PatternWithBottlesSQLiteManager.COLUMN,
                CavePlaceTypeSQLiteManager.PLACE_TYPE_ID, BottleSQLiteManager.BOTTLE_ID, PatternWithBottlesSQLiteManager.IS_CLICKABLE};
    }

    private static int insertCave(SQLiteDatabase db, CaveModel cave, int caveArrangementId) {
        ContentValues caveFields = getCaveContentValues(cave, caveArrangementId);
        return (int) db.insert(CAVES_TABLE_NAME, null, caveFields);
    }

    private static ContentValues getCaveContentValues(CaveModel cave, int caveArrangementId) {
        ContentValues caveFields = new ContentValues(3);
        caveFields.put(NAME, cave.Name);
        caveFields.put(CaveTypeSQLiteManager.CAVE_TYPE_ID, cave.CaveType.id);
        caveFields.put(CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID, caveArrangementId);
        return caveFields;
    }

    private static void updateCave(SQLiteDatabase db, CaveModel cave) {
        ContentValues caveFields = getCaveContentValues(cave, cave.CaveArrangement.Id);
        db.update(CAVES_TABLE_NAME, caveFields, CAVE_ID + "=?", new String[]{String.valueOf(cave.Id)});
    }

    private static void deleteCave(SQLiteDatabase db, int caveId) {
        db.delete(CAVES_TABLE_NAME, CAVE_ID + "=?", new String[]{String.valueOf(caveId)});
    }

    private static int insertCaveArrangement(SQLiteDatabase db, CaveArrangementModel caveArrangement) {
        ContentValues caveArrangementFields = getCaveArrangementValues(caveArrangement);
        return (int) db.insert(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_TABLE_NAME, null, caveArrangementFields);
    }

    private static ContentValues getCaveArrangementValues(CaveArrangementModel caveArrangement) {
        ContentValues caveArrangementFields = new ContentValues(5);
        caveArrangementFields.put(CaveArrangementSQLiteManager.TOTAL_CAPACITY, caveArrangement.TotalCapacity);
        caveArrangementFields.put(CaveArrangementSQLiteManager.TOTAL_USED, caveArrangement.TotalUsed);
        caveArrangementFields.put(CaveArrangementSQLiteManager.NUMBER_BOTTLES_BULK, caveArrangement.NumberBottlesBulk);
        caveArrangementFields.put(CaveArrangementSQLiteManager.NUMBER_BOXES, caveArrangement.NumberBoxes);
        caveArrangementFields.put(CaveArrangementSQLiteManager.NUMBER_BOTTLES_PER_BOX, caveArrangement.NumberBottlesPerBox);
        return caveArrangementFields;
    }

    private static void updateCaveArrangement(SQLiteDatabase db, CaveArrangementModel caveArrangement) {
        ContentValues caveArrangementFields = getCaveArrangementValues(caveArrangement);
        db.update(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_TABLE_NAME, caveArrangementFields,
                CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + "=?", new String[]{String.valueOf(caveArrangement.Id)});
    }

    private static void deleteCaveArrangement(SQLiteDatabase db, int caveArrangementId) {
        db.delete(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_TABLE_NAME, CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + "=?",
                new String[]{String.valueOf(caveArrangementId)});
    }

    private static void insertPatternWithBottleInfos(SQLiteDatabase db, CaveModel cave, CoordinatesModel patternCoordinates, PatternModelWithBottles patternWithBottles) {
        patternWithBottles.Id = insertPatternWithBottles(db, patternWithBottles);
        insertCaveArrangementPatternWithBottles(db, cave.CaveArrangement.Id, patternCoordinates, patternWithBottles.Id);
        for (Map.Entry<CoordinatesModel, CavePlaceModel> cavePlaceEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel placeCoordinates = cavePlaceEntry.getKey();
            CavePlaceModel cavePlace = cavePlaceEntry.getValue();
            insertPatternWithBottlesCavePlaces(db, patternWithBottles.Id, placeCoordinates, cavePlace);
        }
    }

    private static int insertPatternWithBottles(SQLiteDatabase db, PatternModelWithBottles patternWithBottles) {
        ContentValues patternWithBottlesFields = getPatternWithBottlesFields(patternWithBottles);
        return (int) db.insert(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_TABLE_NAME, null, patternWithBottlesFields);
    }

    private static ContentValues getPatternWithBottlesFields(PatternModelWithBottles patternWithBottles) {
        ContentValues patternWithBottlesFields = new ContentValues(1);
        patternWithBottlesFields.put(PatternSQLiteManager.PATTERN_ID, patternWithBottles.Id);
        return patternWithBottlesFields;
    }

    private static void insertCaveArrangementPatternWithBottles(SQLiteDatabase db, int caveArrangementId, CoordinatesModel patternCoordinates, int patternWithBottlesId) {
        ContentValues caveArrangementPatternWithBottlesFields = getCaveArrangementPatternWithBottlesFields(caveArrangementId, patternCoordinates, patternWithBottlesId);
        db.insert(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME, null, caveArrangementPatternWithBottlesFields);
    }

    private static ContentValues getCaveArrangementPatternWithBottlesFields(int caveArrangementId, CoordinatesModel patternCoordinates, int patternWithBottlesId) {
        ContentValues caveArrangementPatternWithBottlesFields = new ContentValues(4);
        caveArrangementPatternWithBottlesFields.put(CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID, caveArrangementId);
        caveArrangementPatternWithBottlesFields.put(CaveArrangementSQLiteManager.ROW, patternCoordinates.Row);
        caveArrangementPatternWithBottlesFields.put(CaveArrangementSQLiteManager.COLUMN, patternCoordinates.Col);
        caveArrangementPatternWithBottlesFields.put(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID, patternWithBottlesId);
        return caveArrangementPatternWithBottlesFields;
    }

    private static void deletePatternWithBottleInfos(SQLiteDatabase db, CaveModel cave, int patternWithBottlesId) {
        // TODO : update bottle.NumberPlaced
        deletePatternWithBottlesCavePlaces(db, patternWithBottlesId);
        deleteCaveArrangementPatternWithBottles(db, cave.CaveArrangement.Id, patternWithBottlesId);
        deletePatternWithBottles(db, patternWithBottlesId);
    }

    private static void deletePatternWithBottles(SQLiteDatabase db, int patternWithBottlesId) {
        db.delete(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_TABLE_NAME, PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(patternWithBottlesId)});
    }

    private static void deleteCaveArrangementPatternWithBottles(SQLiteDatabase db, int caveArrangementId, int patternWithBottlesId) {
        db.delete(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME,
                CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + "=? and " + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(caveArrangementId), String.valueOf(patternWithBottlesId)});
    }

    private static void deletePatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId, CoordinatesModel placeCoordinates) {
        db.delete(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME,
                PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=? and " + PatternWithBottlesSQLiteManager.ROW + "=? and "
                        + PatternWithBottlesSQLiteManager.COLUMN + "=?",
                new String[]{String.valueOf(patternWithBottlesId), String.valueOf(placeCoordinates.Row), String.valueOf(placeCoordinates.Col)});
    }

    private static void deletePatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId) {
        db.delete(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(patternWithBottlesId)});
    }

    private static void updatePatternWithBottleInfos(SQLiteDatabase db, CaveModel cave, CoordinatesModel patternCoordinates, PatternModelWithBottles patternWithBottles) {
        updatePatternWithBottles(db, patternWithBottles);
        updateCaveArrangementPatternWithBottles(db, cave.CaveArrangement.Id, patternCoordinates, patternWithBottles.Id);

        SQLiteDatabase readableDb = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor patternsWthBottlesCavePlacesFromDb = readableDb.query(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME,
                getPatternsWithBottlesCavePlacesColumn(), PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=?", new String[]{String.valueOf(patternWithBottles.Id)},
                null, null, null, null);

        List<CoordinatesModel> placeCoordinatesToDeleteList = new ArrayList<>();
        List<CoordinatesModel> placeCoordinatesToUpdateMap = new ArrayList<>();
        List<CoordinatesModel> placeCoordinatesToDoNothingMap = new ArrayList<>();
        if (patternsWthBottlesCavePlacesFromDb != null && patternsWthBottlesCavePlacesFromDb.moveToFirst()) {
            do {
                CoordinatesModel placeCoordinates = new CoordinatesModel(patternsWthBottlesCavePlacesFromDb.getInt(0), patternsWthBottlesCavePlacesFromDb.getInt(1));
                int oldPlaceTypeId = patternsWthBottlesCavePlacesFromDb.getInt(2);
                int oldBottleId = patternsWthBottlesCavePlacesFromDb.getInt(3);
                boolean oldIsClickable = patternsWthBottlesCavePlacesFromDb.getInt(4) != 0;
                if (patternWithBottles.PlaceMapWithBottles.containsKey(placeCoordinates)) {
                    CavePlaceModel cavePlace = patternWithBottles.PlaceMapWithBottles.get(placeCoordinates);
                    if (cavePlace.PlaceType.id != oldPlaceTypeId || cavePlace.BottleId != oldBottleId || cavePlace.IsClickable != oldIsClickable) {
                        placeCoordinatesToUpdateMap.add(placeCoordinates);
                    } {
                        placeCoordinatesToDoNothingMap.add(placeCoordinates);
                    }
                } else {
                    placeCoordinatesToDeleteList.add(placeCoordinates);
                }
            } while (patternsWthBottlesCavePlacesFromDb.moveToNext());
            patternsWthBottlesCavePlacesFromDb.close();
        }

        for (Map.Entry<CoordinatesModel, CavePlaceModel> cavePlaceEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel placeCoordinates = cavePlaceEntry.getKey();
            if (placeCoordinatesToUpdateMap.contains(placeCoordinates)) {
                CavePlaceModel cavePlace = cavePlaceEntry.getValue();
                updatePatternWithBottlesCavePlaces(db, patternWithBottles.Id, placeCoordinates, cavePlace);
            } else if (placeCoordinatesToDeleteList.contains(placeCoordinates)) {
                deletePatternWithBottlesCavePlaces(db, patternWithBottles.Id, placeCoordinates);
            } else if (placeCoordinatesToDoNothingMap.contains(patternCoordinates)) {
                // Do nothing
            } else {
                CavePlaceModel cavePlace = cavePlaceEntry.getValue();
                insertPatternWithBottlesCavePlaces(db, patternWithBottles.Id, placeCoordinates, cavePlace);
            }
        }
    }

    private static void updatePatternWithBottles(SQLiteDatabase db, PatternModelWithBottles patternWithBottles) {
        ContentValues patternWithBottlesFields = getPatternWithBottlesFields(patternWithBottles);
        db.update(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_TABLE_NAME, patternWithBottlesFields,
                PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=?", new String[]{});
    }

    private static void updateCaveArrangementPatternWithBottles(SQLiteDatabase db, int caveArrangementId, CoordinatesModel patternCoordinates, int patternWithBottlesId) {
        ContentValues caveArrangementPatternWithBottlesFields = getCaveArrangementPatternWithBottlesFields(caveArrangementId, patternCoordinates, patternWithBottlesId);
        db.update(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME, caveArrangementPatternWithBottlesFields,
                CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + "=? and " + CaveArrangementSQLiteManager.ROW + "=? and "
                        + CaveArrangementSQLiteManager.COLUMN + "=?",
                new String[]{String.valueOf(caveArrangementId), String.valueOf(patternCoordinates.Row), String.valueOf(patternCoordinates.Col)});
    }

    private static void insertPatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId, CoordinatesModel placeCoordinates, CavePlaceModel cavePlace) {
        ContentValues patternWithBottlesCavePlacesFields = getPatternWithBottlesCavePlacesFields(patternWithBottlesId, placeCoordinates, cavePlace);
        db.insert(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, null, patternWithBottlesCavePlacesFields);
    }

    private static ContentValues getPatternWithBottlesCavePlacesFields(int patternWithBottlesId, CoordinatesModel placeCoordinates, CavePlaceModel cavePlace) {
        ContentValues patternWithBottlesCavePlacesFields = new ContentValues(6);
        patternWithBottlesCavePlacesFields.put(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID, patternWithBottlesId);
        patternWithBottlesCavePlacesFields.put(PatternWithBottlesSQLiteManager.ROW, placeCoordinates.Row);
        patternWithBottlesCavePlacesFields.put(PatternWithBottlesSQLiteManager.COLUMN, placeCoordinates.Col);
        patternWithBottlesCavePlacesFields.put(CavePlaceTypeSQLiteManager.PLACE_TYPE_ID, cavePlace.PlaceType.id);
        patternWithBottlesCavePlacesFields.put(BottleSQLiteManager.BOTTLE_ID, cavePlace.BottleId);
        patternWithBottlesCavePlacesFields.put(PatternWithBottlesSQLiteManager.IS_CLICKABLE, cavePlace.IsClickable ? 1 : 0);
        return patternWithBottlesCavePlacesFields;
    }

    private static void updatePatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId, CoordinatesModel placeCoordinates, CavePlaceModel cavePlace) {
        ContentValues patternWithBottlesCavePlacesFields = getPatternWithBottlesCavePlacesFields(patternWithBottlesId, placeCoordinates, cavePlace);
        db.update(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, patternWithBottlesCavePlacesFields,
                PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=? and " + PatternWithBottlesSQLiteManager.ROW + "=? and "
                        + PatternWithBottlesSQLiteManager.COLUMN + "=?",
                new String[]{String.valueOf(patternWithBottlesId), String.valueOf(placeCoordinates.Row), String.valueOf(placeCoordinates.Col)});
    }

    @NonNull
    private static String getCavesWithPlacesRawQuery() {
        // cave
        return "select c." + CAVE_ID + ","
                + "c." + NAME + ","
                + "c." + CaveTypeSQLiteManager.CAVE_TYPE_ID + ","
                // CaveArrangement
                + "ca." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + ","
                + "ca." + CaveArrangementSQLiteManager.TOTAL_CAPACITY + ","
                + "ca." + CaveArrangementSQLiteManager.TOTAL_USED + ","
                + "ca." + CaveArrangementSQLiteManager.NUMBER_BOTTLES_BULK + ","
                + "ca." + CaveArrangementSQLiteManager.NUMBER_BOXES + ","
                + "ca." + CaveArrangementSQLiteManager.NUMBER_BOTTLES_PER_BOX + ","
                // CaveArrangementPattern
                + "cap." + CaveArrangementSQLiteManager.ROW + ","
                + "cap." + CaveArrangementSQLiteManager.COLUMN + ","
                // Pattern
                + "p." + PatternSQLiteManager.PATTERN_ID + ","
                + "p." + PatternTypeSQLiteManager.PATTERN_TYPE_ID + ","
                + "p." + PatternSQLiteManager.NUMBER_BOTTLES_BY_COLUMN + ","
                + "p." + PatternSQLiteManager.NUMBER_BOTTLES_BY_ROW + ","
                + "p." + PatternSQLiteManager.IS_HORIZONTALLY_EXPENDABLE + ","
                + "p." + PatternSQLiteManager.IS_VERTICALLY_EXPENDABLE + ","
                + "p." + PatternSQLiteManager.IS_INVERTED + ","
                + "p." + PatternSQLiteManager.ORDER + ","
                // PatternPlaces
                + "pp." + PatternSQLiteManager.ROW + ","
                + "pp." + PatternSQLiteManager.COLUMN + ","
                + "pp." + CavePlaceTypeSQLiteManager.PLACE_TYPE_ID + ","
                // PatternWithBottles
                + "pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID
                + " from " + CAVES_TABLE_NAME + " c "
                + " join " + CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_TABLE_NAME + " ca"
                + " on c." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + " = ca." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID
                + " left join " + CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME + " cap"
                + " on ca." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + " = cap." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID
                + " left join " + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_TABLE_NAME + " pwb"
                + " on cap." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + " = pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID
                + " left join " + PatternSQLiteManager.PATTERNS_TABLE_NAME + " p"
                + " on pwb." + PatternSQLiteManager.PATTERN_ID + " = p." + PatternSQLiteManager.PATTERN_ID
                + " left join " + PatternSQLiteManager.PATTERN_PLACES_TABLE_NAME + " pp"
                + " on p." + PatternSQLiteManager.PATTERN_ID + " = pp." + PatternSQLiteManager.PATTERN_ID;
    }

    @NonNull
    private static String getCavesWithPlacesWithBottlesRawQuery() {
        // cave
        return "select c." + CAVE_ID + ","
                + "c." + NAME + ","
                + "c." + CaveTypeSQLiteManager.CAVE_TYPE_ID + ","
                // CaveArrangement
                + "ca." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + ","
                + "ca." + CaveArrangementSQLiteManager.TOTAL_CAPACITY + ","
                + "ca." + CaveArrangementSQLiteManager.TOTAL_USED + ","
                + "ca." + CaveArrangementSQLiteManager.NUMBER_BOTTLES_BULK + ","
                + "ca." + CaveArrangementSQLiteManager.NUMBER_BOXES + ","
                + "ca." + CaveArrangementSQLiteManager.NUMBER_BOTTLES_PER_BOX + ","
                // CaveArrangementPattern
                + "cap." + CaveArrangementSQLiteManager.ROW + ","
                + "cap." + CaveArrangementSQLiteManager.COLUMN + ","
                // Pattern
                + "p." + PatternSQLiteManager.PATTERN_ID + ","
                + "p." + PatternTypeSQLiteManager.PATTERN_TYPE_ID + ","
                + "p." + PatternSQLiteManager.NUMBER_BOTTLES_BY_COLUMN + ","
                + "p." + PatternSQLiteManager.NUMBER_BOTTLES_BY_ROW + ","
                + "p." + PatternSQLiteManager.IS_HORIZONTALLY_EXPENDABLE + ","
                + "p." + PatternSQLiteManager.IS_VERTICALLY_EXPENDABLE + ","
                + "p." + PatternSQLiteManager.IS_INVERTED + ","
                + "p." + PatternSQLiteManager.ORDER + ","
                // PatternWithBottles
                + "pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + ","
                // PatternWithBottles_CavePlaces
                + "pwbcp." + PatternWithBottlesSQLiteManager.ROW + ","
                + "pwbcp." + PatternWithBottlesSQLiteManager.COLUMN + ","
                + "pwbcp." + CavePlaceTypeSQLiteManager.PLACE_TYPE_ID + ","
                + "pwbcp." + BottleSQLiteManager.BOTTLE_ID + ","
                + "pwbcp." + PatternWithBottlesSQLiteManager.IS_CLICKABLE
                + " from " + CAVES_TABLE_NAME + " c "
                + " join " + CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_TABLE_NAME + " ca"
                + " on c." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + " = ca." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID
                + " left join " + CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME + " cap"
                + " on ca." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + " = cap." + CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID
                + " left join " + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_TABLE_NAME + " pwb"
                + " on cap." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + " = pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID
                + " left join " + PatternSQLiteManager.PATTERNS_TABLE_NAME + " p"
                + " on pwb." + PatternSQLiteManager.PATTERN_ID + " = p." + PatternSQLiteManager.PATTERN_ID
                + " left join " + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME + " pwbcp"
                + " on pwb." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + " = pwbcp." + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID;
    }

    private static List<CaveModel> getCavesWithPlacesFromDbResults(Cursor cavesInfosFromDb) {
        List<CaveModel> caves = new ArrayList<>();
        int lastCaveId = -1;
        int maxIndexCaves = -1;
        int lastPatternWithBottlesId = -1;
        do {
            int caveId = cavesInfosFromDb.getInt(0);
            CaveModel currentCave;
            if (lastCaveId != caveId) {
                CaveModel cave = new CaveModel();
                cave.Id = caveId;
                cave.Name = cavesInfosFromDb.getString(1);
                cave.CaveType = CaveTypeEnum.getById(cavesInfosFromDb.getInt(2));
                cave.CaveArrangement.Id = cavesInfosFromDb.getInt(3);
                cave.CaveArrangement.TotalCapacity = cavesInfosFromDb.getInt(4);
                cave.CaveArrangement.TotalUsed = cavesInfosFromDb.getInt(5);
                cave.CaveArrangement.NumberBottlesBulk = cavesInfosFromDb.getInt(6);
                cave.CaveArrangement.NumberBoxes = cavesInfosFromDb.getInt(7);
                cave.CaveArrangement.NumberBottlesPerBox = cavesInfosFromDb.getInt(8);
                caves.add(cave);
                lastCaveId = caveId;
                maxIndexCaves++;
                currentCave = cave;
            } else {
                currentCave = caves.get(maxIndexCaves);
            }
            String patternWithBottlesIdString = cavesInfosFromDb.getString(22);
            if (patternWithBottlesIdString != null && !patternWithBottlesIdString.isEmpty()) {
                int patternWithBottlesId = Integer.parseInt(patternWithBottlesIdString);
                PatternModelWithBottles currentPattern;
                String patternRowString = cavesInfosFromDb.getString(9);
                if (patternRowString != null && !patternRowString.isEmpty()) {
                    int patternRow = Integer.parseInt(patternRowString);
                    CoordinatesModel patternCoordinates = new CoordinatesModel(patternRow, cavesInfosFromDb.getInt(10));
                    if (lastPatternWithBottlesId != patternWithBottlesId) {
                        PatternModelWithBottles patternWithBottles = new PatternModelWithBottles();
                        patternWithBottles.Id = cavesInfosFromDb.getInt(11);
                        patternWithBottles.Type = PatternTypeEnum.getById(cavesInfosFromDb.getInt(12));
                        patternWithBottles.PatternWithBottlesId = patternWithBottlesId;
                        patternWithBottles.NumberBottlesByColumn = cavesInfosFromDb.getInt(13);
                        patternWithBottles.NumberBottlesByRow = cavesInfosFromDb.getInt(14);
                        patternWithBottles.IsHorizontallyExpendable = cavesInfosFromDb.getInt(15) != 0;
                        patternWithBottles.IsVerticallyExpendable = cavesInfosFromDb.getInt(16) != 0;
                        patternWithBottles.IsInverted = cavesInfosFromDb.getInt(17) != 0;
                        patternWithBottles.Order = cavesInfosFromDb.getInt(18);
                        currentCave.CaveArrangement.PatternMap.put(patternCoordinates, patternWithBottles);
                        lastPatternWithBottlesId = patternWithBottlesId;
                        currentPattern = patternWithBottles;
                    } else {
                        currentPattern = currentCave.CaveArrangement.PatternMap.get(patternCoordinates);
                    }
                    String patternPlaceRowString = cavesInfosFromDb.getString(19);
                    if (patternPlaceRowString != null && !patternPlaceRowString.isEmpty()) {
                        int patternPlaceRow = Integer.parseInt(patternPlaceRowString);
                        CoordinatesModel patternPlaceCoordinates = new CoordinatesModel(patternPlaceRow, cavesInfosFromDb.getInt(20));
                        if (!currentPattern.PlaceMap.containsKey(patternPlaceCoordinates)) {
                            currentPattern.PlaceMap.put(patternPlaceCoordinates, CavePlaceTypeEnum.getById(cavesInfosFromDb.getInt(21)));
                        }
                    }
                }
            }
        } while (cavesInfosFromDb.moveToNext());
        return caves;
    }

    private static List<CaveModel> getCavesWithPlacesWithBottlesFromDbResults(Cursor cavesInfosFromDb, List<CaveModel> caves) {
        do {
            int caveId = cavesInfosFromDb.getInt(0);
            CaveModel currentCave;
            for (CaveModel cave : caves) {
                if (cave.Id == caveId) {
                    currentCave = cave;
                    String patternWithBottlesIdString = cavesInfosFromDb.getString(19);
                    if (patternWithBottlesIdString != null && !patternWithBottlesIdString.isEmpty()) {
                        PatternModelWithBottles currentPattern;
                        String patternRowString = cavesInfosFromDb.getString(9);
                        if (patternRowString != null && !patternRowString.isEmpty()) {
                            int patternRow = Integer.parseInt(patternRowString);
                            CoordinatesModel patternCoordinates = new CoordinatesModel(patternRow, cavesInfosFromDb.getInt(10));
                            currentPattern = currentCave.CaveArrangement.PatternMap.get(patternCoordinates);
                            String patternCavePlaceRowString = cavesInfosFromDb.getString(20);
                            if (patternCavePlaceRowString != null && !patternCavePlaceRowString.isEmpty()) {
                                int patternCavePlaceRow = Integer.parseInt(patternCavePlaceRowString);
                                CoordinatesModel patternCavePlaceCoordinates = new CoordinatesModel(patternCavePlaceRow, cavesInfosFromDb.getInt(21));
                                if (!currentPattern.PlaceMapWithBottles.containsKey(patternCavePlaceCoordinates)) {
                                    CavePlaceModel cavePlace = new CavePlaceModel();
                                    cavePlace.PlaceType = CavePlaceTypeEnum.getById(cavesInfosFromDb.getInt(22));
                                    cavePlace.BottleId = cavesInfosFromDb.getInt(23);
                                    cavePlace.IsClickable = cavesInfosFromDb.getInt(24) != 0;
                                    if (!currentPattern.PlaceMapWithBottles.containsKey(patternCavePlaceCoordinates)) {
                                        currentPattern.PlaceMapWithBottles.put(patternCavePlaceCoordinates, cavePlace);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } while (cavesInfosFromDb.moveToNext());
        return caves;
    }
}
