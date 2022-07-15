package com.magata.fusion;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.magata.eventlisteners.ISDKEventListener;
import com.magata.net.Http;
import com.magata.net.handler.INetResultHandler;
import com.magata.config.ServerConfig;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gundam.sdk.shell.even.EventPublisher;
import cn.gundam.sdk.shell.even.SDKEventKey;
import cn.gundam.sdk.shell.open.ParamInfo;
import cn.gundam.sdk.shell.open.UCOrientation;
import cn.gundam.sdk.shell.param.SDKParamKey;
import cn.gundam.sdk.shell.param.SDKParams;

/**
 * 从 Unity3D 中调用。
 */
public class GameSDK {
    public final static int ORIENTATION_PORTRAIT = 0;//竖屏
    public final static int ORIENTATION_LANDSCAPE = 1;//横屏
    private static SdkEventReceiverImpl sdkEventReceiver = new SdkEventReceiverImpl();

    public static void registerSDKEventReceiver(){
        cn.uc.gamesdk.UCGameSdk.defaultSdk().registerSDKEventReceiver(sdkEventReceiver);
    }

    public static void unregisterSDKEventReceiver(){
        cn.uc.gamesdk.UCGameSdk.defaultSdk().unregisterSDKEventReceiver(sdkEventReceiver);
    }

    public static void bindListener(ISDKEventListener listener) {
        sdkEventReceiver.bindListener(listener);
    }

    public static void init(Activity context){
        boolean debugMode = false;
        int gameId = -1;
        boolean enablePayHistory = true;
        boolean enableUserChange = true;
        int orientation = 0;
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (null != info) {
            debugMode = info.metaData.getBoolean("com.magata.uc.debugmode");
            gameId = info.metaData.getInt("com.magata.uc.gameid");
            enablePayHistory = info.metaData.getBoolean("com.magata.uc.enablepayhistory");
            enableUserChange = info.metaData.getBoolean("com.magata.uc.enableuserchange");
            orientation = info.metaData.getInt("com.magata.uc.orientation");
        }
        initSDK(context, debugMode, gameId, enablePayHistory, enableUserChange, orientation);
    }

    /**
     * 初始化 初始化SDK
     *
     * @param debugMode        是否联调模式， false=连接SDK的正式生产环境，true=连接SDK的测试联调环境
     * @param gameId           游戏ID，该ID由UC游戏中心分配，唯一标识一款游戏
     * @param enablePayHistory 是否启用支付查询功能
     * @param enableUserChange 是否启用账号切换功能
     * @param orientation      游戏横竖屏设置 0：竖屏 1：横屏
     */
    public static void initSDK(Activity context, final boolean debugMode, int gameId, boolean enablePayHistory, boolean enableUserChange, int orientation) {
        registerSDKEventReceiver();
        final ParamInfo gameParamInfo = new ParamInfo();
        gameParamInfo.setGameId(gameId);

        gameParamInfo.setEnablePayHistory(enablePayHistory);
        gameParamInfo.setEnableUserChange(enableUserChange);

        if (ORIENTATION_PORTRAIT == orientation) {
            gameParamInfo.setOrientation(UCOrientation.PORTRAIT);
        } else if (ORIENTATION_LANDSCAPE == orientation) {
            gameParamInfo.setOrientation(UCOrientation.LANDSCAPE);
        } else {
            if (context.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                gameParamInfo.setOrientation(UCOrientation.PORTRAIT);
            } else {
                gameParamInfo.setOrientation(UCOrientation.LANDSCAPE);
            }
        }

        final SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
        sdkParams.put(SDKParamKey.DEBUG_MODE, debugMode);
        //sdkParams.put(SDKParamKey.PULLUP_INFO, pullupInfo);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().initSdk(context, sdkParams);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 调用SDK的用户登录
     */
    public static void login(Activity context) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().login(context, null);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 退出当前登录的账号
     */
    public static void logout(Activity context) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().logout(context, null);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void submitUserInfo(Activity context, String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String zoneId = jsonObject.getString("serverId");
            String zoneName = jsonObject.getString("serverName");
            String roleId = jsonObject.getString("uid");
            String roleName = jsonObject.getString("name");
            long roleLevel = jsonObject.getLong("level");
            long roleCTime = jsonObject.getLong("createTimeStamp");
            submitRoleData(context, zoneId, zoneName, roleId, roleName, roleLevel, roleCTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        /**
         * 提交玩家选择的游戏分区及角色信息
         *
         * @param zoneId    区服ID
         * @param zoneName  区服名称
         * @param roleId    角色编号
         * @param roleName  角色名称
         * @param roleCTime 角色创建时间(单位：秒)，长度10，获取服务器存储的时间，不可用手机本地时间
         */
    public static void submitRoleData(Activity context, String zoneId, final String zoneName, final String roleId, final String roleName, long roleLevel, long roleCTime) {
        final SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.STRING_ROLE_ID, roleId);
        sdkParams.put(SDKParamKey.STRING_ROLE_NAME, roleName);
        sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, roleLevel);
        sdkParams.put(SDKParamKey.STRING_ZONE_ID, zoneId);
        sdkParams.put(SDKParamKey.STRING_ZONE_NAME, zoneName);
        sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, roleCTime);

        context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().submitRoleData(context, sdkParams);
                    sdkEventReceiver.onSaveUserInfoSucc();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void pay(Activity context, String jsonData) {
        try {
            JSONObject toServerJsonData = new JSONObject(jsonData);
            toServerJsonData.put("accountId", sdkEventReceiver.getAccountId());
            Http.post(ServerConfig.payUrl + "sign/getUcOrder", toServerJsonData.toString(), new INetResultHandler() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject jsonRootObject = new JSONObject(data);
                        if (jsonRootObject.getInt("code") == 200) {
                            JSONObject toServerJsonData = new JSONObject(jsonData);
                            JSONObject jsonObject = jsonRootObject.getJSONObject("data");
                            String callbackInfo = toServerJsonData.getString("callbackInfo");
                            String amount = jsonObject.getString("amount");
                            String notifyUrl = jsonObject.getString("notifyUrl");
                            String cpOrderId = jsonObject.getString("cpOrderId");
                            String accountId = jsonObject.getString("accountId");
                            String signType = jsonObject.getString("signType");
                            String sign = jsonObject.getString("sign");

                            pay(context, accountId, cpOrderId, amount, callbackInfo, notifyUrl, signType, sign);
                        } else {
                            EventPublisher.instance().publish(SDKEventKey.ON_PAY_USER_EXIT, new Object[]{data});
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String info) {
                    EventPublisher.instance().publish(SDKEventKey.ON_PAY_USER_EXIT, new Object[]{info});
                }
            }, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行充值下单操作，此操作会调出充值界面。
     *
     * @param amount       充值金额。默认为0，如果不设或设为0，充值时用户从充值界面中选择或输入金额；如果设为大于0的值，表示固定充值金额，
     *                     不允许用户选择或输入其它金额。
     * @param callbackInfo cp自定义信息，在支付结果通知时回传,CP可以自己定义格式,长度不超过250
     * @param notifyUrl    支付回调通知URL
     * @param signType     签名类型
     * @param sign         签名结果
     */
    public static void pay(Activity context, String accountId, String cpOrderID, String amount, String callbackInfo, String notifyUrl, String signType, String sign) {
        final SDKParams sdkParams = new SDKParams();

        if (notifyUrl != null) {
            sdkParams.put(SDKParamKey.NOTIFY_URL, notifyUrl);
        }

        if (cpOrderID != null) {
            sdkParams.put(SDKParamKey.CP_ORDER_ID, cpOrderID);
        }

        if (callbackInfo != null) {
            sdkParams.put(SDKParamKey.CALLBACK_INFO, callbackInfo);
        }

        if (accountId != null) {
            sdkParams.put(SDKParamKey.ACCOUNT_ID, accountId);
        }

        if (amount != null) {
            sdkParams.put(SDKParamKey.AMOUNT, amount);
        }

        if (!TextUtils.isEmpty(signType)) {
            sdkParams.put(SDKParamKey.SIGN_TYPE, signType);
        }

        if (!TextUtils.isEmpty(sign)) {
            sdkParams.put(SDKParamKey.SIGN, sign);
        }

        context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().pay(context, sdkParams);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 退出SDK，游戏退出前必须调用此方法，以清理SDK占用的系统资源。如果游戏退出时不调用该方法，可能会引起程序错误。
     */
    public static void exit(Activity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().exit(context, null);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 打开sdk指定的页面
     *
     * @param action   业务操作指令
     * @param business 页面名
     * @param orientation 1 强制竖屏 2 强制横屏 0 不变，用户原来手机默认状态
     */
    public static void showPage(Activity context, String action, String business, int orientation) {
        final SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.ACTION, action);
        sdkParams.put(SDKParamKey.BUSINESS, business);
        sdkParams.put(SDKParamKey.ORIENTATION, orientation);

        context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    cn.uc.gamesdk.UCGameSdk.defaultSdk().execute(context, sdkParams);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
