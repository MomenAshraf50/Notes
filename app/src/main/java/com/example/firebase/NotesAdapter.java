package com.example.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {
    private List<Note> noteList;

    public NotesAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @NotNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotesAdapter.NotesHolder holder, int position) {
        Note note = noteList.get(position);

        holder.textViewTitle.setText(note.getTitle());
        holder.textViewContent.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NotesHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,textViewContent;
        public NotesHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.notes_item_title_tv);
            textViewContent = itemView.findViewById(R.id.notes_item_content_tv);
        }
    }
}
