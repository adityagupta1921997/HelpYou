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
import android.widget.TextView;
import android.widget.Toast;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    TextView pname,pemail,pcontact,paddress;
    CircleImageView circleImageView;
    Button imagebutton,profile_button;
    private static int PICK_IMAGE_REQUEST = 1;
    Configure configure=new Configure();
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pname=findViewById(R.id.tv_pname);
        pemail=findViewById(R.id.tv_pemail);
        pcontact=findViewById(R.id.tv_pcontact);
        paddress=findViewById(R.id.tv_paddress);
        imagebutton=findViewById(R.id.btn_image);
        circleImageView=findViewById(R.id.p_image);
        profile_button=findViewById(R.id.get_profile);
        circleImageView.setImageBitmap(configure.getImageView());
        rootRef = FirebaseDatabase.getInstance().getReference("users");
        //database reference pointing to demo node
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //userid=rootRef.getKey();

                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Configure configure = dataSnapshot.getValue(Configure.class);
                        pname.setText(configure.profile_name);
                        pemail.setText(configure.profile_email);
                        pcontact.setText(configure.profile_contact);
                        paddress.setText(configure.profile_address);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                circleImageView.setImageBitmap(bitmap);
                configure.setImageView(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

