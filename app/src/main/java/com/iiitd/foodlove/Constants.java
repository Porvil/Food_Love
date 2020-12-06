package com.iiitd.foodlove;

import android.os.Environment;

public class Constants {

    public static final int TIME_DELAY = 1000;
    public static final int TIME_PERIOD = 20000;
    public static final int PERMISSION_ALL = 1000;

    public static final String APP = "Food Love";
    public static final String HERO = "hero";

    public static String getExternalStoragePath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String getAppPath(){
        return getExternalStoragePath() + "/" + APP;
    }

}
