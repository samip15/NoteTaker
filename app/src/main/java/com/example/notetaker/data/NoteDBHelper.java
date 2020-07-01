package com.example.notetaker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteDBHelper extends SQLiteOpenHelper {
    // Database Helper /Version
    private static final String DATABASE_NAME  = "noteDB.db";
    private static final int DATABASE_VERSION  = 1;
    public NoteDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + "(" +
                NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY, " +
                NoteContract.NoteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NoteContract.NoteEntry.COLUMN_PRIORITY + " INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ NoteContract.NoteEntry.TABLE_NAME);
        onCreate(db);
    }
}
