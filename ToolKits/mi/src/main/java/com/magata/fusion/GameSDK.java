package com.magata.fusion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.magata.config.GameConfig;
import com.magata.config.ResultStateCode;
import com.magata.config.ServerConfig;
import com.magata.eventlisteners.ISDKEventListener;
import com.magata.net.Http;
import com.magata.net.handler.INetResultHandler;
import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnExitListner;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.RoleData;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GameSDK {

    private static ISDKEventListener sdkEventListener = null;

    public static void init(Activity context) {
        MiCommplatform.getInstance().onUserAgreed(context);
        sdkEventListener.onInitSucceed();
    }

    public static void bindListener(ISDKEventListener listener) {
        sdkEventListener = listener;
    }

    public static void exit(Activity context) {
        MiCommplatform.getInstance().miAppExit(context, new OnExitListner() {
            @Override
            public void onExit(int i) {
                if (i == MiErrorCode.MI_XIAOMI_EXIT) {
                    //执行退出的一些操作
                    sdkEventListener.onExitSucceed();
                }

            }
        });
    }

    public static void login(Activity context) {
        MiCommplatform.getInstance().miLogin(context, new OnLoginProcessListener() {
            @Override
            public void finishLoginProcess(int code, MiAccountInfo arg1) {
                switch (code) {
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
                        // 登陆成功
                        //获取用户的登陆后的 UID(即用户唯一标识)
                        String uid = arg1.getUid();
                        //获取用户的登陆的 Session(请参考 3.3用户session验证接口)
                        String session = arg1.getSessionId();//若没有登录返回 null
                        //请开发者完成将uid和session提交给开发者自己服务器进行session验证
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("miuid", uid);
                            jsonObject.put("session", session);
                            jsonObject.put("gameId", GameConfig.appId);
                            jsonObject.put("form", GameConfig.appId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Http.post(ServerConfig.accountUrl + "sign/xiaomiUserLogin", jsonObject.toString(), new INetResultHandler() {
                            @Override
                            public void onSuccess(String data) {
                                try {
                                    JSONObject jsonRootObject = new JSONObject(data);
                                    if (jsonRootObject.getInt("code") == 200) {
                                        JSONObject jsonDataRoot = jsonRootObject.getJSONObject("data");
                                        sdkEventListener.onLoginSucceed(data);
                                    } else {
                                        sdkEventListener.onLoginFailed(data);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String info) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("code", ResultStateCode.UNKNOWN_ERROR);
                                    jsonObject.put("msg", info);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sdkEventListener.onLoginFailed(jsonObject.toString());
                            }
                        }, true);
                        break;
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL:
                        // 登陆失败
                        Toast.makeText(context, "登陆失败，请重试!", Toast.LENGTH_SHORT).show();
                        break;
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_CANCEL:
                        // 取消登录
                        break;
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
                        // 登录操作正在进行中
                        Toast.makeText(context, "正在登陆，请稍后...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        // 登录失败
                        Toast.makeText(context, "登陆失败，请重试!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public static void logout(Activity context){
        sdkEventListener.onLogoutSucceed();
    }

    public static void pay(Activity context, String jsonData) {
        try {
            JSONObject toServerJsonData = new JSONObject(jsonData);
            Http.post(ServerConfig.payUrl + "sign/xmGetOrder", toServerJsonData.toString(), new INetResultHandler() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject jsonRootObject = new JSONObject(data);
                        if (jsonRootObject.getInt("code") == 200) {
                            JSONObject toServerJsonData = new JSONObject(jsonData);
                            JSONObject jsonObject = jsonRootObject.getJSONObject("data");
                            String callbackInfo = toServerJsonData.getString("callbackInfo");
                            String productCode = toServerJsonData.getString("pid");

                            String cpOrderId = jsonObject.getString("cpOrderId");
                            int num = 1;
                            if (toServerJsonData.has("num"))
                                num = toServerJsonData.getInt("num");

                            String balance = "";
                            if (toServerJsonData.has("balance"))
                                balance = toServerJsonData.getString("balance");

                            String vip = "";
                            if (toServerJsonData.has("vip"))
                                vip = toServerJsonData.getString("vip");

                            String level = "";
                            if (toServerJsonData.has("level"))
                                level = toServerJsonData.getString("level");

                            String sociaty = "";
                            if (toServerJsonData.has("sociaty"))
                                level = toServerJsonData.getString("sociaty");

                            String uid = toServerJsonData.getString("uid");

                            String roleName = uid;
                            if (toServerJsonData.has("roleName"))
                                roleName = toServerJsonData.getString("roleName");

                            String server = toServerJsonData.getString("serverId");

                            pay(context, cpOrderId, productCode, num, callbackInfo, balance, vip, level, sociaty, roleName, uid, server);
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("code", ResultStateCode.UNKNOWN_ERROR);
                                jsonObject.put("msg", data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            sdkEventListener.onPayFailed(jsonObject.toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String info) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("code", ResultStateCode.UNKNOWN_ERROR);
                        jsonObject.put("msg", info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sdkEventListener.onPayFailed(jsonObject.toString());
                }
            }, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void pay(Activity context, String cpOrderId, String productCode, int num, String cpUserInfo, String balance, String vip, String level, String sociaty, String name, String uid, String server) {
        MiBuyInfo miBuyInfo = new MiBuyInfo();
        miBuyInfo.setCpOrderId(cpOrderId);//订单号唯一（不为空）
        miBuyInfo.setCpUserInfo(cpUserInfo); //此参数在用户支付成功后会透传给CP的服务器

        miBuyInfo.setProductCode(productCode);//商品代码，开发者申请获得（不为空）
        miBuyInfo.setCount(num);//购买数量(商品数量最大9999，最小1)（不为空）

//用户信息，网游必须设置、单机游戏或应用可选
        Bundle mBundle = new Bundle();
        mBundle.putString(GameInfoField.GAME_USER_BALANCE, balance);   //用户余额
        mBundle.putString(GameInfoField.GAME_USER_GAMER_VIP, vip);  //vip等级
        mBundle.putString(GameInfoField.GAME_USER_LV, level);           //角色等级
        mBundle.putString(GameInfoField.GAME_USER_PARTY_NAME, sociaty);  //工会，帮派
        mBundle.putString(GameInfoField.GAME_USER_ROLE_NAME, name); //角色名称
        mBundle.putString(GameInfoField.GAME_USER_ROLEID, uid);    //角色id
        mBundle.putString(GameInfoField.GAME_USER_SERVER_NAME, server);  //所在服务器
        miBuyInfo.setExtraInfo(mBundle); //设置用户信息

        MiCommplatform.getInstance().miUniPay(context, miBuyInfo,
                new OnPayProcessListener() {
                    @Override
                    public void finishPayProcess(int code) {
                        switch (code) {
                            case MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS://购买成功
                                sdkEventListener.onPaySucceed("Success");
                                break;
                            case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_CANCEL://取消购买
                                Toast.makeText(context, "用户取消", Toast.LENGTH_SHORT).show();
                                break;
                            default://购买失败
                                Toast.makeText(context, "购买失败, Code: " + code, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public static void submitUserInfo(Activity context, String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String uid = jsonObject.getString("uid");
            String level = Long.toString(jsonObject.getLong("level"));
            String name = jsonObject.getString("name");
            String serverId = jsonObject.getString("serverId");
            String serverName = jsonObject.getString("serverName");
            String zoneId = jsonObject.getString("serverId");
            String zoneName = jsonObject.getString("serverName");
            submitUserInfo(context, uid, level, name, serverId, serverName, zoneId, zoneName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void submitUserInfo(Activity context, String uid, String level, String name, String sid, String sName, String zoneId, String zoneName) {
        RoleData data = new RoleData();
        data.setLevel(level);
        data.setRoleId(uid);
        data.setRoleName(name);
        data.setServerId(sid);
        data.setServerName(sName);
        data.setZoneId(zoneId);
        data.setZoneName(zoneName);
        MiCommplatform.getInstance().submitRoleData(context, data);
        sdkEventListener.onSubmitUserInfoSucceed();
    }
}
