package com.example.notetaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notetaker.data.NoteContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    RecyclerView mRecyclerView;
    NoteAdapter mAdapter;
    // loader id
    private static final int NOTE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //rv
        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        //swipe function
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                // uri to delete
                String stringId = Integer.toString(id);
                Uri uri = NoteContract.NoteEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                // delete operation
                getContentResolver().delete(uri,null,null);
                // restart and get new data
                getSupportLoaderManager().restartLoader(NOTE_LOADER_ID,null,MainActivity.this);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                Paint p  = new Paint();
                // red color
                p.setARGB(255,255,0,0);
                if (dX>0){
                    // Right swipe
                    c.drawRect(itemView.getLeft(),itemView.getTop(),dX,itemView.getBottom(),p);
                }else{
                    // left swipe
                    c.drawRect(itemView.getRight()+dX,itemView.getTop(),itemView.getRight(),itemView.getBottom(),p);
                }

                new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.my_background))
                        .addActionIcon(R.drawable.delete_ic)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecyclerView);
        // floating to add new task
        FloatingActionButton fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(addNoteIntent);
            }
        });
        getSupportLoaderManager().initLoader(NOTE_LOADER_ID, null, this);
    }

    // --------------------------ACTIVITY LIFECYCLE--------------------

    @Override
    protected void onResume() {
        super.onResume();
        // restart or reaccessed new data
        getSupportLoaderManager().restartLoader(NOTE_LOADER_ID, null, this);
    }


    // -----------------------LOADERS----------------------------------

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            // data hold
            Cursor mNoatData = null;

            @Override
            protected void onStartLoading() {
                if (mNoatData != null) {
                    // cache old data direct passed
                    deliverResult(mNoatData);
                } else {
                    // new data loads
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                  return   getContentResolver().query(NoteContract.NoteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            NoteContract.NoteEntry.COLUMN_PRIORITY);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable Cursor data) {
                mNoatData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // swap cursor display new data
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}