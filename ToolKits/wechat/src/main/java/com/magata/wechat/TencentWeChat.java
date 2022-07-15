package com.magata.wechat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.magata.eventlisteners.ISDKEventListener;
import com.magata.fileprovider.FileProviderUtility;
import com.magata.utility.Utility;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class TencentWeChat {

    public static IWXAPI iwxApi = null;
    public static ISDKEventListener sdkEventListener = null;
    public static String appId = "";

    public static void init(Activity context) {
        if (null == iwxApi) {
            appId = Utility.getMetaData(context, "com.magata.wechat.appid");
            iwxApi = WXAPIFactory.createWXAPI(context, appId, true);
            iwxApi.registerApp(appId);
        }
    }

    public static void bindListener(ISDKEventListener listener) {
        sdkEventListener = listener;
    }

    public static void login(String scope, String state){
        if (!iwxApi.isWXAppInstalled()){
            sdkEventListener.onLoginFailed("未安装微信");
            return;
        }

        SendAuth.Req request = new SendAuth.Req();
        request.scope = scope;
        request.state = state;

        boolean sendResult =  iwxApi.sendReq(request);
        Log.d("TencentWeChat", "wechatPay: " + request.checkArgs());
        Log.d("TencentWeChat", "wechatPay: " + sendResult);
    }

    /// scene 0 分享到好友   1 朋友圈   2 收藏
    public static void shareImage(Activity context, String filePath, int scene) {
        if (!iwxApi.isWXAppInstalled()){
            sdkEventListener.onShareFailed("未安装微信");
            return;
        }

        String realPath = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && iwxApi.getWXAppSupportAPI() >= 0x27000D00) {
            Uri uri = FileProviderUtility.getFileUri(context, filePath);
            context.grantUriPermission("com.tencent.mm", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            realPath = uri.toString();
        } else {
            realPath = filePath;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.imagePath = realPath;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.messageExt = "share";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        iwxApi.sendReq(req);
    }


    public static void pay(String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign){
        if (!iwxApi.isWXAppInstalled()){
            sdkEventListener.onPayFailed("未安装微信");
            return;
        }

        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;

        boolean sendResult =  iwxApi.sendReq(request);
        Log.d("Wechat", "wechatPay: " + request.checkArgs());
        Log.d("Wechat", "wechatPay: " + sendResult);
    }

}
