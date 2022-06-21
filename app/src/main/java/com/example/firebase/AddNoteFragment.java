package com.example.firebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class AddNoteFragment extends DialogFragment {
    FirebaseAuth auth =FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextInputEditText editTextTitle,editTextContent;
    Button buttonAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextContent = view.findViewById(R.id.content_et);
        editTextTitle = view.findViewById(R.id.title_et);
        buttonAdd = view.findViewById(R.id.fragment_btn);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUi();
            }
        });


    }

    private void getDataFromUi() {
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        if (title.isEmpty()||content.isEmpty()){
            Toast.makeText(getContext(), "please fill all fields"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        addNoteToCloud(title,content);
    }

    private void addNoteToCloud(String title, String content) {
        String noteId = String.valueOf(System.currentTimeMillis());

        Note note = new Note(noteId,title,content);
        firestore.collection("notes")
                .document(auth.getCurrentUser().getUid())
                .collection("myNotes")
                .document(noteId)
                .set(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Note Added"
                                    , Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}