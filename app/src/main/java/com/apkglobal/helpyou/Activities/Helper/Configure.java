package com.apkglobal.helpyou.Activities.Helper;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Mayank on 1/14/2018.
 */

public class Configure {
    public static ArrayList<Fragment> test_fragment;

    public static ArrayList<Fragment> getTest_fragment() {
        return test_fragment;
    }

    public static void setTest_fragment(ArrayList<Fragment> test_fragment) {
        Configure.test_fragment = test_fragment;
    }
}
