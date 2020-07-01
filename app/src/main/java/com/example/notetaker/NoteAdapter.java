package com.example.notetaker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    // variables
    private Context mContex;
    private Cursor mCursor;

    public NoteAdapter(Context mContex, Cursor mCursor) {
        this.mContex = mContex;
        this.mCursor = mCursor;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.note_layout,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mCursor== null){
            return 0;
        }
        return mCursor.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteDescriptionTv,priorityTv;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteDescriptionTv = itemView.findViewById(R.id.taskDescription);
            priorityTv = itemView.findViewById(R.id.prioritytv);
        }
    }
}
