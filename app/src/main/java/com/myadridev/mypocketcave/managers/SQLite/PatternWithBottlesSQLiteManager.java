package com.myadridev.mypocketcave.managers.SQLite;

import java.util.ArrayList;
import java.util.List;

public class PatternWithBottlesSQLiteManager {

    public static final String PATTERN_WITH_BOTTLES_TABLE_NAME = "PatternWithBottles";
    public static final String PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME = "PatternWithBottles_CavePlaces";

    public static final String PATTERN_WITH_BOTTLE_ID = "PatternWithBottleId";
    public static final String ROW = "Row";
    public static final String COLUMN = "Column";
    public static final String IS_CLICKABLE = "IsClickable";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(2);
        queries.add("CREATE TABLE " + PATTERN_WITH_BOTTLES_TABLE_NAME + " (" + PATTERN_WITH_BOTTLE_ID + " INTEGER PRIMARY KEY, "
                + PatternSQLiteManager.PATTERN_ID + " INTEGER);");
        queries.add("CREATE TABLE " + PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME + " (" + PATTERN_WITH_BOTTLE_ID + " INTEGER, " + ROW + " INTEGER, " + COLUMN + " INTEGER, "
                + CavePlaceTypeSQLiteManager.PLACE_TYPE_ID + " INTEGER, " + BottleSQLiteManager.BOTTLE_ID + " INTEGER, " + IS_CLICKABLE + " TINYINT);");
        return queries;
    }

    public static String getUpgradeQuery(int oldVersion, int newVersion) {
        return null;
    }
}
