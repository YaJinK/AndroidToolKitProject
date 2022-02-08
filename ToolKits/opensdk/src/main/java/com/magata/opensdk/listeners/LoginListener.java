package com.magata.opensdk.listeners;

import android.util.Log;

import com.magata.eventlisteners.ISDKEventListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class LoginListener implements IUiListener {
    private String TAG = "LoginListener";

    private ISDKEventListener sdkEventListener;

    public LoginListener(ISDKEventListener sdkEventListener){
        this.sdkEventListener = sdkEventListener;
    }

    @Override
    public void onComplete(Object o) {
        JSONObject json = (JSONObject) o;
        Log.d(TAG, json.toString());
        sdkEventListener.onLoginSucceed(json.toString());
    }

    @Override
    public void onError(UiError e) {
        Log.e(TAG, "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        sdkEventListener.onLoginFailed("code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
    }

    @Override
    public void onCancel() {
        sdkEventListener.onLoginCanceled();
    }

//    @Override
//    public void onWarning(int i) {
//
//    }

}
