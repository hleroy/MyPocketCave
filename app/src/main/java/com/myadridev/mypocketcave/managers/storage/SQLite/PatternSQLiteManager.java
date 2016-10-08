package com.myadridev.mypocketcave.managers.storage.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PatternSQLiteManager {

    public static final String PATTERNS_TABLE_NAME = "Patterns";
    public static final String PATTERN_PLACES_TABLE_NAME = "Pattern_Places";

    public static final String PATTERN_ID = "PatternId";
    public static final String ROW = "Row";
    public static final String COLUMN = "Column";
    public static final String NUMBER_BOTTLES_BY_COLUMN = "NumberBottlesByColumn";
    public static final String NUMBER_BOTTLES_BY_ROW = "NumberBottlesByRow";
    public static final String IS_HORIZONTALLY_EXPENDABLE = "IsHorizontallyExpendable";
    public static final String IS_VERTICALLY_EXPENDABLE = "IsVerticallyExpendable";
    public static final String IS_INVERTED = "IsInverted";
    public static final String ORDER = "PatternOrder";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(2);
        queries.add("CREATE TABLE " + PATTERNS_TABLE_NAME + " (" + PATTERN_ID + " INTEGER PRIMARY KEY, "
                + PatternTypeSQLiteManager.PATTERN_TYPE_ID + " INTEGER, " + NUMBER_BOTTLES_BY_COLUMN + " INTEGER, "
                + NUMBER_BOTTLES_BY_ROW + " INTEGER, " + IS_HORIZONTALLY_EXPENDABLE + " TINYINT, "
                + IS_VERTICALLY_EXPENDABLE + " TINYINT, " + IS_INVERTED + " TINYINT, " + ORDER + " INTEGER);");
        queries.add("CREATE TABLE " + PATTERN_PLACES_TABLE_NAME + " (" + PATTERN_ID + " INTEGER, " + ROW + " INTEGER, " + COLUMN + " INTEGER, "
                + CavePlaceTypeSQLiteManager.PLACE_TYPE_ID + " INTEGER);");
        return queries;
    }

    public static String getUpgradeQuery(int oldVersion, int newVersion) {
        return null;
    }

    // Insert
    public static int insertPattern(PatternModel pattern) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteWritableDatabase();

        List<PatternModel> existingPatterns = getPatterns();
        int existingPatternId = getExistingPatternId(pattern);
        try {
            db.beginTransaction();
            if (existingPatternId == -1) {
                // create new pattern
                for (PatternModel existingPatternFromDb : existingPatterns) {
                    updatePatternOrder(db, existingPatternFromDb.Id, existingPatternFromDb.Order + 1);
                }
                pattern.Order = 1;
                pattern.Id = insertPattern(db, pattern);
                for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> cavePlaceEntry : pattern.PlaceMap.entrySet()) {
                    CoordinatesModel placeCoordinates = cavePlaceEntry.getKey();
                    CavePlaceTypeEnum placeType = cavePlaceEntry.getValue();
                    insertPatternPlace(db, pattern.Id, placeCoordinates, placeType.Id);
                }
            } else {
                // existing pattern -> update order
                PatternModel existingPattern = null;
                for (PatternModel existingPatternFromDb : existingPatterns) {
                    if (existingPatternFromDb.Id == existingPatternId) {
                        existingPattern = existingPatternFromDb;
                        break;
                    }
                }
                int existingPatternOrder = existingPattern.Order;

                for (PatternModel existingPatternFromDb : existingPatterns) {
                    if (existingPatternFromDb.Order < existingPatternOrder) {
                        updatePatternOrder(db, existingPatternFromDb.Id, existingPatternFromDb.Order + 1);
                    }
                }
                updatePatternOrder(db, existingPattern.Id, 1);
                pattern.Id = existingPatternId;
            }
            db.setTransactionSuccessful();
            return pattern.Id;
        } catch (Exception e) {
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Update
    public static void updatePatternOrder(int patternId, int newOrder) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteWritableDatabase();
        try {
            db.beginTransaction();
            updatePatternOrder(db, patternId, newOrder);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private static void updatePatternOrder(SQLiteDatabase db, int patternId, int newOrder) {
        ContentValues patternFields = new ContentValues(1);
        patternFields.put(ORDER, newOrder);
        db.update(PATTERNS_TABLE_NAME, patternFields, PATTERN_ID + "=?", new String[]{String.valueOf(patternId)});
    }

    // Get
    public static List<PatternModel> getPatterns() {
        List<PatternModel> patterns = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor patternsFromDb = db.rawQuery(getPatternsRawQuery()
                        + " order by p." + PATTERN_ID,
                null);
        if (patternsFromDb != null && patternsFromDb.moveToFirst()) {
            patterns = getPatternsFromDbResults(patternsFromDb);
            patternsFromDb.close();
        }

        Collections.sort(patterns);
        return patterns;
    }

    public static PatternModel getPattern(int patternId) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor patternsFromDb = db.rawQuery(getPatternsRawQuery() + " where p." + PATTERN_ID + " =?", new String[]{String.valueOf(patternId)});

        PatternModel pattern = null;
        if (patternsFromDb != null && patternsFromDb.moveToFirst()) {
            pattern = getPatternsFromDbResults(patternsFromDb).get(0);
            patternsFromDb.close();
        }

        return pattern;
    }

    private static int getExistingPatternId(PatternModel pattern) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor patternIdFromDb = db.query(PATTERNS_TABLE_NAME, new String[]{PATTERN_ID},
                PatternTypeSQLiteManager.PATTERN_TYPE_ID + "=? and " + IS_HORIZONTALLY_EXPENDABLE + "=? and "
                        + IS_VERTICALLY_EXPENDABLE + "=? and " + IS_INVERTED + "=? and "
                        + NUMBER_BOTTLES_BY_ROW + "=? and " + NUMBER_BOTTLES_BY_COLUMN + "=?",
                new String[]{String.valueOf(pattern.Type.Id), String.valueOf(pattern.IsHorizontallyExpendable ? 1 : 0),
                        String.valueOf(pattern.IsVerticallyExpendable ? 1 : 0), String.valueOf(pattern.IsInverted ? 1 : 0),
                        String.valueOf(pattern.NumberBottlesByRow), String.valueOf(pattern.NumberBottlesByColumn)},
                null, null, null, null);

        int existingId = -1;
        if (patternIdFromDb != null && patternIdFromDb.moveToFirst()) {
            existingId = patternIdFromDb.getInt(0);
            patternIdFromDb.close();
        }

        return existingId;
    }

    // Private
    private static int insertPattern(SQLiteDatabase db, PatternModel pattern) {
        ContentValues patternFields = getPatternContentValues(pattern);
        return (int) db.insert(PATTERNS_TABLE_NAME, null, patternFields);
    }

    private static ContentValues getPatternContentValues(PatternModel pattern) {
        ContentValues patternFields = new ContentValues(7);
        patternFields.put(PatternTypeSQLiteManager.PATTERN_TYPE_ID, pattern.Type.Id);
        patternFields.put(NUMBER_BOTTLES_BY_COLUMN, pattern.NumberBottlesByColumn);
        patternFields.put(NUMBER_BOTTLES_BY_ROW, pattern.NumberBottlesByRow);
        patternFields.put(IS_HORIZONTALLY_EXPENDABLE, pattern.IsHorizontallyExpendable ? 1 : 0);
        patternFields.put(IS_VERTICALLY_EXPENDABLE, pattern.IsVerticallyExpendable ? 1 : 0);
        patternFields.put(IS_INVERTED, pattern.IsInverted ? 1 : 0);
        patternFields.put(ORDER, pattern.Order);
        return patternFields;
    }

    private static int insertPatternPlace(SQLiteDatabase db, int patternId, CoordinatesModel placeCoordinates, int placeTypeId) {
        ContentValues patternPlaceFields = getPatternPlacesContentValues(patternId, placeCoordinates, placeTypeId);
        return (int) db.insert(PATTERN_PLACES_TABLE_NAME, null, patternPlaceFields);
    }

    private static ContentValues getPatternPlacesContentValues(int patternId, CoordinatesModel placeCoordinates, int placeTypeId) {
        ContentValues patternPlaceFields = new ContentValues(4);
        patternPlaceFields.put(PATTERN_ID, patternId);
        patternPlaceFields.put(ROW, placeCoordinates.Row);
        patternPlaceFields.put(COLUMN, placeCoordinates.Col);
        patternPlaceFields.put(CavePlaceTypeSQLiteManager.PLACE_TYPE_ID, placeTypeId);
        return patternPlaceFields;
    }

    @NonNull
    private static String getPatternsRawQuery() {
        return "select p." + PatternSQLiteManager.PATTERN_ID + ","
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
                + "pp." + CavePlaceTypeSQLiteManager.PLACE_TYPE_ID
                + " from " + PATTERNS_TABLE_NAME + " p"
                + " join " + PatternSQLiteManager.PATTERN_PLACES_TABLE_NAME + " pp"
                + " on p." + PatternSQLiteManager.PATTERN_ID + " = pp." + PatternSQLiteManager.PATTERN_ID;
    }

    private static List<PatternModel> getPatternsFromDbResults(Cursor patternsInfosFromDb) {
        List<PatternModel> patterns = new ArrayList<>();
        int lastPatternId = -1;
        int maxIndexPatterns = -1;
        do {
            int patternId = patternsInfosFromDb.getInt(0);
            PatternModel currentPattern;
            if (lastPatternId != patternId) {
                PatternModel pattern = new PatternModel();
                pattern.Id = patternId;
                pattern.Type = PatternTypeEnum.getById(patternsInfosFromDb.getInt(1));
                pattern.NumberBottlesByColumn = patternsInfosFromDb.getInt(2);
                pattern.NumberBottlesByRow = patternsInfosFromDb.getInt(3);
                pattern.IsHorizontallyExpendable = patternsInfosFromDb.getInt(4) != 0;
                pattern.IsVerticallyExpendable = patternsInfosFromDb.getInt(5) != 0;
                pattern.IsInverted = patternsInfosFromDb.getInt(6) != 0;
                pattern.Order = patternsInfosFromDb.getInt(7);
                patterns.add(pattern);
                lastPatternId = patternId;
                maxIndexPatterns++;
                currentPattern = pattern;
            } else {
                currentPattern = patterns.get(maxIndexPatterns);
            }
            CoordinatesModel patternPlaceCoordinates = new CoordinatesModel(patternsInfosFromDb.getInt(8), patternsInfosFromDb.getInt(9));
            if (!currentPattern.PlaceMap.containsKey(patternPlaceCoordinates)) {
                currentPattern.PlaceMap.put(patternPlaceCoordinates, CavePlaceTypeEnum.getById(patternsInfosFromDb.getInt(10)));
            }
        } while (patternsInfosFromDb.moveToNext());
        return patterns;
    }
}
