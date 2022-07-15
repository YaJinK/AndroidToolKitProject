package com.magata.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;

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

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static int getNotchHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            final View decorView = activity.getWindow().getDecorView();
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
            return displayCutout != null ? displayCutout.getSafeInsetTop() : 115;
        } else {
            return 115;
        }
    }

    private static boolean checkIsNotch(Activity context, String brand) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            final View decorView = context.getWindow().getDecorView();
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
            if (displayCutout == null) {
                return false;
            } else {
                List<Rect> rects = displayCutout.getBoundingRects();

                if (rects == null || rects.size() == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if ("huawei".equals(brand)) {
                boolean ret = false;
                try {
                    ClassLoader cl = context.getClassLoader();
                    Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
                    Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
                    ret = (boolean) get.invoke(HwNotchSizeUtil);
                } catch (ClassNotFoundException e) {
                    Log.e("checkIsNotch", "huawei ClassNotFoundException");
                } catch (NoSuchMethodException e) {
                    Log.e("checkIsNotch", "huawei NoSuchMethodException");
                } catch (Exception e) {
                    Log.e("checkIsNotch", "huawei Exception");
                } finally {
                    Log.d("checkIsNotch", "" + ret);
                    return ret;
                }
            } else if ("xiaomi".equals(brand)) {
                boolean ret = false;
                try {
                    ClassLoader cl = context.getClassLoader();
                    Class cls = cl.loadClass("android.os.SystemProperties");
                    Method method = cls.getMethod("getInt", String.class, int.class);
                    ret = ((int) method.invoke(null, "ro.miui.notch", 0) == 1);
                } catch (ClassNotFoundException e) {
                    Log.e("checkIsNotch", "xiaomi ClassNotFoundException");
                } catch (NoSuchMethodException e) {
                    Log.e("checkIsNotch", "xiaomi NoSuchMethodException");
                } catch (Exception e) {
                    Log.e("checkIsNotch", "xiaomi Exception");
                } finally {
                    Log.d("checkIsNotch", "" + ret);
                    return ret;
                }
            } else if ("vivo".equals(brand)) {
                boolean ret = false;
                try {
                    ClassLoader cl = context.getClassLoader();
                    Class cls = cl.loadClass("android.util.FtFeature");
                    Method method = cls.getMethod("isFeatureSupport", int.class);
                    ret = (boolean) method.invoke(cls, 0x00000020);
                } catch (ClassNotFoundException e) {
                    Log.e("checkIsNotch", "vivo ClassNotFoundException");
                } catch (NoSuchMethodException e) {
                    Log.e("checkIsNotch", "vivo NoSuchMethodException");
                } catch (Exception e) {
                    Log.e("checkIsNotch", "vivo Exception");
                } finally {
                    Log.d("checkIsNotch", "" + ret);
                    return ret;
                }
            } else if ("oppo".equals(brand)) {
                boolean ret = false;
                try {
                    ret = context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
                } catch (Exception e) {
                    Log.e("checkIsNotch", "oppo Exception");
                    e.printStackTrace();
                } finally {
                    Log.d("checkIsNotch", "" + ret);
                    return ret;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean checkIsNotch(Activity context) {
        boolean huaweiIsNotch = checkIsNotch(context, "huawei");
        boolean xiaomiIsNotch = checkIsNotch(context, "xiaomi");
        boolean vivoIsNotch = checkIsNotch(context, "vivo");
        boolean oppoIsNotch = checkIsNotch(context, "oppo");

        boolean result = huaweiIsNotch || xiaomiIsNotch || vivoIsNotch || oppoIsNotch;
        Log.d("checkIsNotch", "ResultAll: " + result);
        return result;
    }

}
