package com.magata.alipay;

import android.app.Activity;
import android.util.Log;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.magata.eventlisteners.ISDKEventListener;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Map;

public class Alipay {
    public static ISDKEventListener sdkEventListener = null;

    public static void bindListener(ISDKEventListener listener) {
        sdkEventListener = listener;
    }

    public static void pay(Activity context, final String orderInfo, boolean sandBox){
        if (sandBox){
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        }

        Log.d("Alipay", orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> resultMap = alipay.payV2(orderInfo,true);
                Log.d("Alipay", resultMap.toString());
                try {
                    if (resultMap.get("resultStatus").equals("9000")) {
                        String result = resultMap.get("result");
                        JSONObject resultJson = new JSONObject(result.toString());
                        Log.d("Alipay", resultJson.toString());

                        if (resultJson == null || "".equals(resultJson)) {
                            return;
                        }
                        String code = resultJson.getJSONObject("alipay_trade_app_pay_response").getString("code");
                        Log.d("Alipay", code);

                        if ("10000".equals(code)){
                            sdkEventListener.onPaySucceed("支付成功");
                        } else{
                            sdkEventListener.onPayFailed("支付失败");
                        }
                    } else if (resultMap.get("resultStatus").equals("6001")) {
                        sdkEventListener.onPayFailed("用户取消");
                    } else if (resultMap.get("resultStatus").equals("6002")) {
                        sdkEventListener.onPayFailed("网络连接出错");
                    } else if (resultMap.get("resultStatus").equals("5000")) {
                        sdkEventListener.onPayFailed("操作过快");
                    } else if (resultMap.get("resultStatus").equals("8000") || resultMap.get("resultStatus").equals("6004")) {
                        sdkEventListener.onPayFailed("支付结果未知");
                    }else{
                        sdkEventListener.onPayFailed("支付失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
