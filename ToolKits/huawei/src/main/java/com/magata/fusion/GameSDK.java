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
                //?????????????????????????????????????????????????????????????????????????????????
                new AlertDialog.Builder(context)
                        .setTitle("?????????")
                        .setMessage("????????????????????????????????????????????????????????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                            // ???????????????????????? Default_value????????????status?????????????????????????????????????????????
                            int status = intent.getIntExtra(UpdateKey.STATUS, 4444);
                            // ????????????????????????
                            int rtnCode = intent.getIntExtra(UpdateKey.FAIL_CODE, 4444);
                            // ???????????????????????????
                            String rtnMessage = intent.getStringExtra(UpdateKey.FAIL_REASON);
                            Serializable info = intent.getSerializableExtra(UpdateKey.INFO);
                            //?????????????????????info????????????ApkUpgradeInfo????????????????????????????????????
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
                    // ????????????7401????????????????????????????????????????????????
                    if (statusCode == JosStatusCodes.JOS_PRIVACY_PROTOCOL_REJECTED) {
                        Log.d(TAG, "has reject the protocol");
                        //???????????????????????????
                        context.finish();
                    } else if (statusCode == 907135003) {
                        Log.d(TAG, "????????????????????????????????????");
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

        // ?????????????????????Activity??????
        final Activity activity = context;
        Task<IsEnvReadyResult> task = Iap.getIapClient(activity).isEnvReady();
        task.addOnSuccessListener(new OnSuccessListener<IsEnvReadyResult>() {
            @Override
            public void onSuccess(IsEnvReadyResult result) {
                // ???????????????????????????
                // ????????????PurchaseIntentReq??????
                PurchaseIntentReq req = new PurchaseIntentReq();
// ??????createPurchaseIntent????????????????????????????????????AppGallery Connect????????????????????????
                req.setProductId(productId);
// priceType: 0??????????????????; 1?????????????????????; 2??????????????????
                req.setPriceType(productType);
                req.setDeveloperPayload(callbackInfo);
// ??????createPurchaseIntent??????????????????????????????
                Task<PurchaseIntentResult> task = Iap.getIapClient(activity).createPurchaseIntent(req);
                task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
                    @Override
                    public void onSuccess(PurchaseIntentResult result) {
                        // ???????????????????????????
                        Status status = result.getStatus();
                        if (status.hasResolution()) {
                            try {
                                // 6666??????????????????int????????????
                                // ??????IAP????????????????????????
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
                            // ??????????????????
                            Log.d(TAG, "??????????????????");
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
                        // ???????????????
                        if (status.hasResolution()) {
                            try {
                                // 6666??????????????????int????????????
                                // ??????IAP?????????????????????
                                status.startResolutionForResult(activity, 8888);
                            } catch (IntentSender.SendIntentException exp) {
                            }
                        }
                    } else if (status.getStatusCode() == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                        // ???????????????????????????????????????????????????????????????IAP?????????????????????????????????
                        Toast.makeText(context, "?????????????????????????????????????????????????????????IAP?????????????????????????????????", Toast.LENGTH_LONG).show();
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
        // ????????????OwnedPurchasesReq??????
        OwnedPurchasesReq ownedPurchasesReq = new OwnedPurchasesReq();
// priceType: 0??????????????????; 1?????????????????????; 2??????????????????
        ownedPurchasesReq.setPriceType(productType);
// ?????????????????????Activity??????
        Activity activity = context;
// ??????obtainOwnedPurchases??????????????????????????????????????????????????????
        Task<OwnedPurchasesResult> task = Iap.getIapClient(activity).obtainOwnedPurchases(ownedPurchasesReq);
        task.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                // ?????????????????????????????????
                if (result != null && result.getInAppPurchaseDataList() != null) {
                    for (int i = 0; i < result.getInAppPurchaseDataList().size(); i++) {
                        String inAppPurchaseData = result.getInAppPurchaseDataList().get(i);
                        String inAppSignature = result.getInAppSignature().get(i);
                        // ???????????????IAP????????????inAppPurchaseData???????????????
                        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
                    // ??????????????????
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
