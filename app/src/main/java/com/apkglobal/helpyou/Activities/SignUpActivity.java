package com.apkglobal.helpyou.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.Activities.Helper.ImageInfo;
import com.apkglobal.helpyou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private static EditText fullName, emailId, mobileNumber,
            password;
    private static TextView login;
    private static ImageView imageView;
    private static Button signUpButton,image_button;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    DatabaseReference rootRef,imageReference;
    Uri FilePathUri;
    ImageInfo imageInfo;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;
    String Storage_Path = "All_Image_Uploads/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fullName = findViewById(R.id.fullName);
        emailId = findViewById(R.id.userEmailId);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpBtn);
        image_button=findViewById(R.id.btn_for_image);
        login = findViewById(R.id.already_user);
        imageView=findViewById(R.id.image);
        rootRef = FirebaseDatabase.getInstance().getReference("users");
        imageReference=FirebaseDatabase.getInstance().getReference("image");
        //database reference pointing to demo node
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        login.setTextColor(getResources().getColorStateList(R.color.textview_selector));
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });
        setListeners();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                imageView.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                image_button.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                // Call checkValidation method
                // Get all edittext texts
                String getFullName = fullName.getText().toString().trim();
                String getEmailId = emailId.getText().toString().trim();
                String getMobileNumber = mobileNumber.getText().toString().trim();
                String getPassword = password.getText().toString().trim();

                // Pattern match for email id
       /* Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);
*/
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                // Check if all strings are null or not
                if (getFullName.equals("") || getFullName.length() == 0
                        || getEmailId.equals("") || getEmailId.length() == 0
                        || getMobileNumber.equals("") || getMobileNumber.length() == 0
                        || getPassword.equals("") || getPassword.length() == 0)

                    Toasty.error(getApplicationContext(),"All fields are required.",Toast.LENGTH_SHORT,true).show();

                    // Check if email id valid or not
              else  if (!getEmailId.matches(emailPattern))
                    Toasty.error(getApplicationContext(),"Your Email Id is Invalid.",Toast.LENGTH_SHORT,true).show();

                else if (getPassword.length() < 6) {
                    Toasty.error(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT,true).show();
                }

                else if(FilePathUri==null)
                {
                    Toasty.error(getApplicationContext(),"Please Select Image",Toast.LENGTH_SHORT,true).show();
                }

                    // Else do signup or do your stuff


                else {
                progressBar.setVisibility(View.VISIBLE);
                    StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                    storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           imageInfo=new ImageInfo(taskSnapshot.getDownloadUrl().toString());


                            imageReference.setValue(imageInfo);

                            // Adding image upload id s child element into databaseReference.

                            Toasty.info(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG,true).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Showing exception erro message.
                            Toasty.error(SignUpActivity.this, "Image Not Uploaded", Toast.LENGTH_LONG,true).show();
                        }
                    });
                    //create user
                auth.createUserWithEmailAndPassword(getEmailId, getPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        Toasty.info(SignUpActivity.this, "Authentication done.", Toast.LENGTH_SHORT,true).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toasty.info(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT,true).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            startActivity(new Intent(SignUpActivity.this, NavigationActivity.class));
                            finish();
                        }

                    }
                });
                Configure configure=new Configure(getFullName,getEmailId,getMobileNumber);
                rootRef.setValue(configure);
            }

                break;

            case R.id.already_user:
                Intent login=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(login);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}

