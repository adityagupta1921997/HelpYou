package com.apkglobal.helpyou.Activities.Helper;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mayank on 1/14/2018.
 */

public class Configure {

    public static String fetch_city;
    public String profile_name;
    public String profile_email;
    public String profile_contact;
    public String profile_address;
    public static Bitmap imageView;

    public static Bitmap getImageView() {
        return imageView;
    }

    public static void setImageView(Bitmap imageView) {
        Configure.imageView = imageView;
    }

    public static String getFetch_city() {
        return fetch_city;
    }

    public static void setFetch_city(String fetch_city) {
        Configure.fetch_city = fetch_city;
    }

    public Configure()
    {

    }
    public Configure(String name,String email,String contact,String address)
    {
        this.profile_name=name;
        this.profile_email=email;
        this.profile_contact=contact;
        this.profile_address=address;
    }


}
