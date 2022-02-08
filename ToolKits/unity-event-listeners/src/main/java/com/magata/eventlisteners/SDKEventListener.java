package com.magata.eventlisteners;
import com.unity3d.player.UnityPlayer;

public class SDKEventListener implements ISDKEventListener {
    private final String CALLBACK_FUNCTION = "FusionCallback";

    private static SDKEventListener instance = null;

    public static SDKEventListener Instance() {
        if (instance == null)
            instance = new SDKEventListener();
        return instance;
    }

    @Override
    public void onInitSucceed() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onInitSucc", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onInitFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoginSucceed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLoginSucc", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoginFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLoginFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoginCanceled() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLoginCanceled", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogoutSucceed() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLogoutSucc", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogoutFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLogoutFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogoutCanceled() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLogoutCanceled", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExitSucceed() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onExitSucc", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExitCanceled() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onExitCanceled", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaySucceed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onCreateOrderSucc", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPayFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPayUserExit", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPayCanceled() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPayCanceled", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPushTokenGetSucceed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPushTokenGet", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPushTokenGetFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPushTokenGetFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmitUserInfoSucceed() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onSaveUserInfoSucc", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmitUserInfoFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onSubmitUserInfoFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCertificationInfoGetSucceed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onGetCertificationInfoSucc", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCertificationInfoGetFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onGetCertificationInfoFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShareSucceed() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onShareSucceed", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShareFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onShareFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
