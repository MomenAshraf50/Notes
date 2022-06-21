package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;


import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private final String PROFILE_IMAGES = "usersProfileImages";
    EditText editTextEmail,editTextPassword,editTextRePassword,editTextUserName,editTextAge,editTextPhoneNumber;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    CircleImageView profileImage;
    Uri profileImageUri=null;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail =findViewById(R.id.sign_up_et_email);
        editTextPassword= findViewById(R.id.sign_up_et_password);
        editTextRePassword = findViewById(R.id.sign_up_et_re_password);
        editTextAge = findViewById(R.id.sign_up_et_age);
        editTextPhoneNumber = findViewById(R.id.sign_up_et_phone_number);
        editTextUserName = findViewById(R.id.sign_up_et_user_name);
        profileImage = findViewById(R.id.profile_image);
    }

    public void signUp(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String rePassword = editTextRePassword.getText().toString();
        String userName = editTextUserName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String age = editTextAge.getText().toString();
        if (profileImageUri == null){
            Toast.makeText(this, "Please select Profile image"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()||password.isEmpty()||rePassword.isEmpty()||userName.isEmpty()||phoneNumber.isEmpty()||age.isEmpty()){
            Toast.makeText(this, "Please fill all data"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmailValid(email)){
            editTextEmail.setError("the email is invalid");
            return;
        }
        if(password.length()<=8){
            editTextPassword.setError("Password must be bigger than eight characters");
            return;
        }
        if (rePassword.length()<=8){
            editTextRePassword.setError("Password must be bigger than eight characters");
            return;
        }

        if (!password.equals(rePassword)){
            editTextRePassword.setError("the two passwords not matching");
            return;
        }
        if(!isPasswordValid(password)){
            editTextPassword.setError("the password you entered is invalid");
            return;
        }
        user = new User();
        user.setUserName(userName);
        user.setPhoneNumber(phoneNumber);
        user.setAge(age);
        createAccountByEmailAndPassword(email,password);
                
    }

    private void createAccountByEmailAndPassword(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            uploadImageProfile();
                        }else {
                            showErrorMessage(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    private boolean isPasswordValid(String password){
        final Pattern PASSWORD_REGEX=Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",Pattern.CASE_INSENSITIVE);
        return PASSWORD_REGEX.matcher(password).matches();

    }

    public void openGallery(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                profileImage.setImageURI(profileImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageProfile(){
        storageReference.child(PROFILE_IMAGES).child(auth.getCurrentUser().getUid())
                .putFile(profileImageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            downloadImageProfileUrl();
                            Toast.makeText(SignUpActivity.this, "profile image Uploaded"
                                    , Toast.LENGTH_SHORT).show();
                        }else{
                            showErrorMessage(Objects.requireNonNull(task.getException()));

                        }
                    }
                });
    }

    private void downloadImageProfileUrl(){
        storageReference.child(PROFILE_IMAGES).child(auth.getCurrentUser().getUid())
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                if (task.isSuccessful()){
                    String imageProfileUrl = task.getResult().toString();
                    user.setProfileImageUrl(imageProfileUrl);

                    setUserDataToCloud();
                }else {
                    showErrorMessage(Objects.requireNonNull(task.getException()));
                }

            }
        });
    }

    private void setUserDataToCloud() {
        firebaseFirestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Account Created"
                                    , Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            showErrorMessage(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private void showErrorMessage(Exception e)
    {
        String errorMessage = e.getMessage();
        Toast.makeText(SignUpActivity.this, errorMessage,
                Toast.LENGTH_SHORT).show();
    }
}
