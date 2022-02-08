package com.magata.opensdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.magata.eventlisteners.ISDKEventListener;
import com.magata.opensdk.listeners.LoginListener;
import com.magata.opensdk.activities.TencentQQBridgeActivity;
import com.magata.opensdk.listeners.ShareListener;
import com.magata.utility.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.TDialog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

public class TencentQQ {
    public static Tencent mTencent = null;
    public static LoginListener loginListener = null;
    public static ShareListener shareListener = null;

    public static void init(Context context) {
        if (mTencent == null) {
            String appId = Utility.getMetaData(context, "com.magata.tencent.appid");
            mTencent = Tencent.createInstance(appId, context.getApplicationContext(), context.getPackageName() + ".fileprovider");
        }
    }

    public static void bindListener(ISDKEventListener listener) {
        if (loginListener == null)
            loginListener = new LoginListener(listener);
        if (shareListener == null)
            shareListener = new ShareListener(listener);
    }

    public static void login(Context context, String scope) {
        Intent intent = new Intent(context, TencentQQBridgeActivity.class);
        Bundle data = new Bundle();
        data.putString("action", "login");
        data.putString("scope", scope);
        intent.putExtras(data);
        context.startActivity(intent);
    }

    /// scene 0 分享到好友   1 空间
    public static void shareImage(Context context, String filePath, int scene) {
        if (!mTencent.isQQInstalled(context) ) {
            String downloadUrl = "https://openmobile.qq.com/oauth2.0/m_jump_by_version";
            (new TDialog(context, "", downloadUrl, (IUiListener)null, mTencent.getQQToken())).show();
            return;
        }
        Bundle params = new Bundle();
        if (scene == 0) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,filePath);
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare. SHARE_TO_QQ_TYPE_IMAGE);
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare. SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
            mTencent.shareToQQ((Activity)context, params, shareListener);
        } else if (scene == 1) {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
            ArrayList<String> fileList = new ArrayList<String>();
            fileList.add(filePath);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, fileList);
            mTencent.publishToQzone((Activity)context, params, shareListener);
        }
    }
}
