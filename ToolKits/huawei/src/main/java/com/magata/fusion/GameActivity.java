package com.magata.fusion;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.buoy.BuoyClient;
import com.huawei.hms.jos.games.player.Player;
import com.magata.config.ResultStateCode;
import com.magata.config.ServerConfig;
import com.magata.net.Http;
import com.magata.net.handler.INetResultHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity {

    public void onResume(Activity activity) {
        BuoyClient buoyClient = Games.getBuoyClient(activity);
        buoyClient.showFloatWindow();
    }

    public void onPause(Activity activity) {
        BuoyClient buoyClient = Games.getBuoyClient(activity);
        buoyClient.hideFloatWindow();
    }

    public void onActivityResult (Activity activity,int requestCode, int resultCode, Intent data){
        //授权登录结果处理，从AuthHuaweiId中获取Authorization Code
        if (requestCode == 8888) {
            //调用getPlayersClient方法初始化
            PlayersClient client = Games.getPlayersClient(activity);
            //获取玩家信息
            Task<Player> task = client.getGamePlayer();
            task.addOnSuccessListener(new OnSuccessListener<Player>() {
                @Override
                public void onSuccess(Player player) {
                    String accessToken = player.getAccessToken();
                    String displayName = player.getDisplayName();
                    String unionId = player.getUnionId();
                    String openId = player.getOpenId();

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("accessToken", accessToken);

                        Http.post(ServerConfig.accountUrl + "sign/HWHMSGetAccTokenLoginLE3", jsonObject.toString(), new INetResultHandler() {
                            @Override
                            public void onSuccess(String data) {
                                try {
                                    JSONObject jsonRootObject = new JSONObject(data);
                                    if (jsonRootObject.getInt("code") == 200) {
                                        JSONObject jsonDataRoot = jsonRootObject.getJSONObject("data");
                                        GameSDK.checkMissingOrder(activity, 0);
                                        GameSDK.getListener().onLoginSucceed(data);
                                    } else {
                                        GameSDK.getListener().onLoginFailed(data);
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
                                GameSDK.getListener().onLoginFailed(jsonObject.toString());
                            }
                        }, true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    if (e instanceof ApiException) {
                        String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                        Log.d(GameSDK.TAG, result);

                    }
                }
            });
        }else if (requestCode == 6666) {
            if (data == null) {
                Log.e("onActivityResult", "data is null");
                return;
            }
            // 调用parsePurchaseResultInfoFromIntent方法解析支付结果数据
            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(activity).parsePurchaseResultInfoFromIntent(data);
            switch(purchaseResultInfo.getReturnCode()) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // 用户取消
                    GameSDK.getListener().onPayCanceled();
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // 检查是否存在未发货商品
                    GameSDK.checkMissingOrder(activity, 0);
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // 支付成功
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                    // 使用您应用的IAP公钥验证签名
                    // 若验签成功，则进行发货
                    // 若用户购买商品为消耗型商品，您需要在发货成功后调用consumeOwnedPurchase接口进行消耗
                    try {
                        JSONObject json = new JSONObject();
                        json.put("inAppPurchaseData", inAppPurchaseData);
                        json.put("inAppPurchaseDataSignature", inAppPurchaseDataSignature);
                        GameSDK.payAfterRequest(json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
