package com.magata.fusion;

import android.app.Activity;
import android.util.Log;

import com.magata.config.GameConfig;
import com.magata.eventlisteners.ISDKEventListener;
import com.magata.net.loading.LoadingManager;
import com.magata.utility.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Fusion {

    private static String TAG = Fusion.class.getSimpleName();
    private static ISDKEventListener sdkEventListener = null;
    private static Class sdkClass = null;
    private static String sdkClassName = "com.magata.fusion.GameSDK";
    private static Class sdkListenerClass = null;
    private static String sdkListenerName = "com.magata.eventlisteners.SDKEventListener";


    private static Method GetSDKMethod(String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = sdkClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    private static void InvokeSDKMethod(Method method, Object... args) {
        if (method == null)
            return;
        try {
            method.invoke(null, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void bindListener(ISDKEventListener listener) {
        sdkEventListener = listener;
        Method method = GetSDKMethod("bindListener", ISDKEventListener.class);
        InvokeSDKMethod(method, listener);
    }

    public static void init(Activity context){
        if (null == sdkClass) {
            try {
                sdkClass = Class.forName(sdkClassName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (null == sdkListenerClass) {
            try {
                sdkListenerClass = Class.forName(sdkListenerName);
                Method listenerInstanceGetter = sdkListenerClass.getMethod("Instance");
                ISDKEventListener listener = (ISDKEventListener) listenerInstanceGetter.invoke(null);
                bindListener(listener);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.w(TAG, "找不到" + sdkListenerName + ", 请手动绑定回调");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        GameConfig.init(context);
        Method method = GetSDKMethod("init", Activity.class);
        InvokeSDKMethod(method, context);
    }

    public static void login(Activity context) {
        LoadingManager.invokedActivity = context;
        Method method = GetSDKMethod("login", Activity.class);
        InvokeSDKMethod(method, context);
    }

    public static void logout(Activity context) {
        Method method = GetSDKMethod("logout", Activity.class);
        InvokeSDKMethod(method, context);
    }


    /*
    *
        uid = , 用户id
        level = , 用户等级
        name = , 用户名称
        serverId = , 区服id
        serverName , 区服名称
        createTimeStamp, 创建时间
    * */
    public static void submitUserInfo(Activity context, String jsonData) {
        Method method = GetSDKMethod("submitUserInfo", Activity.class, String.class);
        if (method == null)
            sdkEventListener.onSubmitUserInfoFailed("该渠道客户端无上传用户信息接口");
        else
            InvokeSDKMethod(method, context, jsonData);
    }

    public static void getCertificationInfo(Activity context) {
        Method method = GetSDKMethod("getCertificationInfo", Activity.class);
        if (method == null)
            sdkEventListener.onCertificationInfoGetFailed("该渠道客户端无防沉迷接口");
        else
            InvokeSDKMethod(method, context);
    }

    public static void checkMissingOrder(Activity context) {
        Method method = GetSDKMethod("checkMissingOrder", Activity.class);
        if (method != null)
            InvokeSDKMethod(method, context);
    }

    /*
    *
        rechargeId = , 充值id
        rechargeType = , 充值类型（可选） 默认为0

        pid = , 产品id
        money = , 充值金额
        productName , 商品名称
        serverId, 充值区服
        uid, 充值角色id
        channel, 充值渠道
        callbackInfo, 充值回调信息
    * */
    public static void pay(Activity context, String jsonData) {
        try {
            LoadingManager.invokedActivity = context;
            JSONObject toServerJsonData = new JSONObject(jsonData);
            toServerJsonData.put("gameId", GameConfig.appId);
            toServerJsonData.put("deviceId", Utility.getAndroidId(context));

            Method method = GetSDKMethod("pay", Activity.class, String.class);
            InvokeSDKMethod(method, context, toServerJsonData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void exit(Activity context) {
        Method method = GetSDKMethod("exit", Activity.class);
        InvokeSDKMethod(method, context);
    }
}
