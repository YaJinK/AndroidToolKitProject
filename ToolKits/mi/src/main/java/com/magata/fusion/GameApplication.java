package com.magata.fusion;

import android.app.Application;
import android.util.Log;

import com.magata.utility.Utility;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.OnInitProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;

import java.util.List;

public class GameApplication {

    public void onCreate(Application application) {

        String appIdStr = Utility.getMetaData(application, "com.magata.mi.appid");
        String appKeyStr = Utility.getMetaData(application, "com.magata.mi.appkey");

        String[] appIdArr = appIdStr.split("_");
        String appId = appIdArr[1];

        String[] appKeyArr = appKeyStr.split("_");
        String appKey = appKeyArr[1];

        MiAppInfo appInfo = new MiAppInfo();
        appInfo.setAppId(appId);
        appInfo.setAppKey(appKey);
        MiCommplatform.Init(application, appInfo, new OnInitProcessListener() {
            @Override
            public void finishInitProcess(List<String> list, int i) {
            }

            @Override
            public void onMiSplashEnd() {
            }
        });
    }
}
