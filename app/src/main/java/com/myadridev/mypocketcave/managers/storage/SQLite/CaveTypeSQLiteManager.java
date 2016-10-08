package com.myadridev.mypocketcave.managers.storage.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.enums.CaveTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class CaveTypeSQLiteManager {

    public static final String CAVE_TYPE_TABLE_NAME = "CaveTypes";

    public static final String CAVE_TYPE_ID = "CaveTypeId";
    public static final String STRING_ID = "StringId";
    public static final String DRAWABLE_ID = "DrawableId";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(1);
        queries.add("CREATE TABLE " + CAVE_TYPE_TABLE_NAME + " (" + CAVE_TYPE_ID + " INTEGER, " + STRING_ID + " INTEGER, " + DRAWABLE_ID + " INTEGER);");
        return queries;
    }

    public static void insertAll(SQLiteDatabase db) {
        for (CaveTypeEnum caveType : CaveTypeEnum.values()) {
            ContentValues fields = new ContentValues();
            fields.put(CAVE_TYPE_ID, caveType.Id);
            fields.put(STRING_ID, caveType.StringResourceId);
            fields.put(DRAWABLE_ID, caveType.DrawableResourceId);

            db.insert(CAVE_TYPE_TABLE_NAME, null, fields);
        }
    }
}
