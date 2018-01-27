package com.apkglobal.helpyou.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.apkglobal.helpyou.R;

public class Instructions extends AppCompatActivity {

    TextView instruct,tv_instruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        instruct=findViewById(R.id.instructions);
        tv_instruct=findViewById(R.id.tv_instructions);
    }
}
