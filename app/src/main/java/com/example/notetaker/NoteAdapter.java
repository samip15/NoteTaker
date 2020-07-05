package com.example.notetaker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaker.data.NoteContract;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    // variables
    private Context mContex;
    private Cursor mCursor;

    public NoteAdapter(Context mContex) {
        this.mContex = mContex;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.note_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        // index of column
        int idIndex = mCursor.getColumnIndex(NoteContract.NoteEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_DESCRIPTION);
        int priorityIndex = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_PRIORITY);
        mCursor.moveToPosition(position);
        // assigning value
        // tag
        final int id = mCursor.getInt(idIndex);
        holder.itemView.setTag(id);
        // description
        String description = mCursor.getString(descriptionIndex);
        holder.noteDescriptionTv.setText(description);
        // priority
        int priority = mCursor.getInt(priorityIndex);
        String priorityString = "" + priority;
        holder.priorityTv.setText(priorityString);
        // background color of priority
        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityTv.getBackground();
        // set background
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);
    }

    /**
     * Helps To Get Color For Priority
     * @param priority:we get from database
     * @return
     */
    private int getPriorityColor(int priority){
        int priorityColor = 0;
        switch (priority){
            case 1 :priorityColor = ContextCompat.getColor(mContex,R.color.meterialRed);
            break;
            case 2 :priorityColor = ContextCompat.getColor(mContex,R.color.meterialOrange);
                break;
            case 3 :priorityColor = ContextCompat.getColor(mContex,R.color.meterialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }

    /**
     * when data changes and re-query occurs
     * @param c:
     * @return:
     */
    public Cursor swapCursor(Cursor c){
        // new cursor and old cursor is same
        if (mCursor==c){
            // do nothing
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c; // set new cursor
        if (c!=null){
            this.notifyDataSetChanged();
        }
        return temp;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteDescriptionTv, priorityTv;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteDescriptionTv = itemView.findViewById(R.id.taskDescription);
            priorityTv = itemView.findViewById(R.id.prioritytv);
        }
    }
}
