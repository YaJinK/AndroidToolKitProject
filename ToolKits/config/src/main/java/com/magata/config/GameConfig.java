package com.magata.config;

import android.app.Activity;

import com.magata.utility.Utility;

public class GameConfig {
    public static boolean appRunning = false;
    public static String version = "1.0.0";
    public static String appId = null;
    public static void init(Activity activity) {
        appId = Utility.getMetaData(activity, "com.magata.appid");;
    }
}
