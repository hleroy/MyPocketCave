package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.enums.PositionEnum;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
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
            cave.CaveArrangement.Id = CaveArrangementSQLiteManager.insertCaveArrangement(db, cave.CaveArrangement);
            cave.Id = insertCave(db, cave, cave.CaveArrangement.Id);

            for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : cave.CaveArrangement.PatternMap.entrySet()) {
                CoordinatesModel patternCoordinates = patternEntry.getKey();
                PatternModelWithBottles patternWithBottles = patternEntry.getValue();
                PatternWithBottlesSQLiteManager.insertPatternWithBottleInfos(db, cave, patternCoordinates, patternWithBottles);
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
            CaveArrangementSQLiteManager.updateCaveArrangement(writableDb, cave.CaveArrangement);
            updateCave(writableDb, cave);
            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
            writableDb.close();
        }
        
        SQLiteDatabase readableDb = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor caveArrangementsPatternsWthBottlesFromDb = readableDb.query(CaveArrangementSQLiteManager.CAVE_ARRANGEMENTS_PATTERN_WITH_BOTTLES_TABLE_NAME,
                CaveArrangementSQLiteManager.getCaveArrangementPatternsWithBottlesColumn(), CaveArrangementSQLiteManager.CAVE_ARRANGEMENT_ID + "=?",
                new String[]{String.valueOf(cave.CaveArrangement.Id)}, null, null, null, null);

        Map<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteMap = new HashMap<>();
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
                    patternCoordinatesAndIdToDeleteMap.put(patternCoordinates, patternWithBottlesId);
                }
            } while (caveArrangementsPatternsWthBottlesFromDb.moveToNext());
            caveArrangementsPatternsWthBottlesFromDb.close();
        }
        readableDb.close();

        writableDb = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            writableDb.beginTransaction();
            Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap = removeOldPatternWithBottles(writableDb, cave,
                    patternCoordinatesAndIdToDeleteMap, patternCoordinatesAndIdToDeleteAndAddMap);
            insertNewPatternsWithBottles(writableDb, cave, patternCoordinatesAndIdToDeleteAndAddMap, patternCoordinatesToUpdateList);
            updateBottlesRemovedOnlyHalf(writableDb, cave, coordinatesBottlesRemovedOnlyHalfMap);
            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
            writableDb.close();
        }
    }

    private static void insertNewPatternsWithBottles(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteAndAddMap, List<CoordinatesModel> patternCoordinatesToUpdateList) {
        for (Map.Entry<CoordinatesModel, PatternModelWithBottles> patternEntry : cave.CaveArrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModelWithBottles patternWithBottles = patternEntry.getValue();
            if (patternCoordinatesAndIdToDeleteAndAddMap.containsKey(patternCoordinates)) {
                // there is a different pattern in this place -> delete the old one and add the new one
                PatternWithBottlesSQLiteManager.insertPatternWithBottleInfos(db, cave, patternCoordinates, patternWithBottles);
            } else if (patternCoordinatesToUpdateList.contains(patternCoordinates)) {
                PatternWithBottlesSQLiteManager.updatePatternWithBottleInfos(db, cave, patternCoordinates, patternWithBottles);
            } else {
                // there is a new pattern in a different place
                PatternWithBottlesSQLiteManager.insertPatternWithBottleInfos(db, cave, patternCoordinates, patternWithBottles);
            }
        }
    }

    private static void updateBottlesRemovedOnlyHalf(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap) {
        for (Map.Entry<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfEntry : coordinatesBottlesRemovedOnlyHalfMap.entrySet()) {
            CoordinatesModel patternCoordinates = coordinatesBottlesRemovedOnlyHalfEntry.getKey();
            Map<CoordinatesModel, Map<PositionEnum, Integer>> bottlesRemovedOnlyHalfMap = coordinatesBottlesRemovedOnlyHalfEntry.getValue();
            if (bottlesRemovedOnlyHalfMap.isEmpty()) {
                continue;
            }
            for (Map.Entry<CoordinatesModel, Map<PositionEnum, Integer>> bottlesRemovedOnlyHalfEntry : bottlesRemovedOnlyHalfMap.entrySet()) {
                CoordinatesModel placeCoordinates = bottlesRemovedOnlyHalfEntry.getKey();
                Map<PositionEnum, Integer> positionAndBottleIdMap = bottlesRemovedOnlyHalfEntry.getValue();
                // there is one element only in this map
                for (Map.Entry<PositionEnum, Integer> positionAndBottleId : positionAndBottleIdMap.entrySet()) {
                    PositionEnum position = positionAndBottleId.getKey();
                    int bottleId = positionAndBottleId.getValue();
                    switch (position) {
                        case BOTTOM:
                            updateBottlesRemovedOnlyHalfOnBottom(db, cave, coordinatesBottlesRemovedOnlyHalfMap, patternCoordinates, placeCoordinates, bottleId);
                            break;
                        case LEFT:
                            updateBottlesRemovedOnlyHalfOnLeft(db, cave, coordinatesBottlesRemovedOnlyHalfMap, patternCoordinates, placeCoordinates, bottleId);
                            break;
                        case RIGHT:
                            updateBottlesRemovedOnlyHalfOnRight(db, cave, coordinatesBottlesRemovedOnlyHalfMap, patternCoordinates, placeCoordinates);
                            break;
                        case TOP:
                            updateBottlesRemovedOnlyHalfOnTop(db, cave, coordinatesBottlesRemovedOnlyHalfMap, patternCoordinates, placeCoordinates);
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }
        }
    }

    private static void updateBottlesRemovedOnlyHalfOnTop(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap, CoordinatesModel patternCoordinates, CoordinatesModel placeCoordinates) {
        // the place removed is at the top of the pattern
        // if the pattern above has been deleted, nothing to do
        // else delete the bottle that corresponds in the pattern right above
        // do not update the number placed for this bottle
        CoordinatesModel topPatternCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);
        if (!coordinatesBottlesRemovedOnlyHalfMap.containsKey(topPatternCoordinates)
                && cave.CaveArrangement.PatternMap.containsKey(patternCoordinates)
                && cave.CaveArrangement.PatternMap.containsKey(topPatternCoordinates)) {
            PatternModelWithBottles topPattern = cave.CaveArrangement.PatternMap.get(topPatternCoordinates);
            CoordinatesModel topPlaceCoordinates = new CoordinatesModel(0, placeCoordinates.Col);
            if (topPattern.PlaceMap.containsKey(topPlaceCoordinates)) {
                CavePlaceModel cavePlace = topPattern.PlaceMapWithBottles.get(topPlaceCoordinates);
                if (cavePlace.PlaceType.isBottomLeft()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_LEFT;
                    CoordinatesModel topLeftPlaceCoordinates = new CoordinatesModel(0, placeCoordinates.Col - 1);
                    topPattern.PlaceMapWithBottles.get(topLeftPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT;
                    // TODO update these positions in the database
                } else if (cavePlace.PlaceType.isBottomRight()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT;
                    CoordinatesModel topRightPlaceCoordinates = new CoordinatesModel(0, placeCoordinates.Col + 1);
                    topPattern.PlaceMapWithBottles.get(topRightPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_LEFT;
                    // TODO update these positions in the database
                }
            }
        }
    }

    private static void updateBottlesRemovedOnlyHalfOnRight(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap, CoordinatesModel patternCoordinates, CoordinatesModel placeCoordinates) {
        // the place removed is at the right of the pattern
        // if the pattern on the right has been deleted, nothing to do
        // else delete the bottle that corresponds in the pattern on the right
        // do not update the number placed for this bottle
        CoordinatesModel rightPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
        if (!coordinatesBottlesRemovedOnlyHalfMap.containsKey(rightPatternCoordinates)
                && cave.CaveArrangement.PatternMap.containsKey(rightPatternCoordinates)) {
            PatternModelWithBottles rightPattern = cave.CaveArrangement.PatternMap.get(rightPatternCoordinates);
            CoordinatesModel rightPlaceCoordinates = new CoordinatesModel(placeCoordinates.Row, 0);
            if (rightPattern.PlaceMap.containsKey(rightPlaceCoordinates)) {
                CavePlaceModel cavePlace = rightPattern.PlaceMapWithBottles.get(rightPlaceCoordinates);
                if (cavePlace.PlaceType.isBottomLeft()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_LEFT;
                    CoordinatesModel topLeftPlaceCoordinates = new CoordinatesModel(placeCoordinates.Row - 1, 0);
                    rightPattern.PlaceMapWithBottles.get(topLeftPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_TOP_LEFT;
                    // TODO update these positions in the database
                } else if (cavePlace.PlaceType.isTopLeft()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_TOP_LEFT;
                    CoordinatesModel bottomLeftPlaceCoordinates = new CoordinatesModel(placeCoordinates.Row + 1, 0);
                    rightPattern.PlaceMapWithBottles.get(bottomLeftPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_LEFT;
                    // TODO update these positions in the database
                }
            }
        }
    }

    private static void updateBottlesRemovedOnlyHalfOnLeft(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap, CoordinatesModel patternCoordinates, CoordinatesModel placeCoordinates, int bottleId) {
        // the place removed is at the left of the pattern
        // if the pattern on the left has been deleted, nothing to do
        // else delete the bottle that corresponds in the pattern on the left
        // and update the number placed for the bottle
        CoordinatesModel leftPatternCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
        if (!coordinatesBottlesRemovedOnlyHalfMap.containsKey(leftPatternCoordinates)
                && cave.CaveArrangement.PatternMap.containsKey(leftPatternCoordinates)) {
            PatternModelWithBottles leftPattern = cave.CaveArrangement.PatternMap.get(leftPatternCoordinates);
            int maxCol = CoordinatesManager.getMaxCol(leftPattern.PlaceMapWithBottles.keySet());
            CoordinatesModel leftPlaceCoordinates = new CoordinatesModel(placeCoordinates.Row, maxCol);
            if (leftPattern.PlaceMap.containsKey(leftPlaceCoordinates)) {
                CavePlaceModel cavePlace = leftPattern.PlaceMapWithBottles.get(leftPlaceCoordinates);
                if (cavePlace.PlaceType.isBottomRight()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT;
                    CoordinatesModel topRightPlaceCoordinates = new CoordinatesModel(placeCoordinates.Row - 1, maxCol);
                    leftPattern.PlaceMapWithBottles.get(topRightPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_TOP_RIGHT;
                    // TODO update these positions in the database
                } else if (cavePlace.PlaceType.isTopRight()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_TOP_RIGHT;
                    CoordinatesModel bottomRightPlaceCoordinates = new CoordinatesModel(placeCoordinates.Row + 1, maxCol);
                    leftPattern.PlaceMapWithBottles.get(bottomRightPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_BOTTOM_RIGHT;
                    // TODO update these positions in the database
                }
                BottleSQLiteManager.updateNumberPlaced(db, bottleId, -1);
            }
        }
    }

    private static void updateBottlesRemovedOnlyHalfOnBottom(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap, CoordinatesModel patternCoordinates, CoordinatesModel placeCoordinates, int bottleId) {
        // the place removed is at the bottom of the pattern
        // if the pattern below has been deleted, nothing to do
        // else if there is a new pattern here, delete the bottle that corresponds in the pattern right below
        // and update the number placed for this bottle
        CoordinatesModel bottomPatternCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
        if (!coordinatesBottlesRemovedOnlyHalfMap.containsKey(bottomPatternCoordinates)
                && cave.CaveArrangement.PatternMap.containsKey(patternCoordinates)
                && cave.CaveArrangement.PatternMap.containsKey(bottomPatternCoordinates)) {
            PatternModelWithBottles bottomPattern = cave.CaveArrangement.PatternMap.get(bottomPatternCoordinates);
            int maxRow = CoordinatesManager.getMaxRow(bottomPattern.PlaceMapWithBottles.keySet());
            CoordinatesModel bottomPlaceCoordinates = new CoordinatesModel(maxRow, placeCoordinates.Col);
            if (bottomPattern.PlaceMap.containsKey(bottomPlaceCoordinates)) {
                CavePlaceModel cavePlace = bottomPattern.PlaceMapWithBottles.get(bottomPlaceCoordinates);
                if (cavePlace.PlaceType.isTopLeft()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_TOP_LEFT;
                    CoordinatesModel bottomLeftPlaceCoordinates = new CoordinatesModel(maxRow, placeCoordinates.Col - 1);
                    bottomPattern.PlaceMapWithBottles.get(bottomLeftPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_TOP_RIGHT;
                    // TODO update these positions in the database
                } else if (cavePlace.PlaceType.isTopRight()) {
                    cavePlace.PlaceType = CavePlaceTypeEnum.PLACE_TOP_RIGHT;
                    CoordinatesModel bottomRightPlaceCoordinates = new CoordinatesModel(maxRow, placeCoordinates.Col + 1);
                    bottomPattern.PlaceMapWithBottles.get(bottomRightPlaceCoordinates).PlaceType = CavePlaceTypeEnum.PLACE_TOP_LEFT;
                    // TODO update these positions in the database
                }
                BottleSQLiteManager.updateNumberPlaced(db, bottleId, -1);
            }
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
        SQLiteDatabase writableDb = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            writableDb.beginTransaction();

            for (PatternModelWithBottles patternWithBottles : cave.CaveArrangement.PatternMap.values()) {
                PatternWithBottlesSQLiteManager.deletePatternWithBottleInfos(writableDb, cave, patternWithBottles.Id);
            }
            CaveArrangementSQLiteManager.deleteCaveArrangement(writableDb, cave.CaveArrangement.Id);
            deleteCave(writableDb, cave.Id);

            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
            writableDb.close();
        }
    }

    // Private
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

    private static Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> removeOldPatternWithBottles(SQLiteDatabase db, CaveModel cave, Map<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteMap, Map<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteAndAddMap) {
        Map<CoordinatesModel, Map<CoordinatesModel, Map<PositionEnum, Integer>>> coordinatesBottlesRemovedOnlyHalfMap = new HashMap<>();
        for (Map.Entry<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteEntry : patternCoordinatesAndIdToDeleteMap.entrySet()) {
            // there is no more pattern in this place -> delete the old one
            CoordinatesModel patternCoordinates = patternCoordinatesAndIdToDeleteEntry.getKey();
            int patternId = patternCoordinatesAndIdToDeleteEntry.getValue();
            coordinatesBottlesRemovedOnlyHalfMap.put(patternCoordinates, PatternWithBottlesSQLiteManager.deletePatternWithBottleInfos(db, cave, patternId));
        }
        for (Map.Entry<CoordinatesModel, Integer> patternCoordinatesAndIdToDeleteAndAddEntry : patternCoordinatesAndIdToDeleteAndAddMap.entrySet()) {
            // there is another pattern in this place -> delete the old one and add a new one
            CoordinatesModel patternCoordinates = patternCoordinatesAndIdToDeleteAndAddEntry.getKey();
            int patternId = patternCoordinatesAndIdToDeleteAndAddEntry.getValue();
            coordinatesBottlesRemovedOnlyHalfMap.put(patternCoordinates, PatternWithBottlesSQLiteManager.deletePatternWithBottleInfos(db, cave, patternId));
        }
        return coordinatesBottlesRemovedOnlyHalfMap;
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
