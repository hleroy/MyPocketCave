package com.myadridev.mypocketcave.managers.storage.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.enums.WineColorEnum;

import java.util.ArrayList;
import java.util.List;

public class WineColorSQLiteManager {

    public static final String WINE_COLORS_TABLE_NAME = "WineColors";

    public static final String WINE_COLOR_ID = "WineColorId";
    public static final String STRING_ID = "StringId";
    public static final String DRAWABLE_ID = "DrawableId";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(1);
        queries.add("CREATE TABLE " + WINE_COLORS_TABLE_NAME + " (" + WINE_COLOR_ID + " INTEGER, " + STRING_ID + " INTEGER, " + DRAWABLE_ID + " INTEGER);");
        return queries;
    }

    public static void insertAll(SQLiteDatabase db) {
        for (WineColorEnum color : WineColorEnum.values()) {
            ContentValues fields = new ContentValues();
            fields.put(WINE_COLOR_ID, color.id);
            fields.put(STRING_ID, color.stringResourceId);
            fields.put(DRAWABLE_ID, color.drawableResourceId);

            db.insert(WINE_COLORS_TABLE_NAME, null, fields);
        }
    }
}
