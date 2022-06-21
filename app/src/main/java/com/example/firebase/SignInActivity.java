package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import javax.xml.transform.sax.SAXResult;

public class SignInActivity extends AppCompatActivity {
    EditText editTextEmail,editTextPassword;
    FirebaseAuth auth= FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editTextEmail = findViewById(R.id.sign_in_et_email);
        editTextPassword = findViewById(R.id.sign_in_et_password);
    }

    public void signIn(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please fill all data"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmailValid(email)){
            editTextEmail.setError("the email is invalid");
            return;
        }
        signInWithEmailAndPassword(email,password);
    }

    private void signInWithEmailAndPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Sign In complete"
                            , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                }else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SignInActivity.this, errorMessage
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signUp(View view) {
        startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
    }

    private boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    } 
    }