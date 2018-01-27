package com.apkglobal.helpyou.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apkglobal.helpyou.Activities.Coordinator_tab.DataHolder;
import com.apkglobal.helpyou.Activities.Coordinator_tab.MyPageAdapter;
import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.Activities.Helper.Helper;
import com.apkglobal.helpyou.Activities.Helper.Shared;
import com.apkglobal.helpyou.R;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import es.dmoral.toasty.Toasty;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Shared shared;
    private CoordinatorTabLayout mCoordinatorTabLayout;
    private int[]  mColorArray,mImageArray;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"Errands", "Events", "Healthcare", "Housekeeping"};
    private ViewPager mViewPager;

    //JSONObject response, profile_pic_data, profile_pic_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared=new Shared(getApplicationContext());
        shared.withoutlogin();
        setContentView(R.layout.activity_navigation);
       /* Intent intent = getIntent();
        String jsondata = intent.getStringExtra("userProfile");
        ImageView user_picture = (ImageView) findViewById(R.id.imageView);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* try {
            response = new JSONObject(jsondata);
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(user_picture);

        } catch(Exception e){
            e.printStackTrace();
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(NavigationActivity.this, "Give Your feedback after using this app!!", Toast.LENGTH_LONG,true).show();
                Intent feedback=new Intent(Intent.ACTION_VIEW);
                feedback.setData(Uri.parse("https://play.google.com"));
                startActivity(feedback);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Helper h=new Helper(this);
        h.checkLogin();
        initFragments();
        initViewPager();
        mImageArray = new int[]{
                R.drawable.errands,
                R.drawable.events,
                R.drawable.healthcare,
                R.drawable.housekeeping};
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};

        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTranslucentStatusBar(this)
                .setTitle("")
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        for (String title : mTitles) {
            mFragments.add(DataHolder.getInstance(title));
        }
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            new Helper(this).logout();
            startLogin();
            Toasty.success(getApplicationContext(), "Logout...", Toast.LENGTH_SHORT,true).show();

        }

        return true;
    }

    private void startLogin() {
        Intent toLogin=new Intent(NavigationActivity.this,LoginActivity.class);
        startActivity(toLogin);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_payment) {

        }
        else if(id==R.id.nav_instruction)
        {
           Intent instruct=new Intent(NavigationActivity.this,Instructions.class);
           startActivity(instruct);
        }
            else if (id == R.id.nav_share) {
            Intent share=new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com");
            share.setType("text/plain");
            startActivity(Intent.createChooser(share,"Share App Via"));

        } else if (id == R.id.nav_profile) {

            Intent profile=new Intent(NavigationActivity.this,ProfileActivity.class);
            startActivity(profile);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
