package com.example.notetaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.notetaker.data.NoteContract;

public class AddNoteActivity extends AppCompatActivity {

    private int mPriority;
    EditText etNoteDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        mPriority = 1;
        ((RadioButton) findViewById(R.id.reButton1)).setChecked(true);
        etNoteDescription = findViewById(R.id.etNoteDescription);
    }

    /**
     * When Priority Button Is Clicked and a new priority is set
     *
     */
    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.reButton1)).isChecked()){
            mPriority = 1;
        }else if (((RadioButton) findViewById(R.id.reButton2)).isChecked()){
            mPriority = 2;
        }else if (((RadioButton) findViewById(R.id.reButton3)).isChecked()){
            mPriority = 3;
        }
    }

    /**
     * Adding new Note TO The Database
     * @param view
     */
    public void onClickAddNote(View view) {
        String note = etNoteDescription.getText().toString();
        if (note.length()==0){
            return;
        }
        ContentValues contentValues = new ContentValues();
        // put data to database
        contentValues.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION,note);
        contentValues.put(NoteContract.NoteEntry.COLUMN_PRIORITY,mPriority);
        // insert
        Uri uri = getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI,contentValues);
        if (uri!=null){
            Toast.makeText(this, "Inserted Value"+uri.toString(), Toast.LENGTH_SHORT).show();
        }
        // after data is added close activity
        finish();
    }
}