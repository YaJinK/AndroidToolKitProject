package com.magata.fusion.extensions

import com.magata.fusion.extensions.base.ChannelInfoExtension;
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.Nested;

public abstract class FusionExtension extends ChannelInfoExtension {

    public void handleConfig(Project project) {
        AddToManifestPlaceholders(project, "magataAppId", getAppId())
        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:config:1.0.0")
        dependencyHandler.add("implementation","com.magata.toolkits:event-listeners:1.0.0");
        dependencyHandler.add("implementation","com.magata.toolkits:utility:1.0.0");
        dependencyHandler.add("implementation","com.magata.toolkits:file-provider:1.0.0")
        dependencyHandler.add("implementation","com.magata.toolkits:net:1.0.0")
        dependencyHandler.add("implementation","com.squareup.okhttp3:okhttp:4.9.3")
        dependencyHandler.add("implementation","com.magata.toolkits:fusion:1.0.0")

        // 配置了腾讯opensdk
        def data = getTencent()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了UCsdk
        data = getUC()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了misdk
        data = getMi()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了huaweisdk
        data = getHuaWei()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了微信sdk
        data = getWeChat()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了支付宝
        data = getAlipay()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

        // 配置了隐私政策sdk
        data = getPrivacy()
        if (data.getEnabled().getOrNull() == true) {
            data.handleConfig(project)
        }

    }

    /*
    * 时年渠道
    * */
    @Nested
    abstract TimeYearsExtension getTimeYears();
    public void TimeYears(Action<? super TimeYearsExtension> action) {
        action.execute(getTimeYears());
    }

    /*
    * UC渠道
    * */
    @Nested
    abstract UCExtension getUC();
    public void UC(Action<? super UCExtension> action) {
        action.execute(getUC());
    }

    /*
   * Mi渠道
   * */
    @Nested
    abstract MiExtension getMi();
    public void Mi(Action<? super MiExtension> action) {
        action.execute(getMi());
    }

    /*
  * HuaWei渠道
  * */
    @Nested
    abstract HuaWeiExtension getHuaWei();
    public void HuaWei(Action<? super HuaWeiExtension> action) {
        action.execute(getHuaWei());
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
    * 支付宝
    * */
    @Nested
    abstract AlipayExtension getAlipay();
    public void Alipay(Action<? super AlipayExtension> action) {
        action.execute(getAlipay());
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
