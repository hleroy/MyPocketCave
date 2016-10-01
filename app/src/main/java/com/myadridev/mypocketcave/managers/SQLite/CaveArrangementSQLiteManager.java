package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.ArrayList;
import java.util.List;

public class CaveArrangementSQLiteManager {

    public static final String CAVE_ARRANGEMENTS_TABLE_NAME = "CaveArrangements";
    public static final String CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME = "CaveArrangements_PatternWithBottles";

    public static final String CAVE_ARRANGEMENT_ID = "CaveArrangementId";
    public static final String TOTAL_CAPACITY = "TotalCapacity";
    public static final String TOTAL_USED = "TotalUsed";
    public static final String NUMBER_BOTTLES_BULK = "NumberBottlesBulk";
    public static final String NUMBER_BOXES = "NumberBoxes";
    public static final String NUMBER_BOTTLES_PER_BOX = "NumberBottlesPerBox";
    public static final String ROW = "Row";
    public static final String COLUMN = "Column";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(2);
        queries.add("CREATE TABLE " + CAVE_ARRANGEMENTS_TABLE_NAME + " (" + CAVE_ARRANGEMENT_ID + " INTEGER PRIMARY KEY, " + TOTAL_CAPACITY + " INTEGER, "
                + TOTAL_USED + " INTEGER, " + NUMBER_BOTTLES_BULK + " INTEGER, " + NUMBER_BOXES + " INTEGER, " + NUMBER_BOTTLES_PER_BOX + " INTEGER);");
        queries.add("CREATE TABLE " + CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME + " (" + CAVE_ARRANGEMENT_ID + " INTEGER, "
                + ROW + " INTEGER, " + COLUMN + " INTEGER, " + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + " INTEGER);");
        return queries;
    }

    public static String getUpgradeQuery(int oldVersion, int newVersion) {
        return null;
    }

    public static String[] getCaveArrangementPatternsWithBottlesColumn() {
        return new String[]{ROW, COLUMN, PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID};
    }

    public static int insertCaveArrangement(SQLiteDatabase db, CaveArrangementModel caveArrangement) {
        ContentValues caveArrangementFields = getCaveArrangementValues(caveArrangement);
        return (int) db.insert(CAVE_ARRANGEMENTS_TABLE_NAME, null, caveArrangementFields);
    }

    private static ContentValues getCaveArrangementValues(CaveArrangementModel caveArrangement) {
        ContentValues caveArrangementFields = new ContentValues(5);
        caveArrangementFields.put(TOTAL_CAPACITY, caveArrangement.TotalCapacity);
        caveArrangementFields.put(TOTAL_USED, caveArrangement.TotalUsed);
        caveArrangementFields.put(NUMBER_BOTTLES_BULK, caveArrangement.NumberBottlesBulk);
        caveArrangementFields.put(NUMBER_BOXES, caveArrangement.NumberBoxes);
        caveArrangementFields.put(NUMBER_BOTTLES_PER_BOX, caveArrangement.NumberBottlesPerBox);
        return caveArrangementFields;
    }

    public static void updateCaveArrangement(SQLiteDatabase db, CaveArrangementModel caveArrangement) {
        ContentValues caveArrangementFields = getCaveArrangementValues(caveArrangement);
        db.update(CAVE_ARRANGEMENTS_TABLE_NAME, caveArrangementFields, CAVE_ARRANGEMENT_ID + "=?", new String[]{String.valueOf(caveArrangement.Id)});
    }

    public static void deleteCaveArrangement(SQLiteDatabase db, int caveArrangementId) {
        db.delete(CAVE_ARRANGEMENTS_TABLE_NAME, CAVE_ARRANGEMENT_ID + "=?", new String[]{String.valueOf(caveArrangementId)});
    }

    public static void insertCaveArrangementPatternWithBottles(SQLiteDatabase db, int caveArrangementId, CoordinatesModel patternCoordinates, int patternWithBottlesId) {
        ContentValues caveArrangementPatternWithBottlesFields = getCaveArrangementPatternWithBottlesFields(caveArrangementId, patternCoordinates, patternWithBottlesId);
        db.insert(CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME, null, caveArrangementPatternWithBottlesFields);
    }

    private static ContentValues getCaveArrangementPatternWithBottlesFields(int caveArrangementId, CoordinatesModel patternCoordinates, int patternWithBottlesId) {
        ContentValues caveArrangementPatternWithBottlesFields = new ContentValues(4);
        caveArrangementPatternWithBottlesFields.put(CAVE_ARRANGEMENT_ID, caveArrangementId);
        caveArrangementPatternWithBottlesFields.put(ROW, patternCoordinates.Row);
        caveArrangementPatternWithBottlesFields.put(COLUMN, patternCoordinates.Col);
        caveArrangementPatternWithBottlesFields.put(PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID, patternWithBottlesId);
        return caveArrangementPatternWithBottlesFields;
    }

    public static void deleteCaveArrangementPatternWithBottles(SQLiteDatabase db, int caveArrangementId, int patternWithBottlesId) {
        db.delete(CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME, CAVE_ARRANGEMENT_ID + "=? and " + PatternWithBottlesSQLiteManager.PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(caveArrangementId), String.valueOf(patternWithBottlesId)});
    }

    public static void updateCaveArrangementPatternWithBottles(SQLiteDatabase db, int caveArrangementId, CoordinatesModel patternCoordinates, int patternWithBottlesId) {
        ContentValues caveArrangementPatternWithBottlesFields = getCaveArrangementPatternWithBottlesFields(caveArrangementId, patternCoordinates, patternWithBottlesId);
        db.update(CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME, caveArrangementPatternWithBottlesFields,
                CAVE_ARRANGEMENT_ID + "=? and " + ROW + "=? and " + COLUMN + "=?",
                new String[]{String.valueOf(caveArrangementId), String.valueOf(patternCoordinates.Row), String.valueOf(patternCoordinates.Col)});
    }
}
