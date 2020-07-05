package com.example.notetaker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteContentProvider extends ContentProvider {
    //=================URI=============================
    // uri:directory for single item and notes
    public static final int NOTES = 100;
    public static final int NOTES_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = builduriMatcher();

    /**
     * Helps to find the associated Uri and checks if it is exists or not
     *
     * @return
     */
    public static UriMatcher builduriMatcher() {
        // initialize
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // adding two uri
        uriMatcher.addURI(NoteContract.AUTHORITY, NoteContract.PATH_NOTES, NOTES);
        uriMatcher.addURI(NoteContract.AUTHORITY, NoteContract.PATH_NOTES + "/#", NOTES_WITH_ID);
        return uriMatcher;
    }

    //=============================DB :CONTENT PROVIDER================
    // database helper
    private NoteDBHelper noteDBHelper;

    @Override
    public boolean onCreate() {
        noteDBHelper = new NoteDBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = noteDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case NOTES:
                retCursor = db.query(NoteContract.NoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI"+uri);
        }
         retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // database access
        final SQLiteDatabase db = noteDBHelper.getWritableDatabase();
        // match anusar cursor ra data pathuxa
        int match = sUriMatcher.match(uri);
        // Uri Return Garne
        Uri returnUri;
        switch (match) {
            case NOTES:
                long id = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(NoteContract.NoteEntry.CONTENT_URI, id);

                } else {
                    throw new SQLException("Failed To Insert New Row Into" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        // notify content resolver about new data insertion
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // database access
        final SQLiteDatabase db = noteDBHelper.getWritableDatabase();
        // match anusar cursor ra data pathuxa
        int match = sUriMatcher.match(uri);
        // delete int
        int noteDeleted;
        switch (match) {
            case NOTES_WITH_ID:
                String id = uri.getPathSegments().get(1);
               noteDeleted = db.delete(NoteContract.NoteEntry.TABLE_NAME,"_id=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        // notify content resolver about new data insertion
        if (noteDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return noteDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
