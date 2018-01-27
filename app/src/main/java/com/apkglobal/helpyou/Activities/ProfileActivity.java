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

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    TextView pname,pemail,pcontact,paddress;
    CircleImageView circleImageView;
    Button imagebutton;
    private static int PICK_IMAGE_REQUEST = 1;
    Configure configure=new Configure();

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
        circleImageView.setImageBitmap(configure.getImageView());
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        pname.setText(configure.getProfile_name());
        pemail.setText(configure.getProfile_email());
        pcontact.setText(configure.getProfile_contact());
        paddress.setText(configure.getProfile_address());
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

