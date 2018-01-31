package com.apkglobal.helpyou.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        fullName = findViewById(R.id.fullName);
        emailId = findViewById(R.id.userEmailId);
        mobileNumber = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUpBtn);
        login = findViewById(R.id.already_user);
        terms_conditions = findViewById(R.id.terms_conditions);
        rootRef = FirebaseDatabase.getInstance().getReference("users");
        //database reference pointing to demo node
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        login.setTextColor(getResources().getColorStateList(R.color.textview_selector));
        terms_conditions.setTextColor(getResources().getColorStateList(R.color.textview_selector));
        setListeners();

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
                String getLocation = location.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                String getConfirmPassword = confirmPassword.getText().toString().trim();

                // Pattern match for email id
       /* Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);
*/
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                // Check if all strings are null or not
                if (getFullName.equals("") || getFullName.length() == 0
                        || getEmailId.equals("") || getEmailId.length() == 0
                        || getMobileNumber.equals("") || getMobileNumber.length() == 0
                        || getLocation.equals("") || getLocation.length() == 0
                        || getPassword.equals("") || getPassword.length() == 0
                        || getConfirmPassword.equals("")
                        || getConfirmPassword.length() == 0)

                    Toasty.error(getApplicationContext(),"All fields are required.",Toast.LENGTH_SHORT,true).show();

                    // Check if email id valid or not
              else  if (!getEmailId.matches(emailPattern))
                    Toasty.error(getApplicationContext(),"Your Email Id is Invalid.",Toast.LENGTH_SHORT,true).show();

                    // Check if both password should be equal
               else if (!getConfirmPassword.equals(getPassword))
                    Toasty.error(getApplicationContext(),"Both password doesn't match.",Toast.LENGTH_SHORT,true).show();

                else if (getPassword.length() < 6) {
                    Toasty.error(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT,true).show();
                }
                // Make sure user should check Terms and Conditions checkbox
                else if (!terms_conditions.isChecked())
                    Toasty.error(getApplicationContext(),"Please select Terms and Conditions.",Toast.LENGTH_SHORT,true).show();

                    // Else do signup or do your stuff


                else {
                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(getEmailId, getPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignUpActivity.this, NavigationActivity.class));
                            finish();
                        }

                    }
                });
                Configure configure=new Configure(getFullName,getEmailId,getMobileNumber,getLocation);
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

