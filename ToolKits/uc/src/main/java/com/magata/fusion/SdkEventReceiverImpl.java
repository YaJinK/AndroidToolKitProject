package com.magata.fusion;

import android.util.Log;

import com.magata.config.GameConfig;
import com.magata.config.ResultStateCode;
import com.magata.eventlisteners.ISDKEventListener;
import com.magata.net.Http;
import com.magata.net.handler.INetResultHandler;
import com.magata.config.ServerConfig;

import org.json.JSONException;
import org.json.JSONObject;
import cn.gundam.sdk.shell.even.SDKEventKey;
import cn.gundam.sdk.shell.even.SDKEventReceiver;
import cn.gundam.sdk.shell.even.Subscribe;
import cn.gundam.sdk.shell.open.OrderInfo;

/**
 * Created by junhong.kjh@alibaba.com on 2016/12/20.
 */

public class SdkEventReceiverImpl extends SDKEventReceiver {
    private static final String TAG = SdkEventReceiverImpl.class.getSimpleName();
    ISDKEventListener sdkEventListener = null;

    private String accountId = null;

    public String getAccountId() {
        return accountId;
    }

    public void bindListener(ISDKEventListener sdkEventListener) {
        this.sdkEventListener = sdkEventListener;
    }

    @Subscribe(event = SDKEventKey.ON_INIT_SUCC)
    private void onInitSucc() {
        sdkEventListener.onInitSucceed();
    }

    @Subscribe(event = SDKEventKey.ON_INIT_FAILED)
    private void onInitFailed(String data) {
        if (data != null) {
            Log.d(TAG, data);
            sdkEventListener.onInitFailed(data);
        }
    }

    @Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
    private void onLoginSucc(String sid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", sid);
            jsonObject.put("gameId", GameConfig.appId);
            jsonObject.put("form", GameConfig.appId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onLoginSucc: " + jsonObject.toString());
        Http.post(ServerConfig.accountUrl + "sign/ucGetUserInfo", jsonObject.toString(), new INetResultHandler() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonRootObject = new JSONObject(data);
                    if (jsonRootObject.getInt("code") == 200) {
                        JSONObject jsonDataRoot = jsonRootObject.getJSONObject("data");
                        accountId = jsonDataRoot.getString("accountId");
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
    }
    
    @Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
    private void onLoginFailed(String desc) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", ResultStateCode.UNKNOWN_ERROR);
            jsonObject.put("msg", desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sdkEventListener.onLoginFailed(jsonObject.toString());
    }

    @Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
    private void onLogoutSucc() {
        sdkEventListener.onLogoutSucceed();
    }

    @Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
    private void onLogoutFailed() {
        sdkEventListener.onLogoutFailed("");
    }

    @Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
    private void onExitSucc(String desc) {
        sdkEventListener.onExitSucceed();
    }

    @Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
    private void onExitCanceled(String desc) {
        sdkEventListener.onExitCanceled();
    }

    @Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
    private void onCreateOrderSucc(OrderInfo orderInfo) {
        sdkEventListener.onPaySucceed(createOrderString(orderInfo));
    }

    @Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
    private void onPayUserExit(OrderInfo orderInfo) {
        sdkEventListener.onPayFailed(createOrderString(orderInfo));
    }

    @Subscribe(event = SDKEventKey.ON_EXECUTE_SUCC)
    private void onExecuteSucc(String msg) {
        Log.d(TAG, "onExecuteSucc > " + msg);
    }

    @Subscribe(event = SDKEventKey.ON_EXECUTE_FAILED)
    private void onExecuteFailed(String msg) {
        Log.d(TAG, "onExecuteFailed > " + msg);
    }

    @Subscribe(event = 110001)
    private void onShowPageResult(String business, String result) {
        Log.d(TAG, "onShowPageResult > " + result);
    }

    private String createOrderString(OrderInfo orderInfo) {
        try {
            JSONObject json = new JSONObject();
            json.put("orderId", orderInfo.getOrderId());
            json.put("orderAmount", orderInfo.getOrderAmount());
            json.put("payWay", orderInfo.getPayWay());
            json.put("payWayName", orderInfo.getPayWayName());
            return json.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    private String createShowPageResult(String business, String result) {
        try {
            JSONObject json = new JSONObject();
            json.put("business", business);
            json.put("result", result);
            return json.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public void onSaveUserInfoSucc() {
        sdkEventListener.onSubmitUserInfoSucceed();
    }

    public void onSaveUserInfoFailed(String info) {
        sdkEventListener.onSubmitUserInfoFailed(info);
    }
}

