package com.myadridev.mypocketcave.managers.storage.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class CavePlaceTypeSQLiteManager {

    public static final String PLACE_TYPE_TABLE_NAME = "PlaceTypes";

    public static final String PLACE_TYPE_ID = "PlaceTypeId";
    public static final String DRAWABLE_ID = "DrawableId";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(1);
        queries.add("CREATE TABLE " + PLACE_TYPE_TABLE_NAME + " (" + PLACE_TYPE_ID + " INTEGER, " + DRAWABLE_ID + " INTEGER);");
        return queries;
    }

    public static void insertAll(SQLiteDatabase db) {
        for (CavePlaceTypeEnum cavePlaceType : CavePlaceTypeEnum.values()) {
            ContentValues fields = new ContentValues();
            fields.put(PLACE_TYPE_ID, cavePlaceType.id);
            fields.put(DRAWABLE_ID, cavePlaceType.drawableResourceId);

            db.insert(PLACE_TYPE_TABLE_NAME, null, fields);
        }
    }
}
