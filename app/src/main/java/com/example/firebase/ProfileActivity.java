package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CircleImageView circleImageView;
    TextView textViewUserName,textViewPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        circleImageView = findViewById(R.id.profile_image_profile_activity);
        textViewUserName = findViewById(R.id.profile_user_name);
        textViewPhoneNumber = findViewById(R.id.profile_phone_number);

        getUserData();
    }

    private void getUserData() {
        final String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        firestore.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            User user = Objects.requireNonNull(task.getResult()).toObject(User.class);

                            textViewUserName.setText(Objects.requireNonNull(user).getUserName());
                            textViewPhoneNumber.setText(user.getPhoneNumber());

                            Glide.with(ProfileActivity.this)
                                    .load(user.getProfileImageUrl())
                                    .into(circleImageView);
                        }else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void changeProfileImage(View view) {
    }

    public void logOut(View view) {
        auth.signOut();
        startActivity(new Intent(ProfileActivity.this,SignInActivity.class));
    }
}