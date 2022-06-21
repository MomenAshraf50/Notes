 package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<Note> noteList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (auth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(MainActivity.this,SignInActivity.class));
        }
        recyclerView =findViewById(R.id.main_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        notesAdapter = new NotesAdapter(noteList);
        recyclerView.setAdapter(notesAdapter);
        
        getNotesFromCloud();

    }

    private void getNotesFromCloud() {
        firestore.collection("notes")
                .document(auth.getCurrentUser().getUid())
                .collection("myNotes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        noteList.clear();
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            Note note = snapshot.toObject(Note.class);
                            noteList.add(note);

                        }
                        notesAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;
            case R.id.menu_item_add:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(new AddNoteFragment(),"AddNoteFragment")
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}