package com.magata.opensdk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.magata.opensdk.TencentQQ;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

public class TencentQQBridgeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        String action = data.getString("action");
        if ("login".equals(action)) {
            String scope = data.getString("scope"); // get_simple_userinfo
            if (!TencentQQ.mTencent.isSessionValid())
            {
                TencentQQ.mTencent.login(this, scope, TencentQQ.loginListener);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, TencentQQ.loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
