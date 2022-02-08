package com.magata.fusion.extensions;

import org.gradle.api.Action;
import org.gradle.api.tasks.Nested;

public abstract class FusionExtension {
    /*
    * 时年渠道
    * */
    @Nested
    abstract TimeYearsExtension getTimeYears();
    public void TimeYears(Action<? super TimeYearsExtension> action) {
        action.execute(getTimeYears());
    }

    /*
    * 腾讯OpenSDK
    * */
    @Nested
    abstract TencentExtension getTencent();
    public void Tencent(Action<? super TencentExtension> action) {
        action.execute(getTencent());
    }

    /*
    * 微信sdk
    * */
    @Nested
    abstract WeChatExtension getWeChat();
    public void WeChat(Action<? super WeChatExtension> action) {
        action.execute(getWeChat());
    }

    /*
     * 隐私政策sdk
     * */
    @Nested
    abstract PrivacyExtension getPrivacy();
    public void Privacy(Action<? super PrivacyExtension> action) {
        action.execute(getPrivacy());
    }
}
