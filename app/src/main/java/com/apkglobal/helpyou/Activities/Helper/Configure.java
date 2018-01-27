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
    public static String profile_name;
    public static String profile_email;
    public static String profile_contact;
    public static String profile_address;
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

    public static String getProfile_name() {
        return profile_name;
    }

    public static void setProfile_name(String profile_name) {
        Configure.profile_name = profile_name;
    }

    public static String getProfile_email() {
        return profile_email;
    }

    public static void setProfile_email(String profile_email) {
        Configure.profile_email = profile_email;
    }

    public static String getProfile_contact() {
        return profile_contact;
    }

    public static void setProfile_contact(String profile_contact) {
        Configure.profile_contact = profile_contact;
    }

    public static String getProfile_address() {
        return profile_address;
    }

    public static void setProfile_address(String profile_address) {
        Configure.profile_address = profile_address;
    }
}
