package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.models.BottleModel;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottleSQLiteManager {

    // SQLite
    public static final String BOTTLES_TABLE_NAME = "Bottles";
    public static final String BOTTLES_FOOD_TABLE_NAME = "Bottles_Foods";

    public static final String BOTTLE_ID = "BottleId";
    public static final String NAME = "Name";
    public static final String MILLESIME = "Millesime";
    public static final String DOMAIN = "Domain";
    public static final String COMMENTS = "Comments";
    public static final String PERSON_TO_SHARE_WITH = "PersonToShareWith";
    public static final String STOCK = "Stock";
    public static final String NUMBER_PLACED = "NumberPlaced";

    @NonNull
    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(2);
        queries.add("CREATE TABLE " + BOTTLES_TABLE_NAME + " (" + BOTTLE_ID + " INTEGER PRIMARY KEY," +
                " " + NAME + " TEXT, " + MILLESIME + " INTEGER, " + DOMAIN + " TEXT, " + COMMENTS + " TEXT, " + PERSON_TO_SHARE_WITH + " TEXT," +
                " " + WineColorSQLiteManager.WINE_COLOR_ID + " INTEGER, " + STOCK + " INTEGER, " + NUMBER_PLACED + " INTEGER);");
        queries.add("CREATE TABLE " + BOTTLES_FOOD_TABLE_NAME + " (" + BOTTLE_ID + " INTEGER, " + FoodToEatWithSQLiteManager.FOOD_ID + " INTEGER);");
        return queries;
    }

    public static String getUpgradeQuery(int oldVersion, int newVersion) {
        return null;
    }

    // Insert
    public static int insertBottle(BottleModel bottle) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            db.beginTransaction();
            bottle.Id = insertBottle(db, bottle);

            for (FoodToEatWithEnum food : bottle.FoodToEatWithList) {
                insertBottleFood(db, bottle.Id, food);
            }

            db.setTransactionSuccessful();
            return bottle.Id;
        } catch (Exception e) {
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Update
    public static void updateBottle(BottleModel bottle) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            db.beginTransaction();
            updateBottle(db, bottle);

            Cursor bottleFoodsFromDb = db.query(BOTTLES_FOOD_TABLE_NAME, getBottlesFoodsColumns(), BOTTLE_ID + "=?", new String[]{String.valueOf(bottle.Id)}, null, null, null, null);

            if (bottleFoodsFromDb != null && bottleFoodsFromDb.moveToFirst()) {
                List<FoodToEatWithEnum> oldFoodToEatWithList = new ArrayList<>();
                do {
                    oldFoodToEatWithList.add(FoodToEatWithEnum.getById(bottleFoodsFromDb.getInt(1)));
                } while (bottleFoodsFromDb.moveToNext());
                bottleFoodsFromDb.close();
                for (FoodToEatWithEnum oldFood : oldFoodToEatWithList) {
                    if (!bottle.FoodToEatWithList.contains(oldFood)) {
                        deleteBottleFood(db, bottle.Id, oldFood.id);
                    }
                }

                for (FoodToEatWithEnum food : bottle.FoodToEatWithList) {
                    if (!oldFoodToEatWithList.contains(food)) {
                        insertBottleFood(db, bottle.Id, food);
                    }
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void updateNumberPlaced(int bottleId, int increment) {
        SQLiteDatabase readableDb = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor bottlesPlacedNumberFromDb = readableDb.query(BOTTLES_TABLE_NAME, new String[]{NUMBER_PLACED}, BOTTLE_ID + "=?", new String[]{String.valueOf(bottleId)}, null, null, null);

        int oldNumberPlaced = 0;
        if (bottlesPlacedNumberFromDb != null && bottlesPlacedNumberFromDb.moveToFirst()) {
            oldNumberPlaced = bottlesPlacedNumberFromDb.getInt(0);
            bottlesPlacedNumberFromDb.close();
        }

        SQLiteDatabase writableDb = SQLiteManager.Instance.getSQLiteWritableDatabase();
        ContentValues updatedFields = new ContentValues(1);
        updatedFields.put(NUMBER_PLACED, oldNumberPlaced + increment);
        writableDb.update(BOTTLES_TABLE_NAME, updatedFields, BOTTLE_ID + "=?", new String[]{String.valueOf(bottleId)});

        writableDb.close();
    }

    // Get
    public static List<BottleModel> getBottles() {
        List<BottleModel> bottles = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor bottlesFromDb = db.rawQuery(getBottlesRawQuery()
                        + " order by b." + BOTTLE_ID,
                null);
        if (bottlesFromDb != null && bottlesFromDb.moveToFirst()) {
            bottles = getBottlesFromDbResults(bottlesFromDb);
            bottlesFromDb.close();
        }

        Collections.sort(bottles);
        return bottles;
    }

    public static int getBottlesCount() {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        int count = 0;
        Cursor bottleCountFromDb = db.rawQuery("select sum(" + STOCK + ") from " + BOTTLES_TABLE_NAME + ";", null);
        if (bottleCountFromDb != null && bottleCountFromDb.moveToFirst()) {
            count = bottleCountFromDb.getInt(0);
            bottleCountFromDb.close();
        }

        return count;
    }

    public static int getBottlesCount(int wineColorId) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        int count = 0;
        Cursor bottleCountFromDb = db.rawQuery("select sum(" + STOCK + ") from " + BOTTLES_TABLE_NAME + " where " + WineColorSQLiteManager.WINE_COLOR_ID + " =?;",
                new String[]{String.valueOf(wineColorId)});
        if (bottleCountFromDb != null && bottleCountFromDb.moveToFirst()) {
            count = bottleCountFromDb.getInt(0);
            bottleCountFromDb.close();
        }

        return count;
    }

    public static BottleModel getBottle(int bottleId) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        String query = getBottlesRawQuery() + " where b." + BOTTLE_ID + "=?";
        Cursor bottleFromDb = db.rawQuery(query, new String[]{String.valueOf(bottleId)});

        BottleModel bottle = null;
        if (bottleFromDb != null && bottleFromDb.moveToFirst()) {
            bottle = getBottlesFromDbResults(bottleFromDb).get(0);
            bottleFromDb.close();
        }

        return bottle;
    }

    public static int getExistingBottleId(int id, String name, String domain, int wineColorId, int millesime) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor bottleIdFromDb = db.query(BOTTLES_TABLE_NAME, new String[]{BOTTLE_ID},
                NAME + "=? and " + DOMAIN + "=? and " + WineColorSQLiteManager.WINE_COLOR_ID + "=? and " + MILLESIME + "=?",
                new String[]{name, domain, String.valueOf(wineColorId), String.valueOf(millesime)}, null, null, null, null);

        int existingId = -1;
        if (bottleIdFromDb != null && bottleIdFromDb.moveToFirst()) {
            existingId = bottleIdFromDb.getInt(0);
            bottleIdFromDb.close();
        }

        return existingId != id ? existingId : -1;
    }

    @NonNull
    public static String[] getDistinctPersons() {
        List<String> distinctPersons = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor personsFromDb = db.query(true, BOTTLES_TABLE_NAME, new String[]{PERSON_TO_SHARE_WITH}, null, null, null, null, null, null);
        if (personsFromDb != null && personsFromDb.moveToFirst()) {
            do {
                String person = personsFromDb.getString(0);
                if (person != null && !person.isEmpty()) {
                    distinctPersons.add(person);
                }
            } while (personsFromDb.moveToNext());
            personsFromDb.close();
        }

        Collections.sort(distinctPersons);
        return distinctPersons.toArray(new String[distinctPersons.size()]);
    }

    @NonNull
    public static String[] getDistinctDomains() {
        List<String> distinctDomains = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor domainsFromDb = db.query(true, BOTTLES_TABLE_NAME, new String[]{DOMAIN}, null, null, null, null, null, null);
        if (domainsFromDb != null && domainsFromDb.moveToFirst()) {
            do {
                distinctDomains.add(domainsFromDb.getString(0));
            } while (domainsFromDb.moveToNext());
            domainsFromDb.close();
        }

        Collections.sort(distinctDomains);
        return distinctDomains.toArray(new String[distinctDomains.size()]);
    }

    public static List<BottleModel> getNonPlacedBottles() {
        List<BottleModel> nonPlacedBottles = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        Cursor bottlesAndFoodFromDb = db.rawQuery(getBottlesRawQuery()
                        + " where (b." + STOCK + " - b." + NUMBER_PLACED + ") > 0"
                        + " order by b." + BOTTLE_ID,
                null);
        if (bottlesAndFoodFromDb != null && bottlesAndFoodFromDb.moveToFirst()) {
            nonPlacedBottles = getBottlesFromDbResults(bottlesAndFoodFromDb);
            bottlesAndFoodFromDb.close();
        }

        Collections.sort(nonPlacedBottles);
        return nonPlacedBottles;
    }

    public static List<BottleModel> getSuggestBottles(SuggestBottleCriteria searchCriteria) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteReadableDatabase();

        List<String> arguments = new ArrayList<>();
        String whereClause = getWhereClauseForSuggestion(searchCriteria, arguments);

        Cursor bottlesAndFoodFromDb = db.rawQuery(getBottlesRawQuery() + (!whereClause.isEmpty() ? " where " + whereClause : "")
                + " order by b." + BOTTLE_ID, !whereClause.isEmpty() ? arguments.toArray(new String[arguments.size()]) : null);

        List<BottleModel> suggestedBottlesFromDb = new ArrayList<>();
        if (bottlesAndFoodFromDb != null && bottlesAndFoodFromDb.moveToFirst()) {
            suggestedBottlesFromDb = getBottlesFromDbResults(bottlesAndFoodFromDb);
            bottlesAndFoodFromDb.close();
        }

        return suggestedBottlesFromDb;
    }

    // Delete
    public static void deleteBottle(int bottleId) {
        SQLiteDatabase db = SQLiteManager.Instance.getSQLiteWritableDatabase();

        try {
            db.beginTransaction();
            deleteBottleFood(db, bottleId);
            deleteBottle(db, bottleId);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Private
    private static int insertBottle(SQLiteDatabase db, BottleModel bottle) {
        ContentValues bottleFields = getBottleContentValues(bottle);
        return (int) db.insert(BOTTLES_TABLE_NAME, null, bottleFields);
    }

    private static void updateBottle(SQLiteDatabase db, BottleModel bottle) {
        ContentValues bottleFields = getBottleContentValues(bottle);
        db.update(BOTTLES_TABLE_NAME, bottleFields, BOTTLE_ID + "=?", new String[]{String.valueOf(bottle.Id)});
    }

    @NonNull
    private static ContentValues getBottleContentValues(BottleModel bottle) {
        ContentValues bottleFields = new ContentValues(8);
        bottleFields.put(NAME, bottle.Name);
        bottleFields.put(MILLESIME, bottle.Millesime);
        bottleFields.put(DOMAIN, bottle.Domain);
        bottleFields.put(COMMENTS, bottle.Comments);
        bottleFields.put(PERSON_TO_SHARE_WITH, bottle.PersonToShareWith);
        bottleFields.put(WineColorSQLiteManager.WINE_COLOR_ID, bottle.WineColor.id);
        bottleFields.put(STOCK, bottle.Stock);
        bottleFields.put(NUMBER_PLACED, bottle.NumberPlaced);
        return bottleFields;
    }

    @NonNull
    private static String getBottlesRawQuery() {
        return "select b." + BOTTLE_ID + ","
                + "b." + NAME + ","
                + "b." + MILLESIME + ","
                + "b." + DOMAIN + ","
                + "b." + COMMENTS + ","
                + "b." + PERSON_TO_SHARE_WITH + ","
                + "b." + WineColorSQLiteManager.WINE_COLOR_ID + ","
                + "b." + STOCK + ","
                + "b." + NUMBER_PLACED + ","
                + "bf." + FoodToEatWithSQLiteManager.FOOD_ID
                + " from " + BOTTLES_TABLE_NAME + " b"
                + " left join " + BOTTLES_FOOD_TABLE_NAME + " bf"
                + " on bf." + BOTTLE_ID + " = b." + BOTTLE_ID;
    }

    private static List<BottleModel> getBottlesFromDbResults(Cursor bottlesAndFoodFromDb) {
        List<BottleModel> bottles = new ArrayList<>();
        int lastBottleId = -1;
        int maxIndexBottles = -1;
        do {
            int bottleId = bottlesAndFoodFromDb.getInt(0);
            BottleModel currentBottle;
            if (lastBottleId != bottleId) {
                BottleModel bottle = new BottleModel();
                bottle.Id = bottleId;
                bottle.Name = bottlesAndFoodFromDb.getString(1);
                bottle.Millesime = bottlesAndFoodFromDb.getInt(2);
                bottle.Domain = bottlesAndFoodFromDb.getString(3);
                bottle.Comments = bottlesAndFoodFromDb.getString(4);
                bottle.PersonToShareWith = bottlesAndFoodFromDb.getString(5);
                bottle.WineColor = WineColorEnum.getById(bottlesAndFoodFromDb.getInt(6));
                bottle.Stock = bottlesAndFoodFromDb.getInt(7);
                bottle.NumberPlaced = bottlesAndFoodFromDb.getInt(8);
                bottles.add(bottle);
                maxIndexBottles++;
                lastBottleId = bottleId;
                currentBottle = bottle;
            } else {
                currentBottle = bottles.get(maxIndexBottles);
            }
            String foodId = bottlesAndFoodFromDb.getString(9);
            if (foodId != null && !foodId.isEmpty()) {
                currentBottle.FoodToEatWithList.add(FoodToEatWithEnum.getById(Integer.parseInt(foodId)));
            }
        } while (bottlesAndFoodFromDb.moveToNext());
        return bottles;
    }

    private static void deleteBottle(SQLiteDatabase db, int bottleId) {
        db.delete(BOTTLES_TABLE_NAME, BOTTLE_ID + "=?", new String[]{String.valueOf(bottleId)});
    }

    private static void insertBottleFood(SQLiteDatabase db, int bottleId, FoodToEatWithEnum food) {
        ContentValues bottleFoodFields = getBottleFoodContentValues(bottleId, food);
        db.insert(BOTTLES_FOOD_TABLE_NAME, null, bottleFoodFields);
    }

    @NonNull
    private static ContentValues getBottleFoodContentValues(int bottleId, FoodToEatWithEnum food) {
        ContentValues bottleFoodFields = new ContentValues(2);
        bottleFoodFields.put(BOTTLE_ID, bottleId);
        bottleFoodFields.put(FoodToEatWithSQLiteManager.FOOD_ID, food.id);
        return bottleFoodFields;
    }

    @NonNull
    private static String[] getBottlesFoodsColumns() {
        return new String[]{BOTTLE_ID, FoodToEatWithSQLiteManager.FOOD_ID};
    }

    private static void deleteBottleFood(SQLiteDatabase db, int bottleId) {
        db.delete(BOTTLES_FOOD_TABLE_NAME, BOTTLE_ID + "=?", new String[]{String.valueOf(bottleId)});
    }

    private static void deleteBottleFood(SQLiteDatabase db, int bottleId, int oldFoodId) {
        db.delete(BOTTLES_FOOD_TABLE_NAME, BOTTLE_ID + "=? and " + FoodToEatWithSQLiteManager.FOOD_ID + "=?",
                new String[]{String.valueOf(bottleId), String.valueOf(oldFoodId)});
    }

    @NonNull
    private static String getWhereClauseForSuggestion(SuggestBottleCriteria searchCriteria, List<String> arguments) {
        StringBuilder whereClauseBuilder = new StringBuilder();
        boolean isFirst = true;
        if (searchCriteria.IsWineColorRequired) {
            whereClauseBuilder.append("b." + WineColorSQLiteManager.WINE_COLOR_ID + "=?");
            arguments.add(String.valueOf(searchCriteria.WineColor.id));
            isFirst = false;
        }
        if (searchCriteria.IsMillesimeRequired) {
            int millesimeMin = -1;
            int millesimeMax = -1;
            switch (searchCriteria.Millesime) {
                case LESS_THAN_TWO:
                    millesimeMin = 0;
                    millesimeMax = 2;
                    break;
                case THREE_TO_FIVE:
                    millesimeMin = 3;
                    millesimeMax = 5;
                    break;
                case SIX_TO_TEN:
                    millesimeMin = 6;
                    millesimeMax = 10;
                    break;
                case OVER_TEN:
                    millesimeMin = 10;
                    break;
            }

            if (millesimeMin > -1) {
                if (!isFirst) {
                    whereClauseBuilder.append(" and ");
                }
                whereClauseBuilder.append("b." + MILLESIME + ">=?");
                arguments.add(String.valueOf(millesimeMin));
                isFirst = false;
            }

            if (millesimeMax > -1) {
                if (!isFirst) {
                    whereClauseBuilder.append(" and ");
                }
                whereClauseBuilder.append("b." + MILLESIME + "<=?");
                arguments.add(String.valueOf(millesimeMax));
                isFirst = false;
            }
        }
        if (searchCriteria.IsPersonRequired) {
            if (!isFirst) {
                whereClauseBuilder.append(" and ");
            }
            whereClauseBuilder.append("b." + PERSON_TO_SHARE_WITH + "=?");
            arguments.add(searchCriteria.PersonToShareWith);
            isFirst = false;
        }
        if (searchCriteria.IsDomainRequired) {
            if (!isFirst) {
                whereClauseBuilder.append(" and ");
            }
            whereClauseBuilder.append("b." + DOMAIN + "=?");
            arguments.add(searchCriteria.Domain);
        }
        if (searchCriteria.IsFoodRequired) {
            if (!isFirst) {
                whereClauseBuilder.append(" and ");
            }
            whereClauseBuilder.append("bf." + FoodToEatWithSQLiteManager.FOOD_ID + " IN (");
            boolean isFirstFood = true;
            for (FoodToEatWithEnum food : searchCriteria.FoodToEatWithList) {
                if (!isFirstFood) {
                    whereClauseBuilder.append(", ");
                }
                whereClauseBuilder.append("?");
                arguments.add(String.valueOf(food.id));
                isFirstFood = false;
            }
            whereClauseBuilder.append(")");
        }
        return whereClauseBuilder.toString();
    }
}
