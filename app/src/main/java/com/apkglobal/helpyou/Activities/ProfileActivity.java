package com.apkglobal.helpyou.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.Activities.Helper.ImageInfo;
import com.apkglobal.helpyou.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    TextView pname,pemail,pcontact;
    CircleImageView circleImageView;
    Button imagebutton,profile_button;
    private static int PICK_IMAGE_REQUEST = 1;
    DatabaseReference rootRef,imageReference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pname=findViewById(R.id.tv_pname);
        pemail=findViewById(R.id.tv_pemail);
        pcontact=findViewById(R.id.tv_pcontact);
        circleImageView=findViewById(R.id.p_image);
        profile_button=findViewById(R.id.get_profile);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rootRef = FirebaseDatabase.getInstance().getReference("users");
        imageReference=FirebaseDatabase.getInstance().getReference("image");
        //database reference pointing to demo node
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //userid=rootRef.getKey();
                //progressBar.setVisibility(View.VISIBLE);

                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Configure configure = dataSnapshot.getValue(Configure.class);
                        pname.setText(configure.profile_name);
                        pemail.setText(configure.profile_email);
                        pcontact.setText(configure.profile_contact);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                imageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            ImageInfo imageUploadInfo = dataSnapshot.getValue(ImageInfo.class);
                            Glide.with(getApplicationContext()).load(imageUploadInfo.url).into(circleImageView);
                  //          progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}

