package com.magata.utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class Utility {

    private static String TAG = "Log Utility";

    ///*
    // 反射获取R文件内容
    // *///
    public static int getRContent(String packageName, String className, String resouceName) {
        try {
            Class<?> resource = Class.forName(packageName + ".R");
            Log.d(TAG, "getRContent: " + packageName);

            Class<?>[] classes = resource.getClasses();
            for (Class<?> c : classes) {
                int i = c.getModifiers();
                String name = c.getName();
                Log.d(TAG, "getRContent: " + name);
                String s = Modifier.toString(i);
                if (s.contains("static") && name.contains(className)) {
                    Field intField = c.getField(resouceName);
                    return intField.getInt(c);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
            Log.d(TAG, "getRContent: 没找到" + resouceName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /****************

     * @param url Activity配置的url

     * @return 返回true表示呼起界面成功，返回false表示呼起失败

     ******************/
    public static void startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************
     * @param action action

     * @param url Activity配置的url

     * @return 返回true表示呼起界面成功，返回false表示呼起失败

     ******************/
    public static void startActivity(Context context, String action, String url) {
        Intent intent = new Intent(action, Uri.parse(url));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************
     * @param action action

     * @param url Activity配置的url

     * @param mimeType mimeType

     * @return 返回true表示呼起界面成功，返回false表示呼起失败

     ******************/
    public static void startActivity(Context context, String action, String url, String mimeType) {
        Intent intent = new Intent(action);
        intent.setDataAndType(Uri.parse(url), mimeType);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkClientInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void joinQQGroup(Context context, String key) {
        String url = "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key;
        Utility.startActivity(context, url);
    }

    public static void chatQQ(Context context, String qq) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
        Utility.startActivity(context, Intent.ACTION_VIEW, url);
    }

    public static String getMetaData(Context context, String key) {
        String value = null;
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (null != info) {
            Object v = info.metaData.get(key);
            if (v instanceof String)
                value = (String)v;
            else if (v instanceof Integer)
                value = Integer.toString((Integer) v);
            else if(v instanceof Float)
                value = Float.toString((Float) v);
        }
        return value;
    }
}
