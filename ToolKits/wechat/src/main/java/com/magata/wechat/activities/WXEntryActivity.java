package com.magata.wechat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.magata.wechat.TencentWeChat;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private static String TAG = "Magata.WXEntryActivity";
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, TencentWeChat.appId, false);
        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                ShowMessageFromWX.Req showReq = (ShowMessageFromWX.Req)req;
                WXMediaMessage wxMsg = showReq.message;
                if ("share".equals(wxMsg.messageExt)) {
                    TencentWeChat.sdkEventListener.onShareSucceed();
                }
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {

        Log.d(TAG, "ErrorCode:" + resp.errCode + ", type=" + resp.getType());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    TencentWeChat.sdkEventListener.onPaySucceed("支付成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    TencentWeChat.sdkEventListener.onPayFailed("用户取消");
                    break;
                default:
                    TencentWeChat.sdkEventListener.onPayFailed("支付失败  ErrorCode:" + resp.errCode);
                    break;
            }
        }else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    SendAuth.Resp r = (SendAuth.Resp)resp;

                    JSONObject json = new JSONObject();
                    try {
                        json.put("code", r.code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TencentWeChat.sdkEventListener.onLoginSucceed(json.toString());
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    TencentWeChat.sdkEventListener.onLoginFailed("用户取消");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    TencentWeChat.sdkEventListener.onLoginFailed("用户拒绝授权");
                    break;
                default:
                    TencentWeChat.sdkEventListener.onLoginFailed("登录失败  ErrorCode:" + resp.errCode);
                    break;
            }
        }
        finish();
    }

}