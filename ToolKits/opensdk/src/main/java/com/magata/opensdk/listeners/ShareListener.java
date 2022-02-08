package com.magata.opensdk.listeners;

import android.util.Log;

import com.magata.eventlisteners.ISDKEventListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class ShareListener implements IUiListener {
    private String TAG = "ShareListener";

    private ISDKEventListener sdkEventListener;

    public ShareListener(ISDKEventListener sdkEventListener){
        this.sdkEventListener = sdkEventListener;
    }

    @Override
    public void onComplete(Object o) {
        JSONObject json = (JSONObject) o;
        Log.d(TAG, json.toString());
        sdkEventListener.onShareSucceed();
    }

    @Override
    public void onError(UiError e) {
        Log.e(TAG, "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        sdkEventListener.onShareFailed("code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
    }

    @Override
    public void onCancel() {
    }

//    @Override
//    public void onWarning(int i) {
//    }

}
