package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;

import java.util.ArrayList;
import java.util.List;

public class FoodToEatWithSQLiteManager {

    public static final String FOODS_TABLE_NAME = "Foods";

    public static final String FOOD_ID = "FoodId";
    public static final String STRING_ID = "StringId";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(1);
        queries.add("CREATE TABLE " + FOODS_TABLE_NAME + " (" + FOOD_ID + " INTEGER, " + STRING_ID + " INTEGER);");
        return queries;
    }

    public static void insertAll(SQLiteDatabase db) {
        for (FoodToEatWithEnum food : FoodToEatWithEnum.values()) {
            ContentValues fields = new ContentValues();
            fields.put(FOOD_ID, food.id);
            fields.put(STRING_ID, food.stringResourceId);

            db.insert(FOODS_TABLE_NAME, null, fields);
        }
    }
}
