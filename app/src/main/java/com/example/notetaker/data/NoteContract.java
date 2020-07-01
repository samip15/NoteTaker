package com.example.notetaker.data;

import android.provider.BaseColumns;

public class NoteContract {
    // inner class
    public static class NoteEntry implements BaseColumns{
        // table name
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRIORITY = "priority";
    }
}
