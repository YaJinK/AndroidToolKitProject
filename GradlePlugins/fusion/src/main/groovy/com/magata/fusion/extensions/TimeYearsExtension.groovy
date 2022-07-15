package com.magata.fusion.extensions

import com.magata.fusion.extensions.base.BaseExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Nested;

public abstract class TimeYearsExtension extends BaseExtension{
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

    public void handleConfig(Project project) {
        // 配置了腾讯opensdk
        def data = getTencent()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了微信sdk
        data = getWeChat()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }
    }
}
