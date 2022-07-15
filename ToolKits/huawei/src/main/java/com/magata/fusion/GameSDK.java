package com.magata.fusion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.entity.IsEnvReadyResult;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.OwnedPurchasesReq;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.jos.AntiAddictionCallback;
import com.huawei.hms.jos.AppParams;
import com.huawei.hms.jos.AppUpdateClient;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.JosStatusCodes;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.player.PlayerExtraInfo;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.JosGetNoticeReq;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.magata.config.ResultStateCode;
import com.magata.config.ServerConfig;
import com.magata.eventlisteners.ISDKEventListener;
import com.magata.net.Http;
import com.magata.net.handler.INetResultHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GameSDK {

    private static ISDKEventListener sdkEventListener = null;

    public static String TAG = "HuaWei";

    public static void init(Activity context) {
        AccountAuthParams params = AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME;
        JosAppsClient appsClient = JosApps.getJosAppsClient(context);
        Task<Void> initTask;
        initTask = appsClient.init(new AppParams(params, new AntiAddictionCallback() {
            @Override
            public void onExit() {
                //在此处实现游戏防沉迷功能，如保存游戏、调用帐号退出接口
                new AlertDialog.Builder(context)
                        .setTitle("防沉迷")
                        .setMessage("根据华为防沉迷规定，您已无法进行游戏，点击确定退出游戏。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.finish();
                            }
                        })
                        .show();
            }
        }));
        initTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final AppUpdateClient client = JosApps.getAppUpdateClient(context);
                client.checkAppUpdate(context, new CheckUpdateCallBack(){
                    @Override
                    public void onUpdateInfo(Intent intent) {
                        if (intent != null) {
                            // 获取更新状态码， Default_value为取不到status时默认的返回码，由应用自行决定
                            int status = intent.getIntExtra(UpdateKey.STATUS, 4444);
                            // 错误码，建议打印
                            int rtnCode = intent.getIntExtra(UpdateKey.FAIL_CODE, 4444);
                            // 失败信息，建议打印
                            String rtnMessage = intent.getStringExtra(UpdateKey.FAIL_REASON);
                            Serializable info = intent.getSerializableExtra(UpdateKey.INFO);
                            //可通过获取到的info是否属于ApkUpgradeInfo类型来判断应用是否有更新
                            if (info instanceof ApkUpgradeInfo) {
                                ApkUpgradeInfo apkUpgradeInfo = (ApkUpgradeInfo) info;
                                client.showUpdateDialog(context, apkUpgradeInfo, true);
                            }
                            Log.d(TAG, "onUpdateInfo status: " + status + ", rtnCode: " + rtnCode + ", rtnMessage: " + rtnMessage);
                        }
                    }

                    @Override
                    public void onMarketInstallInfo(Intent intent) {
                    }

                    @Override
                    public void onMarketStoreError(int i) {
                    }

                    @Override
                    public void onUpdateStoreError(int i) {
                    }
                });
                sdkEventListener.onInitSucceed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG,"init failed, " + e.getMessage());
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    int statusCode = apiException.getStatusCode();
                    Log.e(TAG,"init failed, code: " + statusCode);
                    // 错误码为7401时表示用户未同意华为联运隐私协议
                    if (statusCode == JosStatusCodes.JOS_PRIVACY_PROTOCOL_REJECTED) {
                        Log.d(TAG, "has reject the protocol");
                        //在此处实现退出游戏
                        context.finish();
                    } else if (statusCode == 907135003) {
                        Log.d(TAG, "用户取消安装华为移动服务");
                        init(context);
                    }
                }
            }

        });
    }

    public static void bindListener(ISDKEventListener listener) {
        sdkEventListener = listener;
    }
    public static ISDKEventListener getListener() {
        return sdkEventListener;
    }

    public static void exit(Activity context) {
        sdkEventListener.onExitSucceed();
    }

    public static void login(Activity context) {
        HuaweiIdAuthParams authParams = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).setAuthorizationCode().createParams();
        HuaweiIdAuthService service = HuaweiIdAuthManager.getService(context, authParams);
        context.startActivityForResult(service.getSignInIntent(), 8888);
    }

    public static void logout(Activity context){
        sdkEventListener.onLogoutSucceed();
    }

    public static void pay(Activity context, String jsonData) {
        try {
            JSONObject toServerJsonData = new JSONObject(jsonData);
            Http.post(ServerConfig.payUrl + "sign/applePayCheck", toServerJsonData.toString(), new INetResultHandler() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject jsonRootObject = new JSONObject(data);
                        if (jsonRootObject.getInt("code") == 200) {
                            JSONObject toServerJsonData = new JSONObject(jsonData);
                            String callbackInfo = toServerJsonData.getString("callbackInfo");
                            String productCode = toServerJsonData.getString("pid");

                            int productType = 0;
                            if (toServerJsonData.has("rechargeType"))
                                productType = toServerJsonData.getInt("rechargeType");

                            pay(context, productCode, productType, callbackInfo);
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("code", ResultStateCode.UNKNOWN_ERROR);
                                jsonObject.put("msg", data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            sdkEventListener.onPayFailed(jsonObject.toString());
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
                    sdkEventListener.onPayFailed(jsonObject.toString());
                }
            }, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void pay(Activity context, String productIdInput, int productTypeInput, String callbackInfoInput) {
        final String productId = productIdInput;
        final int productType = productTypeInput;
        final String callbackInfo = callbackInfoInput;

        // 获取调用接口的Activity对象
        final Activity activity = context;
        Task<IsEnvReadyResult> task = Iap.getIapClient(activity).isEnvReady();
        task.addOnSuccessListener(new OnSuccessListener<IsEnvReadyResult>() {
            @Override
            public void onSuccess(IsEnvReadyResult result) {
                // 获取接口请求的结果
                // 构造一个PurchaseIntentReq对象
                PurchaseIntentReq req = new PurchaseIntentReq();
// 通过createPurchaseIntent接口购买的商品必须是您在AppGallery Connect网站配置的商品。
                req.setProductId(productId);
// priceType: 0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
                req.setPriceType(productType);
                req.setDeveloperPayload(callbackInfo);
// 调用createPurchaseIntent接口创建托管商品订单
                Task<PurchaseIntentResult> task = Iap.getIapClient(activity).createPurchaseIntent(req);
                task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
                    @Override
                    public void onSuccess(PurchaseIntentResult result) {
                        // 获取创建订单的结果
                        Status status = result.getStatus();
                        if (status.hasResolution()) {
                            try {
                                // 6666是您自定义的int类型常量
                                // 启动IAP返回的收银台页面
                                status.startResolutionForResult(activity, 6666);
                            } catch (IntentSender.SendIntentException exp) {
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof IapApiException) {
                            IapApiException apiException = (IapApiException) e;
                            Status status = apiException.getStatus();
                            int returnCode = apiException.getStatusCode();
                            Log.d(TAG, "Status:"+status +"ErrCode:"+returnCode);

                        } else {
                            // 其他外部错误
                            Log.d(TAG, "其他外部错误");
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    if (status.getStatusCode() == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                        // 未登录帐号
                        if (status.hasResolution()) {
                            try {
                                // 6666是您自定义的int类型常量
                                // 启动IAP返回的登录页面
                                status.startResolutionForResult(activity, 8888);
                            } catch (IntentSender.SendIntentException exp) {
                            }
                        }
                    } else if (status.getStatusCode() == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                        // 用户当前登录的华为帐号所在的服务地不在华为IAP支持结算的国家或地区中
                        Toast.makeText(context, "当前登录的华为帐号所在的服务地不在华为IAP支持结算的国家或地区中", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static void payAfterRequest(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("orderData", jsonObject);

            Http.post(ServerConfig.payUrl + "sign/hwRechargeCallbackLE3", resultJsonObject.toString(), new INetResultHandler() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject jsonRootObject = new JSONObject(data);
                        if (jsonRootObject.getInt("code") == 200) {
                            sdkEventListener.onPaySucceed(data);
                        } else {
                            sdkEventListener.onPayFailed(data);
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
                    sdkEventListener.onPayFailed(jsonObject.toString());
                }
            }, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public static void checkMissingOrder(Activity context, int productType) {
        // 构造一个OwnedPurchasesReq对象
        OwnedPurchasesReq ownedPurchasesReq = new OwnedPurchasesReq();
// priceType: 0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
        ownedPurchasesReq.setPriceType(productType);
// 获取调用接口的Activity对象
        Activity activity = context;
// 调用obtainOwnedPurchases接口获取所有已购但未发货的消耗型商品
        Task<OwnedPurchasesResult> task = Iap.getIapClient(activity).obtainOwnedPurchases(ownedPurchasesReq);
        task.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                // 获取接口请求成功的结果
                if (result != null && result.getInAppPurchaseDataList() != null) {
                    for (int i = 0; i < result.getInAppPurchaseDataList().size(); i++) {
                        String inAppPurchaseData = result.getInAppPurchaseDataList().get(i);
                        String inAppSignature = result.getInAppSignature().get(i);
                        // 使用应用的IAP公钥验证inAppPurchaseData的签名数据
                        // 如果验签成功，确认每个商品的购买状态。确认商品已支付后，检查此前是否已发过货，未发货则进行发货操作。发货成功后执行消耗操作
                        Log.d(TAG, "checkMissingOrder inAppPurchaseData:" + inAppPurchaseData);
                        Log.d(TAG, "checkMissingOrder inAppSignature:" + inAppSignature);
                        try {
                            JSONObject json = new JSONObject();
                            json.put("inAppPurchaseData", inAppPurchaseData);
                            json.put("inAppPurchaseDataSignature", inAppSignature);
                            payAfterRequest(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    int returnCode = apiException.getStatusCode();
                    Log.d(TAG, "checkMissingOrder status:" + status);
                    Log.d(TAG, "checkMissingOrder returnCode:" + returnCode);

                } else {
                    // 其他外部错误
                    if (null != e )
                        e.printStackTrace();
                }
            }
        });
    }


    public static void getCertificationInfo(Activity context) {
        PlayersClient client = Games.getPlayersClient(context);
        Task<PlayerExtraInfo> task = client.getPlayerExtraInfo(null);
        task.addOnSuccessListener(new OnSuccessListener<PlayerExtraInfo>() {
            @Override
            public void onSuccess(PlayerExtraInfo extra) {
                if (extra != null) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("IsVerify", extra.getIsRealName());
                        json.put("IsAdult", extra.getIsAdult());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sdkEventListener.onCertificationInfoGetSucceed(json.toString());
                } else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("IsVerify", true);
                        json.put("IsAdult", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sdkEventListener.onCertificationInfoGetSucceed(json.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    sdkEventListener.onCertificationInfoGetFailed(result);
                }
            }
        });
    }
}
