package com.magata.eventlisteners;

public interface ISDKEventListener {

    void onInitSucceed();
    void onInitFailed(String data);

    void onLoginSucceed(String data);
    void onLoginFailed(String data);
    void onLoginCanceled();

    void onLogoutSucceed();
    void onLogoutFailed(String data);
    void onLogoutCanceled();

    void onExitSucceed();
    void onExitCanceled();

    void onPaySucceed(String data);
    void onPayFailed(String data);
    void onPayCanceled();

    void onPushTokenGetSucceed(String data);
    void onPushTokenGetFailed(String data);

    void onSubmitUserInfoSucceed();
    void onSubmitUserInfoFailed(String data);

    void onCertificationInfoGetSucceed(String data);
    void onCertificationInfoGetFailed(String data);

    void onShareSucceed();
    void onShareFailed(String data);
}
