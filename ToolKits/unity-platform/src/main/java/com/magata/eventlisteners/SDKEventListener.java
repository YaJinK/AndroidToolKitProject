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
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onInitSucceed", "");
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
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLoginSucceed", data);
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
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onLogoutSucceed", "");
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
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onExitSucceed", "");
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
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPaySucceed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPayFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPayFailed", data);
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
    public void onPushDataGetSucceed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPushDataGetSucceed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPushDataGetFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onPushDataGetFailed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmitUserInfoSucceed() {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onSubmitUserInfoSucceed", "");
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
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onCertificationInfoGetSucceed", data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCertificationInfoGetFailed(String data) {
        try {
            UnityPlayer.UnitySendMessage(CALLBACK_FUNCTION, "onCertificationInfoGetFailed", data);
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
