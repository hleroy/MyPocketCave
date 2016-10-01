package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PositionEnum;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CavePlaceModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

    public static String[] getPatternsWithBottlesCavePlacesColumn() {
        return new String[]{ROW, COLUMN, CavePlaceTypeSQLiteManager.PLACE_TYPE_ID, BottleSQLiteManager.BOTTLE_ID, IS_CLICKABLE};
    }

    public static void insertPatternWithBottleInfos(SQLiteDatabase db, CaveModel
            cave, CoordinatesModel patternCoordinates, PatternModelWithBottles patternWithBottles) {
        patternWithBottles.PatternWithBottlesId = insertPatternWithBottles(db, patternWithBottles);
        CaveArrangementSQLiteManager.insertCaveArrangementPatternWithBottles(db, cave.CaveArrangement.Id, patternCoordinates, patternWithBottles.PatternWithBottlesId);
        for (Map.Entry<CoordinatesModel, CavePlaceModel> cavePlaceEntry : patternWithBottles.PlaceMapWithBottles.entrySet()) {
            CoordinatesModel placeCoordinates = cavePlaceEntry.getKey();
            CavePlaceModel cavePlace = cavePlaceEntry.getValue();
            insertPatternWithBottlesCavePlaces(db, patternWithBottles.PatternWithBottlesId, placeCoordinates, cavePlace);
        }
    }

    public static int insertPatternWithBottles(SQLiteDatabase db, PatternModelWithBottles patternWithBottles) {
        ContentValues patternWithBottlesFields = getPatternWithBottlesFields(patternWithBottles);
        return (int) db.insert(PATTERN_WITH_BOTTLES_TABLE_NAME, null, patternWithBottlesFields);
    }

    private static ContentValues getPatternWithBottlesFields(PatternModelWithBottles patternWithBottles) {
        ContentValues patternWithBottlesFields = new ContentValues(1);
        patternWithBottlesFields.put(PatternSQLiteManager.PATTERN_ID, patternWithBottles.Id);
        return patternWithBottlesFields;
    }

    public static void deletePatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId, CoordinatesModel placeCoordinates) {
        db.delete(PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME,
                PATTERN_WITH_BOTTLE_ID + "=? and " + ROW + "=? and "
                        + COLUMN + "=?",
                new String[]{String.valueOf(patternWithBottlesId), String.valueOf(placeCoordinates.Row), String.valueOf(placeCoordinates.Col)});
    }

    public static Map<CoordinatesModel, Map<PositionEnum, Integer>> deletePatternWithBottleInfos(SQLiteDatabase db, CaveModel cave, int patternWithBottlesId) {
        Map<CoordinatesModel, Map<PositionEnum, Integer>> coordinatesBottlesRemovedOnlyHalf = deletePatternWithBottlesCavePlaces(db, patternWithBottlesId);
        CaveArrangementSQLiteManager.deleteCaveArrangementPatternWithBottles(db, cave.CaveArrangement.Id, patternWithBottlesId);
        deletePatternWithBottles(db, patternWithBottlesId);
        return coordinatesBottlesRemovedOnlyHalf;
    }

    public static void deletePatternWithBottles(SQLiteDatabase db, int patternWithBottlesId) {
        db.delete(PATTERN_WITH_BOTTLES_TABLE_NAME, PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(patternWithBottlesId)});
    }

    private static Map<CoordinatesModel, Map<PositionEnum, Integer>> deletePatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId) {
        Cursor placesWithBottlesFromDb = db.query(PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME,
                new String[]{ROW, COLUMN, CavePlaceTypeSQLiteManager.PLACE_TYPE_ID, BottleSQLiteManager.BOTTLE_ID},
                BottleSQLiteManager.BOTTLE_ID + "!=?", new String[]{String.valueOf(-1)}, null, null, null);

        Map<CoordinatesModel, Map<PositionEnum, Integer>> coordinatesBottlesRemovedOnlyHalf = new HashMap<>();
        Map<CoordinatesModel, CavePlaceTypeEnum> coordinatesRemovedMap = new HashMap<>();
        Map<CoordinatesModel, Integer> bottlesRemovedMap = new HashMap<>();

        if (placesWithBottlesFromDb != null && placesWithBottlesFromDb.moveToFirst()) {
            do {
                CoordinatesModel coordinates = new CoordinatesModel(placesWithBottlesFromDb.getInt(0), placesWithBottlesFromDb.getInt(1));
                coordinatesRemovedMap.put(coordinates, CavePlaceTypeEnum.getById(placesWithBottlesFromDb.getInt(2)));
                bottlesRemovedMap.put(coordinates, placesWithBottlesFromDb.getInt(3));
            } while (placesWithBottlesFromDb.moveToNext());
            placesWithBottlesFromDb.close();
        }

        HashSet<CoordinatesModel> handledCoordinates = new HashSet<>();
        Map<Integer, Integer> numberBottlesRemovedMap = new HashMap<>();
        for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> coordinatesRemovedEntry : coordinatesRemovedMap.entrySet()) {
            CoordinatesModel coordinates = coordinatesRemovedEntry.getKey();
            if (handledCoordinates.contains(coordinates)) {
                continue;
            }

            CavePlaceTypeEnum placeType = coordinatesRemovedEntry.getValue();
            // update number placed if bottle on top or on right of a pattern (not left to avoid double decrement, not bottom because the bottle can still be here)
            // update number placed if the 4 are in this pattern too
            if (placeType.isBottomLeft()) {
                handleBottomLeftPlaceToRemove(coordinatesBottlesRemovedOnlyHalf, coordinatesRemovedMap, bottlesRemovedMap, handledCoordinates, numberBottlesRemovedMap, coordinates);
            } else if (placeType.isTopLeft()) {
                handleTopLeftPlaceToRemove(coordinatesBottlesRemovedOnlyHalf, coordinatesRemovedMap, bottlesRemovedMap, handledCoordinates, numberBottlesRemovedMap, coordinates);
            } else if (placeType.isBottomRight()) {
                handleBottomRightPlaceToRemove(coordinatesBottlesRemovedOnlyHalf, coordinatesRemovedMap, bottlesRemovedMap, handledCoordinates, numberBottlesRemovedMap, coordinates);
            } else if (placeType.isTopRight()) {
                handleTopRightPlaceToRemove(coordinatesBottlesRemovedOnlyHalf, coordinatesRemovedMap, bottlesRemovedMap, handledCoordinates, numberBottlesRemovedMap, coordinates);
            }
            handledCoordinates.add(coordinates);
        }

        for (Map.Entry<Integer, Integer> numberBottlesRemovedEntry : numberBottlesRemovedMap.entrySet()) {
            BottleSQLiteManager.updateNumberPlaced(db, numberBottlesRemovedEntry.getKey(), numberBottlesRemovedEntry.getValue());
        }

        db.delete(PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(patternWithBottlesId)});

        return coordinatesBottlesRemovedOnlyHalf;
    }

    private static void handleTopRightPlaceToRemove(Map<CoordinatesModel, Map<PositionEnum, Integer>> coordinatesBottlesRemovedOnlyHalf, Map<CoordinatesModel, CavePlaceTypeEnum> coordinatesRemovedMap, Map<CoordinatesModel, Integer> bottlesRemovedMap, HashSet<CoordinatesModel> handledCoordinates, Map<Integer, Integer> numberBottlesRemovedMap, CoordinatesModel coordinates) {
        boolean isTop = false;
        boolean isRight = false;
        CoordinatesModel rightCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
        if (coordinatesRemovedMap.containsKey(rightCoordinates)) {
            handledCoordinates.add(rightCoordinates);
            isTop = true;
        }
        CoordinatesModel topCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
        if (coordinatesRemovedMap.containsKey(topCoordinates)) {
            handledCoordinates.add(topCoordinates);
            isRight = true;
        }
        CoordinatesModel topRightCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col + 1);
        if (coordinatesRemovedMap.containsKey(topRightCoordinates)) {
            handledCoordinates.add(topRightCoordinates);
            isTop = true;
            isRight = true;
        }
        if (isTop ^ isRight) {
            Map<PositionEnum, Integer> positionAndBottleId = new HashMap<>(1);
            positionAndBottleId.put(isTop ? PositionEnum.TOP : PositionEnum.RIGHT, bottlesRemovedMap.get(coordinates));
            coordinatesBottlesRemovedOnlyHalf.put(coordinates, positionAndBottleId);
        }
        if (isTop || isRight) {
            int bottleId = bottlesRemovedMap.get(coordinates);
            if (!numberBottlesRemovedMap.containsKey(bottleId)) {
                numberBottlesRemovedMap.put(bottleId, -1);
            } else {
                numberBottlesRemovedMap.put(bottleId, numberBottlesRemovedMap.get(bottleId) - 1);
            }
        }
    }

    private static void handleBottomRightPlaceToRemove(Map<CoordinatesModel, Map<PositionEnum, Integer>> coordinatesBottlesRemovedOnlyHalf, Map<CoordinatesModel, CavePlaceTypeEnum> coordinatesRemovedMap, Map<CoordinatesModel, Integer> bottlesRemovedMap, HashSet<CoordinatesModel> handledCoordinates, Map<Integer, Integer> numberBottlesRemovedMap, CoordinatesModel coordinates) {
        boolean isBottom = false;
        boolean isRight = false;
        CoordinatesModel rightCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col + 1);
        if (coordinatesRemovedMap.containsKey(rightCoordinates)) {
            handledCoordinates.add(rightCoordinates);
            isBottom = true;
        }
        CoordinatesModel bottomCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col);
        if (coordinatesRemovedMap.containsKey(bottomCoordinates)) {
            handledCoordinates.add(bottomCoordinates);
            isRight = true;
        }
        CoordinatesModel bottomRightCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col + 1);
        if (coordinatesRemovedMap.containsKey(bottomRightCoordinates)) {
            handledCoordinates.add(bottomRightCoordinates);
            isBottom = true;
            isRight = true;
        }
        if (isRight ^ isBottom) {
            Map<PositionEnum, Integer> positionAndBottleId = new HashMap<>(1);
            positionAndBottleId.put(isRight ? PositionEnum.RIGHT : PositionEnum.BOTTOM, bottlesRemovedMap.get(coordinates));
            coordinatesBottlesRemovedOnlyHalf.put(coordinates, positionAndBottleId);
        }
        if (isRight) {
            int bottleId = bottlesRemovedMap.get(coordinates);
            if (!numberBottlesRemovedMap.containsKey(bottleId)) {
                numberBottlesRemovedMap.put(bottleId, -1);
            } else {
                numberBottlesRemovedMap.put(bottleId, numberBottlesRemovedMap.get(bottleId) - 1);
            }
        }
    }

    private static void handleTopLeftPlaceToRemove(Map<CoordinatesModel, Map<PositionEnum, Integer>> coordinatesBottlesRemovedOnlyHalf, Map<CoordinatesModel, CavePlaceTypeEnum> coordinatesRemovedMap, Map<CoordinatesModel, Integer> bottlesRemovedMap, HashSet<CoordinatesModel> handledCoordinates, Map<Integer, Integer> numberBottlesRemovedMap, CoordinatesModel coordinates) {
        boolean isLeft = false;
        boolean isTop = false;
        CoordinatesModel leftCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col - 1);
        if (coordinatesRemovedMap.containsKey(leftCoordinates)) {
            handledCoordinates.add(leftCoordinates);
            isTop = true;
        }
        CoordinatesModel topCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col);
        if (coordinatesRemovedMap.containsKey(topCoordinates)) {
            handledCoordinates.add(topCoordinates);
            isLeft = true;
        }
        CoordinatesModel topLeftCoordinates = new CoordinatesModel(coordinates.Row + 1, coordinates.Col - 1);
        if (coordinatesRemovedMap.containsKey(topLeftCoordinates)) {
            handledCoordinates.add(topLeftCoordinates);
            isTop = true;
            isLeft = true;
        }
        if (isTop ^ isLeft) {
            Map<PositionEnum, Integer> positionAndBottleId = new HashMap<>(1);
            positionAndBottleId.put(isTop ? PositionEnum.TOP : PositionEnum.LEFT, bottlesRemovedMap.get(coordinates));
            coordinatesBottlesRemovedOnlyHalf.put(coordinates, positionAndBottleId);
        }
        if (isTop) {
            int bottleId = bottlesRemovedMap.get(coordinates);
            if (!numberBottlesRemovedMap.containsKey(bottleId)) {
                numberBottlesRemovedMap.put(bottleId, -1);
            } else {
                numberBottlesRemovedMap.put(bottleId, numberBottlesRemovedMap.get(bottleId) - 1);
            }
        }
    }

    private static void handleBottomLeftPlaceToRemove(Map<CoordinatesModel, Map<PositionEnum, Integer>> coordinatesBottlesRemovedOnlyHalf, Map<CoordinatesModel, CavePlaceTypeEnum> coordinatesRemovedMap, Map<CoordinatesModel, Integer> bottlesRemovedMap, HashSet<CoordinatesModel> handledCoordinates, Map<Integer, Integer> numberBottlesRemovedMap, CoordinatesModel coordinates) {
        boolean isLeft = false;
        boolean isBottom = false;
        CoordinatesModel leftCoordinates = new CoordinatesModel(coordinates.Row, coordinates.Col - 1);
        if (coordinatesRemovedMap.containsKey(leftCoordinates)) {
            handledCoordinates.add(leftCoordinates);
            isBottom = true;
        }
        CoordinatesModel bottomCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col);
        if (coordinatesRemovedMap.containsKey(bottomCoordinates)) {
            handledCoordinates.add(bottomCoordinates);
            isLeft = true;
        }
        CoordinatesModel bottomLeftCoordinates = new CoordinatesModel(coordinates.Row - 1, coordinates.Col - 1);
        if (coordinatesRemovedMap.containsKey(bottomLeftCoordinates)) {
            handledCoordinates.add(bottomLeftCoordinates);
            isBottom = true;
            isLeft = true;
        }
        if (isBottom ^ isLeft) {
            Map<PositionEnum, Integer> positionAndBottleId = new HashMap<>(1);
            positionAndBottleId.put(isBottom ? PositionEnum.BOTTOM : PositionEnum.LEFT, bottlesRemovedMap.get(coordinates));
            coordinatesBottlesRemovedOnlyHalf.put(coordinates, positionAndBottleId);
        }
        if (isBottom && isLeft) {
            int bottleId = bottlesRemovedMap.get(coordinates);
            if (!numberBottlesRemovedMap.containsKey(bottleId)) {
                numberBottlesRemovedMap.put(bottleId, -1);
            } else {
                numberBottlesRemovedMap.put(bottleId, numberBottlesRemovedMap.get(bottleId) - 1);
            }
        }
    }

    private static void insertPatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId, CoordinatesModel placeCoordinates, CavePlaceModel cavePlace) {
        ContentValues patternWithBottlesCavePlacesFields = getPatternWithBottlesCavePlacesFields(patternWithBottlesId, placeCoordinates, cavePlace);
        db.insert(PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, null, patternWithBottlesCavePlacesFields);
    }

    private static ContentValues getPatternWithBottlesCavePlacesFields(int patternWithBottlesId, CoordinatesModel placeCoordinates, CavePlaceModel cavePlace) {
        ContentValues patternWithBottlesCavePlacesFields = new ContentValues(6);
        patternWithBottlesCavePlacesFields.put(PATTERN_WITH_BOTTLE_ID, patternWithBottlesId);
        patternWithBottlesCavePlacesFields.put(ROW, placeCoordinates.Row);
        patternWithBottlesCavePlacesFields.put(COLUMN, placeCoordinates.Col);
        patternWithBottlesCavePlacesFields.put(CavePlaceTypeSQLiteManager.PLACE_TYPE_ID, cavePlace.PlaceType.id);
        patternWithBottlesCavePlacesFields.put(BottleSQLiteManager.BOTTLE_ID, cavePlace.BottleId);
        patternWithBottlesCavePlacesFields.put(IS_CLICKABLE, cavePlace.IsClickable ? 1 : 0);
        return patternWithBottlesCavePlacesFields;
    }

    private static void updatePatternWithBottlesCavePlaces(SQLiteDatabase db, int patternWithBottlesId, CoordinatesModel placeCoordinates, CavePlaceModel cavePlace) {
        ContentValues patternWithBottlesCavePlacesFields = getPatternWithBottlesCavePlacesFields(patternWithBottlesId, placeCoordinates, cavePlace);
        db.update(PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME, patternWithBottlesCavePlacesFields,
                PATTERN_WITH_BOTTLE_ID + "=? and " + ROW + "=? and "
                        + COLUMN + "=?",
                new String[]{String.valueOf(patternWithBottlesId), String.valueOf(placeCoordinates.Row), String.valueOf(placeCoordinates.Col)});
    }

    public static void updatePatternWithBottleInfos(SQLiteDatabase db, CaveModel cave, CoordinatesModel patternCoordinates, PatternModelWithBottles patternWithBottles) {
        updatePatternWithBottles(db, patternWithBottles);
        CaveArrangementSQLiteManager.updateCaveArrangementPatternWithBottles(db, cave.CaveArrangement.Id, patternCoordinates, patternWithBottles.PatternWithBottlesId);

        SQLiteDatabase readableDb = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor patternsWthBottlesCavePlacesFromDb = readableDb.query(PATTERN_WITH_BOTTLES_CAVE_PLACES_TABLE_NAME,
                getPatternsWithBottlesCavePlacesColumn(), PATTERN_WITH_BOTTLE_ID + "=?",
                new String[]{String.valueOf(patternWithBottles.PatternWithBottlesId)},
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
                    }
                    {
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
                updatePatternWithBottlesCavePlaces(db, patternWithBottles.PatternWithBottlesId, placeCoordinates, cavePlace);
            } else if (placeCoordinatesToDeleteList.contains(placeCoordinates)) {
                deletePatternWithBottlesCavePlaces(db, patternWithBottles.PatternWithBottlesId, placeCoordinates);
            } else if (placeCoordinatesToDoNothingMap.contains(patternCoordinates)) {
                // Do nothing
            } else {
                CavePlaceModel cavePlace = cavePlaceEntry.getValue();
                insertPatternWithBottlesCavePlaces(db, patternWithBottles.PatternWithBottlesId, placeCoordinates, cavePlace);
            }
        }
    }

    private static void updatePatternWithBottles(SQLiteDatabase db, PatternModelWithBottles patternWithBottles) {
        ContentValues patternWithBottlesFields = getPatternWithBottlesFields(patternWithBottles);
        db.update(PATTERN_WITH_BOTTLES_TABLE_NAME, patternWithBottlesFields,
                PATTERN_WITH_BOTTLE_ID + "=?", new String[]{String.valueOf(patternWithBottles.PatternWithBottlesId)});
    }
}
