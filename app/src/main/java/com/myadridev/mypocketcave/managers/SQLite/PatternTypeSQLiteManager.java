package com.myadridev.mypocketcave.managers.SQLite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myadridev.mypocketcave.enums.PatternTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class PatternTypeSQLiteManager {

    public static final String PATTERN_TYPES_TABLE_NAME = "PatternTypes";

    public static final String PATTERN_TYPE_ID = "PatternTypeId";
    public static final String STRING_ID = "StringId";
    public static final String DRAWABLE_ID = "DrawableId";

    public static List<String> getCreateTableQuery() {
        List<String> queries = new ArrayList<>(1);
        queries.add("CREATE TABLE " + PATTERN_TYPES_TABLE_NAME + " (" + PATTERN_TYPE_ID + " INTEGER, " + STRING_ID + " INTEGER, " + DRAWABLE_ID + " INTEGER);");
        return queries;
    }

    public static void insertAll(SQLiteDatabase db) {
        for (PatternTypeEnum patternType : PatternTypeEnum.values()) {
            ContentValues fields = new ContentValues();
            fields.put(PATTERN_TYPE_ID, patternType.id);
            fields.put(STRING_ID, patternType.stringResourceId);
            fields.put(DRAWABLE_ID, patternType.drawableResourceId);

            db.insert(PATTERN_TYPES_TABLE_NAME, null, fields);
        }
    }
}
