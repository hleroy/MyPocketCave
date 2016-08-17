package com.myadridev.mypocketcave.managers.SQLite;

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
}
