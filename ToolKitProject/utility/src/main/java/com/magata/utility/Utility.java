package com.magata.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
    public boolean startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /****************

     * @param url Activity配置的url

     * @param mimeType mimeType

     * @return 返回true表示呼起界面成功，返回false表示呼起失败

     ******************/
    public boolean startActivity(Context context, String url, String mimeType) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse(url), mimeType);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
